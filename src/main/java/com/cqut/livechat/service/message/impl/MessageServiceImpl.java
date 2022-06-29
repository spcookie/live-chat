package com.cqut.livechat.service.message.impl;

import com.cqut.livechat.LiveChatException;
import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.constant.SendStatus;
import com.cqut.livechat.dto.message.*;
import com.cqut.livechat.entity.message.*;
import com.cqut.livechat.entity.user.Account;
import com.cqut.livechat.repository.message.ChatFileMessageRepository;
import com.cqut.livechat.repository.message.ChatImageMessageRepository;
import com.cqut.livechat.repository.message.ChatTextMessageRepository;
import com.cqut.livechat.repository.message.CommonMessageRepository;
import com.cqut.livechat.service.BaseService;
import com.cqut.livechat.service.file.FileService;
import com.cqut.livechat.service.message.CommonMessageHandler;
import com.cqut.livechat.service.message.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Augenstern
 * @date 2022/5/26
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MessageServiceImpl extends BaseService implements MessageService {

    @Autowired
    private CommonMessageRepository commonMessageRepository;
    @Autowired
    private ChatTextMessageRepository chatTextMessageRepository;
    @Autowired
    private ChatImageMessageRepository chatImageMessageRepository;
    @Autowired
    private ChatFileMessageRepository chatFileMessageRepository;
    @Autowired
    private CommonMessageHandler<ChatTextMessage> textMessageService;
    @Autowired
    private CommonMessageHandler<ChatImageMessage> imageMessageService;
    @Autowired
    private CommonMessageHandler<AddFriendMessage> addFriendMessageService;
    @Autowired
    private CommonMessageHandler<ChatFileMessage> fileMessageService;
    @Autowired
    private FileService fileService;

    @Override
    public List<? extends CommonMessageDto> findHistoryMessages(QueryMessageDto queryMessage) {
        if (!super.verifyIsFriend(queryMessage.getId())) {
            throw new LiveChatException("非法好友关系");
        }
        if (queryMessage.getType().equals(MessageType.TEXT)) {
            return this.findTextHistoryMessages(queryMessage);
        } else if (queryMessage.getType().equals(MessageType.IMAGE)) {
            return this.findImageHistoryMessages(queryMessage);
        } else if (queryMessage.getType().equals(MessageType.FILE)) {
            return this.findFileHistoryMessages(queryMessage);
        } else {
            return null;
        }
    }

    private List<? extends CommonMessageDto> findTextHistoryMessages(QueryMessageDto queryMessage) {
        PageRequest pageRequest = PageRequest.of(queryMessage.getPage(), queryMessage.getSize());
        Account from = super.getLoginUser().getAccount();
        Account target = new Account();
        target.setId(queryMessage.getId());
        List<ChatTextMessage> messages = chatTextMessageRepository.findHistoryMessages(from, target, queryMessage.getExample(), pageRequest);
        return messages.stream().map(message -> {
            ChatTextMessageDto dto = new ChatTextMessageDto();
            BeanUtils.copyProperties(message, dto);
            dto.setType(MessageType.TEXT);
            return dto;
        }).collect(Collectors.toList());
    }

    private List<? extends CommonMessageDto> findImageHistoryMessages(QueryMessageDto queryMessage) {
        PageRequest pageRequest = PageRequest.of(queryMessage.getPage(), queryMessage.getSize());
        Account from = super.getLoginUser().getAccount();
        Account target = new Account();
        target.setId(queryMessage.getId());
        List<ChatImageMessage> messages = chatImageMessageRepository.findHistoryMessages(from, target, pageRequest);
        return messages.stream().map(message -> {
            ChatImageMessageDto dto = new ChatImageMessageDto();
            BeanUtils.copyProperties(message, dto);
            dto.setType(MessageType.IMAGE);
            return dto;
        }).collect(Collectors.toList());
    }

    private List<? extends CommonMessageDto> findFileHistoryMessages(QueryMessageDto queryMessage) {
        PageRequest pageRequest = PageRequest.of(queryMessage.getPage(), queryMessage.getSize());
        Account from = super.getLoginUser().getAccount();
        Account target = new Account();
        target.setId(queryMessage.getId());
        List<ChatFileMessage> messages = chatFileMessageRepository.findHistoryMessages(from, target, pageRequest);
        return messages.stream().map(message -> {
            ChatFileMessageDto dto = new ChatFileMessageDto();
            BeanUtils.copyProperties(message, dto);
            dto.setType(MessageType.FILE);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CommonMessageDto> getSimpleMessage(long id, int page, int size) {
        // 判断双方是否是好友关系
        if (!super.verifyIsFriend(id)) {
            return null;
        }
        // 消息按创建时间升序排列
        Sort sort = Sort.sort(CommonMessage.class).by(CommonMessage::getDate).descending();
        // 分页条件
        PageRequest pageRequest = PageRequest.of(page, size);
        Account loginAccount = super.getLoginUser().getAccount();
        // 查询消息公共信息
        Account targetAccount = new Account();
        targetAccount.setId(id);
        Iterable<CommonMessage> commonMessages = commonMessageRepository.findAllByFromIsAndTargetIs(loginAccount, targetAccount, sort, pageRequest);
        List<CommonMessageDto> commonMessageDtoList = new ArrayList<>();
        // 将消息转换为对应的dto
        commonMessages.forEach(message -> {
            if (message instanceof ChatTextMessage) {
                // 如果是文本消息
                ChatTextMessage m = (ChatTextMessage) message;
                ChatTextMessageDto textMessageDto = new ChatTextMessageDto();
                BeanUtils.copyProperties(m, textMessageDto);
                textMessageDto.setType(MessageType.TEXT);
                commonMessageDtoList.add(textMessageDto);
            } else if (message instanceof  ChatImageMessage) {
                // 如果是图片消息
                ChatImageMessage m = (ChatImageMessage) message;
                ChatImageMessageDto imageMessageDto = new ChatImageMessageDto();
                imageMessageDto.setType(MessageType.IMAGE);
                BeanUtils.copyProperties(m, imageMessageDto);
                imageMessageDto.setImageBase64(m.getImageBase64());
                commonMessageDtoList.add(imageMessageDto);
            } else if (message instanceof ChatFileMessage){
                ChatFileMessage m = (ChatFileMessage) message;
                ChatFileMessageDto fileMessageDto = new ChatFileMessageDto();
                fileMessageDto.setType(MessageType.FILE);
                BeanUtils.copyProperties(m, fileMessageDto);
                commonMessageDtoList.add(fileMessageDto);
            }
        });
        return commonMessageDtoList;
    }

    @Override
    public MessageSendStatusDto sendTextMessage(ChatTextMessageDto messageDto) {
        ChatTextMessage message = new ChatTextMessage();
        message.setTarget(messageDto.getTarget());
        message.setText(messageDto.getText());
        return textMessageService.handler(message);
    }

    @Override
    public MessageSendStatusDto sendImageMessage(ChatImageMessageDto messageDto) {
        ChatImageMessage message = new ChatImageMessage();
        message.setTarget(messageDto.getTarget());
        message.setImageBase64(messageDto.getImageBase64());
        return imageMessageService.handler(message);
    }

    @Override
    public MessageSendStatusDto sendFileMessage(Long id, MultipartFile file) {
        Optional<String> optionalPath = fileService.saveFileToDist(file);
        MessageSendStatusDto dto;
        if (optionalPath.isPresent()) {
            // 若文件保存到目录下成功，则开始处理消息
            String path = optionalPath.get();
            ChatFileMessage fileMessage = new ChatFileMessage();
            Account account = new Account();
            account.setId(id);
            fileMessage.setTarget(account);
            fileMessage.setOriginalFileName(file.getOriginalFilename());
            fileMessage.setSize(file.getSize());
            fileMessage.setPath(path);
            dto = fileMessageService.handler(fileMessage);
        } else {
            dto = new MessageSendStatusDto(SendStatus.SEND_FAIL, "文化上传失败");
        }
        return dto;
    }

    @Override
    public MessageSendStatusDto sendAddFriendMessage(AddFriendMessageDto messageDto) {
        AddFriendMessage message = new AddFriendMessage();
        message.setTarget(messageDto.getTarget());
        return addFriendMessageService.handler(message);
    }

    @Override
    public Map<Long, Integer> getAllUnreadCount() {
        Account account = super.getLoginUser().getAccount();
        // 查询未读文本消息
        List<ChatTextMessage> unreadTextMessages = chatTextMessageRepository.findUnreadMessagesForAllFriends(account);
        // 查询未读图片消息
        List<ChatImageMessage> unreadImageMessages = chatImageMessageRepository.findUnreadMessagesForAllFriends(account);
        // 合并消息
        List<CommonMessage> commonMessages = new ArrayList<>();
        commonMessages.addAll(unreadImageMessages);
        commonMessages.addAll(unreadTextMessages);
        // 未读消息统计
        HashMap<Long, Integer> map = new HashMap<>(50);
        commonMessages.forEach(message -> {
            Account from = message.getFrom();
            long id = from.getId();
            if (map.isEmpty()) {
                map.put(id, 1);
            } else if (map.get(id) == null) {
                map.put(id, 1);
            } else {
                map.replace(id, map.get(id) + 1);
            }
        });
        return map;
    }

    @Override
    public boolean confirmationMessage(long id) {
        boolean isFriend = super.verifyIsFriend(id);
        if (isFriend) {
            Account account = new Account();
            account.setId(id);
            int i = chatTextMessageRepository.modifyMessageStatusRead(account);
            int j = chatImageMessageRepository.modifyMessageStatusRead(account);
            return i + j >= 0;
        }
        return false;
    }
}

package com.cqut.livechat.service.message.impl;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.*;
import com.cqut.livechat.entity.message.AddFriendMessage;
import com.cqut.livechat.entity.message.ChatImageMessage;
import com.cqut.livechat.entity.message.ChatTextMessage;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.entity.user.Account;
import com.cqut.livechat.repository.message.ChatImageMessageRepository;
import com.cqut.livechat.repository.message.ChatTextMessageRepository;
import com.cqut.livechat.repository.message.CommonMessageRepository;
import com.cqut.livechat.service.BaseService;
import com.cqut.livechat.service.message.CommonMessageHandler;
import com.cqut.livechat.service.message.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CommonMessageHandler<ChatTextMessage> textMessageService;
    @Autowired
    private CommonMessageHandler<ChatImageMessage> imageMessageService;
    @Autowired
    private CommonMessageHandler<AddFriendMessage> addFriendMessageService;

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
                textMessageDto.setType(MessageType.TEXT);
                textMessageDto.setText(m.getText());
                textMessageDto.setId(m.getId());
                textMessageDto.setFrom(m.getFrom());
                textMessageDto.setDate(m.getDate());
                commonMessageDtoList.add(textMessageDto);
            } else if (message instanceof  ChatImageMessage) {
                // 如果是图片消息
                ChatImageMessage m = (ChatImageMessage) message;
                ChatImageMessageDto imageMessageDto = ChatImageMessageDto.builder().imageBase64(m.getImageBase64()).build();
                imageMessageDto.setType(MessageType.IMAGE);
                imageMessageDto.setId(m.getId());
                imageMessageDto.setFrom(m.getFrom());
                imageMessageDto.setDate(m.getDate());
                commonMessageDtoList.add(imageMessageDto);
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

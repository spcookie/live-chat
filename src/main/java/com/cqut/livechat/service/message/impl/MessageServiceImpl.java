package com.cqut.livechat.service.message.impl;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.AddFriendMessageDto;
import com.cqut.livechat.dto.message.ChatImageMessageDto;
import com.cqut.livechat.dto.message.ChatTextMessageDto;
import com.cqut.livechat.dto.message.CommonMessageDto;
import com.cqut.livechat.entity.message.AddFriendMessage;
import com.cqut.livechat.entity.message.ChatImageMessage;
import com.cqut.livechat.entity.message.ChatTextMessage;
import com.cqut.livechat.entity.message.CommonMessage;
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
import java.util.List;

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
        Sort sort = Sort.sort(CommonMessage.class).by(CommonMessage::getDate).ascending();
        // 分页条件
        PageRequest pageRequest = PageRequest.of(page, size);
        Long loginUserId = super.getLoginUserId();
        // 查询消息公共信息
        Iterable<CommonMessage> commonMessages = commonMessageRepository.findAllByFromIsAndTargetIs(loginUserId, id, sort, pageRequest);
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
                textMessageDto.setTarget(m.getTarget());
                textMessageDto.setDate(m.getDate());
                commonMessageDtoList.add(textMessageDto);
            } else if (message instanceof  ChatImageMessage) {
                // 如果是图片消息
                ChatImageMessage m = (ChatImageMessage) message;
                ChatImageMessageDto imageMessageDto = ChatImageMessageDto.builder().image(m.getImage()).build();
                imageMessageDto.setType(MessageType.IMAGE);
                imageMessageDto.setId(m.getId());
                imageMessageDto.setFrom(m.getFrom());
                imageMessageDto.setTarget(m.getTarget());
                imageMessageDto.setDate(m.getDate());
                commonMessageDtoList.add(imageMessageDto);
            }
        });
        return commonMessageDtoList;
    }

    @Override
    public String sendTextMessage(ChatTextMessageDto messageDto) {
        ChatTextMessage message = new ChatTextMessage();
        message.setTarget(messageDto.getTarget());
        message.setText(messageDto.getText());
        return textMessageService.handler(message);
    }

    @Override
    public String sendImageMessage(ChatImageMessageDto messageDto) {
        ChatImageMessage message = new ChatImageMessage();
        message.setTarget(messageDto.getTarget());
        message.setImageBase64(message.getImageBase64());
        return imageMessageService.handler(message);
    }

    @Override
    public String sendAddFriendMessage(AddFriendMessageDto messageDto) {
        AddFriendMessage message = new AddFriendMessage();
        message.setTarget(messageDto.getTarget());
        return addFriendMessageService.handler(message);
    }
}

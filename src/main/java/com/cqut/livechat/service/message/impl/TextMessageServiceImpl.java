package com.cqut.livechat.service.message.impl;

import com.alibaba.fastjson.JSON;
import com.cqut.livechat.MessageTypeException;
import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.ChatTextMessageDto;
import com.cqut.livechat.entity.message.ChatTextMessage;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.repository.message.ChatTextMessageRepository;
import com.cqut.livechat.service.message.AbstractCommonMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TextMessageServiceImpl extends AbstractCommonMessageService {

    @Autowired
    private ChatTextMessageRepository chatTextMessageRepository;

    @Override
    public boolean support(MessageType type) {
        return type.equals(MessageType.TEXT);
    }

    /**
     * 消息类型安全转换
     * @param message 公共消息类型
     * @return 文本消息类型
     */
    private ChatTextMessage castType(CommonMessage message) {
        if (!(message instanceof ChatTextMessage)) {
            throw new MessageTypeException("common message cannot be converted to chat text message");
        }
        return (ChatTextMessage) message;
    }

    @Override
    protected boolean sendTargetMessage(WebSocketSession session, CommonMessage message) {
        ChatTextMessage textMessage = this.castType(message);
        // 封装返回的消息
        ChatTextMessageDto messageDto = ChatTextMessageDto.builder()
                .id(textMessage.getId())
                .from(textMessage.getFrom())
                .date(textMessage.getDate())
                .text(textMessage.getText())
                .build();
        String text = JSON.toJSONString(messageDto);
        try {
            // 发送
            session.sendMessage(new TextMessage(text));
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    protected boolean saveMessage(WebSocketSession session, CommonMessage message) {
        populatePublicFields(session, message);
        // 消息类型转换
        ChatTextMessage textMessage = this.castType(message);
        log.info(textMessage.toString());
        // 持久化消息
        ChatTextMessage save = chatTextMessageRepository.save(textMessage);
        return save.getId() != null;
    }
}

package com.cqut.livechat.service.message.impl;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.ChatTextMessageDto;
import com.cqut.livechat.entity.message.ChatTextMessage;
import com.cqut.livechat.repository.message.ChatTextMessageRepository;
import com.cqut.livechat.service.message.AbstractCommonMessageHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class TextMessageHandlerImpl extends AbstractCommonMessageHandler<ChatTextMessage> {

    @Autowired
    private ChatTextMessageRepository chatTextMessageRepository;

    @Override
    protected boolean sendTargetMessage(WebSocketSession session, ChatTextMessage message) {
        // 封装返回的消息
        ChatTextMessageDto messageDto = new ChatTextMessageDto();
        messageDto.setType(MessageType.TEXT);
        messageDto.setText(message.getText());
        messageDto.setId(message.getId());
        messageDto.setFrom(message.getFrom());
        messageDto.setDate(message.getDate());
        try {
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString(messageDto);
            // 发送
            session.sendMessage(new TextMessage(value));
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    protected ChatTextMessage saveMessage(ChatTextMessage message) {
        // 持久化消息
        return chatTextMessageRepository.save(message);
    }
}

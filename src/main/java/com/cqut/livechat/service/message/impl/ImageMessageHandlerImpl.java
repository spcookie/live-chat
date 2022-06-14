package com.cqut.livechat.service.message.impl;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.ChatImageMessageDto;
import com.cqut.livechat.entity.message.ChatImageMessage;
import com.cqut.livechat.repository.message.ChatImageMessageRepository;
import com.cqut.livechat.service.message.AbstractCommonMessageHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ImageMessageHandlerImpl extends AbstractCommonMessageHandler<ChatImageMessage> {

    @Autowired
    ChatImageMessageRepository imageMessageRepository;

    @Override
    protected boolean sendTargetMessage(WebSocketSession session, ChatImageMessage message) {
        // 将byte转换为Base64
        String imageBase64 = new String(message.getImage(), StandardCharsets.UTF_8);
        ChatImageMessageDto messageDto = ChatImageMessageDto.builder().imageBase64(imageBase64).build();
        messageDto.setType(MessageType.IMAGE);
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
        }
        return false;
    }

    @Override
    protected ChatImageMessage saveMessage(ChatImageMessage message) {
        // 持久化消息
        // bug: 框架底层似乎没有调用方法来取得值, 导致设置的二进制数据为空, 这里手动调用一下
        message.getImage();
        return imageMessageRepository.save(message);
    }
}
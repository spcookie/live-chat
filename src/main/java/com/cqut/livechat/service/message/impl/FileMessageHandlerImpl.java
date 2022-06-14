package com.cqut.livechat.service.message.impl;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.ChatFileMessageDto;
import com.cqut.livechat.entity.message.ChatFileMessage;
import com.cqut.livechat.repository.message.ChatFileMessageRepository;
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
 * @date 2022/6/12
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class FileMessageHandlerImpl extends AbstractCommonMessageHandler<ChatFileMessage> {

    @Autowired
    private ChatFileMessageRepository chatFileMessageRepository;

    @Override
    protected boolean sendTargetMessage(WebSocketSession session, ChatFileMessage message) {
        ChatFileMessageDto dto = new ChatFileMessageDto();
        dto.setType(MessageType.FILE);
        dto.setId(message.getId());
        dto.setDate(message.getDate());
        dto.setOriginalFileName(message.getOriginalFileName());
        dto.setFrom(message.getFrom());
        dto.setSize(message.getSize());
        dto.setPath(message.getPath());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(dto);
            session.sendMessage(new TextMessage(json));
            return true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    protected ChatFileMessage saveMessage(ChatFileMessage message) {
        return chatFileMessageRepository.save(message);
    }
}

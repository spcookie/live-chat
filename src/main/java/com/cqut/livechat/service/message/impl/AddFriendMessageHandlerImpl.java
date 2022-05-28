package com.cqut.livechat.service.message.impl;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.AddFriendMessageDto;
import com.cqut.livechat.entity.message.AddFriendMessage;
import com.cqut.livechat.repository.message.AddFriendMessageRepository;
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
 * @date 2022/5/28
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AddFriendMessageHandlerImpl extends AbstractCommonMessageHandler<AddFriendMessage> {

    @Autowired
    private AddFriendMessageRepository addFriendMessageRepository;

    @Override
    protected boolean sendTargetMessage(WebSocketSession session, AddFriendMessage message) {
        AddFriendMessageDto dto = new AddFriendMessageDto();
        // 返回id和类型
        dto.setId(message.getId());
        dto.setType(MessageType.ADD_FRIEND);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString(dto);
            //发送
            session.sendMessage(new TextMessage(value));
            return true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    protected AddFriendMessage saveMessage(AddFriendMessage message) {
        super.populatePublicFields(message);
        return addFriendMessageRepository.save(message);
    }

    @Override
    protected boolean isVerifyRequire() {
        return false;
    }
}

package com.cqut.livechat.service.message.impl;

import com.alibaba.fastjson.JSON;
import com.cqut.livechat.MessageTypeException;
import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.AddFriendMessageDto;
import com.cqut.livechat.entity.message.AddFriendMessage;
import com.cqut.livechat.entity.message.ChatTextMessage;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.repository.message.AddFriendMessageRepository;
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
 * @date 2022/5/28
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AddFriendMessageServiceImpl extends AbstractCommonMessageService {

    @Autowired
    private AddFriendMessageRepository addFriendMessageRepository;

    @Override
    protected boolean sendTargetMessage(WebSocketSession session, CommonMessage message) {
        AddFriendMessageDto dto = new AddFriendMessageDto();
        // 返回id和类型
        dto.setId(message.getId());
        dto.setType(MessageType.ADD_FRIEND);
        String json = JSON.toJSONString(dto);
        try {
            session.sendMessage(new TextMessage(json));
            return true;
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    protected CommonMessage saveMessage(CommonMessage message) {
        super.populatePublicFields(message);
        AddFriendMessage addFriendMessage = this.castType(message);
        return addFriendMessageRepository.save(addFriendMessage);
    }

    @Override
    public boolean support(MessageType type) {
        return type.equals(MessageType.ADD_FRIEND);
    }

    @Override
    protected boolean isVerifyRequire() {
        return false;
    }

    private AddFriendMessage castType(CommonMessage message) {
        if (!(message instanceof AddFriendMessage)) {
            throw new MessageTypeException("common message cannot be converted to add friend message");
        }
        return (AddFriendMessage) message;
    }
}

package com.cqut.livechat.service.message.impl;

import com.cqut.livechat.MessageTypeException;
import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.AuxiliaryMessageDto;
import com.cqut.livechat.entity.message.ChatImageMessage;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.repository.message.ChatImageMessageRepository;
import com.cqut.livechat.service.message.AbstractCommonMessageService;
import com.cqut.livechat.service.message.AuxiliaryMessageCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ImageMessageServiceImpl extends AbstractCommonMessageService {

    @Autowired
    ChatImageMessageRepository imageMessageRepository;

    /**
     * 消息类型安全转换
     * @param message 公共消息类型
     * @return 图片消息类型
     */
    private ChatImageMessage castType(CommonMessage message) {
        if (!(message instanceof ChatImageMessage)) {
            throw new MessageTypeException("common message cannot be converted to chat image message");
        }
        return (ChatImageMessage) message;
    }

    @Override
    protected boolean sendTargetMessage(WebSocketSession session, CommonMessage message) {
        // 转换消息
        ChatImageMessage imageMessage = this.castType(message);
        try {
            // 发送
            session.sendMessage(new BinaryMessage(imageMessage.getImage()));
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    protected boolean saveMessage(WebSocketSession session, CommonMessage message) {
        ChatImageMessage imageMessage = this.castType(message);
        // 填充公共字段
        super.populatePublicFields(session, imageMessage);
        // 持久化消息
        ChatImageMessage save = imageMessageRepository.save(imageMessage);
        return save.getId() != null;
//        return true;
    }

    @Override
    public boolean support(MessageType type) {
        return type.equals(MessageType.IMAGE);
    }
}

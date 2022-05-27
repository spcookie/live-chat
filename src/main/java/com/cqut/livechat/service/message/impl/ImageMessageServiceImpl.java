package com.cqut.livechat.service.message.impl;

import com.alibaba.fastjson.JSON;
import com.cqut.livechat.MessageTypeException;
import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.ChatImageMessageDto;
import com.cqut.livechat.entity.message.ChatImageMessage;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.repository.message.ChatImageMessageRepository;
import com.cqut.livechat.service.message.AbstractCommonMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
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
        // 将byte转换为Base64
        String imageBase64 = new String(imageMessage.getImage());
        ChatImageMessageDto messageDto = ChatImageMessageDto.builder().imageBase64(imageBase64).build();
        messageDto.setType(MessageType.IMAGE);
        messageDto.setId(imageMessage.getId());
        messageDto.setFrom(imageMessage.getFrom());
        messageDto.setDate(imageMessage.getDate());
        String text = JSON.toJSONString(messageDto);
        try {
            // 发送
            session.sendMessage(new TextMessage(text));
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    protected boolean saveMessage(CommonMessage message) {
        ChatImageMessage imageMessage = this.castType(message);
        // 填充公共字段
        super.populatePublicFields(imageMessage);
        // 持久化消息
        ChatImageMessage save = imageMessageRepository.save(imageMessage);
        return save.getId() != null;
    }

    @Override
    public boolean support(MessageType type) {
        return type.equals(MessageType.IMAGE);
    }
}

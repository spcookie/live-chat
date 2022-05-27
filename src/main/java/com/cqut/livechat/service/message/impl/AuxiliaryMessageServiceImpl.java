package com.cqut.livechat.service.message.impl;

import com.cqut.livechat.MessageTypeException;
import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.AuxiliaryMessageDto;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.service.message.AbstractCommonMessageService;
import com.cqut.livechat.service.message.AuxiliaryMessageCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Service
@Slf4j
public class AuxiliaryMessageServiceImpl extends AbstractCommonMessageService {
    @Override
    protected boolean sendTargetMessage(WebSocketSession session, CommonMessage message) {
        return false;
    }

    @Override
    protected boolean saveMessage(WebSocketSession session, CommonMessage message) {
        AuxiliaryMessageDto auxiliaryMessageDto = castType(message);
        AuxiliaryMessageCache.add(session, auxiliaryMessageDto);
        return true;
    }

    /**
     * 消息类型安全转换
     * @param message 公共消息类型
     * @return 辅助消息类型
     */
    protected AuxiliaryMessageDto castType(CommonMessage message) {
        if (!(message instanceof AuxiliaryMessageDto)) {
            throw new MessageTypeException("common message cannot be converted to auxiliary message");
        }
        return (AuxiliaryMessageDto) message;
    }

    @Override
    public String handler(WebSocketSession session, CommonMessage message) {
        log.info("收到辅助消息 -> " + message);
        saveMessage(session, message);
        return null;
    }

    @Override
    public boolean support(MessageType type) {
        return type.equals(MessageType.AUXILIARY);
    }
}

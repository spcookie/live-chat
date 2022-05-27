package com.cqut.livechat.service.message;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.entity.message.CommonMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
public interface CommonMessageService {
    /**
     *  处理消息
     * @param session 套接字会话
     * @param message 代类型的消息
     * @return 返回消息发送状态
     */
    String handler(WebSocketSession session, CommonMessage message);

    /**
     * 支持处理的消息
     * @param type 消息类型
     * @return 是否支持
     */
    boolean support(MessageType type);
}

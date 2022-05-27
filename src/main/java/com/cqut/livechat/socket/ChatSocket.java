package com.cqut.livechat.socket;

import com.cqut.livechat.utils.SocketUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * @author Augenstern
 * @date 2022/5/21
 */
@Slf4j
@Component
public class ChatSocket extends AbstractWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        long id = SocketUserUtil.getLoginUserId(session);
        log.info("Socket连接成功 -> " + "id:" + id + " address:" + session.getRemoteAddress());
        // 保存连接
        ChatSocketCache.add(id, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        long id = SocketUserUtil.getLoginUserId(session);
        // 连接断开后将套接字清除
        ChatSocketCache.del(id);
        log.info("Socket连接关闭 -> " + "id:" + id);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Socket错误 -> " + exception);
    }
}

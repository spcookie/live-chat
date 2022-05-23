package com.cqut.livechat.socket;

import com.alibaba.fastjson.JSON;
import com.cqut.livechat.constant.Security;
import com.cqut.livechat.dto.chatMessage.SimpleMessage;
import com.cqut.livechat.entity.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.NativeWebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.websocket.Session;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Augenstern
 * @date 2022/5/21
 */
@Slf4j
@Component
public class ChatLinkHandler extends AbstractWebSocketHandler {

    private static final Map<Long ,WebSocketSession> WEB_SOCKET_SESSIONS = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        long id = getLoginUserId(session);
        log.info("Socket连接成功 -> " + "id:" + id + " address:" + session.getRemoteAddress());
        // 保存连接
        WEB_SOCKET_SESSIONS.put(id, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        InetSocketAddress remoteAddress = session.getRemoteAddress();
        String payload = message.getPayload();
        log.info("收到消息 -> " + remoteAddress + ":" + payload);
        // 文本转换对象
        SimpleMessage simpleMessage = JSON.parseObject(payload, SimpleMessage.class);
        System.out.println(simpleMessage);
        // 查找对方是否在线
        // 如果在线就直接发送消息
        // 不在线将消息缓存到队列
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        long id = getLoginUserId(session);
        log.info("Socket连接关闭 -> " + "id:" + id);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Socket错误 -> " + exception);
    }

    /**
     *  获取当前连接的登录用户id
     * @param session Socket会话
     * @return id
     */
    private long getLoginUserId(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        User user  = (User) attributes.get(Security.USER.getVal());
        return user.getId();
    }
}

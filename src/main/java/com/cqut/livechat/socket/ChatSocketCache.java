package com.cqut.livechat.socket;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
public class ChatSocketCache {

    private static final Map<Long , WebSocketSession> WEB_SOCKET_SESSIONS = new ConcurrentHashMap<>();

    public static WebSocketSession get(long key) {
        return WEB_SOCKET_SESSIONS.get(key);
    }

    public static WebSocketSession add(long key, WebSocketSession session) {
        return WEB_SOCKET_SESSIONS.put(key, session);
    }

    public static WebSocketSession del(long key) {
        return WEB_SOCKET_SESSIONS.remove(key);
    }
}

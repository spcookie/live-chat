package com.cqut.livechat.service.message;

import com.cqut.livechat.dto.message.AuxiliaryMessageDto;
import com.cqut.livechat.utils.SocketUserUtil;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
public class AuxiliaryMessageCache {
    private static final Map<Long, AuxiliaryMessageDto> MESSAGE_CACHE = new HashMap<>();

    public static AuxiliaryMessageDto get(WebSocketSession session) {
        long socketUserId = SocketUserUtil.getLoginUserId(session);
        return MESSAGE_CACHE.remove(socketUserId);
    }

    public static void add(WebSocketSession session, AuxiliaryMessageDto message) {
        long socketUserId = SocketUserUtil.getLoginUserId(session);
        MESSAGE_CACHE.put(socketUserId, message);
    }
}

package com.cqut.livechat.utils;

import com.cqut.livechat.constant.Security;
import com.cqut.livechat.entity.auth.User;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
public class SocketUserUtil {
    /**
     *  获取当前连接的登录用户id
     * @param session Socket会话
     * @return id
     */
    public static long getLoginUserId(WebSocketSession session) {
        User user = getLoginUser(session);
        return user.getId();
    }

    /**
     * 获取当前连接的用户
     * @param session Socket会话
     * @return 当前用户
     */
    public static User getLoginUser(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        return (User) attributes.get(Security.USER.getVal());
    }
}

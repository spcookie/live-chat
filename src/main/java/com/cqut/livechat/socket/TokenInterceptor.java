package com.cqut.livechat.socket;

import com.cqut.livechat.constant.Security;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.redis.auth.UserRedisUtil;
import com.cqut.livechat.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
@Component
@Slf4j
public class TokenInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpHeaders headers = request.getHeaders();
        List<String> authorization = headers.get(Security.AUTHORIZATION.getVal());
        if (!ObjectUtils.isEmpty(authorization)) {
            if (!authorization.isEmpty()) {
                // 获取Token
                String token = authorization.get(0);
                // 得到当前用户登录信息
                int id = TokenUtil.getIdFromToken(token);
                User user = UserRedisUtil.getUser(id);
                // 将用户信息放入attributes，以便后续Socket操作使用
                attributes.put(Security.USER.getVal(), user);
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}

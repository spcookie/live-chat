package com.cqut.livechat.filter;

import com.cqut.livechat.constant.Security;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.redis.auth.UserRedisUtil;
import com.cqut.livechat.utils.TokenUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Augenstern
 * @date 2022/4/4
 */
@Transactional
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 检查请求头中是否包含token
        String token = request.getHeader(Security.AUTHORIZATION.getVal());
        // 如果不包含token，则直接放行
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // 如果包含token，则验证token是否合法
        boolean verifyToken = TokenUtil.verifyToken(token);
        if (verifyToken) {
            // 如果token合法，则获取用户信息
            long userId = TokenUtil.getIdFromToken(token);
            User user = UserRedisUtil.getUser(userId);
            // 如果用户信息不存在，则直接放行
            if (!ObjectUtils.isEmpty(user)) {
                // 将用户信息放入安全上下文
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
                SecurityContextHolder.setContext(context);
            }
        }
        // 如果token不合法，则直接放行
        filterChain.doFilter(request, response);
    }
}

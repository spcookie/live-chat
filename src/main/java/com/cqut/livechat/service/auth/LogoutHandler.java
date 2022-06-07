package com.cqut.livechat.service.auth;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.redis.auth.UserRedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Augenstern
 * @date 2022/5/27
 */
@Service
public class LogoutHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 删除缓存中用户登录信息
        User user = (User) authentication.getPrincipal();
        UserRedisUtil.delUser(user.getId());
        response.setContentType("application/json;charset=utf-8");
        Result<String> result = Result.<String>builder()
                .code(ResultCode.OK)
                .message("退出登录成功")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(result);
        response.getWriter().print(json);
    }
}

package com.cqut.livechat.service.auth;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.redis.auth.UserRedisUtil;
import com.cqut.livechat.utils.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
@Service
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 获取验证成功后的User对象
        User user = (User) authentication.getPrincipal();
        Long id = user.getId();
        // 生成携带id的Token
        String token = TokenUtil.generateTokenWithId(id);
        int exp = TokenUtil.getExpFromToken(token);
        // 保存临时User对象到Redis
        UserRedisUtil.saveUser(id, user, exp);
        // 封装结果
        Result<String> result = Result.<String>builder()
                .code(ResultCode.OK)
                .message("登录成功")
                .data(token)
                .build();
        // 转换json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(result);
        // 返回结果
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(json);
    }
}

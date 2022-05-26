package com.cqut.livechat.service.auth;

import com.alibaba.fastjson.JSON;
import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 返回登录失败信息
        Result<String> result = Result.<String>builder()
                .code(ResultCode.ERROR)
                .message("登录失败 " + exception.getLocalizedMessage())
                .build();
        String json = JSON.toJSONString(result);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(json);
    }
}

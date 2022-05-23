package com.cqut.livechat.service.auth;

import com.alibaba.fastjson.JSON;
import com.cqut.livechat.dto.common.ResultCode;
import com.cqut.livechat.dto.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
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
public class NoAuthenticatedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Result<String> result = Result.<String>builder()
                .code(ResultCode.NO_PERMISSION)
                .message("拒绝访问, 请登录")
                .build();
        String json = JSON.toJSONString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}

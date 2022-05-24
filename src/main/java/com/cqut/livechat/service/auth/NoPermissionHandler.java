package com.cqut.livechat.service.auth;

import com.alibaba.fastjson.JSON;
import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
public class NoPermissionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result<String> result = Result.<String>builder()
                .code(ResultCode.NO_PERMISSION)
                .message("拒绝访问，没有权限")
                .build();
        String json = JSON.toJSONString(result);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}

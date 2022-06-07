package com.cqut.livechat.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
public class SimpleAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String PROCESSES_URL = "/api/auth/login";
    private static final String METHOD = "POST";

    public SimpleAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(PROCESSES_URL, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (METHOD.equals(request.getMethod())) {
            // 获取请求的body
            String body = this.getBody(request);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode tree = mapper.readTree(body);
            // 获取账号和密码
            String username = tree.get("username").asText();
            String password = tree.get("password").asText();
            // 创建验证信息
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            // 开始校验
            return getAuthenticationManager().authenticate(token);
        }
        return null;
    }

    private String getBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        String line;
        StringBuilder body = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }
}

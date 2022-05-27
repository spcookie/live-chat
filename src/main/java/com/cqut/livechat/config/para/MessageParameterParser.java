package com.cqut.livechat.config.para;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.MessageWithTypeDto;
import com.cqut.livechat.entity.message.CommonMessage;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * @author Augenstern
 * @date 2022/5/27
 */
@Component
public class MessageParameterParser implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 支持的参数类型
        return parameter.getParameterType().equals(MessageWithTypeDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        // 读取请求体
        BufferedReader reader = request.getReader();
        String line;
        StringBuilder buffer = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String body = buffer.toString();
        // 转换成对应的对象
        JSONObject jsonObject = JSON.parseObject(body);
        MessageType type = jsonObject.getObject("type", MessageType.class);
        CommonMessage message = jsonObject.getObject("message", type.getType());
        return MessageWithTypeDto.builder()
                .type(type)
                .message(message)
                .build();
    }
}

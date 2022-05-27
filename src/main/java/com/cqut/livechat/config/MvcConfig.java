package com.cqut.livechat.config;

import com.cqut.livechat.config.para.MessageParameterParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Augenstern
 * @date 2022/5/21
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private MessageParameterParser messageParameterParser;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("GET", "POST");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        // 添加自定义消息参数解析器
        resolvers.add(0, messageParameterParser);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addFormatter(new Formatter<Date>() {
//            @Override
//            public Date parse(String text, Locale locale) throws ParseException {
//                LocalDateTime parse = LocalDateTime.parse(text, DateTimeFormatter.ofPattern(""));
//            }
//
//            @Override
//            public String print(Date object, Locale locale) {
//                return null;
//            }
//        });
    }
}

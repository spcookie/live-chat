package com.cqut.livechat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author Augenstern
 * @date 2022/5/21
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("GET", "POST");
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

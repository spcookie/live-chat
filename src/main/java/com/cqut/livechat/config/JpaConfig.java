package com.cqut.livechat.config;

import com.cqut.livechat.entity.auth.User;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@EntityScan(basePackages = "com.cqut.livechat.entity")
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        // 获取安全上下文
        SecurityContext context = SecurityContextHolder.getContext();
        // 从上下文中获取认证信息
        Authentication authentication = context.getAuthentication();
        String username = null;
        if (!ObjectUtils.isEmpty(authentication)) {
            // 得到当前登录用户
            User user = (User) authentication.getPrincipal();
            // 获取当前登录用户的用户名
            username = user.getUsername();
        }
        // 添加审计信息
        String finalUsername = username;
        return () -> Optional.ofNullable(finalUsername);
    }
}

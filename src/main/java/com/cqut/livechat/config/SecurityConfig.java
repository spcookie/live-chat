package com.cqut.livechat.config;

import com.cqut.livechat.filter.SimpleAuthenticationFilter;
import com.cqut.livechat.filter.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @author Augenstern
 * @date 2022/5/21
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    AuthenticationFailureHandler failureHandler;
    @Autowired
    AuthenticationEntryPoint entryPoint;
    @Autowired
    AccessDeniedHandler deniedHandler;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public SimpleAuthenticationFilter simpleAuthenticationFilter() throws Exception {
        // 添加一个自定义的简单用户名密码验证过滤器
        SimpleAuthenticationFilter filter = new SimpleAuthenticationFilter(super.authenticationManager());
        // 设置验证成功处理器
        filter.setAuthenticationSuccessHandler(successHandler);
        // 设置验证失败处理器
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 添加一个BCrypt哈希函数的编码器
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 添加直接放行的url
        web.ignoring().antMatchers("/error", "/actuator");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置身份验证和密码解码器
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用csrf
        http.csrf().disable();
        // 允许跨域
        http.cors();
        http.authorizeRequests()
                .antMatchers("/auth/login").anonymous()
                .anyRequest().authenticated();
        // 禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        // 添加未认证处理器
        http.exceptionHandling().authenticationEntryPoint(entryPoint);
        // 添加无权限拒绝访问处理器
        http.exceptionHandling().accessDeniedHandler(deniedHandler);
        // 添加自定义用户名密码认证过滤器
        http.addFilterAt(simpleAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // 添加token认证过滤器
        http.addFilterBefore(tokenAuthenticationFilter(), LogoutFilter.class);
    }
}

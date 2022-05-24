package com.cqut.livechat.service;

import com.cqut.livechat.entity.auth.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
public class BaseService {

    public User getLoginUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (ObjectUtils.isEmpty(authentication)) {
            return null;
        }
        return  (User) authentication.getPrincipal();
    }

    public Long getLoginUserId() {
        User loginUser = getLoginUser();
        return ObjectUtils.isEmpty(loginUser) ? null : loginUser.getId();
    }
}

package com.cqut.livechat.service;

import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.entity.friends.FriendShip;
import com.cqut.livechat.repository.friends.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
public class BaseService {

    @Autowired
    private FriendRepository friendRepository;

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

    public boolean verifyIsFriend(long id) {
        User user = getLoginUser();
        // 获取用户的好友关系
        List<FriendShip> friendShip = friendRepository.findFriendShip(user);
        // 查找是否该用户与目标用户是否是好友关系
        for (FriendShip ship : friendShip) {
            if (!Objects.equals(ship.getUser().getId(), id)) {
                if (!Objects.equals(ship.getFriend().getId(), id)) {
                    continue;
                }
            }
            return true;
        }
        return false;
    }
}

package com.cqut.livechat.service.status;

import com.cqut.livechat.constant.UserStatus;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
public interface UserStatusService {

    /**
     *  获取指定用户登录状态
     * @param id 用户id
     * @return 登录状态
     */
    UserStatus getUserLoginStatus(long id);
}

package com.cqut.livechat.service.status.impl;

import com.cqut.livechat.constant.UserStatus;
import com.cqut.livechat.service.BaseService;
import com.cqut.livechat.service.status.UserStatusService;
import com.cqut.livechat.socket.ChatSocketCache;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
@Service
public class UserStatusServiceImpl extends BaseService implements UserStatusService {
    @Override
    public UserStatus getUserLoginStatus(long id) {
        WebSocketSession session = ChatSocketCache.get(id);
        return ObjectUtils.isEmpty(session) ? UserStatus.OFF_LINE : UserStatus.ON_LINE;
    }
}

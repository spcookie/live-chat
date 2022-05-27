package com.cqut.livechat.service.message;

import com.cqut.livechat.constant.UserStatus;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.entity.friends.FriendShip;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.repository.friends.FriendRepository;
import com.cqut.livechat.service.BaseService;
import com.cqut.livechat.service.status.UserStatusService;
import com.cqut.livechat.socket.ChatSocketCache;
import com.cqut.livechat.utils.SocketUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
@Slf4j
public abstract class AbstractCommonMessageService extends BaseService implements CommonMessageService {

    @Autowired
    protected UserStatusService userStatusService;
    @Autowired
    protected FriendRepository friendRepository;

    /**
     *  发送消息到目标客户端
     * @param session 当前连接的会话
     * @param message 发送的消息
     * @return 发送状态
     */
    protected abstract boolean sendTargetMessage(WebSocketSession session, CommonMessage message);

    /**
     *  持久化消息
     * @param message 消息
     * @return 是否成功
     */
    protected abstract boolean saveMessage(CommonMessage message);

    @Override
    public String handler(CommonMessage message) {
        // 获取消息接受者
        Long target = message.getTarget();
        // 校验消息接收者是否合法
        boolean isLegal = super.verifyIsFriend(target);
        if (!isLegal) {
            return "消息发送非法, 已取消发送";
        }
        // 持久保存消息
        boolean isSave = this.saveMessage(message);
        String result;
        if (isSave) {
            log.info("消息保存成功 -> " + message);
            result = "消息发送成功";
        } else {
            result = "消息发送失败";
        }
        // 获取接收者登录状态
        UserStatus status = userStatusService.getUserLoginStatus(target);
        if (status.equals(UserStatus.ON_LINE)) {
            // 如果接收者在线
            // 获取套接字连接
            WebSocketSession targetSession = ChatSocketCache.get(target);
            // 发送消息
            boolean b = this.sendTargetMessage(targetSession, message);
        }
        return result;
    }

    protected void populatePublicFields(CommonMessage message) {
        message.setFrom(super.getLoginUserId());
        message.setDate(new Date());
    }
}

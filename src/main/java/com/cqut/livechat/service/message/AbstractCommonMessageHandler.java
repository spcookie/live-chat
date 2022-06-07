package com.cqut.livechat.service.message;

import com.cqut.livechat.constant.SendStatus;
import com.cqut.livechat.constant.UserStatus;
import com.cqut.livechat.dto.message.MessageSendStatusDto;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.repository.friends.FriendRepository;
import com.cqut.livechat.service.BaseService;
import com.cqut.livechat.service.status.UserStatusService;
import com.cqut.livechat.socket.ChatSocketCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
@Slf4j
public abstract class AbstractCommonMessageHandler<T extends CommonMessage> extends BaseService implements CommonMessageHandler<T> {

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
    protected abstract boolean sendTargetMessage(WebSocketSession session, T message);

    /**
     *  持久化消息
     * @param message 消息
     * @return 是否成功
     */
    protected abstract T saveMessage(T message);

    @Override
    public MessageSendStatusDto handler(T message) {
        // 获取消息接受者
        Long target = message.getTarget().getId();
        if (!super.userIsExist(target)) {
            return new MessageSendStatusDto(SendStatus.USER_NOT_EXIST, "用户不存在");
        }
        if (this.onlyFriend()) {
            // 校验消息接收者是否是好友
            boolean isLegal = super.verifyIsFriend(target);
            if (!isLegal) {
                return new MessageSendStatusDto(SendStatus.ILLEGAL_SEND, "消息发送非法, 已取消发送");
            }
        }
        // 填充公共字段
        this.populatePublicFields(message);
        // 判断是否能重复发送
        if (!this.canRepeatSend()) {
            if (this.isRepeatSend(message)) {
                return new MessageSendStatusDto(SendStatus.NOT_REPEAT_SEND, "消息通知不能重复发送");
            }
        }
        // 持久保存消息
        T save = this.saveMessage(message);
        if (save.getId() != null) {
            log.info("消息保存成功 -> " + save);
            // 获取接收者登录状态
            UserStatus status = userStatusService.getUserLoginStatus(target);
            if (status.equals(UserStatus.ON_LINE)) {
                // 如果接收者在线
                // 获取套接字连接
                WebSocketSession targetSession = ChatSocketCache.get(target);
                // 发送消息
                boolean b = this.sendTargetMessage(targetSession, save);
            }
            return new MessageSendStatusDto(SendStatus.SEND_SUCCESS, "消息发送成功");
        } else {
            return new MessageSendStatusDto(SendStatus.SEND_FAIL, "消息发送失败");
        }
    }

    private void populatePublicFields(CommonMessage message) {
        message.setFrom(super.getLoginUser().getAccount());
        message.setDate(new Date());
    }

    protected boolean onlyFriend() {
        return true;
    }

    protected boolean canRepeatSend() {
        return true;
    }

    protected boolean isRepeatSend(T message) {
        return true;
    }
}
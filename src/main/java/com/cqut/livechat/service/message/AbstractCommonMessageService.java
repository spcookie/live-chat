package com.cqut.livechat.service.message;

import com.alibaba.fastjson.JSON;
import com.cqut.livechat.constant.UserStatus;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.service.status.UserStatusService;
import com.cqut.livechat.socket.ChatSocketCache;
import com.cqut.livechat.utils.SocketUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
@Slf4j
public abstract class AbstractCommonMessageService implements CommonMessageService {

    @Autowired
    protected UserStatusService userStatusService;

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
     * @param session 套接字上下文
     * @return 是否成功
     */
    protected abstract boolean saveMessage(WebSocketSession session, CommonMessage message);

    /**
     * 客户端收到消息后发送的回执消息
     * @param session 套接字会话
     * @param obj 发送的消息对象
     */
    protected <T> void sendReceiptMessage(WebSocketSession session, T obj) {
        String json = JSON.toJSONString(obj);
        try {
            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void handler(WebSocketSession session, CommonMessage message) {
        // 获取消息接受者
        Long target = message.getTarget();
        //TODO: 在此校验消息接收者是否合法

        // 持久保存消息
        boolean isSave = this.saveMessage(session, message);
        if (isSave) {
            log.info("消息保存成功 -> " + message);
            this.sendReceiptMessage(session ,"消息发送成功");
        } else {
            this.sendReceiptMessage(session ,"消息发送失败");
        }
        // 获取接收者登录状态
        UserStatus status = userStatusService.getUserLoginStatus(target);
        if (status.equals(UserStatus.ON_LINE)) {
            // 如果接收者在线
            // 获取套接字连接
            WebSocketSession targetSession = ChatSocketCache.get(target);
            // 发送消息
            boolean b = this.sendTargetMessage(targetSession, message);
            log.info("消息发送状态 " + session.getRemoteAddress() + " -> " + targetSession.getRemoteAddress() + " :" + b);
        }
    }

    protected CommonMessage getMessage(long id) {
        //TODO: 消息获取实现
        return null;
    }

    protected void populatePublicFields(WebSocketSession session, CommonMessage message) {
        message.setFrom(SocketUserUtil.getLoginUserId(session));
        message.setDate(new Date());
    }
}

package com.cqut.livechat.constant;

import com.cqut.livechat.entity.message.AddFriendMessage;
import com.cqut.livechat.entity.message.ChatImageMessage;
import com.cqut.livechat.entity.message.ChatTextMessage;
import com.cqut.livechat.entity.message.CommonMessage;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
public enum MessageType {
    /**
     * 简单文本
     */
    TEXT(ChatTextMessage.class),
    /**
     * 添加好友消息
     */
    ADD_FRIEND(AddFriendMessage.class),
    /**
     * 图片
     */
    IMAGE(ChatImageMessage.class),
    ;

    private final Class<? extends CommonMessage> type;

    MessageType(Class<? extends CommonMessage> clazz) {
        this.type = clazz;
    }

    public Class<? extends CommonMessage> getType() {
        return this.type;
    }
}

package com.cqut.livechat.constant;

/**
 * @author Augenstern
 * @date 2022/5/28
 */
public enum SendStatus {
    /**
     * 用户不存在
     */
    USER_NOT_EXIST,
    /**
     * 非法发送消息
     */
    ILLEGAL_SEND,
    /**
     * 消息不能重复发送
     */
    NOT_REPEAT_SEND,
    /**
     * 消息发送成功
     */
    SEND_SUCCESS,
    /**
     * 消息发送失败
     */
    SEND_FAIL
    ;
}

package com.cqut.livechat.service.message;

import com.cqut.livechat.dto.message.*;
import com.cqut.livechat.entity.message.AddFriendMessage;

import java.util.List;
import java.util.Map;

/**
 * @author Augenstern
 * @date 2022/5/26
 */
public interface MessageService {
    /**
     * 获取好友简单消息
     * @param id 好友id
     * @param page 页数
     * @param size 长度
     * @return 简单消息
     */
    List<CommonMessageDto> getSimpleMessage(long id, int page, int size);

    /**
     * 发送文本消息
     * @param messageDto 文本消息
     * @return 发送状态信息
     */
    MessageSendStatusDto sendTextMessage(ChatTextMessageDto messageDto);

    /**
     * 发送图片消息
     * @param messageDto 图片消息
     * @return 发送状态信息
     */
    MessageSendStatusDto sendImageMessage(ChatImageMessageDto messageDto);

    /**
     * 发送添加朋友消息
     * @param messageDto 消息
     * @return 发送状态信息
     */
    MessageSendStatusDto sendAddFriendMessage(AddFriendMessageDto messageDto);

    /**
     * 查询未读的好友消息
     * @return 未读数量
     */
    Map<Long, Integer> getAllUnreadCount();

    /**
     * 用户确认消息已读
     * @param id 好友id
     * @return 修改消息状态是否成功
     */
    boolean confirmationMessage(long id);
}

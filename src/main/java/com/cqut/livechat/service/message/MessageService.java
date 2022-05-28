package com.cqut.livechat.service.message;

import com.cqut.livechat.dto.message.AddFriendMessageDto;
import com.cqut.livechat.dto.message.ChatImageMessageDto;
import com.cqut.livechat.dto.message.ChatTextMessageDto;
import com.cqut.livechat.dto.message.CommonMessageDto;

import java.util.List;

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
    String sendTextMessage(ChatTextMessageDto messageDto);

    /**
     * 发送图片消息
     * @param messageDto 图片消息
     * @return 发送状态信息
     */
    String sendImageMessage(ChatImageMessageDto messageDto);

    /**
     * 发送添加朋友消息
     * @param messageDto 消息
     * @return 发送状态信息
     */
    String sendAddFriendMessage(AddFriendMessageDto messageDto);
}

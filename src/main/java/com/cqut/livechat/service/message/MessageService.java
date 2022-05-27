package com.cqut.livechat.service.message;

import com.cqut.livechat.dto.message.CommonMessageDto;
import com.cqut.livechat.dto.message.MessageWithTypeDto;
import com.cqut.livechat.entity.message.CommonMessage;

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
     * 发送消息通用方法
     * 根据消息类型选择对应消息处理器处理消息
     * @param messageWithType 带消息类型的消息
     * @return 发送状态信息
     */
    String sendMessage(MessageWithTypeDto<CommonMessage> messageWithType);
}

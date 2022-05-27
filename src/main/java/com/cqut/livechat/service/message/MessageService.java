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

    String sendMessage(MessageWithTypeDto<CommonMessage> messageWithType);
}

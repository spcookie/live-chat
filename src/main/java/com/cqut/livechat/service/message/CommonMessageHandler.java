package com.cqut.livechat.service.message;

import com.cqut.livechat.dto.message.MessageSendStatusDto;
import com.cqut.livechat.entity.message.CommonMessage;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
public interface CommonMessageHandler<T extends CommonMessage> {
    /**
     *  处理消息
     * @param message 代类型的消息
     * @return 返回消息发送状态
     */
    MessageSendStatusDto handler(T message);
}

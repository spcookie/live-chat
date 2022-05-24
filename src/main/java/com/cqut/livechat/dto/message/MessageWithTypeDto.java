package com.cqut.livechat.dto.message;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.entity.message.CommonMessage;
import lombok.Builder;
import lombok.Data;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Data
@Builder
public class MessageWithTypeDto<T extends CommonMessage> {
    private MessageType type;
    private T message;
}

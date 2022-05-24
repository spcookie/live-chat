package com.cqut.livechat.dto.message;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.entity.message.CommonMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Getter
@Setter
public class AuxiliaryMessageDto extends CommonMessage {
    private MessageType transmissionType;
}

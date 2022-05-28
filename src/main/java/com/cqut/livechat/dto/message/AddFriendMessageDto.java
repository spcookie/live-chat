package com.cqut.livechat.dto.message;

import com.cqut.livechat.constant.MessageStatus;
import com.cqut.livechat.entity.message.AddFriendMessage;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Augenstern
 * @date 2022/5/28
 */
@Getter
@Setter
@ToString(callSuper = true)
public class AddFriendMessageDto extends CommonMessageDto {
    private MessageStatus status = MessageStatus.PENDING;
}

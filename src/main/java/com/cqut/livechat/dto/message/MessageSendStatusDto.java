package com.cqut.livechat.dto.message;

import com.cqut.livechat.constant.SendStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Augenstern
 * @date 2022/5/28
 */
@Getter
@Setter
@AllArgsConstructor
public class MessageSendStatusDto {
    private SendStatus status;
    private String message;
}

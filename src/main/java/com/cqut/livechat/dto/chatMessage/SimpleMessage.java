package com.cqut.livechat.dto.chatMessage;

import lombok.Data;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
@Data
public class SimpleMessage {
    private Long target;
    private String message;
}

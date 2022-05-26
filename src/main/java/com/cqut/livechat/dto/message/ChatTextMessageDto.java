package com.cqut.livechat.dto.message;

import lombok.*;

import java.util.Date;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
public class ChatTextMessageDto extends CommonMessageDto {
    private String text;
}



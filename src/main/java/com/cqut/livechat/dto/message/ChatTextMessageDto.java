package com.cqut.livechat.dto.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ChatTextMessageDto extends CommonMessageDto {
    private String text;
}



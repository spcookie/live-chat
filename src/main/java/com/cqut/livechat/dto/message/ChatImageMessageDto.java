package com.cqut.livechat.dto.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Augenstern
 * @date 2022/5/26
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
public class ChatImageMessageDto extends CommonMessageDto {
    private byte[] image;
}

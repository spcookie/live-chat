package com.cqut.livechat.dto.message;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Data
@ToString
@Builder
public class ChatTextMessageDto {
    private Long id;
    private Long from;
    private Date date;
    private String text;
}

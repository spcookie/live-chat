package com.cqut.livechat.dto.message;

import com.cqut.livechat.constant.MessageType;
import lombok.Data;

/**
 * @author Augenstern
 * @date 2022/6/24
 */
@Data
public class QueryMessageDto {
    private Long id;
    private Integer page;
    private Integer size;
    private MessageType type;
    private String example;
}

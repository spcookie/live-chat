package com.cqut.livechat.dto.message;

import com.cqut.livechat.constant.MessageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author Augenstern
 * @date 2022/5/26
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonMessageDto {
    private MessageType type;
    private Long id;
    private Long from;
    private Long target;
    private Date date;
}

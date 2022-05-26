package com.cqut.livechat.dto.message;

import com.cqut.livechat.constant.MessageType;
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
public class CommonMessageDto {
    private MessageType type;
    private Long id;
    private Long from;
    private Long target;
    private Date date;
}

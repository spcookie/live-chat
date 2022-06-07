package com.cqut.livechat.dto.message;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.entity.user.Account;
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
    private Account from;
    private Account target;
    private Date date;
}

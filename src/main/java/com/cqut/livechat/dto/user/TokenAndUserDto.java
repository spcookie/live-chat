package com.cqut.livechat.dto.user;

import com.cqut.livechat.entity.auth.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Augenstern
 * @date 2022/5/29
 */
@Getter
@Setter
@ToString
public class TokenAndUserDto {
    String token;
    User user;
}

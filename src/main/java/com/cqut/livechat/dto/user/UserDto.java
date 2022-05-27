package com.cqut.livechat.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author Augenstern
 * @date 2022/5/27
 */
@Getter
@Setter
@ToString
public class UserDto implements Serializable {
    @Pattern(regexp = "[0-9]{10}")
    private String username;
    @Length(min = 8, max = 16)
    private String password;
    @Valid
    private AccountDto account;
}

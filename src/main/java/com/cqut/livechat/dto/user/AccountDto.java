package com.cqut.livechat.dto.user;

import com.cqut.livechat.entity.user.Account;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

/**
 * @author Augenstern
 * @date 2022/5/25
 */
@Getter
@Setter
@ToString
public class AccountDto {
    @Min(1)
    private Long id;
    private String username;
    @Length(max = 4)
    private String name;
    @Length(max = 11)
    private String phone;
    @Range(min = 1, max = 150)
    private Integer age;
}

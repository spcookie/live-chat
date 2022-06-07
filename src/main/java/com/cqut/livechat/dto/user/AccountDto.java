package com.cqut.livechat.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

/**
 * @author Augenstern
 * @date 2022/5/25
 */
@Getter
@Setter
@ToString
public class AccountDto {
    @Min(groups = Group.Add.class, value = 1)
    private Long id;
    @NotNull(groups = Group.Find.class)
    private String username;
    @Length(groups = Group.Add.class, max = 4)
    private String name;
    @Pattern(groups = Group.Add.class, regexp = "[0-9]{11}")
    private String phone;
    @Range(groups = Group.Add.class, min = 1, max = 150)
    private Integer age;

    public interface Group extends Default {
        interface Add extends Group {}
        interface Find extends Group {}
    }
}

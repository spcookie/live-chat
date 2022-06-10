package com.cqut.livechat.controller.account;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.user.AccountDto;
import com.cqut.livechat.dto.user.UserDto;
import com.cqut.livechat.service.account.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Augenstern
 * @date 2022/5/27
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @PutMapping("/register")
    public Result<Void> registeredAccount(@RequestBody @Validated(value = AccountDto.Group.Add.class) UserDto accountDto) {
        return accountService.registeredAccount(accountDto);
    }
}

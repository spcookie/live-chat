package com.cqut.livechat.service.account.impl;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.user.AccountDto;
import com.cqut.livechat.dto.user.UserDto;
import com.cqut.livechat.entity.auth.Role;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.entity.user.Account;
import com.cqut.livechat.repository.auth.RoleRepository;
import com.cqut.livechat.repository.auth.UserRepository;
import com.cqut.livechat.repository.user.AccountRepository;
import com.cqut.livechat.service.BaseService;
import com.cqut.livechat.service.account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;

/**
 * @author Augenstern
 * @date 2022/5/27
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl extends BaseService implements AccountService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Result<AccountDto> registeredAccount(UserDto userDto) {
        // 判断用户是否存在
        AccountDto dtoAccount = userDto.getAccount();
        String phone = dtoAccount.getPhone();
        boolean existsAccount = accountRepository.existsAccountByPhoneIs(phone);
        if (existsAccount) {
            return Result.error("注册失败, 该手机号已被注册", null);
        }
        // 生成账号
        String number = this.generateAccountNumber();
        // 封装前端获取的消息
        User user = new User();
        user.setUsername(number);
        String encode = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encode);
        Account account = new Account();
        account.setUsername(number);
        account.setName(dtoAccount.getName());
        account.setAge(dtoAccount.getAge());
        account.setPhone(phone);
        account.setSex(dtoAccount.getSex());
        user.setAccount(account);
        // 添加账户状态
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        // 添加权限
        Optional<Role> optionalRole = roleRepository.findFirstByRoleNameIs("ROOT");
        Role role = optionalRole.get();
        user.setRoles(Collections.singleton(role));
        // 保存用户信息
        User save = userRepository.save(user);
        dtoAccount.setUsername(number);
        return Result.success("注册成功", dtoAccount);
    }

    private String generateAccountNumber() {
        return DateTimeFormatter.ofPattern("MMddhhmmss").format(LocalDateTime.now());
    }
}

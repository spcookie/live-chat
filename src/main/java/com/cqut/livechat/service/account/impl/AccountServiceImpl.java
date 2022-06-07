package com.cqut.livechat.service.account.impl;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import com.cqut.livechat.dto.user.AccountDto;
import com.cqut.livechat.dto.user.UserDto;
import com.cqut.livechat.entity.auth.Role;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.entity.user.Account;
import com.cqut.livechat.repository.auth.RoleRepository;
import com.cqut.livechat.repository.auth.UserRepository;
import com.cqut.livechat.service.BaseService;
import com.cqut.livechat.service.account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Result<Void> registeredAccount(UserDto userDto) {
        // 判断用户是否存在
        String username = userDto.getUsername();
        Optional<User> optionalUser = userRepository.findFirstByUsernameIs(username);
        if (optionalUser.isPresent()) {
            return Result.<Void>builder()
                    .code(ResultCode.ERROR)
                    .message("注册失败, 该用户已存在")
                    .build();
        }
        // 封装前端获取的消息
        User user = new User();
        user.setUsername(username);
        String encode = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encode);
        AccountDto accountDto = userDto.getAccount();
        Account account = new Account();
        account.setName(accountDto.getName());
        account.setAge(accountDto.getAge());
        account.setPhone(accountDto.getPhone());
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
        userRepository.save(user);
        return Result.<Void>builder()
                .code(ResultCode.OK)
                .message("用户注册成功")
                .build();
    }
}

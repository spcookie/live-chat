package com.cqut.livechat.service.account;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.user.UserDto;

/**
 * @author Augenstern
 * @date 2022/5/27
 */
public interface AccountService {
    Result<Void> registeredAccount(UserDto userDto);
}

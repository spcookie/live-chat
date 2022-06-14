package com.cqut.livechat.repository.user;

import com.cqut.livechat.entity.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Augenstern
 * @date 2022/5/27
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * 查询对应手机号的用户是否存在
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsAccountByPhoneIs(String phone);
}
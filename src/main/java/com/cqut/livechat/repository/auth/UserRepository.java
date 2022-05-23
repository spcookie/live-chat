package com.cqut.livechat.repository.auth;

import com.cqut.livechat.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /** 通过用户名查询对应用户
     * @param username 用户名
     * @return 用户
     */
    Optional<User> findFirstByUsernameIs(String username);
}
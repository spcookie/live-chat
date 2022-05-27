package com.cqut.livechat.repository.auth;

import com.cqut.livechat.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    /**
     * 根据权限名查询权限
     * @param roleName 权限名
     * @return 权限
     */
    Optional<Role> findFirstByRoleNameIs(String roleName);

}
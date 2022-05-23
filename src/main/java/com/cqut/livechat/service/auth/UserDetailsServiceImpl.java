package com.cqut.livechat.service.auth;

import com.cqut.livechat.constant.Security;
import com.cqut.livechat.entity.auth.Authority;
import com.cqut.livechat.entity.auth.Role;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opUser = userRepository.findFirstByUsernameIs(username);
        if (opUser.isPresent()) {
            User user = opUser.get();
            // 将角色和权限存入Set, 以便序列化
            HashSet<String> auth = user.getAuth();
            Set<Role> roles = user.getRoles();
            roles.forEach(role -> {
                auth.add(Security.ROLE_PREFIX.getVal() + role.getRoleName());
                Set<Authority> authorities = role.getAuthorities();
                authorities.forEach(authority -> auth.add(authority.getAuthorityName()));
            });
            return user;
        } else {
            throw new UsernameNotFoundException("该用户未找到");
        }
    }
}

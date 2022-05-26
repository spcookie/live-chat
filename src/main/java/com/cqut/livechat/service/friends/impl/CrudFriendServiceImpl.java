package com.cqut.livechat.service.friends.impl;

import com.cqut.livechat.dto.user.AccountDto;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.entity.friends.FriendShip;
import com.cqut.livechat.entity.user.Account;
import com.cqut.livechat.repository.auth.UserRepository;
import com.cqut.livechat.repository.friends.FriendRepository;
import com.cqut.livechat.repository.user.AccountRepository;
import com.cqut.livechat.service.BaseService;
import com.cqut.livechat.service.friends.CrudFriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Augenstern
 * @date 2022/5/25
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CrudFriendServiceImpl extends BaseService implements CrudFriendService {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<AccountDto> getAllFriends() {
        // 获取当前登录用户
        User user = super.getLoginUser();
        // 查询当前用户的好友关系
        List<FriendShip> friends = friendRepository.findFriendShip(user);
        // 关系 -> 好友
        return friends.stream().map(friend -> {
            // 从好友关系中获取好友
            User f;
            if (friend.getUser().equals(user)) {
                f = friend.getFriend();
            } else {
                f = friend.getUser();
            }
            // 移除不需要的数据
            AccountDto accountDto = new AccountDto();
            Account account = f.getAccount();
            accountDto.setId(f.getId());
            accountDto.setUsername(f.getUsername());
            accountDto.setName(account.getName());
            accountDto.setPhone(account.getPhone());
            accountDto.setAge(account.getAge());
            return accountDto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean deleteFriendById(long id) {
        User user = super.getLoginUser();
        // 如果存在直接删除
        return friendRepository.deleteFriend(user.getId(), id) > 0;
    }

    @Override
    public boolean addFriendById(long id) {
        User user = super.getLoginUser();
        // 查找是否存在该用户
        Optional<User> optionalFriend = userRepository.findById(id);
        if (optionalFriend.isPresent()) {
            // 创建一个好友关系
            FriendShip friendShip = new FriendShip();
            friendShip.setUser(user);
            friendShip.setFriend(optionalFriend.get());
            // 判断是否已经是好友
            boolean exists = friendRepository.exists(Example.of(friendShip));
            if (exists) {
                // 如果已经是好友关系直接返回
                return true;
            }
            // 保存好友关系
            FriendShip save = friendRepository.save(friendShip);
            return save.getId() != null;
        } else {
            return false;
        }
    }

    @Override
    public AccountDto findOneFriend(AccountDto accountDto) {
        //TODO: 细化查询好友
        Account account = new Account();
        account.setName(accountDto.getName());
        account.setAge(accountDto.getAge());
        account.setPhone(accountDto.getPhone());
        Example<Account> example = Example.of(account);
        Optional<Account> optionalAccount = accountRepository.findOne(example);
        if (optionalAccount.isPresent()) {
            AccountDto dto = new AccountDto();
            Account a = optionalAccount.get();
            dto.setId(a.getId());
            dto.setName(a.getName());
            dto.setPhone(a.getPhone());
            dto.setAge(a.getAge());
            return dto;
        } else {
            return null;
        }
    }

}

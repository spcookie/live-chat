package com.cqut.livechat.service.friends.impl;

import com.cqut.livechat.constant.MessageStatus;
import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.AddFriendMessageDto;
import com.cqut.livechat.dto.message.CommonMessageDto;
import com.cqut.livechat.dto.user.AccountDto;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.entity.friends.FriendShip;
import com.cqut.livechat.entity.message.AddFriendMessage;
import com.cqut.livechat.entity.user.Account;
import com.cqut.livechat.repository.friends.FriendRepository;
import com.cqut.livechat.repository.message.AddFriendMessageRepository;
import com.cqut.livechat.repository.user.AccountRepository;
import com.cqut.livechat.service.BaseService;
import com.cqut.livechat.service.friends.CrudFriendService;
import com.cqut.livechat.socket.ChatSocketCache;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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
    private AccountRepository accountRepository;
    @Autowired
    private AddFriendMessageRepository addFriendMessageRepository;

    @Override
    public List<AccountDto> getAllFriends() {
        // 获取当前登录用户
        Account account = super.getLoginUser().getAccount();
        // 查询当前用户的好友关系
        List<FriendShip> friends = friendRepository.findFriendShip(account);
        // 关系 -> 好友
        return friends.stream().map(friend -> {
            // 从好友关系中获取好友
            Account f;
            if (friend.getUser().equals(account)) {
                f = friend.getFriend();
            } else {
                f = friend.getUser();
            }
            // 移除不需要的数据
            AccountDto accountDto = new AccountDto();
            accountDto.setId(f.getId());
//            accountDto.setUsername(f.getUsername());
            accountDto.setName(f.getName());
            accountDto.setPhone(f.getPhone());
            accountDto.setAge(f.getAge());
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
    public boolean addFriendById(long id, boolean handle) {
        // 查找是否存该好友请求
        Optional<AddFriendMessage> optionalMessage = addFriendMessageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            AddFriendMessage message = optionalMessage.get();
            // 校验是否是该好友请求的接收方
            User user = super.getLoginUser();
            boolean check = message.getTarget().getId().equals(user.getId());
            if (!check) {
                return false;
            }
            // 如何处理好友请求
            if (handle) {
                message.setStatus(MessageStatus.ACCEPT);
            } else {
                // 拒绝直接返回
                message.setStatus(MessageStatus.REJECT);
                return true;
            }
            // 如果接受好友，添加好友关系
            // 获取消息发送方
            Optional<Account> optionalFriend = accountRepository.findById(message.getFrom().getId());
            if (optionalFriend.isPresent()) {
                Account friend = optionalFriend.get();
                // 创建一个好友关系
                FriendShip friendShip = new FriendShip();
                friendShip.setUser(user.getAccount());
                friendShip.setFriend(friend);
                // 判断是否已经是好友
                boolean exists = friendRepository.exists(Example.of(friendShip));
                if (exists) {
                    // 如果已经是好友关系直接返回
                    return true;
                }
                // 保存好友关系
                FriendShip save = friendRepository.save(friendShip);
                // 通知好友添加方添加成功
                CommonMessageDto messageDto = new CommonMessageDto();
                messageDto.setType(MessageType.ACCEPT_ADD_FRIEND);
                ObjectMapper mapper = new ObjectMapper();
                try {
                    String json = mapper.writeValueAsString(messageDto);
                    ChatSocketCache.get(friend.getId()).sendMessage(new TextMessage(json));
                } catch (IOException e) {
                    log.error(e.getLocalizedMessage());
                }
                return save.getId() != null;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<AccountDto> findAllNotFriend(AccountDto accountDto) {
        //TODO: 细化查询用户
        Account account = new Account();
        account.setUsername(accountDto.getUsername());
        account.setName(accountDto.getName());
        account.setAge(accountDto.getAge());
        account.setPhone(accountDto.getPhone());
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher(
                        "username",
                        ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.STARTING)
                );
        Example<Account> example = Example.of(account, exampleMatcher);
        // 查询所有符合条件的用户
        List<Account> accounts = accountRepository.findAll(example);
        // 查询好友关系
        User loginUser = super.getLoginUser();
        Account loginAccount = loginUser.getAccount();
        List<FriendShip> friendShip = friendRepository.findFriendShip(loginAccount);
        List<Account> friends = friendShip.stream().map(val -> {
            if (loginAccount.equals(val.getUser())) {
                return val.getFriend();
            } else {
                return val.getUser();
            }
        }).collect(Collectors.toList());
        if (!accounts.isEmpty()) {
            // 过滤好友
            accounts.removeAll(friends);
            // 移除自己
            accounts.remove(loginAccount);
            return accounts.stream().map(val -> {
                AccountDto dto = new AccountDto();
                dto.setId(val.getId());
                dto.setUsername(val.getUsername());
                dto.setName(val.getName());
                dto.setPhone(val.getPhone());
                dto.setAge(val.getAge());
                return dto;
            }).collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Override
    public List<AddFriendMessageDto> getAllFriendVerifyMessage() {
        Account account = super.getLoginUser().getAccount();
        List<AddFriendMessage> messages = addFriendMessageRepository.findAllByAndTargetIs(account);
        List<AddFriendMessageDto> dtos = new ArrayList<>();
        messages.forEach(message -> {
            AddFriendMessageDto dto = new AddFriendMessageDto();
            dto.setId(message.getId());
            dto.setFrom(message.getFrom());
            dto.setDate(message.getDate());
            dto.setStatus(message.getStatus());
            dtos.add(dto);
        });
        dtos.sort(Comparator.comparingInt(o -> o.getStatus().ordinal()));
        return dtos;
    }
}

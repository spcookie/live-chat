package com.cqut.livechat.repository.message;

import com.cqut.livechat.constant.MessageStatus;
import com.cqut.livechat.entity.message.AddFriendMessage;
import com.cqut.livechat.entity.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Augenstern
 * @date 2022/5/28
 */
public interface AddFriendMessageRepository extends JpaRepository<AddFriendMessage, Long> {

    /**
     * 查找好友请求
     * @param account 登录用户
     * @return 好友请求
     */
    List<AddFriendMessage> findAllByAndTargetIs(Account account);
}
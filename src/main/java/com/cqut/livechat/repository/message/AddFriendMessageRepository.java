package com.cqut.livechat.repository.message;

import com.cqut.livechat.constant.MessageStatus;
import com.cqut.livechat.entity.message.AddFriendMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Augenstern
 * @date 2022/5/28
 */
public interface AddFriendMessageRepository extends JpaRepository<AddFriendMessage, Long> {

    /**
     * 查找对应处理状态的好友请求
     * @param status 处理状态
     * @return 好友请求
     */
    List<AddFriendMessage> findAllByStatusIs(MessageStatus status);
}
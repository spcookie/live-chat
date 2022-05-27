package com.cqut.livechat.repository.message;

import com.cqut.livechat.entity.message.AddFriendMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Augenstern
 * @date 2022/5/28
 */
public interface AddFriendMessageRepository extends JpaRepository<AddFriendMessage, Long> {
}
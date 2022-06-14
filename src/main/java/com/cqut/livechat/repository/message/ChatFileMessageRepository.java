package com.cqut.livechat.repository.message;

import com.cqut.livechat.entity.message.ChatFileMessage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Augenstern
 * @date 2022/6/12
 */
public interface ChatFileMessageRepository extends JpaRepository<ChatFileMessage, Long> {
}
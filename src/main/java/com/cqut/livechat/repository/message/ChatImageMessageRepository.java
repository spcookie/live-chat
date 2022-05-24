package com.cqut.livechat.repository.message;

import com.cqut.livechat.entity.message.ChatImageMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
public interface ChatImageMessageRepository extends JpaRepository<ChatImageMessage, Long>, JpaSpecificationExecutor<ChatImageMessage> {
}
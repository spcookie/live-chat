package com.cqut.livechat.repository.message;

import com.cqut.livechat.entity.message.ChatTextMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
public interface ChatTextMessageRepository extends JpaRepository<ChatTextMessage, Long>, JpaSpecificationExecutor<ChatTextMessage> {

    /**
     * 根据消息id查询对应消息
     * @param ids 消息id
     * @return 文字消息
     */
    List<ChatTextMessage> findAllByIdIn(Set<Long> ids);
}
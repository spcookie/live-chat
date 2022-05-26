package com.cqut.livechat.repository.message;

import com.cqut.livechat.entity.message.ChatImageMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
public interface ChatImageMessageRepository extends JpaRepository<ChatImageMessage, Long>, JpaSpecificationExecutor<ChatImageMessage> {

    /**
     * 根据消息id查询对应消息
     * @param ids 消息id
     * @return 图片消息
     */
    List<ChatImageMessage> findAllByIdIn(Set<Long> ids);
}
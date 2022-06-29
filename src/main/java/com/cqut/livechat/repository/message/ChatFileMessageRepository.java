package com.cqut.livechat.repository.message;

import com.cqut.livechat.entity.message.ChatFileMessage;
import com.cqut.livechat.entity.user.Account;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Augenstern
 * @date 2022/6/12
 */
public interface ChatFileMessageRepository extends JpaRepository<ChatFileMessage, Long> {
    /**
     *  查询历史文件消息
     * @param from id
     * @param target id
     * @param pageRequest 分页
     * @return 消息
     */
    @Query("from ChatFileMessage m where (m.from = ?1 and m.target = ?2) or (m.from = ?2 and m.target = ?1) order by m.date desc")
    List<ChatFileMessage> findHistoryMessages(Account from, Account target, PageRequest pageRequest);
}
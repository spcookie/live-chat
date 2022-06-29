package com.cqut.livechat.repository.message;

import com.cqut.livechat.entity.message.ChatTextMessage;
import com.cqut.livechat.entity.user.Account;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * 获取所有好友未读消息
     * @param account 好友id
     * @return 所有好友的未读消息数
     */
    @Query("from ChatTextMessage m where m.target = :account and m.messageStatus = 4")
    List<ChatTextMessage> findUnreadMessagesForAllFriends(@Param("account") Account account);

    /**
     * 修改未读消息为已读
     * @param account 好友
     * @return 修改状态
     */
    @Modifying
    @Query("update ChatTextMessage m set m.messageStatus = 3 where m.from = :account")
    int modifyMessageStatusRead(@Param("account") Account account);


    /**
     *  查询历史文本消息
     * @param from id
     * @param target id
     * @param text 文本
     * @param pageRequest 分页
     * @return 消息
     */
    @Query("from ChatTextMessage m where ((m.from = ?1 and m.target = ?2) or (m.from = ?2 and m.target = ?1)) and m.text like %?3% order by m.date desc")
    List<ChatTextMessage> findHistoryMessages(Account from, Account target, String text, PageRequest pageRequest);
}
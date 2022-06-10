package com.cqut.livechat.repository.message;

import com.cqut.livechat.entity.message.ChatImageMessage;
import com.cqut.livechat.entity.user.Account;
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
public interface ChatImageMessageRepository extends JpaRepository<ChatImageMessage, Long>, JpaSpecificationExecutor<ChatImageMessage> {

    /**
     * 根据消息id查询对应消息
     * @param ids 消息id
     * @return 图片消息
     */
    List<ChatImageMessage> findAllByIdIn(Set<Long> ids);

    /**
     * 获取所有好友未读消息
     * @param account 好友id
     * @return 所有好友的未读消息数
     */
    @Query("from ChatImageMessage m where m.target = :account and m.messageStatus = 4")
    List<ChatImageMessage> findUnreadMessagesForAllFriends(@Param("account") Account account);

    /**
     * 修改未读消息为已读
     * @param account 好友
     * @return 修改状态
     */
    @Modifying
    @Query("update ChatImageMessage m set m.messageStatus = 3 where m.from = :account")
    int modifyMessageStatusRead(@Param("account") Account account);
}
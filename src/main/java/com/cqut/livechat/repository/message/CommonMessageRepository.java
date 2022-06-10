package com.cqut.livechat.repository.message;

import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.entity.user.Account;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
public interface CommonMessageRepository extends JpaRepository<CommonMessage, Long>, JpaSpecificationExecutor<CommonMessage> {

    /**
     *  根据好友id和分页条件查询聊天记录
     * @param from 消息发送方id
     * @param target 消息接收方id
     * @param sort 排序规则
     * @param pageRequest 分页条件
     * @return 消息列表
     */
    @Query("from CommonMessage m where (m.from = ?1 and m.target = ?2) or (m.from = ?2 and m.target = ?1)")
    List<CommonMessage> findAllByFromIsAndTargetIs(Account from, Account target, Sort sort, PageRequest pageRequest);
}
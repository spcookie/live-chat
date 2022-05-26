package com.cqut.livechat.service.message;

import com.cqut.livechat.dto.message.CommonMessageDto;
import com.cqut.livechat.entity.message.CommonMessage;

import java.util.List;

/**
 * @author Augenstern
 * @date 2022/5/26
 */
public interface HistoricalMessageService {
    /**
     * 获取好友简单消息
     * @param id 好友id
     * @return 简单消息
     */
    List<CommonMessageDto> getSimpleMessage(long id, int page, int size);
}

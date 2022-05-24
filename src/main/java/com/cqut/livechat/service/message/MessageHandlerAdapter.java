package com.cqut.livechat.service.message;

import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.utils.BeanUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Service
public class MessageHandlerAdapter {

    private Map<String, CommonMessageService> handlers;

    /**
     *  根据传入的类型获取一给适配的消息处理器
     * @param type 消息类型
     * @return 消息处理器
     */
    public CommonMessageService adaptation(MessageType type) {
        for (CommonMessageService handler : getHandlers().values()) {
            if (handler.support(type)) {
                return handler;
            }
        }
        return null;
    }

    private Map<String, CommonMessageService> getHandlers() {
        if (ObjectUtils.isEmpty(handlers)) {
            ApplicationContext applicationContext = BeanUtil.getApplicationContext();
            handlers = applicationContext.getBeansOfType(CommonMessageService.class);
        }
        return handlers;
    }
}

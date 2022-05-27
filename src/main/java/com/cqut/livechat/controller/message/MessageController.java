package com.cqut.livechat.controller.message;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import com.cqut.livechat.dto.message.AddFriendMessageDto;
import com.cqut.livechat.dto.message.CommonMessageDto;
import com.cqut.livechat.dto.message.MessageWithTypeDto;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.service.message.MessageService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author Augenstern
 * @date 2022/5/26
 */
@RestController
@RequestMapping("/message")
@Validated
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 加载对应好友历史消息
     * @param id 好友id
     * @param page 页数
     * @param size 长度
     * @return 历史消息
     */
    @GetMapping("/friend/{id}/{page}/{size}")
    public Result<List<CommonMessageDto>> loadFriendMessage(
            @PathVariable("id") @Min(1) long id,
            @PathVariable("page") @Min(0) int page,
            @PathVariable("size") @Range(min = 0, max = 10) int size
    ) {
        List<CommonMessageDto> simpleMessage = messageService.getSimpleMessage(id, page, size);
        ResultCode code = ResultCode.OK;
        String message = "查询id为" + id + "的消息成功";
        if (simpleMessage == null) {
            code = ResultCode.ERROR;
            message = "查询消息非法, 对方不是你的好友";
        }
        return Result.<List<CommonMessageDto>>builder()
                .code(code)
                .message(message)
                .data(simpleMessage)
                .build();
    }

    /**
     * 加载好友验证消息
     * @return 验证消息
     */
    @GetMapping("/friend/verify")
    public Result<List<AddFriendMessageDto>> loadFriendVerify() {
        return null;
    }

    /**
     * 发送消息通用接口
     * @param message 通用公共消息
     * @return 消息发送状态
     */
    @PutMapping("/send")
    public Result<String> sendMessage(MessageWithTypeDto<CommonMessage> message) {
        String result = messageService.sendMessage(message);
        return Result.<String>builder()
                .code(ResultCode.OK)
                .message("消息发送状态")
                .data(result)
                .build();
    }

}

package com.cqut.livechat.controller.message;

import com.cqut.livechat.config.para.Message;
import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
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

    @GetMapping("/friend/{id}/{page}/{length}")
    public Result<List<CommonMessageDto>> loadFriendMessage(
            @PathVariable("id") @Min(1) long id,
            @PathVariable("page") @Min(0) int page,
            @PathVariable("length") @Range(min = 0, max = 10) int size
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

    @PutMapping("/send")
    public Result<String> sendMessage(@Message MessageWithTypeDto<CommonMessage> messageWithType) {
        String result = messageService.sendMessage(messageWithType);
        return Result.<String>builder()
                .code(ResultCode.OK)
                .message("消息发送状态")
                .data(result)
                .build();
    }

}

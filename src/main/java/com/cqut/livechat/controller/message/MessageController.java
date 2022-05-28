package com.cqut.livechat.controller.message;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import com.cqut.livechat.dto.message.*;
import com.cqut.livechat.entity.message.AddFriendMessage;
import com.cqut.livechat.entity.message.ChatTextMessage;
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

    @GetMapping("/friend/{id}/{page}/{size}")
    public Result<List<CommonMessageDto>> loadFriendMessage(
            @PathVariable("id") @Min(1) long id,
            @PathVariable("page") @Min(0) int page,
            @PathVariable("size") @Range(min = 0, max = 10) int size
    ) {
        List<CommonMessageDto> simpleMessage = messageService.getSimpleMessage(id, page, size);
        if (simpleMessage == null) {
            return Result.error("查询消息非法, 对方不是你的好友", null);
        } else {
            return Result.success("查询id为" + id + "的消息成功", simpleMessage);
        }
    }

    @GetMapping("/friend/verify")
    public Result<List<AddFriendMessageDto>> loadFriendVerify() {
        return null;
    }

    @PutMapping("/send/text")
    public Result<Void> sendTextMessage(@RequestBody @Validated ChatTextMessageDto message) {
        String result = messageService.sendTextMessage(message);
        return Result.success(result, null);
    }

    @PutMapping("/send/image")
    public Result<String> sendImageMessage(@RequestBody @Validated ChatImageMessageDto message) {
        String result = messageService.sendImageMessage(message);
        return Result.success(result, null);
    }

    @PutMapping("/send/addFriend/{target}")
    public Result<String> sendAddFriendMessage(@PathVariable("target") long id) {
        AddFriendMessageDto messageDto = new AddFriendMessageDto();
        messageDto.setTarget(id);
        String result = messageService.sendAddFriendMessage(messageDto);
        return Result.success(result, null);
    }
}

package com.cqut.livechat.controller.message;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.message.*;
import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.entity.user.Account;
import com.cqut.livechat.service.message.MessageService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

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

    @PutMapping("/send/text")
    public Result<MessageSendStatusDto> sendTextMessage(@RequestBody @Validated ChatTextMessageDto message) {
        MessageSendStatusDto result = messageService.sendTextMessage(message);
        return Result.success(null, result);
    }

    @PutMapping("/send/image")
    public Result<MessageSendStatusDto> sendImageMessage(@RequestBody @Validated ChatImageMessageDto message) {
        MessageSendStatusDto result = messageService.sendImageMessage(message);
        return Result.success(null, result);
    }

    @PutMapping("/send/addFriend/{target}")
    public Result<MessageSendStatusDto> sendAddFriendMessage(@PathVariable("target") long id) {
        AddFriendMessageDto messageDto = new AddFriendMessageDto();
        Account account = new Account();
        account.setId(id);
        messageDto.setTarget(account);
        MessageSendStatusDto result = messageService.sendAddFriendMessage(messageDto);
        return Result.success(null, result);
    }

    @GetMapping("/unread/count")
    public Result<Map<Long, Integer>> getAllUnreadCount() {
        Map<Long, Integer> count = messageService.getAllUnreadCount();
        return Result.success("查询成功", count);
    }

    @PutMapping("/unread/confirm/{id}")
    public Result<Boolean> confirmUnread(@PathVariable("id") long id) {
        boolean statue = messageService.confirmationMessage(id);
        return statue
                ? Result.success("", true)
                : Result.error("", false);
    }
}

package com.cqut.livechat.controller.message;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.message.*;
import com.cqut.livechat.entity.user.Account;
import com.cqut.livechat.service.message.MessageService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @PostMapping("/friend/history")
    public Result<List<? extends CommonMessageDto>> queryHistoricalMessages(@RequestBody QueryMessageDto queryMessage) {
        List<? extends CommonMessageDto> historyMessages = messageService.findHistoryMessages(queryMessage);
        return Objects.isNull(historyMessages)
                ? Result.error("没有此类型的消息", null)
                : Result.success("查询历史消息成功", historyMessages);
    }

    @GetMapping("/friend/{id}/{page}/{size}")
    public Result<List<CommonMessageDto>> loadFriendMessage(
            @PathVariable("id") @Min(1) long id,
            @PathVariable("page") @Min(0) int page,
            @PathVariable("size") @Range(min = 0, max = 50, message = "查询条数在0-50之间") int size
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

    @PostMapping("/send/file")
    public Result<MessageSendStatusDto> uploadFile(
            @RequestParam("target") @Min(0) Long id,
            @RequestPart("file") @NotNull MultipartFile multipartFile
    ) {
        MessageSendStatusDto dto = messageService.sendFileMessage(id, multipartFile);
        return Result.success(null, dto);
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

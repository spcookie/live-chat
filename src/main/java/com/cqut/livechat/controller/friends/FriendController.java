package com.cqut.livechat.controller.friends;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import com.cqut.livechat.dto.message.AddFriendMessageDto;
import com.cqut.livechat.dto.user.AccountDto;
import com.cqut.livechat.service.friends.CrudFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * @author Augenstern
 * @date 2022/5/25
 */
@RestController
@RequestMapping("/friend")
@Validated
public class FriendController {

    @Autowired
    private CrudFriendService crudFriendService;

    @GetMapping("/load/friendList")
    public Result<List<AccountDto>> loadAllFriends() {
        List<AccountDto> friends = crudFriendService.getAllFriends();
        return Result.success("加载好友成功", friends);
    }

    @GetMapping("/load/friendVerify")
    public Result<List<AddFriendMessageDto>> loadAllFriendVerifyMessage() {
        List<AddFriendMessageDto> messages = crudFriendService.getAllFriendVerifyMessage();
        return Result.success("获取好友验证消息成功", messages);
    }

    @PutMapping("/{id}/{handle}")
    public Result<Boolean> addFriend(@PathVariable("id") @Min(1) long id, @PathVariable("handle") boolean handle) {
        boolean b = crudFriendService.addFriendById(id, handle);
        if (b) {
            return Result.success("处理成功", null);
        } else {
            return Result.error("处理失败", null);
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteFriend(@PathVariable("id") @Min(1) long id) {
        boolean b = crudFriendService.deleteFriendById(id);
        if (b) {
            return Result.success("删除成功", null);
        } else {
            return Result.error("删除失败", null);
        }
    }

    @PostMapping("/find")
    public Result<AccountDto> findFriend(@RequestBody @Validated AccountDto accountDto) {
        AccountDto friend = crudFriendService.findOneFriend(accountDto);
        return Result.success("查找成功", friend);
    }
}

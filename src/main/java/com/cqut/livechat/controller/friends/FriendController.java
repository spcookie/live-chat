package com.cqut.livechat.controller.friends;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import com.cqut.livechat.dto.user.AccountDto;
import com.cqut.livechat.entity.user.Account;
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

    @RequestMapping(value = "/load", method = RequestMethod.GET)
    public Result<List<AccountDto>> loadAllFriends() {
        List<AccountDto> friends = crudFriendService.getAllFriends();
        return Result.<List<AccountDto>>builder()
                .code(ResultCode.OK)
                .message("加载好友成功")
                .data(friends)
                .build();
    }

    @GetMapping("/{id}")
    public Result<Boolean> addFriend(@PathVariable("id") @Min(1) long id) {
        boolean b = crudFriendService.addFriendById(id);
        Result<Boolean> state;
        if (b) {
            state = Result.<Boolean>builder().code(ResultCode.OK).message("添加成功").data(true).build();
        } else {
            state = Result.<Boolean>builder().code(ResultCode.ERROR).message("添加失败").data(false).build();
        }
        return state;
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteFriend(@PathVariable("id") @Min(1) long id) {
        boolean b = crudFriendService.deleteFriendById(id);
        Result<Boolean> state;
        if (b) {
            state = Result.<Boolean>builder().code(ResultCode.OK).message("删除成功").data(true).build();
        } else {
            state = Result.<Boolean>builder().code(ResultCode.ERROR).message("删除失败").data(false).build();
        }
        return state;
    }

    @PostMapping("/find")
    public Result<AccountDto> findFriend(@RequestBody @Validated AccountDto accountDto) {
        AccountDto friend = crudFriendService.findOneFriend(accountDto);
        return Result.<AccountDto>builder()
                .code(ResultCode.OK)
                .message("查找成功")
                .data(friend)
                .build();
    }
}

package com.cqut.livechat.service.friends;

import com.cqut.livechat.dto.message.AddFriendMessageDto;
import com.cqut.livechat.dto.user.AccountDto;

import java.util.List;

/**
 * @author Augenstern
 * @date 2022/5/25
 */
public interface CrudFriendService {
    /**
     *  加载好友列表
     * @return 好友集合
     */
    List<AccountDto> getAllFriends();

    /**
     *  根据Id删除好友
     * @param id 好友id
     * @return 删除状态
     */
    boolean deleteFriendById(long id);

    /**
     *  添加一个好友
     * @param id 好友验证消息id
     * @param handle 接受or拒绝
     * @return 添加是否成功
     */
    boolean addFriendById(long id, boolean handle);

    /**
     *  根据提供的样例查找好友
     * @param user 样例
     * @return 查找到的用户
     */
    AccountDto findOneFriend(AccountDto user);

    /**
     * 获取所有待处理的添加好友请求
     * @return 好友请求
     */
    List<AddFriendMessageDto> getAllFriendVerifyMessage();
}

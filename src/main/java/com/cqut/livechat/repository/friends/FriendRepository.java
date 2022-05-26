package com.cqut.livechat.repository.friends;

import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.entity.friends.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Augenstern
 * @date 2022/5/25
 */
public interface FriendRepository extends JpaRepository<FriendShip, Long>, JpaSpecificationExecutor<FriendShip> {

    /**
     * 获取用户的好友关系
     * @param user 当前用户
     * @return 好友关系
     */
    @Query("from FriendShip f where f.user = ?1 or f.friend = ?1")
    List<FriendShip> findFriendShip(User user);

    /**
     * 根据id删除好友
     * @param id1 当前用户id
     * @param id2 好友id
     * @return 是否删除成功
     */
    @Modifying
    @Query(
            nativeQuery = true,
            value = "delete from chat_friend_ship " +
                    "where (chat_user_id = ?1 and chat_friend_id = ?2) or (chat_user_id = ?2 and chat_friend_id = ?1)"
    )
    int deleteFriend(long id1, long id2);
}
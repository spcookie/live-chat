package com.cqut.livechat.redis.auth;

import com.cqut.livechat.entity.auth.User;
import com.cqut.livechat.utils.RedisUtil;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
public class UserRedisUtil {

    public static final String REDIS_KEY_PREFIX_USER = "auth:user:";

    public static void saveUser(long id, User user, long exp) {
        String key = REDIS_KEY_PREFIX_USER + id;
        RedisUtil.setEx(key, user, exp);
    }

    public static User getUser(long id) {
        String key = REDIS_KEY_PREFIX_USER + id;
        return RedisUtil.get(key, User.class);
    }

    public static void delUser(long id) {
        String key = REDIS_KEY_PREFIX_USER + id;
        RedisUtil.del(key);
    }
}

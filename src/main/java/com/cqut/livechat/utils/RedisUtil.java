package com.cqut.livechat.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author Augenstern
 * @date 2022/4/3
 */
public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate;

    public static RedisTemplate<String, Object> getRedisTemplate() {
        // 从spring容器中获取redisTemplate
        if (ObjectUtils.isEmpty(redisTemplate)) {
            redisTemplate = BeanUtil.getBean("redisTemplate", RedisTemplate.class);
        }
        return redisTemplate;
    }

    public static void set(String key, Object value) {
        RedisTemplate<String, Object> redisTemplate = RedisUtil.getRedisTemplate();
        redisTemplate.opsForValue().set(key, value);
    }

    public static void setEx(String key, Object value, long timeout) {
        RedisTemplate<String, Object> redisTemplate = RedisUtil.getRedisTemplate();
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public static <T> T get(String key, Class<T> clazz) {
        RedisTemplate<String, Object> redisTemplate = RedisUtil.getRedisTemplate();
        Object o = redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(o)) {
            return null;
        }
        return clazz.cast(o);
    }

    public static void del(String key) {
        RedisTemplate<String, Object> redisTemplate = RedisUtil.getRedisTemplate();
        redisTemplate.delete(key);
    }
}

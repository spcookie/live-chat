package com.cqut.livechat.utils;

import cn.hutool.jwt.JWT;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
public class TokenUtil {

    /**
     * 用于加密的密钥
     */
    private static final byte[] KEY = "QWERTYUIOP".getBytes(StandardCharsets.UTF_8);
    /**
     * Token过期时间设置为一天
     */
    private static final Long EXPIRES = 1000 * 60 * 60 * 24L;
    private static final String ID = "id";

    public static String generateTokenWithId(Long id) {
        long now = System.currentTimeMillis();
        Date exp = new Date(now + EXPIRES);
        return JWT.create()
                .setExpiresAt(exp)
                .setPayload(TokenUtil.ID, id)
                .setKey(TokenUtil.KEY)
                .sign();
    }

    public static boolean verifyToken(String token) {
        return JWT.of(token).setKey(TokenUtil.KEY).verify();
    }

    public static int getIdFromToken(String token) {
        JWT jwt = JWT.of(token);
        return (int) jwt.getPayload(TokenUtil.ID);
    }

    public static int getExpFromToken(String token) {
        JWT jwt = JWT.of(token);
        return (int) jwt.getPayload("exp");
    }
}

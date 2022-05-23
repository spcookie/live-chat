package com.cqut.livechat.constant;

/**
 * @author Augenstern
 * @date 2022/5/23
 */
public enum Security {
    /**
     * 权限验证
     */
    AUTHORIZATION("Authorization"),
    /**
     * 登录用户
     */
    USER("user"),
    /**
     * 角色前缀
     */
    ROLE_PREFIX("ROLE_"),
    ;

    private final String VAL;

    Security(String val) {
        this.VAL = val;
    }

    public String getVal() {
        return VAL;
    }
}

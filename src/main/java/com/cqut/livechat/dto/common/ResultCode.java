package com.cqut.livechat.dto.common;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
public enum ResultCode {
    /**
     *  成功的状态
     */
    OK(200),
    /**
     * 失败的状态
     */
    ERROR(500),
    /**
     * 没有权限
     */
    NO_PERMISSION(401),
    ;

    /**
     *  状态码
     */
    private final Integer val;

    ResultCode(int val) {
        this.val = val;
    }

    @JSONField(name = "code")
    @JsonValue
    public Integer getVal() {
        return val;
    }

    @Override
    public String toString() {
        return "ResultCode(" +
                "val=" + val +
                ") " + super.toString();
    }
}

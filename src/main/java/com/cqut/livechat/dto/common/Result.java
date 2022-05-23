package com.cqut.livechat.dto.common;

import lombok.Builder;
import lombok.Data;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
@Data
@Builder
public class Result<T> {
    private ResultCode code;
    private String message;
    private T data;
}

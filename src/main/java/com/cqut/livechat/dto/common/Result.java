package com.cqut.livechat.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
    private ResultCode code;
    private String message;
    private T data;

    public static <E> Result<E> success(String message, E data) {
        return Result.<E>builder().code(ResultCode.OK).message(message).data(data).build();
    }

    public static <E> Result<E> error(String message, E data) {
        return Result.<E>builder().code(ResultCode.ERROR).message(message).data(data).build();
    }
}

package com.cqut.livechat.controller;

import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author Augenstern
 * @date 2022/5/25
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandleController {

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<String> illegalParameterHandler(Exception e) {
        ConstraintViolationException violationException = (ConstraintViolationException) e;
        log.error(violationException.getLocalizedMessage());
        return Result.<String>builder()
                .code(ResultCode.ERROR)
                .message("参数校验失败")
                .data(violationException.getLocalizedMessage())
                .build();
    }
}

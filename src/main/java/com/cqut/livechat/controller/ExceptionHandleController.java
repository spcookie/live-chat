package com.cqut.livechat.controller;

import com.cqut.livechat.LiveChatException;
import com.cqut.livechat.dto.common.Result;
import com.cqut.livechat.dto.common.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @author Augenstern
 * @date 2022/5/25
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandleController {

    @ExceptionHandler({ConstraintViolationException.class, BindException.class, MethodArgumentNotValidException.class})
    public Result<String> illegalParameterHandler(Exception e) {
        String message;
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException violationException = (ConstraintViolationException) e;
            message = violationException.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException argumentNotValidException = (MethodArgumentNotValidException) e;
            message = argumentNotValidException.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
        } else {
            BindException bindException = (BindException) e;
            message = bindException.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining("; "));
        }
        return Result.<String>builder()
                .code(ResultCode.PARAMETER_VERIFICATION_FAILED)
                .message("参数校验失败")
                .data(message)
                .build();
    }

    @ExceptionHandler(LiveChatException.class)
    public Result<String> liveChatException(Exception e) {
        return Result.error(e.getMessage(), null);
    }
}

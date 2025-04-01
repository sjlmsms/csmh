package com.example.hello.exception;

import com.example.hello.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        logger.error("参数校验失败", e);
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error("参数校验失败: " + message);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        logger.error("系统异常", e);
        return Result.error("系统异常: " + e.getMessage());
    }
} 
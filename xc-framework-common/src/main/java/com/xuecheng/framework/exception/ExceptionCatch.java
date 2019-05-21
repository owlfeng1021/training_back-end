package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionCatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    //定义map 配置异常类型所对应的类型
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //定义map的builder对象 构建
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    // 获取所有关于CustomException的类
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customExceptionHandler(CustomException customException) {
        LOGGER.error("catch exception:{}", customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult otherExceptionHandler(Exception exception) {
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();
        }
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        if (resultCode != null) {
            return new ResponseResult(resultCode);
        } else {
            LOGGER.error("catch exception:{}", exception.getMessage());
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }

    static {
        // 定义异常对象 相应的数据
        builder.put(HttpMediaTypeNotSupportedException.class, CommonCode.INVALID_PARAM);
    }
}

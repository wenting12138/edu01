package com.xc.ommon.exception;

import com.google.common.collect.ImmutableBiMap;
import com.xc.ommon.model.response.CommonCode;
import com.xc.ommon.model.response.ResponseResult;
import com.xc.ommon.model.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class SelfExceptionHandler {

    private static ImmutableBiMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    protected static ImmutableBiMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableBiMap.builder();
    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID);
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException c){
        ResultCode resultCode = c.resultCode;
        log.error("[catch CustomException], {}", c.getMessage());
        return new ResponseResult(resultCode);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){
        log.error("[catch Exception], {}", e.getMessage());
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();  // 构建成功
        }
        if (EXCEPTIONS.containsKey(e.getClass())){
            return new ResponseResult(EXCEPTIONS.get(e.getClass()));
        }
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }



}

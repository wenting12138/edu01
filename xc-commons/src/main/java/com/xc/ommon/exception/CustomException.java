package com.xc.ommon.exception;

import com.xc.ommon.model.response.ResultCode;

public class CustomException extends RuntimeException {

    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        super("错误代码: " + resultCode.code() + "  错误信息: " + resultCode.message());
        this.resultCode = resultCode;
    }

}

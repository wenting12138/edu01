package com.xc.ommon.exception;

import com.xc.ommon.model.response.ResultCode;

public class ExceptionCast {

    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }

}

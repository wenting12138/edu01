package com.xc.model.learning;

import com.xc.ommon.model.response.ResultCode;
import lombok.ToString;

@ToString
public enum  LearningCode implements ResultCode {

    LEARNING_GETMEDIA_ERROR(false,23001, "获取学习地址失败");

    boolean success;
    String message;
    int code;


    LearningCode(boolean b, int s, String msg) {
        this.code = s;
        this.message = msg;
        this.success = b;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}

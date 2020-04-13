package com.xc.ommon.model.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryResponseResult<T> extends ResponseResult {

    QueryResult<T> queryResult;

    T data;
    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
       this.queryResult = queryResult;
    }
    public QueryResponseResult(ResultCode resultCode,T data){
        super(resultCode);
        this.data = data;
    }
}

package com.jh.entity;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class ResultData<T> implements Serializable {

    //请求码
    private int code = Code.OK;
    //响应的消息
    private String msg;
    //响应的数据部分
    private T data;

    public static interface Code{
        //请求成功处理并返回
        int OK = 200;
        //请求错误
        int ERROR = 300;
        //成功登录
        int TOLOGIN=555;
    }
}

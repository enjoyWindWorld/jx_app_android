package com.jx.maneger.intf;

public interface ResponseResult {
    void resSuccess(Object object); //成功

    void resFailure(int statusCode, String message); //失败
}

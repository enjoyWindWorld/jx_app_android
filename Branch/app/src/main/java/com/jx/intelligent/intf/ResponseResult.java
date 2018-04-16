package com.jx.intelligent.intf;

public interface ResponseResult {
    void resSuccess(Object object); //成功

    void resFailure(String message); //失败
}

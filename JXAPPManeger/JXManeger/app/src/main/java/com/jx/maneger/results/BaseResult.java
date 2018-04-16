package com.jx.maneger.results;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class BaseResult implements Serializable {

    private int result;
    private int errcode;
    private String msg;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

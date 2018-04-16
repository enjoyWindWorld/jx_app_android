package com.jx.intelligent.result;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/5 0005.
 */

public class NormalResult implements Serializable{
    private String result;
    private String errcode;
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

package com.jx.intelligent.result;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class BaseResult implements Serializable {

    private String errcode;
    private String msg;

    public String getErrcode() {
        return errcode;
    }

    public String getMsg() {
        return msg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    private String result;


}

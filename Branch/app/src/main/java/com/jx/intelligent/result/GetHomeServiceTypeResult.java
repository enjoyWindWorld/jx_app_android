package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/1 0001.
 */

public class GetHomeServiceTypeResult {
    private String errcode;
    private String msg;
    private List<ServiceFlBean> data = new ArrayList<ServiceFlBean>();
    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<ServiceFlBean> getData() {
        return data;
    }

    public void setData(List<ServiceFlBean> data) {
        this.data = data;
    }

    public class ServiceFlBean{
        public String menu_name;
        private String menu_icourl;
        private String id;

        public String getMenu_name() {
            return menu_name;
        }

        public void setMenu_name(String menu_name) {
            this.menu_name = menu_name;
        }

        public String getMenu_icourl() {
            return menu_icourl;
        }

        public void setMenu_icourl(String menu_icourl) {
            this.menu_icourl = menu_icourl;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

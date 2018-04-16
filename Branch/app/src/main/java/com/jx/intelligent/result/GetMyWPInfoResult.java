package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24 0024.
 * 登录请求结果类
 */

public class GetMyWPInfoResult extends BaseResult {

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    private List<Data> data = new ArrayList<Data>();


    public class Data {
        private String pp;
        private String cto;
        private String ro;
        private String t33;
        private String wfr;

        public String getPp() {
            return pp;
        }

        public void setPp(String pp) {
            this.pp = pp;
        }

        public String getCto() {
            return cto;
        }

        public void setCto(String cto) {
            this.cto = cto;
        }

        public String getRo() {
            return ro;
        }

        public void setRo(String ro) {
            this.ro = ro;
        }

        public String getT33() {
            return t33;
        }

        public void setT33(String t33) {
            this.t33 = t33;
        }

        public String getWfr() {
            return wfr;
        }

        public void setWfr(String wfr) {
            this.wfr = wfr;
        }
    }
}

package com.jx.maneger.results;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24 0024.
 * 注册请求结果类
 */

public class RegisterResult extends BaseResult {

    private List<Data> data = new ArrayList<Data>();

   
    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {
        private String PAR_AREA;
        private String ispermissions;
        private String parParentid;
        private String PAR_PARENT;
        private String PAR_PHONE;
        private String id;
        private String PAR_NAME;
        private String par_pact;
        private String PAR_ADDRESS;
        private String PAR_OTHER;
        private String par_sellernum;
        private String PAR_LEVEL;
        private String par_shop;

        public String getPAR_AREA() {
            return PAR_AREA;
        }

        public void setPAR_AREA(String PAR_AREA) {
            this.PAR_AREA = PAR_AREA;
        }

        public String getIspermissions() {
            return ispermissions;
        }

        public void setIspermissions(String ispermissions) {
            this.ispermissions = ispermissions;
        }

        public String getParParentid() {
            return parParentid;
        }

        public void setParParentid(String parParentid) {
            this.parParentid = parParentid;
        }

        public String getPAR_PARENT() {
            return PAR_PARENT;
        }

        public void setPAR_PARENT(String PAR_PARENT) {
            this.PAR_PARENT = PAR_PARENT;
        }

        public String getPAR_PHONE() {
            return PAR_PHONE;
        }

        public void setPAR_PHONE(String PAR_PHONE) {
            this.PAR_PHONE = PAR_PHONE;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPAR_NAME() {
            return PAR_NAME;
        }

        public void setPAR_NAME(String PAR_NAME) {
            this.PAR_NAME = PAR_NAME;
        }

        public String getPar_pact() {
            return par_pact;
        }

        public void setPar_pact(String par_pact) {
            this.par_pact = par_pact;
        }

        public String getPAR_ADDRESS() {
            return PAR_ADDRESS;
        }

        public void setPAR_ADDRESS(String PAR_ADDRESS) {
            this.PAR_ADDRESS = PAR_ADDRESS;
        }

        public String getPAR_OTHER() {
            return PAR_OTHER;
        }

        public void setPAR_OTHER(String PAR_OTHER) {
            this.PAR_OTHER = PAR_OTHER;
        }

        public String getPar_sellernum() {
            return par_sellernum;
        }

        public void setPar_sellernum(String par_sellernum) {
            this.par_sellernum = par_sellernum;
        }

        public String getPAR_LEVEL() {
            return PAR_LEVEL;
        }

        public void setPAR_LEVEL(String PAR_LEVEL) {
            this.PAR_LEVEL = PAR_LEVEL;
        }

        public String getPar_shop() {
            return par_shop;
        }

        public void setPar_shop(String par_shop) {
            this.par_shop = par_shop;
        }
    }
}

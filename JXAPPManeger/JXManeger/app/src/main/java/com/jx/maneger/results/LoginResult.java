package com.jx.maneger.results;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24 0024.
 * 登录请求结果类
 */

public class LoginResult extends BaseResult {

    private List<Data> data = new ArrayList<Data>();

   
    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {
        private String partnerNumber;//编号
        private String username;//名字
        private String level;//级别
        private String ParParentName;//上级合伙人姓名
        private String unboundedalipay;//没绑定支付宝
        private String ParParentid;//上级合伙人编号
        private String usernum;//销售台数
        private String safetyMark;//安全标识
        private String originalpassword;//重置密码
        private String operator;//0,可以升级为运营商，-1是未达到

        public String getPartnerNumber() {
            return partnerNumber;
        }

        public void setPartnerNumber(String partnerNumber) {
            this.partnerNumber = partnerNumber;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getParParentName() {
            return ParParentName;
        }

        public void setParParentName(String parParentName) {
            ParParentName = parParentName;
        }

        public String getUnboundedalipay() {
            return unboundedalipay;
        }

        public void setUnboundedalipay(String unboundedalipay) {
            this.unboundedalipay = unboundedalipay;
        }

        public String getParParentid() {
            return ParParentid;
        }

        public void setParParentid(String parParentid) {
            ParParentid = parParentid;
        }

        public String getUsernum() {
            return usernum;
        }

        public void setUsernum(String usernum) {
            this.usernum = usernum;
        }

        public String getSafetyMark() {
            return safetyMark;
        }

        public void setSafetyMark(String safetyMark) {
            this.safetyMark = safetyMark;
        }

        public String getOriginalpassword() {
            return originalpassword;
        }

        public void setOriginalpassword(String originalpassword) {
            this.originalpassword = originalpassword;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }
    }
}

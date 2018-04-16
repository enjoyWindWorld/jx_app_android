package com.jx.maneger.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 提现记录
 * Created by Administrator on 2017/8/11.
 */

public class WidthdrawalRecordResult extends NormalResult{

    public List<Data> data = new ArrayList<Data>();
    public List<Data> getData() {
        return data;
    }
    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable
    {
        private int w_id;//订单id
        private String user_number;//产品经理编号
        private String real_name;//姓名
        private String withdrawal_order;//提现订单号
        private float withdrawal_amount;//总金额
        private int withdrawal_way;//支付方式
        private int withdrawal_state;//状态
        private String withdrawal_reason;//失败原因
        private String pay_name;//支付宝账号名
        private String pay_account;//支付宝账号
        private String add_time;//提现发起时间
        private String audit_time;//审核时间
        private String arrive_time;//到账时间
        private String last_modtime;//最后更新时间

        public int getW_id() {
            return w_id;
        }

        public void setW_id(int w_id) {
            this.w_id = w_id;
        }

        public String getUser_number() {
            return user_number;
        }

        public void setUser_number(String user_number) {
            this.user_number = user_number;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getWithdrawal_order() {
            return withdrawal_order;
        }

        public void setWithdrawal_order(String withdrawal_order) {
            this.withdrawal_order = withdrawal_order;
        }

        public float getWithdrawal_amount() {
            return withdrawal_amount;
        }

        public void setWithdrawal_amount(float withdrawal_amount) {
            this.withdrawal_amount = withdrawal_amount;
        }

        public int getWithdrawal_way() {
            return withdrawal_way;
        }

        public void setWithdrawal_way(int withdrawal_way) {
            this.withdrawal_way = withdrawal_way;
        }

        public int getWithdrawal_state() {
            return withdrawal_state;
        }

        public void setWithdrawal_state(int withdrawal_state) {
            this.withdrawal_state = withdrawal_state;
        }

        public String getWithdrawal_reason() {
            return withdrawal_reason;
        }

        public void setWithdrawal_reason(String withdrawal_reason) {
            this.withdrawal_reason = withdrawal_reason;
        }

        public String getPay_name() {
            return pay_name;
        }

        public void setPay_name(String pay_name) {
            this.pay_name = pay_name;
        }

        public String getPay_account() {
            return pay_account;
        }

        public void setPay_account(String pay_account) {
            this.pay_account = pay_account;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getAudit_time() {
            return audit_time;
        }

        public void setAudit_time(String audit_time) {
            this.audit_time = audit_time;
        }

        public String getArrive_time() {
            return arrive_time;
        }

        public void setArrive_time(String arrive_time) {
            this.arrive_time = arrive_time;
        }

        public String getLast_modtime() {
            return last_modtime;
        }

        public void setLast_modtime(String last_modtime) {
            this.last_modtime = last_modtime;
        }
    }
}

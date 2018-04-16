package com.jx.maneger.results;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现比例
 * Created by Administrator on 2017/9/14.
 */

public class CashWithdrawalRatioResult extends BaseResult{

    List<Data> data = new ArrayList<>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private float install;
        private float service_fee;
        private float total_rebate;
        private String username;

        public float getInstall() {
            return install;
        }

        public void setInstall(float install) {
            this.install = install;
        }

        public float getService_fee() {
            return service_fee;
        }

        public void setService_fee(float service_fee) {
            this.service_fee = service_fee;
        }

        public float getTotal_rebate() {
            return total_rebate;
        }

        public void setTotal_rebate(float total_rebate) {
            this.total_rebate = total_rebate;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}

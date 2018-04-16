package com.jx.maneger.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class WithdrawalAmountResult extends BaseResult {

    private List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable
    {
        private float withdrawal_total_amount;
        private String withdrawalOrderNo;

        public float getWithdrawal_total_amount() {
            return withdrawal_total_amount;
        }

        public void setWithdrawal_total_amount(float withdrawal_total_amount) {
            this.withdrawal_total_amount = withdrawal_total_amount;
        }

        public String getWithdrawalOrderNo() {
            return withdrawalOrderNo;
        }

        public void setWithdrawalOrderNo(String withdrawalOrderNo) {
            this.withdrawalOrderNo = withdrawalOrderNo;
        }
    }
}

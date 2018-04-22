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
        private float my_balance;
        private float myTotalIncome;
        private float myWithdrawalLimit;
        private String withdrawalOrderNo;

        public float getMy_balance() {
            return my_balance;
        }

        public void setMy_balance(float my_balance) {
            this.my_balance = my_balance;
        }

        public float getMyTotalIncome() {
            return myTotalIncome;
        }

        public void setMyTotalIncome(float myTotalIncome) {
            this.myTotalIncome = myTotalIncome;
        }

        public float getMyWithdrawalLimit() {
            return myWithdrawalLimit;
        }

        public void setMyWithdrawalLimit(float myWithdrawalLimit) {
            this.myWithdrawalLimit = myWithdrawalLimit;
        }

        public String getWithdrawalOrderNo() {
            return withdrawalOrderNo;
        }

        public void setWithdrawalOrderNo(String withdrawalOrderNo) {
            this.withdrawalOrderNo = withdrawalOrderNo;
        }
    }
}

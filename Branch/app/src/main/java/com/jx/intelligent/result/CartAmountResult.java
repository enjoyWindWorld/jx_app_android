package com.jx.intelligent.result;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class CartAmountResult extends NormalResult {


    /**
     * data : [{"sum":4}]
     * errcode : 0
     * result : 0
     */


    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sum : 4
         */

        private int sum;

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }
    }
}

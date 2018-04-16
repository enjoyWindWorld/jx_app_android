package com.jx.intelligent.result;

import java.util.List;

/**
 * Created by Administrator on 2017/6/16 0016.
 */

public class AddToShopPingCartResult extends NormalResult {

    /**
     * result : 0
     * errcode : 0
     * data : [{"sum":4}]
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

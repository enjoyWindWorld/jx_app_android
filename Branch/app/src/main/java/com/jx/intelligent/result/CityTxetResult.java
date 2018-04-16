package com.jx.intelligent.result;

import java.util.List;

/**
 * Created by 王云 on 2017/5/19 0019.
 */

public class CityTxetResult extends NormalResult {



    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * user_number : 用户数量:18
         */

        private String user_number;

        public String getUser_number() {
            return user_number;
        }

        public void setUser_number(String user_number) {
            this.user_number = user_number;
        }
    }
}

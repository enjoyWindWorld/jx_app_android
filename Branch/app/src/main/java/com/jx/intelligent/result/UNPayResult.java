package com.jx.intelligent.result;

import java.util.List;

/**
 * Created by 王云 on 2017/4/26 0026.
 */

public class UNPayResult extends NormalResult {


    public List<PayInfo> getData()
    {
        return data;
    }

    public void setData(List<PayInfo> data) {
        this.data = data;
    }

    List<PayInfo> data;



   public class PayInfo
    {
        /**
         * tn : 716797144058837462800
         */

        private String tn;

        public String getTn() {
            return tn;
        }

        public void setTn(String tn) {
            this.tn = tn;
        }
    }
}

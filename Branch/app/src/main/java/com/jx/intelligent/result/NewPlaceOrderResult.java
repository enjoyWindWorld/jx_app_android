package com.jx.intelligent.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class NewPlaceOrderResult extends NormalResult {

    /**
     * data : [{"context":"台式净水机","ord_no":"352650626144840","paytype":0,"price":"6120.0","tag":"0"}]
     * errcode : 0
     * result : 0
     */

    @SerializedName("errcode")
    private int errcodeX;
    @SerializedName("result")
    private int resultX;
    private List<DataBean> data;

    public int getErrcodeX() {
        return errcodeX;
    }

    public void setErrcodeX(int errcodeX) {
        this.errcodeX = errcodeX;
    }

    public int getResultX() {
        return resultX;
    }

    public void setResultX(int resultX) {
        this.resultX = resultX;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * context : 台式净水机
         * ord_no : 352650626144840
         * paytype : 0
         * price : 6120.0
         * tag : 0
         */

        private String context;
        private String ord_no;
        private int paytype;
        private String price;
        private String tag;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getOrd_no() {
            return ord_no;
        }

        public void setOrd_no(String ord_no) {
            this.ord_no = ord_no;
        }

        public int getPaytype() {
            return paytype;
        }

        public void setPaytype(int paytype) {
            this.paytype = paytype;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}

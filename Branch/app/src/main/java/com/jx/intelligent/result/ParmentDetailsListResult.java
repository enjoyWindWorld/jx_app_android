package com.jx.intelligent.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public class ParmentDetailsListResult extends NormalResult {

    /**
     * data : [{"color":"珍珠黑","model":"JX-RO75G-1601","name":"立式净水机","number":"2","pledge":1000,"ppdnum":"2","price":1480,"proid":"3","sc_id":272,"totalPrice":6920,"type":"0","url":"http://data.jx-inteligent.tech:15010/jx/64965631fd322a0f548814f1801983f7.png","userid":"121","weight":"18kg","yearsorflow":"包年购买: 2年"}]
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
         * color : 珍珠黑
         * model : JX-RO75G-1601
         * name : 立式净水机
         * number : 2
         * pledge : 1000
         * ppdnum : 2
         * price : 1480
         * proid : 3
         * sc_id : 272
         * totalPrice : 6920
         * type : 0
         * url : http://data.jx-inteligent.tech:15010/jx/64965631fd322a0f548814f1801983f7.png
         * userid : 121
         * weight : 18kg
         * yearsorflow : 包年购买: 2年
         */

        private String color;
        private String model;
        private String name;
        private String number;
        private int pledge;
        private String ppdnum;
        private float price;
        private String proid;
        private int sc_id;
        private float totalPrice;
        private String type;
        private String url;
        private String userid;
        private String weight;
        private String yearsorflow;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getPledge() {
            return pledge;
        }

        public void setPledge(int pledge) {
            this.pledge = pledge;
        }

        public String getPpdnum() {
            return ppdnum;
        }

        public void setPpdnum(String ppdnum) {
            this.ppdnum = ppdnum;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getProid() {
            return proid;
        }

        public void setProid(String proid) {
            this.proid = proid;
        }

        public int getSc_id() {
            return sc_id;
        }

        public void setSc_id(int sc_id) {
            this.sc_id = sc_id;
        }

        public float getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(float totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getYearsorflow() {
            return yearsorflow;
        }

        public void setYearsorflow(String yearsorflow) {
            this.yearsorflow = yearsorflow;
        }
    }
}

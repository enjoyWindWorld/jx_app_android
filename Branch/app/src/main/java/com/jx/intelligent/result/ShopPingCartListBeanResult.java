package com.jx.intelligent.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class ShopPingCartListBeanResult implements Serializable {

    @Override
    public String toString() {
        return "ShopPingCartListBeanResult{" +
                "list=" + list +
                '}';
    }

    private List<ListBean> list ;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * color : 珍珠黑
         * model : JX-RO75G-1602
         * name : 台式净水机
         * number : 1
         * pledge : 500
         * ppdnum : 2
         * price : 1280
         * proid : 2
         * sc_id : 229
         * totalPrice : 3060
         * type : 0
         * url : http://data.jx-inteligent.tech:15010/jx/808b6aa915ffc568fb2fa267788fd5b9.png
         * userid : 121
         * weight : 13kg
         * yearsorflow : 包年购买: 2年
         */

        private String color;
        private String model;
        private String name;
        private int number;
        private int pledge;
        private int ppdnum;
        private int price;
        private int proid;
        private int sc_id;
        private int totalPrice;
        private int type;
        private String url;
        private int userid;
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

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getPledge() {
            return pledge;
        }

        public void setPledge(int pledge) {
            this.pledge = pledge;
        }

        public int getPpdnum() {
            return ppdnum;
        }

        public void setPpdnum(int ppdnum) {
            this.ppdnum = ppdnum;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getProid() {
            return proid;
        }

        public void setProid(int proid) {
            this.proid = proid;
        }

        public int getSc_id() {
            return sc_id;
        }

        public void setSc_id(int sc_id) {
            this.sc_id = sc_id;
        }

        public int getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(int totalPrice) {
            this.totalPrice = totalPrice;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
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

        @Override
        public String toString() {
            return "ListBean{" +
                    "color='" + color + '\'' +
                    ", model='" + model + '\'' +
                    ", name='" + name + '\'' +
                    ", number=" + number +
                    ", pledge=" + pledge +
                    ", ppdnum=" + ppdnum +
                    ", price=" + price +
                    ", proid=" + proid +
                    ", sc_id=" + sc_id +
                    ", totalPrice=" + totalPrice +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    ", userid=" + userid +
                    ", weight='" + weight + '\'' +
                    ", yearsorflow='" + yearsorflow + '\'' +
                    '}';
        }
    }
}
package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7 0007.
 * 商品详情
 */

public class ProductDetailResult extends NormalResult {

    List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        List<DetailInfo> detail = new ArrayList<DetailInfo>();
        List<ColorInfo> color = new ArrayList<ColorInfo>();
        List<PayType> paytype = new ArrayList<PayType>();

        public List<DetailInfo> getDetail() {
            return detail;
        }

        public void setDetail(List<DetailInfo> detail) {
            this.detail = detail;
        }

        public List<ColorInfo> getColor() {
            return color;
        }

        public void setColor(List<ColorInfo> color) {
            this.color = color;
        }

        public List<PayType> getPaytype() {
            return paytype;
        }

        public void setPaytype(List<PayType> paytype) {
            this.paytype = paytype;
        }

        public class DetailInfo{
            private String id;
            private String name;
            private String typename;
            private String PROD_HZ;
            private String PROD_W;
            private String PROD_MPA;
            private String PROD_C;
            private String PROD_HL;
            private String PROD_FL;
            private String PROD_WT;
            private String PROD_IW;
            private String PROD_WX;
            private String PROD_WD;
            private String PROD_SZ;
            private String PROD_SZI;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTypename() {
                return typename;
            }

            public void setTypename(String typename) {
                this.typename = typename;
            }

            public String getPROD_HZ() {
                return PROD_HZ;
            }

            public void setPROD_HZ(String PROD_HZ) {
                this.PROD_HZ = PROD_HZ;
            }

            public String getPROD_W() {
                return PROD_W;
            }

            public void setPROD_W(String PROD_W) {
                this.PROD_W = PROD_W;
            }

            public String getPROD_MPA() {
                return PROD_MPA;
            }

            public void setPROD_MPA(String PROD_MPA) {
                this.PROD_MPA = PROD_MPA;
            }

            public String getPROD_C() {
                return PROD_C;
            }

            public void setPROD_C(String PROD_C) {
                this.PROD_C = PROD_C;
            }

            public String getPROD_HL() {
                return PROD_HL;
            }

            public void setPROD_HL(String PROD_HL) {
                this.PROD_HL = PROD_HL;
            }

            public String getPROD_FL() {
                return PROD_FL;
            }

            public void setPROD_FL(String PROD_FL) {
                this.PROD_FL = PROD_FL;
            }

            public String getPROD_WT() {
                return PROD_WT;
            }

            public void setPROD_WT(String PROD_WT) {
                this.PROD_WT = PROD_WT;
            }

            public String getPROD_IW() {
                return PROD_IW;
            }

            public void setPROD_IW(String PROD_IW) {
                this.PROD_IW = PROD_IW;
            }

            public String getPROD_WX() {
                return PROD_WX;
            }

            public void setPROD_WX(String PROD_WX) {
                this.PROD_WX = PROD_WX;
            }

            public String getPROD_WD() {
                return PROD_WD;
            }

            public void setPROD_WD(String PROD_WD) {
                this.PROD_WD = PROD_WD;
            }

            public String getPROD_SZ() {
                return PROD_SZ;
            }

            public void setPROD_SZ(String PROD_SZ) {
                this.PROD_SZ = PROD_SZ;
            }

            public String getPROD_SZI() {
                return PROD_SZI;
            }

            public void setPROD_SZI(String PROD_SZI) {
                this.PROD_SZI = PROD_SZI;
            }
        }

        public class ColorInfo{
            private String pic_color;
            private String url;
            private String tone;

            public String getPic_color() {
                return pic_color;
            }

            public void setPic_color(String pic_color) {
                this.pic_color = pic_color;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTone() {
                return tone;
            }

            public void setTone(String tone) {
                this.tone = tone;
            }
        }

        public class PayType{
            private String paytype;
            private String price;
            //押金
            private int pay_pledge;
            public int getPay_pledge() {
                return pay_pledge;
            }

            public void setPay_pledge(int pay_pledge) {
                this.pay_pledge = pay_pledge;
            }

            public String getPaytype() {
                return paytype;
            }

            public void setPaytype(String paytype) {
                this.paytype = paytype;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }
}

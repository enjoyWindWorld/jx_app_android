package com.jx.intelligent.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 韦飞 on 2017/6/30 0030.
 */

public class ServiceReleaseResult extends BaseResult {

    List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable
    {
        private String price;
        private String ord_no;
        private String type;
        private String seller;
        private String pubid;
        private String isPush;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOrd_no() {
            return ord_no;
        }

        public void setOrd_no(String ord_no) {
            this.ord_no = ord_no;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSeller() {
            return seller;
        }

        public void setSeller(String seller) {
            this.seller = seller;
        }

        public String getPubid() {
            return pubid;
        }

        public void setPubid(String pubid) {
            this.pubid = pubid;
        }

        public String getIsPush() {
            return isPush;
        }

        public void setIsPush(String isPush) {
            this.isPush = isPush;
        }
    }
}

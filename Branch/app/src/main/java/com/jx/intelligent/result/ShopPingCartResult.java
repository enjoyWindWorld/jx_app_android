package com.jx.intelligent.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 王云 on 2017/6/19 0019.
 */

public class ShopPingCartResult extends NormalResult implements Serializable {

    /**
     * data : [{"list":[{"color":"中国红","model":"JX-RO75G-1603S","name":"壁挂式净水机","number":2,"pledge":1000,"ppdnum":3,"price":0.20000000298023224,"proid":1,"sc_id":47,"totalPrice":1001.2000122070312,"type":1,"url":"http://data.jx-inteligent.tech:15010/jx/0c3568b708591631657a32305409b579.png,http://data.jx-inteligent.tech:15010/jx/0c3568b708591631657a32305409b579.png","userid":121,"weight":"12.3kg","yearsorflow":"流量预付: 0升(0.2*2/0.48=0升)"}],"name":[{"name":"壁挂式净水机"}],"proid":[{"proid":"1"}]},{"list":[{"color":"中国红","model":"JX-RO75G-1602","name":"台式净水机","number":1,"pledge":500,"ppdnum":2,"price":1280,"proid":2,"sc_id":29,"totalPrice":3060,"type":0,"url":"http://data.jx-inteligent.tech:15010/jx/c1009d90cefb531edade29692a599e2a.png","userid":121,"weight":"13kg","yearsorflow":"包年购买: 2年"}],"name":[{"name":"台式净水机"}],"proid":[{"proid":"2"}]}]
     * errcode : 0
     * result : 0
     */
//是这个类的一部分

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        private List<ListBean> list;
        private List<NameBean> name;
        private List<ProidBean> proid;
        private boolean ischeck;
        public boolean ischeck() {
            return ischeck;
        }

        public void setIscheck(boolean ischeck) {
            this.ischeck = ischeck;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public List<NameBean> getName() {
            return name;
        }

        public void setName(List<NameBean> name) {
            this.name = name;
        }

        public List<ProidBean> getProid() {
            return proid;
        }

        public void setProid(List<ProidBean> proid) {
            this.proid = proid;
        }


        //ba zhe ge na chu qu ,dan du
        public static class ListBean implements Serializable {
            /**
             * color : 中国红
             * model : JX-RO75G-1603S
             * name : 壁挂式净水机
             * number : 2
             * pledge : 1000
             * ppdnum : 3
             * price : 0.20000000298023224
             * proid : 1
             * sc_id : 47
             * totalPrice : 1001.2000122070312
             * type : 1
             * url : http://data.jx-inteligent.tech:15010/jx/0c3568b708591631657a32305409b579.png,http://data.jx-inteligent.tech:15010/jx/0c3568b708591631657a32305409b579.png
             * userid : 121
             * weight : 12.3kg
             * yearsorflow : 流量预付: 0升(0.2*2/0.48=0升)
             */

            private String color;
            private String model;
            private String name;
            private int number;
            private int pledge;
            private int ppdnum;
            private float price;
            private int proid;
            private int sc_id;
            private float totalPrice;
            private int type;
            private String url;
            private int userid;
            private String weight;
            private String yearsorflow;
            private boolean ischeck;
            public boolean ischeck() {
                return ischeck;
            }

            public void setIscheck(boolean ischeck) {
                this.ischeck = ischeck;
            }

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

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
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

            public float getTotalPrice() {
                return totalPrice;
            }

            public void setTotalPrice(float totalPrice) {
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
        }

        public static class NameBean {
            /**
             * name : 壁挂式净水机
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ProidBean {
            /**
             * proid : 1
             */

            private String proid;

            public String getProid() {
                return proid;
            }

            public void setProid(String proid) {
                this.proid = proid;
            }
        }
    }
}
package com.jx.intelligent.result;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class HomeBannerResult extends NormalResult {


    /**
     * data : [{"home_page":[{"adv_imgurl":"http://data.jx-inteligent.tech:15010/jx/58877beabda9710f628b629c6dbcedd5.png"}]},{"ranking_list":[{"adv_imgurl":"http://data.jx-inteligent.tech:15010/jx/3671817ec2b30a3877e1c0b6819deda3.png","adv_name":"空调、洗衣机、冰箱、小家电等维修与清洗","pub_id":72},{"adv_imgurl":"http://data.jx-inteligent.tech:15010/jx/27b41009f030fcdcd281178d007e314c.png","adv_name":"灯饰灯具清洁","pub_id":71},{"adv_imgurl":"http://data.jx-inteligent.tech:15010/jx/f6fced94d4405b88d12d2965737e9ad5.png","adv_name":"软件开发","pub_id":151}]}]
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
        private List<HomePageBean> home_page;
        private List<RankingListBean> ranking_list;

        public List<HomePageBean> getHome_page() {
            return home_page;
        }

        public void setHome_page(List<HomePageBean> home_page) {
            this.home_page = home_page;
        }

        public List<RankingListBean> getRanking_list() {
            return ranking_list;
        }

        public void setRanking_list(List<RankingListBean> ranking_list) {
            this.ranking_list = ranking_list;
        }

        public static class HomePageBean {
            /**
             * adv_imgurl : http://data.jx-inteligent.tech:15010/jx/58877beabda9710f628b629c6dbcedd5.png
             */

            private String adv_imgurl;
            private String adv_url;

            public String getAdv_imgurl() {
                return adv_imgurl;
            }

            public void setAdv_imgurl(String adv_imgurl) {
                this.adv_imgurl = adv_imgurl;
            }

            public String getAdv_url() {
                return adv_url;
            }

            public void setAdv_url(String adv_url) {
                this.adv_url = adv_url;
            }
        }

        public static class RankingListBean {
            /**
             * adv_imgurl : http://data.jx-inteligent.tech:15010/jx/3671817ec2b30a3877e1c0b6819deda3.png
             * adv_name : 空调、洗衣机、冰箱、小家电等维修与清洗
             * pub_id : 72
             */

            private String adv_imgurl;
            private String adv_name;
            private int pub_id;

            public String getAdv_imgurl() {
                return adv_imgurl;
            }

            public void setAdv_imgurl(String adv_imgurl) {
                this.adv_imgurl = adv_imgurl;
            }

            public String getAdv_name() {
                return adv_name;
            }

            public void setAdv_name(String adv_name) {
                this.adv_name = adv_name;
            }

            public int getPub_id() {
                return pub_id;
            }

            public void setPub_id(int pub_id) {
                this.pub_id = pub_id;
            }
        }
    }
}

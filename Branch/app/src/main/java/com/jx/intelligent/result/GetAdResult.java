package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24 0024.
 * 登录请求结果类
 */

public class GetAdResult extends NormalResult{

    private List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }


    public static class Data {
        private String id;
        private String adv_phone;
        private String adv_imgurl;
        private String adv_dir;
        private String adv_name;
        private String adv_type;
        private String adv_url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAdv_phone() {
            return adv_phone;
        }

        public void setAdv_phone(String adv_phone) {
            this.adv_phone = adv_phone;
        }

        public String getAdv_imgurl() {
            return adv_imgurl;
        }

        public void setAdv_imgurl(String adv_imgurl) {
            this.adv_imgurl = adv_imgurl;
        }

        public String getAdv_dir() {
            return adv_dir;
        }

        public void setAdv_dir(String adv_dir) {
            this.adv_dir = adv_dir;
        }

        public String getAdv_name() {
            return adv_name;
        }

        public void setAdv_name(String adv_name) {
            this.adv_name = adv_name;
        }

        public String getAdv_type() {
            return adv_type;
        }

        public void setAdv_type(String adv_type) {
            this.adv_type = adv_type;
        }

        public String getAdv_url() {
            return adv_url;
        }

        public void setAdv_url(String adv_url) {
            this.adv_url = adv_url;
        }
    }
}

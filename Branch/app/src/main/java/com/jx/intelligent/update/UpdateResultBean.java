package com.jx.intelligent.update;

import java.util.ArrayList;
import java.util.List;

public class UpdateResultBean {
//    public String message;
//    public UpdateInfoBean result;
//    public String retCode;
//
//    public class UpdateInfoBean {
//        public String description;
//        public String dloadUrl;
//        public String iosStatus;
//        public double packageSize;
//        public String versionName;
//        public int versionNo;
//    }

    public String errcode;
    public String msg;
    public String result;
    public List<Data> data = new ArrayList<>();


    public class Data{
        private String name;
        private String length;
        private String versionCode;
        private String downurl;
        private String mustupgrade;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getDownurl() {
            return downurl;
        }

        public void setDownurl(String downurl) {
            this.downurl = downurl;
        }

        public String getMustupgrade() {
            return mustupgrade;
        }

        public void setMustupgrade(String mustupgrade) {
            this.mustupgrade = mustupgrade;
        }
    }
}

package com.jx.maneger.update;

import java.util.ArrayList;
import java.util.List;

public class UpdateResultBean {

    public int errcode = -100;
    public String msg;
    public int result = -100;
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

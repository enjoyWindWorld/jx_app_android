package com.jx.maneger.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/16 0016.
 * 消息的结果类
 */

public class MessageResult extends NormalResult {

    public List<Data> data = new ArrayList<Data>();
    public List<Data> getData() {
        return data;
    }
    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable
    {
        private String p_id;
        private String p_name;
        private String p_title;
        private String p_content;
        private String nextparams;
        private String p_isread;
        private String message_time;
        private String p_type;
        private boolean isCheck;
        private boolean isDelete;

        public String getP_id() {
            return p_id;
        }

        public void setP_id(String p_id) {
            this.p_id = p_id;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public String getP_title() {
            return p_title;
        }

        public void setP_title(String p_title) {
            this.p_title = p_title;
        }

        public String getP_content() {
            return p_content;
        }

        public void setP_content(String p_content) {
            this.p_content = p_content;
        }

        public String getNextparams() {
            return nextparams;
        }

        public void setNextparams(String nextparams) {
            this.nextparams = nextparams;
        }

        public String getP_isread() {
            return p_isread;
        }

        public void setP_isread(String p_isread) {
            this.p_isread = p_isread;
        }

        public String getMessage_time() {
            return message_time;
        }

        public void setMessage_time(String message_time) {
            this.message_time = message_time;
        }

        public String getP_type() {
            return p_type;
        }

        public void setP_type(String p_type) {
            this.p_type = p_type;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public boolean isDelete() {
            return isDelete;
        }

        public void setDelete(boolean delete) {
            isDelete = delete;
        }
    }
}

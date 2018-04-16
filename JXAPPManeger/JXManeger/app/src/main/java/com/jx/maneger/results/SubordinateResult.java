package com.jx.maneger.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的下级
 * Created by Administrator on 2017/8/11.
 */

public class SubordinateResult extends NormalResult{

    public List<Data> data = new ArrayList<Data>();
    public List<Data> getData() {
        return data;
    }
    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable
    {
        private String permissions;
        private List<SubordinateData> date = new ArrayList<>();

        public String getPermissions() {
            return permissions;
        }

        public void setPermissions(String permissions) {
            this.permissions = permissions;
        }

        public List<SubordinateData> getDate() {
            return date;
        }

        public void setDate(List<SubordinateData> date) {
            this.date = date;
        }

        public class SubordinateData implements Serializable
        {
            private String id;
            private String par_name;
            private String par_level;
            private String super_id;
            private String super_par_name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPar_name() {
                return par_name;
            }

            public void setPar_name(String par_name) {
                this.par_name = par_name;
            }

            public String getPar_level() {
                return par_level;
            }

            public void setPar_level(String par_level) {
                this.par_level = par_level;
            }

            public String getSuper_id() {
                return super_id;
            }

            public void setSuper_id(String super_id) {
                this.super_id = super_id;
            }

            public String getSuper_par_name() {
                return super_par_name;
            }

            public void setSuper_par_name(String super_par_name) {
                this.super_par_name = super_par_name;
            }
        }
    }
}

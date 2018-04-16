package com.jx.intelligent.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

public class GetAddrResult extends BaseResult {


    List<HomeAddrBean> data = new ArrayList<HomeAddrBean>();


    public void setData(List<HomeAddrBean> data) {
        this.data = data;
    }

    public List<HomeAddrBean> getData() {
        return data;
    }

    public class HomeAddrBean implements Serializable {
        public int id;
        public String name;
        public String phone;
        public String area;
        public String detail;
        public String code;
        public String isdefault;

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhone() {
            return phone;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setIsdefault(String isdefault) {
            this.isdefault = isdefault;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getArea() {
            return area;
        }

        public String getDetail() {
            return detail;
        }

        public String getCode() {
            return code;
        }

        public String getIsdefault() {
            return isdefault;
        }
    }
}

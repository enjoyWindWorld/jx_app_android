package com.jx.intelligent.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/10 0010.
 */

public class UploadImgResult extends NormalResult {

    private List<Data> data = new ArrayList<Data>();

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        private String imgUrl;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}

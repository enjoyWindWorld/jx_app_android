package com.jx.intelligent.bean;

/**
 * Created by Administrator on 2016/12/8 0008.
 * 地址选择控件的封装类
 */

public class CcityBean {

    public String code;
    public String sheng;
    public String di;
    public String xian;
    public String name;
    public int level;


    public CcityBean(String code, String sheng, String di, String xian, String name, int level) {
        this.code = code;
        this.sheng = sheng;
        this.di = di;
        this.xian = xian;
        this.name = name;
        this.level = level;
    }

    @Override
    public String toString() {
        return "CcityBean{" +
                "code='" + code + '\'' +
                ", sheng='" + sheng + '\'' +
                ", di='" + di + '\'' +
                ", xian='" + xian + '\'' +
                ", name='" + name + '\'' +
                ", level=" + level +
                '}';
    }
}

package com.jx.intelligent.bean;

/**
 * author zaaach on 2016/1/26.
 * 城市选择的封装类
 */
public class City {

    private String city_key;//编号
    private String city_name;//城市全名
    private String initials;//城市短名
    private String pinyin;//城市全拼
    private String short_name;//城市短拼

    public City(String city_key, String city_name, String initials, String pinyin, String short_name) {
        this.city_key = city_key;
        this.city_name = city_name;
        this.initials = initials;
        this.pinyin = pinyin;
        this.short_name = short_name;
    }

    public String getCity_key() {
        return city_key;
    }

    public void setCity_key(String city_key) {
        this.city_key = city_key;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }
}

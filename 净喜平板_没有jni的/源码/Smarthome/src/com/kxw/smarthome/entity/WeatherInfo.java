package com.kxw.smarthome.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "WeatherInfo")
public class WeatherInfo{

	@Column(name = "updataTime",isId=true,autoGen=false)
	public String updataTime;

	@Column(name = "temperature")
	public String temperature;

	@Column(name = "state")
	public String state;

	@Column(name = "province")
	public String province;
	
	@Column(name = "city")
	public String city;
	
	@Column(name = "district")
	public String district;

	public String getUpdataTime() {
		return updataTime;
	}

	public void setUpdataTime() {
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		updataTime=format.format(date);
		this.updataTime = updataTime;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Override
	public String toString() {
		return "WeatherInfo [updataTime=" + updataTime + ", temperature="
				+ temperature + ", state=" + state + ", province=" + province
				+ ", city=" + city + ", district=" + district + "]";
	}
	
}
package com.kxw.smarthome.entity;

import java.io.Serializable;


public class BaseData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int tds;	
	public int temperature;
	public int state;
	public int waterUsed;
	public int waterSum;
	public int waterSurplus;
	public int timeSurplus;
	public int firstFilter;
	public int secondFilter;
	public int thirdFilter;
	public int fourthFilter;
	public int fivethFilter;
	
	public int getTds() {
		return tds;
	}
	public void setTds(int tds) {
		this.tds = tds;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getWaterUsed() {
		return waterUsed;
	}
	public void setWaterUsed(int waterUsed) {
		this.waterUsed = waterUsed;
	}
	public int getWaterSum() {
		return waterSum;
	}
	public void setWaterSum(int waterSum) {
		this.waterSum = waterSum;
	}
	public int getWaterSurplus() {
		return waterSurplus;
	}
	public void setWaterSurplus(int waterSurplus) {
		this.waterSurplus = waterSurplus;
	}
	public int getTimeSurplus() {
		return timeSurplus;
	}
	public void setTimeSurplus(int timeSurplus) {
		this.timeSurplus = timeSurplus;
	}
	public int getFirstFilter() {
		return firstFilter;
	}
	public void setFirstFilter(int firstFilter) {
		this.firstFilter = firstFilter;
	}
	public int getSecondFilter() {
		return secondFilter;
	}
	public void setSecondFilter(int secondFilter) {
		this.secondFilter = secondFilter;
	}
	public int getThirdFilter() {
		return thirdFilter;
	}
	public void setThirdFilter(int thirdFilter) {
		this.thirdFilter = thirdFilter;
	}
	public int getFourthFilter() {
		return fourthFilter;
	}
	public void setFourthFilter(int fourthFilter) {
		this.fourthFilter = fourthFilter;
	}
	public int getFivethFilter() {
		return fivethFilter;
	}
	public void setFivethFilter(int fivethFilter) {
		this.fivethFilter = fivethFilter;
	}
	
	@Override
	public String toString() {
		return "BaseData [tds=" + tds + ", temperature=" + temperature
				+ ", state=" + state + ", waterUsed=" + waterUsed
				+ ", waterSum=" + waterSum + ", waterSurplus=" + waterSurplus
				+ ", timeSurplus=" + timeSurplus + ", firstFilter="
				+ firstFilter + ", secondFilter=" + secondFilter
				+ ", thirdFilter=" + thirdFilter + ", fourthFilter="
				+ fourthFilter + ", fivethFilter=" + fivethFilter + "]";
	}
}
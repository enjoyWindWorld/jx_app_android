package com.kxw.smarthome.entity;


public class WaterStateInfo {
	
	public int useState;	
	public int temperature;
	public int childrenLock;
	public int model = -1; //00：壁挂；01：台式；02：立式
	

	public int getUseState() {
		return useState;
	}
	public void setUseState(int useState) {
		this.useState = useState;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public int getChildrenLock() {
		return childrenLock;
	}
	public void setChildrenLock(int childrenLock) {
		this.childrenLock = childrenLock;
	}
	public int getModel() {
		return model;
	}
	public void setModel(int model) {
		this.model = model;
	}
	@Override
	public String toString() {
		return "WaterStateInfo [useState=" + useState + ", temperature="
				+ temperature + ", childrenLock=" + childrenLock + ", model=" + model + "]";
	}
}
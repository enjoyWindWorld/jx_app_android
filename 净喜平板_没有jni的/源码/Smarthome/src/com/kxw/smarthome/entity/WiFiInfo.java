package com.kxw.smarthome.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "WiFiInfo")
public class WiFiInfo {
	
	@Column(name = "SSID",isId = true, autoGen = false)
	public String SSID;
	
	@Column(name = "BSSID")
	public String BSSID;
	
	@Column(name = "pwd")
	public String pwd;
	
	@Column(name = "capabilities")
	public String capabilities;

	public String getSSID() {
		return SSID;
	}

	public void setSSID(String SSID) {
		this.SSID = SSID;
	}

	public String getBSSID() {
		return BSSID;
	}
	
	public void setBSSID(String BSSID) {
		this.BSSID = BSSID;
	}
	
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(String capabilities) {
		this.capabilities = capabilities;
	}

	@Override
	public String toString() {
		return "WiFiInfo [SSID=" + SSID + ", BSSID=" + BSSID + ", pwd=" + pwd
				+ ", capabilities=" + capabilities + "]";
	}
	
}

package com.kxw.smarthome.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

//@Table(name = "CommunityServicesInfo")
public class CommunityServicesInfo{
	
//	@Column(name = "id", isId = true, autoGen = false)
	public int id;
	
//	@Column(name = "menu_icourl")
	public String menu_icourl;
	
//	@Column(name = "menu_name")
	public String menu_name;
	
	public String getMenu_icourl() {
		return menu_icourl;
	}

	public void setMenu_icourl(String menu_icourl) {
		this.menu_icourl=menu_icourl; 
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id=id; 
	}
	
	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name=menu_name; 
	}

	@Override
	public String toString() {
		return "CommunityServicesInfo [id=" + id + ", menu_icourl="
				+ menu_icourl + ", menu_name=" + menu_name + "]";
	}
	
}
package com.kxw.smarthome.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OptionDescriptions implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<OptionDescriptionInfo> dates = new ArrayList<OptionDescriptionInfo>();

	public List<OptionDescriptionInfo> getDates() {
		return dates;
	}

	public void setDates(List<OptionDescriptionInfo> dates) {
		this.dates = dates;
	}
}

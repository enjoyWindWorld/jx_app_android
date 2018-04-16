package com.kxw.smarthome.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "FilterLifeInfo")
public class FilterLifeInfo {
	
	@Column(name = "_id",isId = true, autoGen = true)
	public int _id;
	
	@Column(name = "pp")
	public int pp;
	
	@Column(name = "cto")
	public int cto;
	
	@Column(name = "ro")
	public int ro;
	
	@Column(name = "t33")
	public int t33;
	
	@Column(name = "wfr")
	public int wfr;

	public int getPp() {
		return pp;
	}
	
	public void setPp(int pp) {
		this.pp = pp;
	}

	public int getCto() {
		return cto;
	}

	public void setCto(int cto) {
		this.cto = cto;
	}

	public int getRo() {
		return ro;
	}

	public void setRo(int ro) {
		this.ro = ro;
	}

	public int getT33() {
		return t33;
	}

	public void setT33(int t33) {
		this.t33 = t33;
	}

	public int getWfr() {
		return wfr;
	}

	public void setWfr(int wfr) {
		this.wfr = wfr;
	}

	@Override
	public String toString() {
		return "FilterLifeInfo [_id=" + _id + ", pp=" + pp + ", cto=" + cto
				+ ", ro=" + ro + ", t33=" + t33 + ", wfr=" + wfr + "]";
	}
	
	
}

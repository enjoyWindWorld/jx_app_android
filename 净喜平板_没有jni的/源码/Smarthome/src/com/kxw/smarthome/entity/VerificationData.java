package com.kxw.smarthome.entity;

import android.R.integer;
import android.content.Context;
import android.text.TextUtils;

import com.kxw.smarthome.utils.SharedPreferencesUtil;

/**
 * 这个类是用来每半个小时检测剩余套餐值和剩余寿命对不对的
 * 存入系统文件
 * @author Administrator
 *
 */
public class VerificationData {
	
	private Context mContext;
	private int pay_proid; //0：包流量；1：包年
	private int waterSurplus;
	private int timeSurplus;
	private int firstFilter;
	private int secondFilter;
	private int thirdFilter;
	private int fourthFilter;
	private int fivethFilter;
	private long bindDate;
	private int multiple;
	
	public VerificationData(Context context){
		mContext = context;
	}
	
	
	public int getPay_proid() {
		return SharedPreferencesUtil.getIntData(mContext, "pay_proid", -1);
	}

	public void setPay_proid(int pay_proid) {
		SharedPreferencesUtil.saveIntData(mContext, "pay_proid", pay_proid);
	}

	public int getWaterSurplus() {
		return SharedPreferencesUtil.getIntData(mContext, "waterSurplus", -1);
	}
	public void setWaterSurplus(int waterSurplus) {
		SharedPreferencesUtil.saveIntData(mContext, "waterSurplus", waterSurplus);
	}
	public int getTimeSurplus() {
		if(getMultiple() == 3)
		{
			return 1095;
		}
		else
		{
			return SharedPreferencesUtil.getIntData(mContext, "timeSurplus", -1);
		}
	}
	public void setTimeSurplus(int timeSurplus) {
		if(getMultiple() == 3)
		{
			SharedPreferencesUtil.saveIntData(mContext, "timeSurplus", 1095);
		}
		else
		{
			SharedPreferencesUtil.saveIntData(mContext, "timeSurplus", timeSurplus);
		}
		
	}
	public int getFirstFilter() {
		return SharedPreferencesUtil.getIntData(mContext, "firstFilter", -1);
	}
	public void setFirstFilter(int firstFilter) {
		SharedPreferencesUtil.saveIntData(mContext, "firstFilter", firstFilter);
	}
	public int getSecondFilter() {
		return SharedPreferencesUtil.getIntData(mContext, "secondFilter", -1);
	}
	public void setSecondFilter(int secondFilter) {
		SharedPreferencesUtil.saveIntData(mContext, "secondFilter", secondFilter);
	}
	public int getThirdFilter() {
		return SharedPreferencesUtil.getIntData(mContext, "thirdFilter", -1);
	}
	public void setThirdFilter(int thirdFilter) {
		SharedPreferencesUtil.saveIntData(mContext, "thirdFilter", thirdFilter);
	}
	public int getFourthFilter() {
		return SharedPreferencesUtil.getIntData(mContext, "fourthFilter", -1);
	}
	public void setFourthFilter(int fourthFilter) {
		SharedPreferencesUtil.saveIntData(mContext, "fourthFilter", fourthFilter);
	}
	public int getFivethFilter() {
		return SharedPreferencesUtil.getIntData(mContext, "fivethFilter", -1);
	}
	public void setFivethFilter(int fivethFilter) {
		SharedPreferencesUtil.saveIntData(mContext, "fivethFilter", fivethFilter);
	}

	public long getBindDate() {
		return SharedPreferencesUtil.getLongtData(mContext, "bindDate", -1);
	}

	public void setBindDate(long bindDate) {
		SharedPreferencesUtil.saveLongData(mContext, "bindDate", bindDate);
	}
	
	public int getMultiple() {
		return SharedPreferencesUtil.getIntData(mContext, "multiple", -1);
	}

	public void setMultiple(int multiple) {
		SharedPreferencesUtil.saveIntData(mContext, "multiple", multiple);
	}
	
	public void clearVerificationData()
	{
		setBindDate(-1);
		setFirstFilter(-1);
		setSecondFilter(-1);
		setThirdFilter(-1);
		setFourthFilter(-1);
		setFivethFilter(-1);
		setPay_proid(-1);
		setTimeSurplus(-1);
		setWaterSurplus(-1);
		setMultiple(-1);
	}
	
	public void play()
	{
		System.out.println("pay_proid=="+getPay_proid()+"; waterSurplus=="+getWaterSurplus()+"; timeSurplus=="+getTimeSurplus()+"; firstFilter=="+getFirstFilter()
				+"; secondFilter=="+getSecondFilter()+"; thirdFilter=="+getThirdFilter()+"; fourthFilter=="+getFourthFilter()+"; fivethFilter=="+getFivethFilter()
				+"; bindDate=="+getBindDate() + "; multiple=="+getMultiple());
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "pay_proid=="+getPay_proid()+"; waterSurplus=="+getWaterSurplus()+"; timeSurplus=="+getTimeSurplus()+"; firstFilter=="+getFirstFilter()
				+"; secondFilter=="+getSecondFilter()+"; thirdFilter=="+getThirdFilter()+"; fourthFilter=="+getFourthFilter()+"; fivethFilter=="+getFivethFilter()
				+"; bindDate=="+getBindDate() + "; multiple=="+getMultiple();
	}
	
}

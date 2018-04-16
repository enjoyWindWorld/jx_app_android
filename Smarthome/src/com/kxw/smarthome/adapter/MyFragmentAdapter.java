package com.kxw.smarthome.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> listdata;

	public MyFragmentAdapter(FragmentManager fragmentManager, List<Fragment> listdata) {
		super(fragmentManager);
		this.listdata = listdata;
	}

	@Override
	public Fragment getItem(int arg0) {

		return listdata.get(arg0);
	}

	@Override
	public int getCount() {
		return listdata.size();
	}

}

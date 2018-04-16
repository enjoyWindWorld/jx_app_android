/*
 * 选择需要更换的滤芯的名称
*/
package com.kxw.smarthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class FilterChangeListActivity extends BaseActivity implements OnClickListener{

	private LinearLayout pp_filter_ll,cto_filter_ll,ro_filter_ll,t33_filter_ll,wfr_filter_ll;
	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.filter_change_list_activity);
		context=this;
		initView();
		initData();


	}

	private void initView() {
		// TODO Auto-generated method stub

		pp_filter_ll = (LinearLayout)findViewById(R.id.pp_filter_change_ll);
		ro_filter_ll = (LinearLayout)findViewById(R.id.ro_filter_change_ll);
		cto_filter_ll = (LinearLayout)findViewById(R.id.cto_filter_change_ll);
		t33_filter_ll = (LinearLayout)findViewById(R.id.t33_filter_change_ll);
		wfr_filter_ll = (LinearLayout)findViewById(R.id.wfr_filter_change_ll);

		pp_filter_ll.setOnClickListener(this);
		ro_filter_ll.setOnClickListener(this);
		cto_filter_ll.setOnClickListener(this);
		t33_filter_ll.setOnClickListener(this);
		wfr_filter_ll.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent =new Intent();
		switch (v.getId()) {
		case R.id.title_back_ll:
			finish();
			break;

		case R.id.pp_filter_change_ll:
			intent.putExtra("filter_no", 1);
			intent.setClass(context, FilterChangeActivity.class);
			startActivity(intent);
			break;
			
		case R.id.cto_filter_change_ll:
			intent.putExtra("filter_no", 2);
			intent.setClass(context, FilterChangeActivity.class);
			startActivity(intent);
			break;
			
		case R.id.ro_filter_change_ll:
			intent.putExtra("filter_no", 3);
			intent.setClass(context, FilterChangeActivity.class);
			startActivity(intent);
			break;
			
		case R.id.t33_filter_change_ll:
			intent.putExtra("filter_no", 4);
			intent.setClass(context, FilterChangeActivity.class);
			startActivity(intent);
			break;
			
		case R.id.wfr_filter_change_ll:
			intent.putExtra("filter_no", 5);
			intent.setClass(context, FilterChangeActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}




	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

}
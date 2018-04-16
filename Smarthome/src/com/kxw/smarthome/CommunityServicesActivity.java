/*
 * 社区服务列表，目前共八大模块，具体模块由服务器配置
*/
package com.kxw.smarthome;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.kxw.smarthome.adapter.CommunityServicesAdapter;
import com.kxw.smarthome.entity.CommunityServicesInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.NetUtils;

public class CommunityServicesActivity extends BaseActivity implements OnItemClickListener{

	private LinearLayout network_disconnect_ll;
	private Button refresh_data_bt;
	private GridView community_gv;
	private Context context;
	private static CommunityServicesAdapter communityServicesAdapter;
	private static List<CommunityServicesInfo> list;
	private ProgressBar progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.community_services_activity);
		context=this;
		initView();
		initData();

	}

	private void initView() {

		network_disconnect_ll = (LinearLayout)findViewById(R.id.network_disconnect_ll);
		refresh_data_bt = (Button)findViewById(R.id.refresh_data_bt);
		refresh_data_bt.setOnClickListener(this);
		community_gv=(GridView)findViewById(R.id.community_gv);
		community_gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		community_gv.setOnItemClickListener(this);
		progress=(ProgressBar)findViewById(R.id.progress);
	}

	private void initData() { 		
		// TODO Auto-generated method stub
		if(!NetUtils.isConnected(context)){
			network_disconnect_ll.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
		}else{
			list=new ArrayList<CommunityServicesInfo>();
			//		list=DBUtils.getAllToList(CommunityServicesInfo.class);
			communityServicesAdapter = new CommunityServicesAdapter(this ,list);
			community_gv.setAdapter(communityServicesAdapter);
			community_gv.setVisibility(View.VISIBLE);
			getServicesList();
		}	
	}

	public void getServicesList(){
		community_gv.setEmptyView(progress);
		RequestParams params= new RequestParams(ConfigUtils.get_servicesList_url);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				MyLogger.getInstance().e("onError Throwable="+arg0+"  boolean="+arg1);
				network_disconnect_ll.setVisibility(View.VISIBLE);
				progress.setVisibility(View.GONE);
			}
			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}
			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				MyLogger.getInstance().e(response);
				MyLogger.getInstance().e(DataProcessingUtils.decode(response));
				if(JsonUtils.result(response)==0){					
					//					list= new ArrayList<CommunityServicesInfo>();
					try {
						list.addAll(JsonUtils.jsonToArrayList(DataProcessingUtils.decode(new JSONObject(response).getString("data")),CommunityServicesInfo.class));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(list!=null&&list.size()>0){
						network_disconnect_ll.setVisibility(View.GONE);
						communityServicesAdapter.notifyDataSetChanged();
					}
					/*MyLogger.getInstance().e("  list ="+list.toString());
					boolean save = false;
					try {
						save = DBUtils.replaceList(CommunityServicesInfo.class,list);
						MyLogger.getInstance().e("boolean  save ="+save);
						if(save){
							communityServicesAdapter.notifyDataSetChanged();
						}
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}else{
					network_disconnect_ll.setVisibility(View.VISIBLE);
					progress.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.title_back_ll:
			finish();
			break;

		case R.id.refresh_data_bt:			
			network_disconnect_ll.setVisibility(View.GONE);
			getServicesList();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(CommunityServicesActivity.this, StoreListActivity.class);
		intent.putExtra("id", list.get(position).id);
		intent.putExtra("title", list.get(position).menu_name);
		startActivity(intent);
	}
}
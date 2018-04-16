/*
 * 社区服务中商家列表
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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kxw.smarthome.adapter.StoreListInfoAdapter;
import com.kxw.smarthome.entity.StoreListInfo;
import com.kxw.smarthome.pulltorefresh.ILoadingLayout;
import com.kxw.smarthome.pulltorefresh.PullToRefreshBase;
import com.kxw.smarthome.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.kxw.smarthome.pulltorefresh.PullToRefreshListView;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.MyToast;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.Utils;

public class StoreListActivity extends BaseActivity implements OnClickListener, OnRefreshListener2<ListView>, OnItemClickListener {

	private TextView title_tv;
	private ListView store_info_lv;
	private PullToRefreshListView pullToRefreshListView;
	private List<StoreListInfo> storeInfoList;
	private List<StoreListInfo> storeInfoMore = null;
	private StoreListInfoAdapter storeInfoAdapter;
	private Intent intent;
	private int id;
	private String title;
	private int default_page = 1;
	private int page = 1;
	private int len = 0;
	private Context context;

	private LinearLayout network_disconnect_ll,progress_ll;
	private ProgressBar progress;
	private Button refresh_data_bt;
	private TextView hit_text_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.appliance_repair_activity);
		context = this;
		intent = getIntent();
		id=intent.getIntExtra("id", 1);
		title=intent.getStringExtra("title");
		initView();
		addLoadingView();
		getStoreList(default_page);
		initData();
	}

	private void initView() {
		title_tv = (TextView)findViewById(R.id.title_tv);
		title_tv.setText(title);

		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_view);
		pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

		ILoadingLayout startLayout = pullToRefreshListView
				.getLoadingLayoutProxy(true, false);
		startLayout.setPullLabel(getString(R.string.pull_down_to_refresh));
		startLayout.setRefreshingLabel(getString(R.string.is_refreshing));
		startLayout.setReleaseLabel(getString(R.string.to_refresh));

		ILoadingLayout endLayout = pullToRefreshListView.getLoadingLayoutProxy(
				false, true);
		endLayout.setPullLabel(getString(R.string.pull_up_to_load_more));
		endLayout.setRefreshingLabel(getString(R.string.is_loading_more));
		endLayout.setReleaseLabel(getString(R.string.to_load_more));
		pullToRefreshListView.setOnItemClickListener(this);
		pullToRefreshListView.setOnRefreshListener(this);
	}


	private void addLoadingView(){
		progress_ll= (LinearLayout)findViewById(R.id.progress_ll);
		network_disconnect_ll = (LinearLayout)findViewById(R.id.network_disconnect_ll);
		progress=(ProgressBar)findViewById(R.id.progress);
		hit_text_tv = (TextView)findViewById(R.id.hit_text_tv);
		refresh_data_bt = (Button)findViewById(R.id.refresh_data_bt);
		refresh_data_bt.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		storeInfoList = new ArrayList<StoreListInfo>();
		storeInfoMore = new ArrayList<StoreListInfo>();
		storeInfoAdapter = new StoreListInfoAdapter(this, storeInfoList);
		pullToRefreshListView.getRefreshableView().setAdapter(storeInfoAdapter);

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
			progress_ll.setVisibility(View.VISIBLE);
			getStoreList(1);
			break;


		default:
			break;
		}
	}


	public void getStoreList(int page) {

		if(!NetUtils.isConnected(context)){
			network_disconnect_ll.setVisibility(View.VISIBLE);
			hit_text_tv.setText(getString(R.string.network_disconnected));
			progress_ll.setVisibility(View.GONE);
			return;
		}


		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("id", id);
			jObj.accumulate("page", page);
			jObj.accumulate("address", Utils.district);		
		} catch (Exception e) {
		}
		MyLogger.getInstance().e(jObj.toString());
		String a=DataProcessingUtils.encrypt(jObj.toString());
		MyLogger.getInstance().e(a);
		RequestParams params = new RequestParams(ConfigUtils.get_storeList_url);
		params.setBodyContent(a);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				// TODO Auto-generated method stub
				network_disconnect_ll.setVisibility(View.VISIBLE);
				hit_text_tv.setText(getString(R.string.data_get_error));
				progress_ll.setVisibility(View.GONE);
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String response) {
				MyLogger.getInstance().e(response);
				try {
					MyLogger.getInstance().e(new JSONObject(response).getString("data"));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated method stub
				//				{"data":[{"content":"the ","pubId":4,"seller":"he ","address":"北京 | 北京市内 | 东城区usa","vaildtime":"1:00","invildtime":"88:00","url":"1"}],"result":0,"msg":"ok","errcode":0}
				if(JsonUtils.result(response)==0){
					storeInfoMore.clear();
					try {
						storeInfoMore.addAll(JsonUtils.jsonToArrayList(DataProcessingUtils.decode(new JSONObject(response).getString("data")),StoreListInfo.class));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(storeInfoMore!=null && storeInfoMore.size()>0){
						len = storeInfoMore.size();
					}
					MyLogger.getInstance().e("storeInfoMore = "+storeInfoMore.toString());
					storeInfoList.addAll(storeInfoMore);
					if(storeInfoList!=null&&storeInfoList.size()>0){
						progress_ll.setVisibility(View.GONE);
						storeInfoAdapter.notifyDataSetChanged();
					}else{
						progress_ll.setVisibility(View.GONE);
						network_disconnect_ll.setVisibility(View.VISIBLE);
						hit_text_tv.setText(getString(R.string.data_is_null));
					}					
					pullToRefreshListView.onRefreshComplete();
				}else{
					network_disconnect_ll.setVisibility(View.VISIBLE);
					hit_text_tv.setText(getString(R.string.data_get_error));
					progress_ll.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		MyLogger.getInstance().e(storeInfoList.get(position-1).toString());
		Intent intent = new Intent();
		/*Bundle bundle = new Bundle();
		bundle.putSerializable("StoreInfo", storeInfoList.get(position-1));
		MyLogger.getInstance().e(bundle.getSerializable("StoreInfo"));*/
		intent.putExtra("pubId",storeInfoList.get(position-1).getPubId());
		intent.setClass(this, StoreInfoActivity.class);
		startActivity(intent);
		//		storeInfoList.get(position).toString();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		MyLogger.getInstance().e("onPullDownToRefresh");
		pullToRefreshListView.postDelayed(new Runnable() {


			//			@Override
			public void run() {
				storeInfoList.clear();
				getStoreList(default_page);
				//				pullToRefreshListView.onRefreshComplete();
			}
		}, 1000);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		MyLogger.getInstance().e("onPullUpToRefresh");
		pullToRefreshListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(len == 10){
					page++;
					getStoreList(page);
				}else {
					MyToast.getManager(context).show(getString(R.string.all_data_load_completed));
					pullToRefreshListView.onRefreshComplete();
				}

				//				pullToRefreshListView.onRefreshComplete();
			}
		}, 1000);
	}

}
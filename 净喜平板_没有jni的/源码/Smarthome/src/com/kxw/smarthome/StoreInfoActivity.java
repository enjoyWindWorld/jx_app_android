/*
 * 社区服务中商家具体的信息显示
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kxw.smarthome.entity.StoreDetailedInfo;
import com.kxw.smarthome.entity.StoreListInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DataProcessingUtils;
import com.kxw.smarthome.utils.JsonUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
import com.kxw.smarthome.view.BannerView;


public class StoreInfoActivity extends BaseActivity{

	private TextView title_tv,describe_tv,phoneNum_tv,addr_tv,vaild_time_tv,invild_time_tv;
	private int pubId;
	private StoreDetailedInfo storeDetailedInfo=null;
	private LinearLayout show_info_ll;
	private Context context;
	private static List<StoreDetailedInfo> list;

	private LinearLayout network_disconnect_ll;
	private Button refresh_data_bt;
	private ProgressBar progress;
	private TextView hit_text_tv;	
    private BannerView my_banner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setBaseContentView(R.layout.store_info_activity);
		context = this;
		Intent intent = this.getIntent(); 
		pubId = intent.getIntExtra("pubId",0);
		MyLogger.getInstance().e("pubId = "+pubId);
		initView();
		addLoadingView();
		getStoreInfo(pubId);
		//		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		show_info_ll = (LinearLayout)findViewById(R.id.show_info_ll);
		title_tv =(TextView)findViewById(R.id.title_tv);
		describe_tv=(TextView)findViewById(R.id.describe_tv);
		phoneNum_tv=(TextView)findViewById(R.id.phone_num_tv);
		addr_tv=(TextView)findViewById(R.id.addr_tv);
		vaild_time_tv=(TextView)findViewById(R.id.vaild_time_tv);
		invild_time_tv=(TextView)findViewById(R.id.invild_time_tv);
        my_banner = (BannerView)findViewById(R.id.my_banner);
	}

	private void addLoadingView(){
		network_disconnect_ll = (LinearLayout)findViewById(R.id.network_disconnect_ll);
		progress=(ProgressBar)findViewById(R.id.progress);
		hit_text_tv = (TextView)findViewById(R.id.hit_text_tv);
		refresh_data_bt = (Button)findViewById(R.id.refresh_data_bt);
		refresh_data_bt.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		StoreListInfo storeInfo =new StoreListInfo();
		Intent intent = this.getIntent(); 		
		MyLogger.getInstance().e(intent.getSerializableExtra("StoreInfo"));
		storeInfo=(StoreListInfo)intent.getSerializableExtra("StoreInfo");
		title_tv.setText(storeInfo.seller);
		describe_tv.setText(storeInfo.content);
		//		phoneNum_tv.setText(storeInfo.phoneNum);
		addr_tv.setText(storeInfo.address);
		invild_time_tv.setText(storeInfo.invildtime);
		vaild_time_tv.setText(storeInfo.vaildtime);
	}

	public void getStoreInfo(int pubId) {

		if(!NetUtils.isConnected(context)){
			network_disconnect_ll.setVisibility(View.VISIBLE);
			hit_text_tv.setText(getString(R.string.network_disconnected));
			return;
		}

		JSONObject jObj = new JSONObject();
		try {
			jObj.accumulate("pubId", pubId);
			jObj.accumulate("userlong", SharedPreferencesUtil.getStringData(StoreInfoActivity.this, "longitude", "0"));
			jObj.accumulate("userlat", SharedPreferencesUtil.getStringData(StoreInfoActivity.this, "latitude", "0"));
		} catch (Exception e) {
		}

		RequestParams params = new RequestParams(ConfigUtils.get_storeInfo_url);
		//		params.addBodyParameter("params", JsonUtils.getStoreJson(pages));
		params.setBodyContent(DataProcessingUtils.encrypt(jObj.toString()));
		params.setConnectTimeout(10000);
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
				show_info_ll.setVisibility(View.GONE);
				progress.setVisibility(View.GONE);
			}

			@Override
			public void onFinished() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String response) {
				MyLogger.getInstance().e(response);
				// TODO Auto-generated method stub
				//{"data":[{"url":"1","name":"from ","address":"北京北京市内未设定usa","content":"you ","invildtime":"3:21","vaildtime":"3:19","phoneNum":"13434327491"}],"errcode":0,"msg":"ok","result":0}
				if(JsonUtils.result(response)==0){
					list = new ArrayList<StoreDetailedInfo>();
					try {
						list.addAll(JsonUtils.jsonToArrayList(DataProcessingUtils.decode(new JSONObject(response).getString("data")),StoreDetailedInfo.class));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						MyLogger.getInstance().e("storeInfoMore = e"+e);
					}
					if(list!=null&&list.size()>0){
						show_info_ll.setVisibility(View.VISIBLE);
						network_disconnect_ll.setVisibility(View.GONE);
						progress.setVisibility(View.GONE);
						title_tv.setText(list.get(0).name);						
						describe_tv.setText(list.get(0).content);	
						phoneNum_tv.setText(list.get(0).phoneNum);				
						addr_tv.setText(list.get(0).address);
						invild_time_tv.setText(list.get(0).invildtime);
						vaild_time_tv.setText(list.get(0).vaildtime);
						String[] str_arr = list.get(0).url.split(",");
						showAdDataView(str_arr);
					}else{
						network_disconnect_ll.setVisibility(View.VISIBLE);
						hit_text_tv.setText(getString(R.string.data_is_null));
						progress.setVisibility(View.GONE);
					}
				}else{
					network_disconnect_ll.setVisibility(View.VISIBLE);
					hit_text_tv.setText(getString(R.string.data_get_error));
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
			progress.setVisibility(View.VISIBLE);
			getStoreInfo(pubId);
			break;


		default:
			break;
		}
	}
	
    /**
     * 显示广告数据
     */
    void showAdDataView(String[] img_url)
    {
        if(img_url != null && img_url.length > 0)
        {
            my_banner.setImagesRes(new int[]{});
            my_banner.removeAllViewsInLayout();
            List<String> imgUrlLis = new ArrayList<String>();
            for (int a = 0; a < img_url.length; a++) {
                imgUrlLis.add(img_url[a]);
            }
            my_banner.setImagesUrl(imgUrlLis);
            my_banner.startAutoScroll();
        }
        else
        {
            my_banner.removeAllViewsInLayout();
            my_banner.setImagesRes(new int[]{R.drawable.list_load_fail});
        }
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();		
	}
}
/*
 * WiFi连接管理
 */
package com.kxw.smarthome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kxw.smarthome.adapter.WifiListAdapter;
import com.kxw.smarthome.entity.WiFiInfo;
import com.kxw.smarthome.utils.ConfigUtils;
import com.kxw.smarthome.utils.DBUtils;
import com.kxw.smarthome.utils.LocationAndWeatherUtils;
import com.kxw.smarthome.utils.MyLogger;
import com.kxw.smarthome.utils.NetUtils;
import com.kxw.smarthome.utils.Utils;
import com.kxw.smarthome.utils.WiFiConnectDialog;
import com.kxw.smarthome.utils.WiFiConnectProgress;
import com.kxw.smarthome.utils.WifiUtils;

public class WifiManageActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener, OnItemClickListener {

	private ToggleButton wifi_manage_tb;
	private LinearLayout is_connected_ll;
	private TextView wifi_name;
	private ImageView wifi_level_iv, wifi_lock_iv, wifi_state_iv;
	private ListView wifi_list;
	private WifiListAdapter wifiListAdapter;
	private WifiUtils wifiUtils;
	private WifiManager wifiManager = null;
	private Context context = null;
	List<ScanResult> wifiList, connectedWifi, newWifList;
	private WiFiInfo wiFiInfo=null;
	private String pwd=null;
	private WiFiConnectDialog wiFiConnectDialog;
	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setBaseContentView(R.layout.wifi_manage_activity);
		context = this;
		initView();
		initData();		
	}

	private void initView() {
		// TODO Auto-generated method stub
		wifi_state_iv = (ImageView) findViewById(R.id.wifi_state_iv);

		wifi_manage_tb = (ToggleButton) findViewById(R.id.wifi_manage_tb);
		wifi_manage_tb.setOnCheckedChangeListener(this);
		wifi_list = (ListView) findViewById(R.id.wifi_list_view);
		wifi_list.setOnItemClickListener(this);
		is_connected_ll = (LinearLayout) findViewById(R.id.is_connected_ll);
		is_connected_ll.setOnClickListener(this);
		wifi_name = (TextView) findViewById(R.id.wifi_name_tv);
		wifi_level_iv = (ImageView) findViewById(R.id.wifi_level_iv);
		wifi_lock_iv = (ImageView) findViewById(R.id.wifi_lock_iv);
		
		connectedWifi = new ArrayList<ScanResult>();
		newWifList = new ArrayList<ScanResult>();
		wifiListAdapter = new WifiListAdapter(context, newWifList);
		wifi_list.setAdapter(wifiListAdapter);
	}

	private void initData() {
		// TODO Auto-generated method stub
		wifiManager = (WifiManager) context
				.getSystemService(Service.WIFI_SERVICE);
		wifiUtils = new WifiUtils(context);
		
		regWifiReceiver();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		wifi_manage_tb.setChecked(wifiUtils.checkWifiState());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.is_connected_ll:
			showDialogForConnect(connectedWifi.get(0));
			break;

		case R.id.title_back_ll:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (wifi_manage_tb.isChecked()) {
			MyLogger.getInstance().e("is Checked");
			if (!wifiUtils.checkWifiState()) {
				wifiManager.setWifiEnabled(true);
				wifiManager.startScan();
				handler.post(task)/*(task,5000)*/;//延迟调用
			}
		} else {
			MyLogger.getInstance().e("not Checked");
			if (wifiUtils.checkWifiState()) {
				Utils.connected=false;
				wifiManager.setWifiEnabled(false);
				is_connected_ll.setVisibility(View.GONE);
				if(newWifList != null&&newWifList.size() > 0)
					newWifList.clear();
				if(connectedWifi != null&&connectedWifi.size() > 0)
					connectedWifi.clear();
				wifiListAdapter.notifyDataSetChanged();
				wifi_state_iv.setImageResource(R.drawable.wifi_disconnect_img);
			}
		}
	}

	/**
	 * 接收wifi热点改变事件
	 */
	private final BroadcastReceiver wifiResultChange = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			MyLogger.getInstance().e("  action = "+action);
			if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
				showWifiList();				
			} else if (action.equals("android.net.wifi.STATE_CHANGE")) {
				// 刷新状态显示
				
				showWifiList();
			}else if (action.equals("android.net.wifi.SCAN_RESULTS")) {
				// 刷新状态显示
				showWifiList();				
			}
		}
	};

	private void regWifiReceiver() {

		IntentFilter labelIntentFilter = new IntentFilter();
		labelIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		labelIntentFilter.addAction("android.net.wifi.STATE_CHANGE");
		labelIntentFilter.addAction("android.net.wifi.SCAN_RESULTS");
		labelIntentFilter.addAction(WifiManager.EXTRA_SUPPLICANT_CONNECTED);
		labelIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		
		labelIntentFilter.setPriority(1000); // 设置优先级，最高为1000
		context.registerReceiver(wifiResultChange, labelIntentFilter);
	}

	private void showWifiList() {
		// 剔除ssid中的重复项，只保留相同ssid中信号最强的哪一个
		wifiList = wifiManager.getScanResults();
		if(newWifList != null&&newWifList.size() > 0)
			newWifList.clear();
		if(connectedWifi != null&&connectedWifi.size() > 0)
			connectedWifi.clear();

		if (wifiList != null && !wifiList.isEmpty()) {
			for (int i = 0; i < wifiList.size(); i++) {

				String ssid = wifiList.get(i).SSID;
				int level = wifiList.get(i).level;
				for (int j = i + 1; j < wifiList.size(); j++) {
					if (ssid.equals(wifiList.get(j).SSID)
							&& level >= wifiList.get(j).level) {
						wifiList.remove(j);
						j--;
					} else if (ssid.equals(wifiList.get(j).SSID)
							&& level < wifiList.get(j).level) {
						wifiList.remove(i);
						i--;
					}
				}
			}
		}

		if (wifiList != null && !wifiList.isEmpty()) {
			for (int i = 0; i < wifiList.size(); i++) {
				final ScanResult scanResult = wifiList.get(i);
				if (NetUtils.isConnected(context) && wifiUtils.IsExsits(scanResult.SSID) != null
						&& wifiUtils.IsExsits(scanResult.SSID).networkId == wifiManager
						.getConnectionInfo().getNetworkId()) {
					connectedWifi.add(scanResult);
				} else {
					newWifList.add(scanResult);
				}
			}
		}

		if (connectedWifi != null && !connectedWifi.isEmpty()) {
			isConnectedWifi();
		}else {
			is_connected_ll.setVisibility(View.GONE);
			WiFiConnectProgress.dismiss();
		}

		Collections.sort(newWifList, new Comparator<ScanResult>() {

			@Override
			public int compare(ScanResult oldval, ScanResult newval) {
				int data1 = oldval.level;
				int data2 = newval.level;
				if (data1 < data2) {
					return 1;
				}
				return -1;
			}
		});
		wifiListAdapter.notifyDataSetChanged();
	}

	private void isConnectedWifi() {
		MyLogger.getInstance().e("--- 0 showWifiList isConnectedWifi  ="+ connectedWifi.size());		
		is_connected_ll.setVisibility(View.VISIBLE);
		wifi_state_iv.setImageResource(R.drawable.wifi_connect_img);
		MyLogger.getInstance().e("--- 1 showWifiList isConnectedWifi  ="+ connectedWifi.size());
		if (wifiUtils.IsExsits(connectedWifi.get(0).SSID) != null
				&& wifiUtils.IsExsits(connectedWifi.get(0).SSID).networkId == wifiManager
				.getConnectionInfo().getNetworkId()) {
			wifi_name.setText(connectedWifi.get(0).SSID);
			if (hasPwd(connectedWifi.get(0))) {
				wifi_lock_iv.setVisibility(View.VISIBLE);
			} else {
				// 无密码
				wifi_lock_iv.setVisibility(View.INVISIBLE);
			}
			wifiLevel(connectedWifi.get(0));
			WiFiConnectProgress.dismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		// TODO Auto-generated method stub
		MyLogger.getInstance().e("onItemClick SSID =" + newWifList.get(position).SSID);
		try {
			showDialogForConnect(newWifList.get(position));		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	@SuppressLint("NewApi")
	private void showDialogForConnect(final ScanResult wifiinfo) {

		if (wifiUtils.IsExsits(wifiinfo.SSID) != null) {
			final int netID = wifiUtils.IsExsits(wifiinfo.SSID).networkId;

			String actionStr;
			// 如果目前连接了此网络
			if (wifiManager.getConnectionInfo().getNetworkId() == netID) {
				actionStr = getString(R.string.wifi_manage_disconnect);
			} else {
				actionStr = getString(R.string.wifi_manage_connect);
			}
			dialog = new AlertDialog.Builder(context, R.style.AlertDialog)
			.setTitle(getString(R.string.wifi_manage_hint))
			.setMessage(getString(R.string.wifi_dialog_todo))
			.setPositiveButton(actionStr,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int whichButton) {

					if (wifiManager.getConnectionInfo().getNetworkId() == netID) {
						wifiManager.disconnect();
						is_connected_ll.setVisibility(View.GONE);
						Utils.connected=false;
						wifi_state_iv.setImageResource(R.drawable.wifi_disconnect_img);
					} else {
						WifiConfiguration config = wifiUtils.IsExsits(wifiinfo.SSID);
						wifiUtils.setMaxPriority(config);
						wifiUtils.ConnectToNetID(config.networkId);
						// is_connected_ll.setVisibility(View.VISIBLE);
						new WiFiConnectProgress(context);
						wifi_state_iv.setImageResource(R.drawable.wifi_connect_img);						
					}

				}
			})
			.setNeutralButton(getString(R.string.wifi_manage_forget),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int whichButton) {
					is_connected_ll.setVisibility(View.GONE);
					wifiManager.removeNetwork(netID);
					Utils.connected=false;
					wifi_state_iv.setImageResource(R.drawable.wifi_disconnect_img);
					DBUtils.deleteAll(WiFiInfo.class);
					return;
				}
			})
			.setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int whichButton) {
					return;
				}
			}).show();
			setDialogTextSize(dialog);
			return;
		}

		if (hasPwd(wifiinfo)) {
			//add show wifi pwd start 
			showConnectDialog(wifiinfo);
			//add show wifi pwd end 		
		} else {
			// 无密码
			dialog = new AlertDialog.Builder(context, R.style.AlertDialog)
			.setTitle(getString(R.string.wifi_manage_hint))
			.setMessage(getString(R.string.wifi_dialog_nopwd))
			.setPositiveButton(getString(R.string.confirm),
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int whichButton) {

					// 此处加入连接wifi代码
					int netID = wifiUtils.CreateWifiInfo2(wifiinfo, "");
					//					pwd="isNull";
					wifiUtils.ConnectToNetID(netID);
				}
			})
			.setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int whichButton) {
					return;
				}
			}).show();
			setDialogTextSize(dialog);
		}
	}

	private void setDialogTextSize(AlertDialog dialog){
		dialog.getWindow().setLayout(500,dialog.getWindow().getAttributes().height);
		Button positiveBtn =  dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		Button negativeBtn = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		Button neutralBtn = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
		positiveBtn.setTextColor(Color.parseColor("#1bb6ef"));
		positiveBtn.setTextSize(22);
		negativeBtn.setTextColor(Color.parseColor("#1bb6ef"));
		negativeBtn.setTextSize(22);
		neutralBtn.setTextColor(Color.parseColor("#1bb6ef"));
		neutralBtn.setTextSize(22);
		TextView textView = (TextView) dialog.findViewById(android.R.id.message);
		textView.setTextColor(Color.parseColor("#1bb6ef"));
		textView.setTextSize(22);
	}

	//add for wiFiDialog start
	private void showConnectDialog(final ScanResult wifiinfo){
		wiFiConnectDialog = new WiFiConnectDialog(context);
		wiFiConnectDialog.setTitle(getString(R.string.wifi_dialog_title));
		wiFiConnectDialog.setMessage(wifiinfo.SSID);
		wiFiConnectDialog.SetConfirmOnClickListener(new WiFiConnectDialog.onConfirmOnClickListener() {

			@Override
			public void onConfirmClick() {
				// TODO Auto-generated method stub
				pwd = wiFiConnectDialog.getText();
				MyLogger.getInstance().e("Dialog---name = " + wifiinfo.SSID+ " wifiinfo.BSSID = "+ wifiinfo.BSSID+ " pwd = " + pwd);
				// 此处加入连接wifi代码
				int netID = wifiUtils.CreateWifiInfo2(wifiinfo, pwd);
				MyLogger.getInstance().e("Dialog---  netid = " + netID);
				boolean connect =  wifiUtils.ConnectToNetID(netID);
				if(connect){
					DBUtils.deleteAll(WiFiInfo.class);
					if(pwd != null && wifiinfo.SSID != null && wifiinfo.BSSID != null && wifiinfo.capabilities != null){
						wiFiInfo=new WiFiInfo();
						wiFiInfo.SSID = wifiinfo.SSID;			
						wiFiInfo.BSSID = wifiinfo.BSSID;
						wiFiInfo.capabilities = wifiinfo.capabilities;
						wiFiInfo.pwd = pwd;
						DBUtils.updateDB(wiFiInfo);
						Utils.connected=true;		
						MyLogger.getInstance().e("save ```````"+DBUtils.updateDB(wiFiInfo));

					}
					LocationAndWeatherUtils.getLocation(context,ConfigUtils.get_weather_and_location);
					new WiFiConnectProgress(context);
				}
				//					MyToast.getManager(context).show("confirm  "+ wiFiConnectDialog.getText());
				wiFiConnectDialog.dismiss();
			}
		});
		wiFiConnectDialog.SetCancelOnClickListener(new WiFiConnectDialog.onCancelOnClickListener() {

			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub
				//					MyToast.getManager(context).show("cancel  "+ wiFiConnectDialog.getText());
				wiFiConnectDialog.dismiss();
			}
		});
		wiFiConnectDialog.show();
	}

	//add for wiFiDialog end

	private boolean hasPwd(ScanResult wifiinfo) {
		if (wifiinfo.capabilities.contains("WPA2-PSK")
				|| wifiinfo.capabilities.contains("WPA-PSK")
				|| wifiinfo.capabilities.contains("WPA-EAP")
				|| wifiinfo.capabilities.contains("WEP"))
			return true;
		return false;
	}

	private void wifiLevel(ScanResult wifiinfo) {
		if (wifiinfo.level < -85) {
			wifi_level_iv.setImageResource(R.drawable.wifi_level_0);
		} else if (wifiinfo.level < -70 && wifiinfo.level >= -85) {
			wifi_level_iv.setImageResource(R.drawable.wifi_level_1);
		} else if (wifiinfo.level < -60 && wifiinfo.level >= -70) {
			wifi_level_iv.setImageResource(R.drawable.wifi_level_2);
		} else if (wifiinfo.level >= -60) {
			wifi_level_iv.setImageResource(R.drawable.wifi_level_3);
		}
	}


	private Handler handler = new Handler();    

	private Runnable task =new Runnable() {    
		public void run() {    
			// TODOAuto-generated method stub  
			handler.postDelayed(this,5*1000);//设置延迟时间，此处是5秒  
			//需要执行的代码  
			wifiManager.startScan();
		}     
	};

	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MyLogger.getInstance().e("onPause");
	/*	handler.removeCallbacks(task); 
		context.unregisterReceiver(wifiResultChange); // 注销此广播接收器
*/	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyLogger.getInstance().e("onDestroy");
		handler.removeCallbacks(task); 
		unregisterReceiver(wifiResultChange); // 注销此广播接收器
	}

	@Override  
	public boolean onTouchEvent(MotionEvent event) {  
		// TODO Auto-generated method stub  
		if(event.getAction() == MotionEvent.ACTION_DOWN){  
			if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
				InputMethodManager manager  = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
			}  
		}  
		return super.onTouchEvent(event);  
	} 
}

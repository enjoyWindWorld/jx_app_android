package com.kxw.smarthome;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kxw.smarthome.adapter.AreaAdapter;
import com.kxw.smarthome.adapter.CityListAdapter;
import com.kxw.smarthome.adapter.CityListAdapter.OnCityClickListener;
import com.kxw.smarthome.adapter.ResultListAdapter;
import com.kxw.smarthome.entity.City;
import com.kxw.smarthome.utils.KeyboardUtil;
import com.kxw.smarthome.utils.LocationAndWeatherUtils.ILocationResult;
import com.kxw.smarthome.utils.ScreenSizeUtil;
import com.kxw.smarthome.utils.SharedPreferencesUtil;
import com.kxw.smarthome.view.MyGridView;
import com.kxw.smarthome.view.SideLetterBar;

import db.DBManager;

public class CityPickerActivity extends Activity implements OnClickListener{

    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ViewGroup emptyView;
    private RelativeLayout layout_get_area;
    private LinearLayout layout_area;
    private TextView txt_city;
    private ImageView title_back_img;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities = new ArrayList<City>();
    private DBManager dbManager;

    private MyGridView service_fl_gridview;
    private AreaAdapter areaAdapter;
    private List<City> arealist;
    private String cityname, areaname;
    private PopupWindow popupwindow;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_list);
		
		title_back_img = (ImageView) findViewById(R.id.title_back_img);
        mListView = (ListView) findViewById(R.id.listview_all_city);
        title_back_img.setOnClickListener(this);

        layout_get_area = (RelativeLayout) findViewById(R.id.layout_get_area);
        txt_city = (TextView) findViewById(R.id.txt_city);
        layout_area = (LinearLayout) findViewById(R.id.layout_area);

        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City> result = searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });

        hideSoftKeyboard(searchBox);
        
    	initData();

        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				 back(mResultAdapter.getItem(position).getCity_name(), "");
			}
		});
        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(this);
        layout_get_area.setOnClickListener(this);
        
        MyApplication.getInstance().getLocationAndWeatherUtils().start(new ILocationResult() {
			
			@Override
			public void setResult(String p, String c, String d) {
				// TODO Auto-generated method stub
				System.out.println("==================");
                mCityAdapter.updateLocateState(1, c);
			}
			
			@Override
			public void failResult() {
				// TODO Auto-generated method stub
				mCityAdapter.updateLocateState(-1, null);
			}
		});
    	
    	arealist = dbManager.getAllAreasFromMT(SharedPreferencesUtil.getStringData(CityPickerActivity.this, "city", ""));
        if(arealist.size() > 0)
        {
            arealist.add(0, new City("0", "全城", "全城", "", ""));
            initmPopupWindowView();
        }
        else
        {
            layout_area.setVisibility(View.GONE);
        }
	}
	
    protected void hideSoftKeyboard(final EditText view)
    {
        view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				KeyboardUtil.hideSoftKeyboard(CityPickerActivity.this, view);
				return false;
			}
        });
    }
	
    /**
     * 查找城市
     * @param keyword
     * @return
     */
    List<City> searchCity(String keyword)
    {
        List<City> searchCities = new ArrayList<City>();
        for (City c : mAllCities)
        {
            if(c.getInitials().contains(keyword) || c.getCity_name().contains(keyword) || c.getPinyin().contains(keyword) || c.getShort_name().contains(keyword))
            {
                searchCities.add(c);
            }
        }
        return searchCities;
    }
    
    private void back(String city, String district){
    	Intent intent = new Intent();
    	intent.putExtra("city", city);
       	intent.putExtra("district", district);
    	setResult(100, intent);
        finish();
    }
    
    public void initmPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.layout_service_type,
                null, false);
        customView.findViewById(R.id.txt_temp).setVisibility(View.GONE);
        service_fl_gridview = (MyGridView) customView.findViewById(R.id.service_fl_gridview);
        service_fl_gridview.removeAllViewsInLayout();
        areaAdapter = new AreaAdapter(CityPickerActivity.this, arealist);
        service_fl_gridview.setAdapter(areaAdapter);
        if (arealist.size() > 0) {
            for (int i = 0; i < arealist.size(); i ++)
            {
                if(arealist.get(i).getCity_name().equals(areaname))
                {
                    areaAdapter.setSelectItem(i);
                    areaAdapter.notifyDataSetChanged();
                }
            }
        }

        service_fl_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                areaAdapter.setSelectItem(i);
                areaAdapter.notifyDataSetChanged();
                areaname = arealist.get(i).getCity_name();
                popupwindow.dismiss();
                if(areaname.equals("全城"))
                {
                    back(cityname, "");
                }
                else
                {
                    back(cityname, areaname);
                }
            }
        });

        // 创建PopupWindow实例,宽度和高度是屏幕的
        popupwindow = new PopupWindow(customView, ScreenSizeUtil.getInstance(CityPickerActivity.this).getScreenWidth(), ScreenSizeUtil.getInstance(CityPickerActivity.this).getScreenHeight());
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupwindow.setAnimationStyle(R.style.AnimationFade);
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
//                    popupwindow = null;
                }
				return false;
			}
        });
    }
    
    private void initData() {
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        mAllCities.clear();
        if(MyApplication.getInstance().getmAllCities() != null && MyApplication.getInstance().getmAllCities().size() > 0)
        {
            mAllCities.addAll(MyApplication.getInstance().getmAllCities());
        }
        else
        {
        	List<City> cities = dbManager.getAllCitiesFromMT();
            mAllCities.addAll(cities);
            MyApplication.getInstance().setmAllCities(cities);
        }
        mCityAdapter = new CityListAdapter(CityPickerActivity.this, mAllCities);
        mCityAdapter.setOnCityClickListener(new OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                back(name, "");
            }

            @Override
            public void onLocateClick() {
                mCityAdapter.updateLocateState(0, null);
                MyApplication.getInstance().getLocationAndWeatherUtils().start(new ILocationResult() {
        			
        			@Override
        			public void setResult(String p, String c, String d) {
        				// TODO Auto-generated method stub
                        mCityAdapter.updateLocateState(1, c);
        			}
        			
        			@Override
        			public void failResult() {
        				// TODO Auto-generated method stub
        				mCityAdapter.updateLocateState(-1, null);
        			}
        		});
            }
        });
        mResultAdapter = new ResultListAdapter(this, null);
        mListView.setAdapter(mCityAdapter);
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back_img:
			finish();
			break;
		case R.id.layout_get_area:
            if (popupwindow != null && popupwindow.isShowing()) {
                popupwindow.dismiss();
            } else {
                if(popupwindow != null)
                {
                    popupwindow.showAsDropDown(v, 0, 0);
                }
            }
            break;
		default:
			break;
		}
	}
}

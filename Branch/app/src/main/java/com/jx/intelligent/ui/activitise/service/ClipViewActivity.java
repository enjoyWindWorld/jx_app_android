package com.jx.intelligent.ui.activitise.service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.AreaAdapter;
import com.jx.intelligent.adapter.jxAdapter.CityListAdapter;
import com.jx.intelligent.adapter.jxAdapter.ResultListAdapter;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.bean.City;
import com.jx.intelligent.bean.LocateState;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.util.LocationUtils;
import com.jx.intelligent.util.ScreenSizeUtil;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.XBitmapUtils;
import com.jx.intelligent.view.CommonClipImageView;
import com.jx.intelligent.view.MyGridView;
import com.jx.intelligent.view.SideLetterBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 照片剪切界面
 */
public class ClipViewActivity extends RHBaseActivity {

    private CommonClipImageView mClipView;
    String imgUrl;
    Bitmap bitmap;
    boolean flag;
    int size = 800;
    private Bitmap mBitmap = null;
	private TitleBarHelper titleBarHelper;

	private void getPreData() {
	// TODO Auto-generated method stub
		imgUrl = getIntent().getStringExtra("value");
		flag = getIntent().getBooleanExtra("flag",false );
    }


	@Override
	protected void init() {
		getPreData();
		if (!TextUtils.isEmpty(imgUrl)) {
			bitmap = XBitmapUtils.decodeFile(imgUrl, size);
			mClipView.setImageBitmap(bitmap);
		}
	}

	@Override
	protected int setContentLayout() {
		return R.layout.activity_clip_photo;
	}
	@Override
	protected void initTitle() {
		titleBarHelper = new TitleBarHelper(ClipViewActivity.this);
		titleBarHelper.setMiddleTitleText("剪切");
		titleBarHelper.setLeftImageRes(R.drawable.selector_back);
		titleBarHelper.setLeftClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		titleBarHelper.setRightText("保存");
		titleBarHelper.setRightClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveData();
			}
		});
	}

	@Override
	protected void findView(View contentView) {
		mClipView = (CommonClipImageView)contentView.findViewById(R.id.src_pic);
	}

    private void saveData()
 	{
		mBitmap = mClipView.clip();
		imgUrl = Constant.ROOT_DIR + System.currentTimeMillis()+ ".jpg";
		XBitmapUtils.SaveBitMap(this, imgUrl, mBitmap);
		if (mBitmap != null) {
			try {
			mBitmap.recycle();
			bitmap.recycle();
			} catch (Exception e) {
			}
		}
		Intent intent = new Intent();
		intent.putExtra("value", imgUrl);
		setResult(-1, intent);
		finish();
    }

    /**
     * author zaaach on 2016/1/26.
     * 城市选择界面
     */
    public static class CityPickerActivity extends RHBaseActivity implements View.OnClickListener {

        private ListView mListView;
        private ListView mResultListView;
        private SideLetterBar mLetterBar;
        private EditText searchBox;
        private ImageView clearBtn;
        private ViewGroup emptyView;
        private RelativeLayout layout_get_area;
        private LinearLayout layout_area;
        private TextView txt_city;
        private TitleBarHelper titleBarHelper;

        private CityListAdapter mCityAdapter;
        private ResultListAdapter mResultAdapter;
        private List<City> mAllCities = new ArrayList<City>();
        private DBManager dbManager;

        private AMapLocationClient mLocationClient;
        private MyGridView service_fl_gridview;
        private AreaAdapter areaAdapter;
        private List<City> arealist;
        private String cityname, areaname;
        private PopupWindow popupwindow;

        @Override
        protected void init() {
            initLocation();
        }

        @Override
        protected int setContentLayout() {
            return R.layout.activity_city_list;
        }

        @Override
        protected void initTitle() {
            titleBarHelper = new TitleBarHelper(CityPickerActivity.this);
            titleBarHelper.setLeftImageRes(R.drawable.selector_back);
            titleBarHelper.setLeftClickListener(this);
        }

        @Override
        protected void findView(View contentView) {
            initData();
            initView(contentView);
            if(getIntent() != null)
            {
                if(StringUtil.isEmpty(getIntent().getStringExtra("city_result")))
                {
                    titleBarHelper.setMiddleTitleText("暂无地址");
                }
                else
                {
                    titleBarHelper.setMiddleTitleText("当前地址-"+getIntent().getStringExtra("city_result"));
                    cityname = getIntent().getStringExtra("city_result");
                    areaname = getIntent().getStringExtra("district_result");
                    String address = "";
                    if (!StringUtil.isEmpty(cityname) && !StringUtil.isEmpty(areaname))
                    {
                        address = cityname+"-"+areaname;
                    }
                    else if (!StringUtil.isEmpty(cityname) && StringUtil.isEmpty(areaname))
                    {
                        address = cityname;
                    }
                    else if (StringUtil.isEmpty(cityname) && !StringUtil.isEmpty(areaname))
                    {
                        address = cityname;
                    }
                    txt_city.setText("当前: "+address);
                    arealist = dbManager.getAllAreasFromMT(cityname);
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
            }
        }

        private void initLocation() {
            mLocationClient = new AMapLocationClient(this);
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setOnceLocation(true);
            mLocationClient.setLocationOption(option);
            mLocationClient.setLocationListener(new AMapLocationListener() {
                @Override
                public void onLocationChanged(AMapLocation aMapLocation) {
                    if (aMapLocation != null) {
                        if (aMapLocation.getErrorCode() == 0) {
                            String city = aMapLocation.getCity();
                            String district = aMapLocation.getDistrict();
                            Log.e("onLocationChanged", "city: " + city);
                            Log.e("onLocationChanged", "district: " + district);
                            String location = LocationUtils.extractLocation(city, district);
                            mCityAdapter.updateLocateState(LocateState.SUCCESS, city);
                        } else {
                            //定位失败
                            mCityAdapter.updateLocateState(LocateState.FAILED, null);
                        }
                    }
                }
            });
            mLocationClient.startLocation();
        }

        private void initData() {
            dbManager = new DBManager(this);
            dbManager.copyDBFile();
            mAllCities.clear();
            if(RHBaseApplication.getInstance().getmAllCitise() != null && RHBaseApplication.getInstance().getmAllCitise().size() > 0)
            {
                mAllCities.addAll(RHBaseApplication.getInstance().getmAllCitise());
            }
            else
            {
                mAllCities.addAll(dbManager.getAllCitiesFromMT());
            }
            mCityAdapter = new CityListAdapter(this, mAllCities);
            mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
                @Override
                public void onCityClick(String name) {
                    back(name, "");
                }

                @Override
                public void onLocateClick() {
                    Log.e("onLocateClick", "重新定位...");
                    mCityAdapter.updateLocateState(LocateState.LOCATING, null);
                    mLocationClient.startLocation();
                }
            });
            mResultAdapter = new ResultListAdapter(this, null);
        }

        private void initView(View contentView) {
            mListView = (ListView) contentView.findViewById(R.id.listview_all_city);
            mListView.setAdapter(mCityAdapter);

            layout_get_area = (RelativeLayout) contentView.findViewById(R.id.layout_get_area);
            txt_city = (TextView) contentView.findViewById(R.id.txt_city);
            layout_area = (LinearLayout) contentView.findViewById(R.id.layout_area);

            TextView overlay = (TextView) contentView.findViewById(R.id.tv_letter_overlay);
            mLetterBar = (SideLetterBar) contentView.findViewById(R.id.side_letter_bar);
            mLetterBar.setOverlay(overlay);
            mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
                @Override
                public void onLetterChanged(String letter) {
                    int position = mCityAdapter.getLetterPosition(letter);
                    mListView.setSelection(position);
                }
            });

            searchBox = (EditText) contentView.findViewById(R.id.et_search);
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
    //                    List<City> result = dbManager.searchCity(keyword);
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

            emptyView = (ViewGroup) contentView.findViewById(R.id.empty_view);
            mResultListView = (ListView) contentView.findViewById(R.id.listview_search_result);
            mResultListView.setAdapter(mResultAdapter);
            mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    back(mResultAdapter.getItem(position).getCity_name(), "");
                }
            });

            clearBtn = (ImageView) contentView.findViewById(R.id.iv_search_clear);
            clearBtn.setOnClickListener(this);
            layout_get_area.setOnClickListener(this);
        }

        private void back(String city, String area){
            Intent data = new Intent();
            data.putExtra(Constant.KEY_PICKED_CITY, city);
            data.putExtra(Constant.KEY_PICKED_AREA, area);
            setResult(RESULT_OK, data);
            finish();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_search_clear:
                    searchBox.setText("");
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                    break;
                case R.id.titlebar_left_rl:
                    finish();
                    break;
                case R.id.layout_get_area:
                    if (popupwindow != null && popupwindow.isShowing()) {
                        popupwindow.dismiss();
                        return;
                    } else {
                        if(popupwindow != null)
                        {
                            titleBarHelper.centerRlEnable(false);
                            popupwindow.showAsDropDown(v, 0, 0);
                        }
                    }
                    break;
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            mLocationClient.stopLocation();
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
                    if (popupwindow != null && popupwindow.isShowing()) {
                        popupwindow.dismiss();
                        titleBarHelper.centerRlEnable(true);
    //                    popupwindow = null;
                    }
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
    }
}

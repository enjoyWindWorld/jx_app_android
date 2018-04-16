package com.jx.intelligent.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.RecycleAdapter;
import com.jx.intelligent.adapter.RecycleCommonAdapter;
import com.jx.intelligent.adapter.holder.RecycleCommonViewHolder;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.base.RHBaseFragment;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CityTxtDao;
import com.jx.intelligent.dao.CommunityService;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.OnItemClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.intf.ViewPagerOnItemClickListener;
import com.jx.intelligent.result.CityTxetResult;
import com.jx.intelligent.result.GetAdResult;
import com.jx.intelligent.result.GetHomeServiceTypeResult;
import com.jx.intelligent.ui.activitise.service.ClipViewActivity;
import com.jx.intelligent.ui.activitise.service.ServiceListActivity;
import com.jx.intelligent.util.LocationUtils;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.MsgEvent;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.BannerView;
import com.jx.intelligent.view.SpaceItemDecoration;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @创建者： weifei
 * @项目名: jx
 * @包名: com.jx.intelligent.ui.fragments
 * @创建时间： 2016/11/10 5:10
 * @描述： ${TODO} 社区服务
 */

public class ServiceFragment extends RHBaseFragment implements ActivityCompat.OnRequestPermissionsResultCallback {

    private RelativeLayout layout_city;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecycleCommonAdapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    private BannerView my_banner;
    private TextView txt_city;
    private LinearLayout refresh_empty_view_ll;
    private CommunityService dao;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();
    private boolean isFresh = false;
    private List<GetAdResult.Data> ads = new ArrayList<GetAdResult.Data>();
    private List<GetHomeServiceTypeResult.ServiceFlBean> serviceType = new ArrayList<GetHomeServiceTypeResult.ServiceFlBean>();
    private String older_city = " ", result_city = " ", older_district = " ", result_district = " ";
    private ProgressWheelDialog dialog;
    private NormalAlertDialog normalAlertDialog;
    private boolean isLocation;
    private DBManager dbManager;
    private TextView mUser_number;
    private String user_number;

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new CommunityService();
        getAdInfo();
        startLocation();
        initQCDialog();
    }


    @Override
    protected int setContentLayout() {
        return R.layout.servicefragment_layout;
    }

    @Override
    protected void initTitle(View titleView) {
        new TitleBarHelper(titleView).setHideTitleBar();
    }


    @Override
    protected void findView(View contentView) {
        dialog = new ProgressWheelDialog(getActivity());
        layout_city = (RelativeLayout) contentView.findViewById(R.id.layout_city);
        my_banner = (BannerView) contentView.findViewById(R.id.my_banner);
        txt_city = (TextView) contentView.findViewById(R.id.txt_city);
        refresh_empty_view_ll = (LinearLayout) contentView.findViewById(R.id.refresh_empty_view_ll_no_refresh);

        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        //用户的数量，就是选中合肥的时候出来的用户数量TextView
        mUser_number = (TextView) contentView.findViewById(R.id.user_number);

        mManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));

        refresh_empty_view_ll.setOnClickListener(this);
        layout_city.setOnClickListener(this);
        //初始化定位
        initLocation();
    }

    private void initServiceTypeData(final List<GetHomeServiceTypeResult.ServiceFlBean> mList) {

        mAdapter = new RecycleAdapter<GetHomeServiceTypeResult.ServiceFlBean>(UIUtil.getContext(), mList, true, R.layout.layout_home_service_item, mSwipeRefreshLayout) {
            @Override
            public void convert(RecycleCommonViewHolder holder, final GetHomeServiceTypeResult.ServiceFlBean data) {
                holder.setImageByUrl(R.id.my_img, data.getMenu_icourl(), R.id.loading_anim_iv, R.mipmap.img_service_type_empty);
                holder.setText(R.id.my_txt, data.getMenu_name());
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
                        if(StringUtil.isEmpty(result_city) && StringUtil.isEmpty(result_district))
                        {
                            ToastUtil.showToast("正在尝试获取地址");
                            if (!isLocation)
                            {
                                startLocation();
                            }
                        }
                        else
                        {
                            String address = "";
                            Intent intent = new Intent(getActivity(), ServiceListActivity.class);
                            if (!StringUtil.isEmpty(result_city) && !StringUtil.isEmpty(result_district))
                            {
                                address = result_city+"-"+result_district;
                            }
                            else if (!StringUtil.isEmpty(result_city) && StringUtil.isEmpty(result_district))
                            {
                                address = result_city;
                            }
                            else if (StringUtil.isEmpty(result_city) && !StringUtil.isEmpty(result_district))
                            {
                                address = result_district;
                            }

                            intent.putExtra("address", address);
                            intent.putExtra("title", mList.get(position).getMenu_name());
                            intent.putExtra("categoryid", mList.get(position).getId());
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            protected void opHeader(RecycleCommonViewHolder holder) {
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了头");
                    }
                });
            }
        };
        mAdapter.setHavefooter(false);
        mRecyclerView.setAdapter(mAdapter);
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isFresh = true;
                                getAdInfo();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.layout_city:

                Intent intent = new Intent(getActivity(), ClipViewActivity.CityPickerActivity.class);
                intent.putExtra("city_result", result_city);
                intent.putExtra("district_result", result_district);
                startActivityForResult(intent, 100);
                break;
            case R.id.refresh_empty_view_ll_no_refresh:
                getAdInfo();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(getActivity().getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
//        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
//        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
//        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                String result = LocationUtils.getLocationStr(loc);
                System.out.println("result======="+result);
                result_district = loc.getDistrict();
                result_city = loc.getCity();
                //王云修改 解析定位结果以后  发送网络请求查看该城市是否有 用户数量
               getUsernuberTask(result_city);
                EventBus.getDefault().post(new MsgEvent(result_city));
                //TODO
                older_city = SesSharedReferences.getCity(getActivity());
                older_district = SesSharedReferences.getDistrict(getActivity());

                if(StringUtil.isEmpty(result_city) && StringUtil.isEmpty(result_district))//定位不到，显示原来的
                {
                    result_city = SesSharedReferences.getCity(getActivity());


                    result_district = SesSharedReferences.getDistrict(getActivity());
                    //王云修改  定位不到显示原来的城市，要去网络更新一次该城市是否有用户数量

                    getUsernuberTask(result_city);

                    //定位不到，显示原来的城市也需要把这个城市传递给首页 让首页去显示 该城市的水质




                    if(!StringUtil.isEmpty(result_district))
                    {
                        txt_city.setText(result_district);

                    }
                    else if(!StringUtil.isEmpty(result_city))
                    {
                        txt_city.setText(result_city);
                        EventBus.getDefault().post(new MsgEvent(result_city));
                    }
                }
                else
                {
                    if(StringUtil.isEmpty(older_city) && StringUtil.isEmpty(older_district))//第一次定位，没有上一次地址
                    {
                        SesSharedReferences.setCity(getActivity(), result_city);
                        SesSharedReferences.setDistrict(getActivity(), result_district);

                        if(!StringUtil.isEmpty(result_city))
                        {
                            txt_city.setText(result_city);
                        }

                        if(!StringUtil.isEmpty(result_district))
                        {
                            txt_city.setText(result_district);
                        }
                    }
                    else if(older_city.equals(result_city) && older_district.equals(result_district))//定位过了，判断和上次一样不
                    {
                        result_city = SesSharedReferences.getCity(getActivity());
                        result_district = SesSharedReferences.getDistrict(getActivity());

                        if(!StringUtil.isEmpty(result_district))
                        {
                            txt_city.setText(result_district);
                        }
                        else if(!StringUtil.isEmpty(result_city))
                        {
                            txt_city.setText(result_city);
                        }
                    }
                    else //不一样，提示是否替换
                    {
                        normalAlertDialog.show();
                    }
                }

                isLocation = false;
                stopLocation();
                RHBaseApplication.getInstance().setLoc(loc);
            }
            else
            {
                result_city = SesSharedReferences.getCity(getActivity());
                result_district = SesSharedReferences.getDistrict(getActivity());

                if(!StringUtil.isEmpty(result_district))
                {
                    txt_city.setText(result_district);
                }
                else if(!StringUtil.isEmpty(result_city))
                {
                    txt_city.setText(result_city);
                }
            }
        }
    };

    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
//        locationOption.setLocationCacheEnable(true);
//        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
//        locationOption.setOnceLocationLatest(true);
//        //设置是否使用传感器
//        locationOption.setSensorEnable(false);
        // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
//        locationOption.setInterval(Long.valueOf(60*1000));
        // 设置网络请求超时时间
        locationOption.setHttpTimeOut(Long.valueOf(60 * 1000));
    }

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        isLocation = true;
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();


    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            stopLocation();
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == getActivity().RESULT_OK) {
            result_city = data.getStringExtra(Constant.KEY_PICKED_CITY);

            EventBus.getDefault().post(new MsgEvent(result_city));

//            //判断是否是合肥市 如果是合肥市 则用户数量TextView显示
//            if("合肥市".equals(result_city)){
//                mUser_number.setVisibility(View.VISIBLE);
//            }else {
//                mUser_number.setVisibility(View.GONE);
//            }
            //去拿城市的用户数量
            getUsernuberTask(result_city);

            result_district = data.getStringExtra(Constant.KEY_PICKED_AREA);

            SesSharedReferences.setCity(getActivity(), result_city);
            SesSharedReferences.setDistrict(getActivity(), result_district);

            if(!StringUtil.isEmpty(result_district))
            {
                txt_city.setText(result_district);
            }
            else if(!StringUtil.isEmpty(result_city))
            {

                txt_city.setText(result_city);
            }
        }
    }

    /**
     * 获取城市 用户数量
     */
    public void  getUsernuberTask(String city){
        getpreUserNumber(city);

        CityTxtDao dao = new CityTxtDao();
            dao.getUsernuberTask(city, new ResponseResult() {
                @Override
                public void resSuccess(Object object) {

                    CityTxetResult cityResult= (CityTxetResult) object;
                    String user_number = cityResult.getData().get(0).getUser_number();
                    //如果这个城市有 用户数量则显示  没有 则让控件隐藏
                    if(user_number!=null) {
                        mUser_number.setVisibility(View.VISIBLE);
                        mUser_number.setText(user_number);
                    }else {

                        mUser_number.setVisibility(View.GONE);
                    }

                }

                @Override
                public void resFailure(String message) {

                }
            });

    }

    /**
     * 数据库获取 用户数量
     * @param city
     */
    private String getpreUserNumber(String city ) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("city", city);
        String js = dbManager.getUrlJsonData(Constant.USER_NUMBER+StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            CityTxetResult  cityTxetResultBean = new Gson().fromJson(js,CityTxetResult.class);
            user_number = cityTxetResultBean.getData().get(0).getUser_number();
        }
        return user_number;

    }

    /**
     * 获取服务类型
     */
    public void getServiceType() {
        getPreServiceTypeData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            if(dialog.isShowing())
            {
                dialog.dismiss();
            }
            mSwipeRefreshLayout.setRefreshing(false);//刷新完成
            if(serviceType.size() <= 0 && ads.size() <= 0)
            {
                mDiffViewHelper.showEmptyView();
            }
            else if(serviceType.size() <= 0)
            {
                refresh_empty_view_ll.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
            return;
        }

        dao.getServiceTypeTask(new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
                GetHomeServiceTypeResult getHomeServiceTypeResult = (GetHomeServiceTypeResult) object;
                showServiceTypeDataView(getHomeServiceTypeResult);
            }

            @Override
            public void resFailure(String message) {
                if(serviceType.size() <= 0 && ads.size() <= 0)
                {
                    mDiffViewHelper.showEmptyView();
                }
                else if(serviceType.size() <= 0)
                {
                    refresh_empty_view_ll.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                }
                if(dialog.isShowing())
                {
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 获取广告信息
     */
    public void getAdInfo() {
        getPreAdData("0");
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            if(ads == null || ads.size()<=0) {
                my_banner.removeAllViewsInLayout();
                my_banner.setViewpagerItemClickListener(null);
                my_banner.setImagesRes(new int[]{R.mipmap.img_empty_banner});
            }
            return;
        }

        dialog.show();
        dao.getAdInfoTask("0" ,new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                GetAdResult getAdResult = (GetAdResult) object;
                showAdDataView(getAdResult);
            }

            @Override
            public void resFailure(String message) {
                getServiceType();
                if(ads == null || ads.size()<=0) {
                    my_banner.removeAllViewsInLayout();
                    my_banner.setViewpagerItemClickListener(null);
                    my_banner.setImagesRes(new int[]{R.mipmap.img_empty_banner});
                }
            }
        });
    }

    /**
     * 获取广告的缓存数据
     * @param type
     */
    void getPreAdData(String type)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        String js = dbManager.getUrlJsonData(Constant.USER_GET_AD_URL + StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            GetAdResult adResult = new Gson().fromJson(js, GetAdResult.class);
            showAdDataView(adResult);
        }
    }

    /**
     * 显示广告数据
     * @param adResult
     */
    void showAdDataView(GetAdResult adResult)
    {
        if(adResult != null)
        {
            ads = adResult.getData();
            if(ads != null && ads.size()>0)
            {
                my_banner.setImagesRes(new int[]{});
                my_banner.removeAllViewsInLayout();
                List<String> imgUrlLis = new ArrayList<String>();
                for (int a = 0; a < ads.size(); a++) {
                    imgUrlLis.add(ads.get(a).getAdv_imgurl());
                }
                my_banner.setImagesUrl(imgUrlLis);
                my_banner.startAutoScroll();
                my_banner.setViewpagerItemClickListener(new ViewPagerOnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if(!StringUtil.isEmpty(ads.get(position).getAdv_url()))
                        {
                            goWebJX(ads.get(position).getAdv_url());
                        }
                    }
                });
            }
            else
            {
                my_banner.removeAllViewsInLayout();
                my_banner.setViewpagerItemClickListener(null);
                my_banner.setImagesRes(new int[]{R.mipmap.img_empty_banner});
            }
        }
        else
        {
            if(ads == null || ads.size()<=0) {
                my_banner.removeAllViewsInLayout();
                my_banner.setViewpagerItemClickListener(null);
                my_banner.setImagesRes(new int[]{R.mipmap.img_empty_banner});
            }
        }
        getServiceType();
    }

    /**
     * 获取缓存下来的服务类型的数据
     */
    void getPreServiceTypeData()
    {
        String js = dbManager.getUrlJsonData(Constant.HOME_SERVICE_TYPE_URL);
        if(!StringUtil.isEmpty(js))
        {
            GetHomeServiceTypeResult getHomeServiceTypeResult = new Gson().fromJson(js, GetHomeServiceTypeResult.class);
            showServiceTypeDataView(getHomeServiceTypeResult);

            //更新界面上的本地缓存数据
            isFresh = true;
        }
    }

    /**
     * 显示服务类型数据
     * @param getHomeServiceTypeResult
     */
    void showServiceTypeDataView( GetHomeServiceTypeResult getHomeServiceTypeResult)
    {
        if(getHomeServiceTypeResult != null)
        {
            serviceType = getHomeServiceTypeResult.getData();
            if(serviceType.size() <= 0 && ads.size() <= 0)
            {
                mDiffViewHelper.showEmptyView();
            }
            else if(serviceType.size() <= 0)
            {
                refresh_empty_view_ll.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
            else
            {
                mDiffViewHelper.showDataView();
                refresh_empty_view_ll.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                initServiceTypeData(getHomeServiceTypeResult.getData());
                if (isFresh)
                {
                    mSwipeRefreshLayout.setRefreshing(false);//刷新完成
                    if(mAdapter != null)
                    {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        else
        {
            if(serviceType.size() <= 0 && ads.size() <= 0)
            {
                mDiffViewHelper.showEmptyView();
            }
            else if(serviceType.size() <= 0)
            {
                refresh_empty_view_ll.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 跳转到浏览器界面
     */
    private void goWebJX(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
    }


    private void initQCDialog()
    {
        normalAlertDialog = new NormalAlertDialog.Builder(getActivity())
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("提示")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("是否切换到当前地理位置")
                .setContentTextColor(R.color.color_000000)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.color_000000)
                .setRightButtonText("确定")
                .setRightButtonTextColor(R.color.color_000000)
                .setCancelable(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();

                        result_city = SesSharedReferences.getCity(getActivity());
                        result_district = SesSharedReferences.getDistrict(getActivity());

                        if(!StringUtil.isEmpty(result_district))
                        {
                            txt_city.setText(result_district);
                        }
                        else if(!StringUtil.isEmpty(result_city))
                        {
                            txt_city.setText(result_city);
                        }
                    }
                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();

                        SesSharedReferences.setCity(getActivity(), result_city);
                        SesSharedReferences.setDistrict(getActivity(), result_district);

                        if(!StringUtil.isEmpty(result_city))
                        {
                            txt_city.setText(result_city);
                        }

                        if(!StringUtil.isEmpty(result_district))
                        {

                            txt_city.setText(result_district);

                        }
                    }
                })
                .build();
    }
}

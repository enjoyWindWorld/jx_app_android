package com.jx.intelligent.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.ProductRecycleAdapter;
import com.jx.intelligent.adapter.jxAdapter.ReportListAdapter;
import com.jx.intelligent.adapter.jxAdapter.ServiceListAdapter;
import com.jx.intelligent.adapter.jxAdapter.WaterListAdapter;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.base.RHBaseFragment;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.HomeBannerDao;
import com.jx.intelligent.dao.HomeWaterReportDao;
import com.jx.intelligent.dao.ProductChoosingDao;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.intf.ViewPagerOnItemClickListener;
import com.jx.intelligent.result.GetMainHomeProductsResult;
import com.jx.intelligent.result.HomeBannerResult;
import com.jx.intelligent.result.HomeTextResult;
import com.jx.intelligent.result.HomeWaterResult;
import com.jx.intelligent.ui.activitise.payment.NewProductDetailActivity;
import com.jx.intelligent.ui.activitise.service.ServiceDetailActivity;
import com.jx.intelligent.util.MsgEvent;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.BannerView;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.view.wheelCityView.AutoVerticalScrollTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新版首页
 * Created by 王云 on 2017/5/23 0023.
 */

public class NewHomeFragment extends RHBaseFragment implements ProductRecycleAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, ServiceListAdapter.OnItemClickListener {

    private RecyclerView mHome_recycle;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<HomeBannerResult.DataBean> mResultData;
    private RecyclerView mService_recycle;
    private ProgressWheelDialog dialog;
    private ListView mWaterList;
    private ListView mReportList;
    private    String         type="-2";
    private List<GetMainHomeProductsResult.Data> productData;
    private List<HomeWaterResult.DataBean.WaterQualityBean> water_quality;
    private AutoVerticalScrollTextView mTv_scrallow;
    private boolean isRunning=true;
    private HomeTextResult TextResut;
    private List<HomeTextResult.DataBean> mScrollTextDatas;
    private String news_content;
    private String news_type_name;
    private String CityCode;
    private List<HomeWaterResult.DataBean.CurrentExponentBean> ReportBean;
    private String userId;
    private List<HomeBannerResult.DataBean.RankingListBean> mServiceListData;
    private WaterListAdapter waterAdapter;
    private ReportListAdapter reportAdapter;
    private ArrayList<HomeTextResult.DataBean> textDatas = new ArrayList<>();
    private LinearLayout mEmptie_text;
    private String news_url;
    private ProductRecycleAdapter productAdapter;
    private BannerView my_banner;

    @Override
    /**
     * 初始化数据
     */
    protected void init() {
        SesSharedReferences sp = new SesSharedReferences();
        userId = sp.getUserId(UIUtil.getContext());
        SesSharedReferences sp2 =new SesSharedReferences();
        String city = sp2.getCity(UIUtil.getContext());
        if(StringUtil.isEmpty(city)){
            CityCode="";
        }else {
            CityCode = city;
        }
        //初始化的时候 先去拿缓存数据
        getPreCache(1);
        EventBus.getDefault().register(this);
        initTextView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MsgEvent event){
        String city = event.getMsg();
        CityCode=city;
        Log.e("城市：：：",city);
    }



    private void initTextView() {
        String content = "";
        String type_name = "";
//        if(textDatas != null && textDatas.size() > 0)
//        {
//            content = mScrollTextDatas.get(0).getNews_content();
//            type_name = mScrollTextDatas.get(0).getNews_type_name();
//        }
//
//        String text=type_name+"         "+content;
//        Spannable span = new SpannableString(text);
//        span.setSpan(new ForegroundColorSpan(Color.parseColor("#F49F28")), 0,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        span.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 7, text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        mTv_scrallow.setText(span);

        new Thread(){
            @Override
            public void run() {
                while (isRunning){
                    if(textDatas != null && textDatas.size() > 0)
                    {
                        for(int i=0;i<textDatas.size();i++){
                            if(textDatas != null && textDatas.size() > 0)
                            {
                                news_content = textDatas.get(i).getNews_content();
                                news_type_name = textDatas.get(i).getNews_type_name();
                                news_url = textDatas.get(i).getNews_url();
                            }
                            SystemClock.sleep(3000);
                            handler.sendEmptyMessage(199);
                        }
                    }
                }
            }
        }.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 199) {
                mTv_scrallow.next();
                //用Spannable 来实现 一个TextView中显示不同颜色的字体
                String text=news_type_name+"     "+news_content;
                Spannable span = new SpannableString(text);
                span.setSpan(new ForegroundColorSpan(Color.parseColor("#F49F28")), 0,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 7, text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTv_scrallow.setText(span);
                mTv_scrallow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转浏览器
                        goWebJX(news_url);
                    }
                });
            }
        }
    };

    @Override
    protected int setContentLayout() {
        return R.layout.newhome_fragment;
    }

    @Override
    protected void initTitle(View titleView) {
        new TitleBarHelper(titleView).setHideTitleBar();
    }

    @Override
    protected void findView(View contentView) {
        //下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        //首页Banner的ViewPager
        my_banner = (BannerView) contentView.findViewById(R.id.my_banner);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UIUtil.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(UIUtil.getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        //产品选购的recycle
        mHome_recycle = (RecyclerView) contentView.findViewById(R.id.home_recyle);
        mHome_recycle.setLayoutManager(linearLayoutManager);
        mHome_recycle.setHasFixedSize(true);
        //社区服务排行榜的recycle
        mService_recycle = (RecyclerView) contentView.findViewById(R.id.service_recyle);
        mService_recycle.setLayoutManager(linearLayoutManager2);
        mService_recycle.setHasFixedSize(true);
        //饮水数据的listView
        mWaterList = (ListView) contentView.findViewById(R.id.waterlist);
        mWaterList.setDivider(null);
        //水质报告的listView
        mReportList = (ListView) contentView.findViewById(R.id.reportlist);
        mReportList.setDivider(null);
        dialog =  new ProgressWheelDialog(getActivity());
        //滚动的textview
        mTv_scrallow = (AutoVerticalScrollTextView) contentView.findViewById(R.id.tv_shuizhi);
        //饮水质量和报告的  隐藏页面
        mEmptie_text = (LinearLayout) contentView
                .findViewById(R.id.home_text_emptie);
    }

    /**
     * 网络获取所有数据的方法
     */
    private void LoadDataFromNet(){
        ShowDialog();
        //去网络获取数据首先判断是否有网络
        if(Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext())) {

            mDiffViewHelper.showDataView();
            /**
             * 网络获取产品选购栏目的数据
             */
            final ProductChoosingDao ProductDao = new ProductChoosingDao();
            ProductDao.getProductChoosingTask(1, new ResponseResult() {
                @Override
                public void resSuccess(Object object) {
                    GetMainHomeProductsResult ProductChoosingResult = (GetMainHomeProductsResult) object;
                    productData = ProductChoosingResult.getData();
                    //显示产品数据
                    ShowProductView(productData);
                    DismissDialog();
                }
                @Override
                public void resFailure(String message) {
                    DismissDialog();
                }
            });

            /**
             * 网络获取首页Banner图片的数据以及社区服务排行榜的数据
             * 2个是同一个URL 在同一个Data下面
             */
            HomeBannerDao BannerDao = new HomeBannerDao();
            BannerDao.getHomeBannerTask(type, new ResponseResult() {
                @Override
                public void resSuccess(Object object) {

                    HomeBannerResult getHomeBannerResult = (HomeBannerResult) object;
                    mResultData = getHomeBannerResult.getData();

                    // Home首页 banner 的数据集合
                    List<HomeBannerResult.DataBean.HomePageBean> mHome_pageDatas = mResultData.get(0).getHome_page();

                    //Home首页 社区服务排行榜的数据集合
                    mServiceListData = mResultData.get(1).getRanking_list();

                    //显示 banner和社区服务排行数据
                    ShowBannerAndReportView(mHome_pageDatas, mServiceListData);
                }

                @Override
                public void resFailure(String message) {
                    DismissDialog();
                }
            });

            /**
             * 网络获取今日饮水量和水质报告的数据
             */
            HomeWaterReportDao ReportDao = new HomeWaterReportDao();

            ReportDao.getWaterReportTask(userId, CityCode, new ResponseResult() {
                @Override
                public void resSuccess(Object object) {
                    mEmptie_text.setVisibility(View.INVISIBLE);
                    HomeWaterResult getHomeWaterResult = (HomeWaterResult) object;
                    //今日饮水量的数据
                    water_quality = getHomeWaterResult.getData().get(0).getWater_quality();
                    //水质报告的数据
                    ReportBean = getHomeWaterResult.getData().get(0).getCurrent_exponent();
                    //Home首页饮水数据的显示
                    ShowWaterReportView(water_quality);
                    //Home首页水质报告的显示
                    ShowExaminationReportView(ReportBean);
                }

                @Override
                public void resFailure(String message) {
                    mEmptie_text.setVisibility(View.VISIBLE);
                    DismissDialog();
                }
            });
        }
        else
        {
            DismissDialog();
            mDiffViewHelper.showEmptyView();
            mEmptie_text.setVisibility(View.GONE);
        }

        /**
         * 网络获取首页 滚动TextView的数据
         */
        HomeWaterReportDao ReportDao = new HomeWaterReportDao();
        ReportDao.getScrollTextViewTask(0+"", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                TextResut = (HomeTextResult) object;
                mScrollTextDatas = TextResut.getData();
                if(textDatas!=null&&textDatas.size()>0) {
                    textDatas.clear();
                }
                for (int i=0;i<mScrollTextDatas.size();i++){
                    HomeTextResult.DataBean dataBean = mScrollTextDatas.get(i);

                    textDatas.add(dataBean);
                }
            }
            @Override
            public void resFailure(String message) {
            }
        });
    }

    /**
     * Home首页产品选购
     * @param productData
     */
    public void ShowProductView(List<GetMainHomeProductsResult.Data> productData){
        //Home首页产品选购数据的显示
        productAdapter = new ProductRecycleAdapter(getActivity(), productData);
        mHome_recycle.setAdapter(productAdapter);
        //首页产品选购RecycleView的  Item点击事件
        productAdapter.setOnItemClickListener(this);
    }

    /**
     * 首页Banner 和社区服务排行数据的显示
     * @param mHome_pageDatas
     * @param mServiceListData
     */
    public void ShowBannerAndReportView(final List<HomeBannerResult.DataBean.HomePageBean> mHome_pageDatas, List<HomeBannerResult.DataBean.RankingListBean> mServiceListData){

        //Home首页 banner
        if(mHome_pageDatas != null && mHome_pageDatas.size()>0)
        {
            my_banner.setImagesRes(new int[]{});
            my_banner.removeAllViewsInLayout();
            List<String> imgUrlLis = new ArrayList<String>();
            for (int a = 0; a < mHome_pageDatas.size(); a++) {
                imgUrlLis.add(mHome_pageDatas.get(a).getAdv_imgurl());
            }
            my_banner.setImagesUrl(imgUrlLis);
            if(mHome_pageDatas.size() > 1)
            {
                my_banner.startAutoScroll();
            }
            else
            {
                my_banner.stopAutoScroll();
            }
            my_banner.setViewpagerItemClickListener(new ViewPagerOnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    goWebJX(mHome_pageDatas.get(position).getAdv_url());
                }
            });
        }
        else
        {
            my_banner.removeAllViewsInLayout();
            my_banner.setViewpagerItemClickListener(null);
            my_banner.setImagesRes(new int[]{R.mipmap.img_empty_banner});
        }

        //Home首页 社区服务排行recycleAdapter
        ServiceListAdapter ServiceAdapter = new ServiceListAdapter(getActivity(), mServiceListData);
        mService_recycle.setAdapter(ServiceAdapter);
        ServiceAdapter.setOnItemClickListener(this);
    }

    /**
     * Home首页饮水数据的显示
     * @param mWaterBean
     */
    public  void  ShowWaterReportView( List<HomeWaterResult.DataBean.WaterQualityBean> mWaterBean){
        //Home首页饮水数据的Adapter
        waterAdapter = new WaterListAdapter(mWaterBean);
        mWaterList.setAdapter(waterAdapter);
        waterAdapter.notifyDataSetChanged();
    }
    /**
     * Home首页水质报告的显示
     * @param
     */
    public  void  ShowExaminationReportView( List<HomeWaterResult.DataBean.CurrentExponentBean> ReportBean){
        //Home首页饮水数据的Adapter
        reportAdapter = new ReportListAdapter(ReportBean);
        mReportList.setAdapter(reportAdapter);
        reportAdapter.notifyDataSetChanged();
    }

    /**
     * 先获取缓存数据
     *
     * @param page
     */
    public void getPreCache(int page) {

        DBManager dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        //获取缓存的首页 3种类型的净水机的选购栏目数据
        if (page == 1) {
            Map<String, String> ProductsMap = new HashMap<String, String>();
            ProductsMap.put("page", "1");
            String ProductsJs = dbManager.getUrlJsonData(Constant.HOME_PRODUCT_URL + StringUtil.obj2JsonStr(ProductsMap));
            if (!StringUtil.isEmpty(ProductsJs)) {
                //通过dbManager拿到存在数据库的js字符串 进行gson解析
                GetMainHomeProductsResult getMainHomeProductsResult = new Gson().fromJson(ProductsJs, GetMainHomeProductsResult.class);
                //拿到解析过后的数据去给adapter设置数据
                if (getMainHomeProductsResult != null && getMainHomeProductsResult.getData().size() > 0) {
                    List<GetMainHomeProductsResult.Data> CacheHomeProductsData = getMainHomeProductsResult.getData();
                    //用缓存的数据去显示产品View
                    ShowProductView(CacheHomeProductsData);
                }
            }
            //数据库获取首页Banner图片的数据以及社区服务排行榜的缓存数据
            final Map<String, String> BannerMap = new HashMap<String, String>();
            BannerMap.put("type",type);
            String BannerJs = dbManager.getUrlJsonData(Constant.HOME_BANNER_URL + StringUtil.obj2JsonStr(BannerMap));
            if (!StringUtil.isEmpty(BannerJs)) {
                //通过dbManager拿到存在数据库的js字符串 进行gson解析
                HomeBannerResult getHomeBannerResult = new Gson().fromJson(BannerJs, HomeBannerResult.class);
                //拿到解析过后的数据去给adapter设置数据
                if (getHomeBannerResult != null && getHomeBannerResult.getData().size() > 0) {
                    //缓存的Banner的数据集合
                    List<HomeBannerResult.DataBean.HomePageBean> mHome_pageDatas =  getHomeBannerResult.getData().get(0).getHome_page();
                   //缓存的社区服务排行数据集合
                    List<HomeBannerResult.DataBean.RankingListBean> mServiceListData= getHomeBannerResult.getData().get(1).getRanking_list();
                    //用缓存的数据去显示 Banner和 社区服务排行
                    ShowBannerAndReportView(mHome_pageDatas,mServiceListData);
                }
            }

            //数据库获取 今日饮水量和水质报告的数据
            final Map<String, String> WaterReportMap = new HashMap<String, String>();
            WaterReportMap.put("userid",userId);
            WaterReportMap.put("cityCode",CityCode);
            LoadDataFromNet();
        }
    }

    void ShowDialog(){
       dialog.show();
    }

    void DismissDialog() {
        if (dialog.isShowing()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    /**
     * 产品选购RecycleView的Item点击事件
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position, List<GetMainHomeProductsResult.Data> productData) {
        GetMainHomeProductsResult.Data data = productData.get(position);
        int id = data.getId();
        //首先要判断是否登录
        if(SesSharedReferences.getUsrLoginState(UIUtil.getContext())){
            //吧ID传递给详情页面
            Intent intent = new Intent(getActivity(), NewProductDetailActivity.class);
            intent.putExtra("type_id",id);
            startActivity(intent);
        }
    }

    /**
     * 刷新空视图
     */
    @Override
    public void emptyRetryRefreshListener() {
        super.emptyRetryRefreshListener();
        getPreCache(1);
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                        SystemClock.sleep(3000);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //去网络加载一次数据
                      LoadDataFromNet();
                        //去网络拿过数据以后  则显示 刷新完成
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    /**
     * 服务排行RecycleView的点击事件
     * @param view
     * @param position
     * @param mServiceListData
     */
    @Override
    public void onItemClick2(View view, int position, List<HomeBannerResult.DataBean.RankingListBean> mServiceListData) {
        int pub_id = mServiceListData.get(position).getPub_id();
        Intent intent = new Intent(getActivity(), ServiceDetailActivity.class);
        intent.putExtra("Pub_id",pub_id);
        startActivity(intent);
    }
    /**
     * 跳转到浏览器界面
     */
    private void goWebJX(String url) {
        if(!StringUtil.isEmpty(url))
        {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}


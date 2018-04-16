package com.jx.intelligent.ui.activitise.service;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.RecycleCommonAdapter;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CommunityService;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.view.SpaceItemDecoration;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.adapter.holder.RecycleCommonViewHolder;
import com.jx.intelligent.intf.OnItemClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.intf.ViewPagerOnItemClickListener;
import com.jx.intelligent.adapter.RecycleAdapter;
import com.jx.intelligent.adapter.jxAdapter.ServiceFlAdapter;
import com.jx.intelligent.listener.OnRecycleScrollListener;
import com.jx.intelligent.result.GetAdResult;
import com.jx.intelligent.result.GetHomeServiceTypeResult;
import com.jx.intelligent.result.ServicesResult;
import com.jx.intelligent.ui.activitise.loginAndRegister.LoginActivity;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.ScreenSizeUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.BannerView;
import com.jx.intelligent.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/16 0016.
 * 社区服务列表
 */

public class ServiceListActivity extends RHBaseActivity {

    private ImageView btn_add_ad;
    private PopupWindow popupwindow;
    private BannerView my_banner;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout refresh_empty_view_ll;
    private RecycleCommonAdapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    private List<ServicesResult.Data> datas = new ArrayList<ServicesResult.Data>();
    private List<ServicesResult.Data> moreDatas = new ArrayList<ServicesResult.Data>();
    private List<GetAdResult.Data> ads = new ArrayList<GetAdResult.Data>();
    private TitleBarHelper titleBarHelper;

    private MyGridView service_fl_gridview;
    private ServiceFlAdapter serviceFlAdapter;

    private CommunityService communityService;
    private List<GetHomeServiceTypeResult.ServiceFlBean> serviceFlBeanList = new ArrayList<GetHomeServiceTypeResult.ServiceFlBean>();
    private String categoryid, title, address = "";
    private int page = 1;
    private boolean isFresh;
    private DBManager dbManager;

    @Override
    protected void init() {
        if(getIntent() != null)
        {
            categoryid = getIntent().getStringExtra("categoryid");
            title = getIntent().getStringExtra("title");
            address = getIntent().getStringExtra("address");
        }
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        titleBarHelper.setMiddleTitleText(title);
        communityService = new CommunityService();
        getServiceType();
        getServices(address, page);
        getAdInfo();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_service_list;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(ServiceListActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setMiddleImageRes(R.mipmap.ic_dowm);
        titleBarHelper.setLeftClickListener(this);
        titleBarHelper.setMiddleClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        btn_add_ad = (ImageView) contentView.findViewById(R.id.btn_add_ad);
        my_banner = (BannerView) contentView.findViewById(R.id.my_banner);
        refresh_empty_view_ll = (LinearLayout) contentView.findViewById(R.id.refresh_empty_view_ll_no_refresh);

        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));
        titleBarHelper.setMiddleTitleText("家庭服务");
        refresh_empty_view_ll.setOnClickListener(this);
        btn_add_ad.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.layout_center:
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    titleBarHelper.centerRlEnable(true);
                    return;
                } else {
                    if(popupwindow != null)
                    {
                        titleBarHelper.centerRlEnable(false);
                        popupwindow.showAsDropDown(view, 0, 0);
                    }
                }
                break;
            case R.id.btn_add_ad:
                if(Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
                {
                    Intent intent;
                    if(SesSharedReferences.getUsrLoginState(ServiceListActivity.this))
                    {
                        intent = new Intent(ServiceListActivity.this, ServiceReleaseActivity.class);
                        intent.putExtra("categoryid", categoryid);
                        startActivityForResult(intent, Constant.REQUEST_CODE);
                    }
                    else
                    {
                        intent = new Intent(ServiceListActivity.this, LoginActivity.class);
                        intent.putExtra("flag", Constant.LOGIN_FLAG);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.refresh_empty_view_ll_no_refresh:
                getServiceType();
                getServices(address, page);
                getAdInfo();
                break;
        }
    }

    private void initData() {

        mAdapter = new RecycleAdapter<ServicesResult.Data>(UIUtil.getContext(), datas, true, R.layout.layout_service_item, mSwipeRefreshLayout) {
            @Override
            public void convert(RecycleCommonViewHolder holder, final ServicesResult.Data data) {
                String [] urls = null;
                if(!StringUtil.isEmpty(data.getUrl()))
                {
                    urls = data.getUrl().split(",");
                }

                if(urls != null && urls.length > 0)
                {
                    holder.setImageByUrl(R.id.my_img, urls[0], R.id.loading_anim_iv, R.mipmap.small_empty_img);
                }

                holder.setText(R.id.tv_title, data.getSeller());
                holder.setText(R.id.tv_date, data.getPub_addtime().split(" ")[0]);
                holder.setText(R.id.tv_content, data.getContent());
                holder.setText(R.id.tv_addr, data.getAddress());
                holder.getView(R.id.tv_type).setVisibility(View.GONE);
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
                        Intent intent = new Intent(ServiceListActivity.this, ServiceDetailActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("pubId", data.getPubId());
                        startActivity(intent);

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

        if(datas.size()<10)
        {
            mAdapter.setHavefooter(false);
            mAdapter.setmLoadFinish(true);
        }
        else
        {
            mAdapter.setHavefooter(true);
            mAdapter.setmLoadFinish(false);
        }

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new OnRecycleScrollListener(mAdapter, (LinearLayoutManager) mManager) {
            @Override
            protected void loadMore() {
                if (!mAdapter.isLoadFinish()) { //有更多数据
                    mSwipeRefreshLayout.setEnabled(false); //正在加载更多,不可下拉刷新
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isFresh = false;
                                    page += 1;
                                    getServices(address, page);
                                }
                            });
                        }
                    }).start();
                }
            }
        });
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isFresh = true;
                                page = 1;
                                if(mAdapter != null)
                                {
                                    mAdapter.setHavefooter(true);
                                    mAdapter.setmLoadFinish(false);
                                }
                                datas.clear();
                                moreDatas.clear();
                                getServiceType();
                                getServices(address, page);
                                getAdInfo();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    public void initmPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.layout_service_type,
                null, false);
        service_fl_gridview = (MyGridView) customView.findViewById(R.id.service_fl_gridview);
        service_fl_gridview.removeAllViewsInLayout();
        serviceFlAdapter = new ServiceFlAdapter(ServiceListActivity.this, serviceFlBeanList);
        service_fl_gridview.setAdapter(serviceFlAdapter);
        if (serviceFlBeanList.size() > 0) {
            for (int i = 0; i < serviceFlBeanList.size(); i ++)
            {
                if(serviceFlBeanList.get(i).getId().equals(categoryid))
                {
                    serviceFlAdapter.setSelectItem(i);
                    serviceFlAdapter.notifyDataSetChanged();
                }
            }
        }

        service_fl_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                serviceFlAdapter.setSelectItem(i);
                serviceFlAdapter.notifyDataSetChanged();

                categoryid = serviceFlBeanList.get(i).getId();
                title = serviceFlBeanList.get(i).getMenu_name();
                titleBarHelper.setMiddleTitleText(title);
                if(mAdapter != null)
                {
                    mAdapter.clean();
                }
                datas.clear();
                moreDatas.clear();
                page = 1;
                getServices(address, page);
                getAdInfo();
                popupwindow.dismiss();
                titleBarHelper.centerRlEnable(true);
            }
        });

        // 创建PopupWindow实例,宽度和高度是屏幕的
        popupwindow = new PopupWindow(customView, ScreenSizeUtil.getInstance(ServiceListActivity.this).getScreenWidth(), ScreenSizeUtil.getInstance(ServiceListActivity.this).getScreenHeight());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null)
        {
            categoryid = data.getStringExtra("categoryid");
            if (serviceFlBeanList.size() > 0) {
                for (int i = 0; i < serviceFlBeanList.size(); i ++)
                {
                    if(serviceFlBeanList.get(i).getId().equals(categoryid))
                    {
                        serviceFlAdapter.setSelectItem(i);
                        serviceFlAdapter.notifyDataSetChanged();
                        titleBarHelper.setMiddleTitleText(serviceFlBeanList.get(i).getMenu_name());
                    }
                }
            }
            page = 1;
            getServices(address, page);
        }
    }


    void getServiceType() {

        getPreServiceTypeData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        communityService.getServiceTypeTask(new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                GetHomeServiceTypeResult getHomeServiceTypeResult = (GetHomeServiceTypeResult) object;
                showServiceTypeDataView(getHomeServiceTypeResult);
            }

            @Override
            public void resFailure(String message) {

            }
        });
    }

    void getServices(String address, final int page)
    {
        getPreServiceData(address, categoryid, page);
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            if(isFresh)
            {
                mSwipeRefreshLayout.setRefreshing(false);//刷新完成
            }
            else
            {
                mSwipeRefreshLayout.setEnabled(true);
            }
            if(datas.size() <=0 && moreDatas.size() <= 0)
            {
                refresh_empty_view_ll.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
            return;
        }

        communityService.servicesTask(address, categoryid, page+"", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                ServicesResult servicesResult = (ServicesResult)object;
                showServiceDataView(servicesResult);
            }

            @Override
            public void resFailure(String message) {
                if(datas.size() <=0 && moreDatas.size() <= 0)
                {
                    refresh_empty_view_ll.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 获取广告信息
     */
    public void getAdInfo() {
        getPreAdData(categoryid);
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            if(ads == null || ads.size()<=0)
            {
                my_banner.removeAllViewsInLayout();
                my_banner.setViewpagerItemClickListener(null);
                my_banner.setImagesRes(new int[]{R.mipmap.img_empty_banner});
            }
            return;
        }
        communityService.getAdInfoTask(categoryid ,new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                GetAdResult getAdResult = (GetAdResult) object;
                showAdDataView(getAdResult);
            }

            @Override
            public void resFailure(String message) {
                if(ads == null || ads.size()<=0)
                {
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
    }

    /**
     * 获取缓存的服务列表数据
     * @param address
     * @param id
     * @param page
     */
    void getPreServiceData(String address, String id, int page)
    {
        if(page == 1)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("address", address);
            map.put("id", id);
            map.put("page", page+"");

            String js = dbManager.getUrlJsonData(Constant.HOME_SERVICE_LIST_URL + StringUtil.obj2JsonStr(map));
            if(!StringUtil.isEmpty(js))
            {
                ServicesResult servicesResult = new Gson().fromJson(js, ServicesResult.class);
                showServiceDataView(servicesResult);
                if(servicesResult != null && servicesResult.getData().size() > 0)
                {
                    isFresh = true;
                }
            }
        }
    }

    /**
     * 显示服务列表
     * @param servicesResult
     */
    void showServiceDataView(ServicesResult servicesResult)
    {
        if(servicesResult != null && servicesResult.getData().size()>0)
        {
            refresh_empty_view_ll.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            moreDatas = servicesResult.getData();
            if(page == 1)
            {
                if(isFresh)
                {
                    datas.clear();
                    mSwipeRefreshLayout.setRefreshing(false);//刷新完成
                    datas.addAll(moreDatas);
                    if(mAdapter != null)
                    {
                        mAdapter.notifyDataSetChanged();

                        if(datas.size()<10)
                        {
                            mAdapter.setHavefooter(false);
                            mAdapter.setmLoadFinish(true);
                        }
                        else
                        {
                            mAdapter.setHavefooter(true);
                            mAdapter.setmLoadFinish(false);
                        }
                    }

                }
                else
                {
                    datas = moreDatas;
                    initData();
                }
            }
            else
            {
                mSwipeRefreshLayout.setEnabled(true);

                if(mAdapter != null)
                {
                    mAdapter.loadMore(moreDatas); //数据长度为0,表示没有更多数据
                    mAdapter.notifyDataSetChanged();


                    if(moreDatas.size()<10)
                    {
                        mAdapter.setHavefooter(false);
                        mAdapter.setmLoadFinish(true);
                    }
                    else
                    {
                        mAdapter.setHavefooter(true);
                        mAdapter.setmLoadFinish(false);
                    }
                }
            }
        }
        else
        {
            if(datas.size() <=0 && moreDatas.size() <= 0)
            {
                refresh_empty_view_ll.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        }
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
        }
    }

    /**
     * 显示服务类型数据
     * @param getHomeServiceTypeResult
     */
    void showServiceTypeDataView(GetHomeServiceTypeResult getHomeServiceTypeResult)
    {
        if(getHomeServiceTypeResult != null)
        {
            if(getHomeServiceTypeResult.getData().size() > 0)
            {
                serviceFlBeanList.clear();
            }
            serviceFlBeanList.addAll(getHomeServiceTypeResult.getData());
            initmPopupWindowView();
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
}

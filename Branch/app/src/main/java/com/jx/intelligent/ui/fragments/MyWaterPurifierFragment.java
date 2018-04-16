package com.jx.intelligent.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.RecycleCommonAdapter;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.base.RHBaseFragment;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.view.SpaceItemDecoration;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.adapter.holder.RecycleCommonViewHolder;
import com.jx.intelligent.intf.OnItemClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.adapter.RecycleAdapter;
import com.jx.intelligent.listener.OnRecycleScrollListener;
import com.jx.intelligent.result.GetMyWPListResult;
import com.jx.intelligent.ui.activitise.personalcenter.GoFilterElementStatusOrPriceActivity;
import com.jx.intelligent.ui.activitise.personalcenter.SharedBindingsActivity;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的净水器界面
 */

public class MyWaterPurifierFragment extends RHBaseFragment {

    private List<GetMyWPListResult.WaterPurifierBean> datas = new ArrayList<GetMyWPListResult.WaterPurifierBean>();
    private List<GetMyWPListResult.WaterPurifierBean> moreDatas = new ArrayList<GetMyWPListResult.WaterPurifierBean>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecycleCommonAdapter mAdapter;
    ProgressWheelDialog dialog;
    int page = 1;
    private boolean isFresh;
    private DBManager dbManager;
    UserCenter userCenterDao;
    String type;//0：查询我的， 1：查询收到的

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        userCenterDao = new UserCenter();
        getMyWaterPurifierList();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_my_water_purifier;
    }

    @Override
    protected void initTitle(View titleView) {
        new TitleBarHelper(titleView).setHideTitleBar();
    }

    @Override
    protected void findView(View contentView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        mManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));
        dialog = new ProgressWheelDialog(getActivity());
    }

    private void goFilterElementStatusOrPrice(GetMyWPListResult.WaterPurifierBean waterPurifierBean) {
        Intent intent = new Intent(getActivity(), GoFilterElementStatusOrPriceActivity.class);
        intent.putExtra("obj", waterPurifierBean);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_right_rl:
                goSharedBindings();
                break;
        }
    }

    private void initData() {

        mAdapter = new RecycleAdapter<GetMyWPListResult.WaterPurifierBean>(UIUtil.getContext(), datas, true, R.layout.layout_wd_jsq_item, mSwipeRefreshLayout) {
            @Override
            public void convert(RecycleCommonViewHolder holder, final GetMyWPListResult.WaterPurifierBean data) {
                holder.setImageByUrl(R.id.wd_jsq_icon, data.getUrl(), R.id.loading_anim_iv, R.mipmap.small_empty_img);
                if(data.getPro_alias()!=null){

                    holder.setText(R.id.wd_jsq_type, data.getPro_alias()+"（"+data.getColor()+"）");
                }else {

                    holder.setText(R.id.wd_jsq_type, data.getName() + "（" + data.getColor() + "）");
                }
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
                        goFilterElementStatusOrPrice(data);

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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isFresh = false;
                                    page += 1;
                                    getMyWaterPurifierList();
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isFresh = true;
                                page = 1;
                                getMyWaterPurifierList();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    /**
     * 获取我的净水器列表
     */
    public void getMyWaterPurifierList() {
        getPreData();
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
                mDiffViewHelper.showEmptyView();
            }
            else
            {
                mDiffViewHelper.showDataView();
            }
            return;
        }

        dialog.show();
        userCenterDao.getMyWaterPurifierListTask(SesSharedReferences.getUserId(getActivity()), type, page+"", new ResponseResult() {

            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                GetMyWPListResult getMyWPResult = (GetMyWPListResult) object;
                showWPData(getMyWPResult);
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                if(datas.size() <=0 && moreDatas.size() <= 0)
                {
                    mDiffViewHelper.showEmptyView();
                }
                else
                {
                    mDiffViewHelper.showDataView();
                }
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 获取缓存下来的我的净水器的数据
     */
    void getPreData()
    {
        if(page == 1)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("page", "1");
            map.put("userid", SesSharedReferences.getUserId(RHBaseApplication.getContext()));
            String js = dbManager.getUrlJsonData(Constant.USER_GET_WP + StringUtil.obj2JsonStr(map));
            if(!StringUtil.isEmpty(js))
            {
                GetMyWPListResult getMyWPListResult = new Gson().fromJson(js, GetMyWPListResult.class);
                showWPData(getMyWPListResult);
                if(getMyWPListResult != null && getMyWPListResult.getData().size() > 0)
                {
                    isFresh = true;
                }
            }
        }
    }

    /**
     * 显示数据
     * @param getMyWPListResult
     */
    void showWPData(GetMyWPListResult getMyWPListResult)
    {
        if(getMyWPListResult != null)
        {
            moreDatas = getMyWPListResult.getData();
            if(datas.size() <=0 && moreDatas.size() <= 0)
            {
                mDiffViewHelper.showEmptyView();
            }
            else {
                mDiffViewHelper.showDataView();
                if (page == 1) {
                    if (isFresh) {
                        datas.clear();
                        mSwipeRefreshLayout.setRefreshing(false);//刷新完成
                        datas.addAll(moreDatas);
                        if(mAdapter != null)
                        {
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        datas = moreDatas;
                        initData();
                    }
                } else {
                    if(mAdapter != null)
                    {
                        mAdapter.loadMore(moreDatas); //数据长度为0,表示没有更多数据
                        mAdapter.notifyDataSetChanged();
                    }
                    mSwipeRefreshLayout.setEnabled(true);
                }
            }
        }
        else
        {
            if(datas.size() <=0 && moreDatas.size() <= 0)
            {
                mDiffViewHelper.showEmptyView();
            }
        }
    }


    @Override
    public void emptyRetryRefreshListener() {
        super.emptyRetryRefreshListener();
        page = 1;
        getMyWaterPurifierList();
    }

    /**
     * 跳转分享绑定界面
     */
    private void goSharedBindings() {
        Intent intent = new Intent(getActivity(), SharedBindingsActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("TianJ", bundle);
        startActivity(intent);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

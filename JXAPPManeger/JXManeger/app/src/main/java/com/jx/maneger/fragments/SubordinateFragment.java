package com.jx.maneger.fragments;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jx.maneger.R;
import com.jx.maneger.activities.MainActivity;
import com.jx.maneger.activities.SubordinateDetailActivity;
import com.jx.maneger.adapter.RecycleAdapter;
import com.jx.maneger.adapter.RecycleCommonAdapter;
import com.jx.maneger.adapter.holder.RecycleCommonViewHolder;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.base.RHBaseFragment;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.SubordinateDao;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.OnItemClickListener;
import com.jx.maneger.intf.OnRecycleScrollListener;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.SubordinateResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.UIUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.SpaceItemDecoration;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/29.
 */

public class SubordinateFragment extends RHBaseFragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecycleCommonAdapter mAdapter;
    private int page = 1;
    private boolean isFresh;
    private List<SubordinateResult.Data.SubordinateData> datas = new ArrayList<SubordinateResult.Data.SubordinateData>();
    private List<SubordinateResult.Data.SubordinateData> moreDatas = new ArrayList<SubordinateResult.Data.SubordinateData>();
    private ProgressWheelDialog dialog;
    private DBManager dbManager;
    private SubordinateDao dao;
    private String tag, permissions;

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new SubordinateDao();
        getSubordinates(page);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_subordinate;
    }

    @Override
    protected void initTitle(View titleView) {
        new TitleBarHelper(titleView).setHideTitleBar();
    }

    @Override
    protected void findView(View contentView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        ((LinearLayout) contentView.findViewById(R.id.refresh_empty_view_ll_no_refresh)).setVisibility(View.GONE);
        mManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));
        dialog = new ProgressWheelDialog(getActivity());
    }

    private void initData() {

        mAdapter = new RecycleAdapter<SubordinateResult.Data.SubordinateData>(UIUtil.getContext(), datas, true, R.layout.layout_subor_item, mSwipeRefreshLayout) {
            @Override
            public void convert(RecycleCommonViewHolder holder, final SubordinateResult.Data.SubordinateData data) {
                holder.setText(R.id.tv_code, "编号："+data.getId());
                holder.setText(R.id.tv_name, "姓名："+data.getPar_name());
                if("0".equals(data.getPar_level()))
                {
                    holder.setText(R.id.tv_level, "级别：分公司");
                }
                else if("1".equals(data.getPar_level()))
                {
                    holder.setText(R.id.tv_level, "级别：运营商");
                }
                else if ("2".equals(data.getPar_level()))
                {
                    holder.setText(R.id.tv_level, "级别：创客");
                }
                else
                {
                    holder.setText(R.id.tv_level, "级别：创客");
                }
//                else if ("3".equals(data.getPar_level()))
//                {
//                    holder.setText(R.id.tv_level, "级别：创客");
//                }
//                else if ("4".equals(data.getPar_level()))
//                {
//                    holder.setText(R.id.tv_level, "级别：创客");
//                }
//                else if ("-1".equals(data.getPar_level()))
//                {
//                    holder.setText(R.id.tv_level, "级别：创客");
//                }
//                else if ("-2".equals(data.getPar_level()))
//                {
//                    holder.setText(R.id.tv_level, "级别：创客");
//                }
//                else if ("-3".equals(data.getPar_level()))
//                {
//                    holder.setText(R.id.tv_level, "级别：创客");
//                }

                if(!StringUtil.isEmpty(data.getSuper_id()))
                {
                    holder.getView(R.id.tv_super_code).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_super_code, "所属直接成员："+data.getSuper_id());
                }
                else
                {
                    holder.getView(R.id.tv_super_code).setVisibility(View.GONE);
                }

                if(!StringUtil.isEmpty(data.getSuper_par_name()))
                {
                    holder.getView(R.id.tv_super_name).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_super_name, "所属直接成员编号："+data.getSuper_par_name());
                }
                else
                {
                    holder.getView(R.id.tv_super_name).setVisibility(View.GONE);
                }

                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
                        Intent intent = new Intent(getActivity(), SubordinateDetailActivity.class);
                        intent.putExtra("name", datas.get(position).getPar_name());
                        intent.putExtra("ord_managerno", datas.get(position).getId());
                        intent.putExtra("permissions", permissions);
                        intent.putExtra("level", datas.get(position).getPar_level());
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isFresh = false;
                                    page += 1;
                                    getSubordinates(page);
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
                                if(mAdapter != null)
                                {
                                    mAdapter.setHavefooter(true);
                                    mAdapter.setmLoadFinish(false);
                                }
                                isFresh = true;
                                page = 1;
                                getSubordinates(page);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    void getSubordinates(final int page)
    {
        getPreSubordinatesData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(getActivity())))
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
        dao.getSubordinates(SesSharedReferences.getSafetyMark(getActivity()), page+"", tag, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                SubordinateResult servicesResult = (SubordinateResult)object;
                showServicesData(servicesResult);
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
                if(statusCode == 4)
                {
                    gotoLogin();
                }
                else
                {
                    ToastUtil.showToast(message);
                    if (datas.size() <= 0 && moreDatas.size() <= 0) {
                        mDiffViewHelper.showEmptyView();
                    } else {
                        mDiffViewHelper.showDataView();
                    }
                }
            }
        });
    }

    /**
     * 获取缓存下来的我的发布的数据
     */
    void getPreSubordinatesData()
    {
        if(page == 1)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("safetyMark", SesSharedReferences.getSafetyMark(getActivity()));
            map.put("page", "1");
            map.put("tag", tag);

            String js = dbManager.getUrlJsonData(Constant.USER_MY_PARTNER + StringUtil.obj2JsonStr(map));
            if(!StringUtil.isEmpty(js))
            {
                SubordinateResult subordinateResult = new Gson().fromJson(js, SubordinateResult.class);
                showServicesData(subordinateResult);
                if(subordinateResult != null && subordinateResult.getData().size()>0)
                {
                    isFresh = true;
                }
            }
        }
    }

    void showServicesData(SubordinateResult subordinateResult)
    {
        if(subordinateResult != null)
        {
            permissions = subordinateResult.getData().get(0).getPermissions();
            moreDatas = subordinateResult.getData().get(0).getDate();
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
                            if (moreDatas.size() < 10) {
                                mAdapter.setHavefooter(false);
                                mAdapter.setmLoadFinish(true);
                            } else {
                                mAdapter.setHavefooter(true);
                                mAdapter.setmLoadFinish(false);
                            }
                        }
                    } else {
                        datas = moreDatas;
                        initData();
                    }
                } else {
                    mSwipeRefreshLayout.setEnabled(true);
                    if(mAdapter != null) {
                        mAdapter.loadMore(moreDatas); //数据长度为0,表示没有更多数据
                        mAdapter.notifyDataSetChanged();
                        if (moreDatas.size() < 10) {
                            mAdapter.setHavefooter(false);
                            mAdapter.setmLoadFinish(true);
                        } else {
                            mAdapter.setHavefooter(true);
                            mAdapter.setmLoadFinish(false);
                        }
                    }
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
        getSubordinates(page);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.LOGIN_OK)
        {
            getSubordinates(page);
        }
    }

    public void freshView()
    {
        LogUtil.e("刷新界面");
        if(mAdapter != null)
        {
            mAdapter.setHavefooter(true);
            mAdapter.setmLoadFinish(false);
        }
        isFresh = true;
        page = 1;
        getSubordinates(page);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

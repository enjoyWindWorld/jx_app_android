package com.jx.intelligent.ui.fragments;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.RecycleAdapter;
import com.jx.intelligent.adapter.RecycleCommonAdapter;
import com.jx.intelligent.adapter.holder.RecycleCommonViewHolder;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.base.RHBaseFragment;
import com.jx.intelligent.dao.CustomerServiceDao;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.OnItemClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.listener.OnRecycleScrollListener;
import com.jx.intelligent.result.CustomerServiceTaskDetailResult;
import com.jx.intelligent.result.CustomerServiceTasksResult;
import com.jx.intelligent.ui.activitise.customerService.AddChangeFilterTaskActivity;
import com.jx.intelligent.ui.activitise.customerService.AddRepairMachineTaskActivity;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.SpaceItemDecoration;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.util.ArrayList;
import java.util.List;

public class RepairTaskListFragment extends RHBaseFragment {

    private List<CustomerServiceTasksResult.Data> datas = new ArrayList<CustomerServiceTasksResult.Data>();
    private List<CustomerServiceTasksResult.Data> moreDatas = new ArrayList<CustomerServiceTasksResult.Data>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecycleCommonAdapter mAdapter;
    ProgressWheelDialog dialog;
    int page = 1;
    private boolean isFresh;
    private DBManager dbManager;
    private CustomerServiceDao dao;
    String type;//1：进行中， 200：已完成

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new CustomerServiceDao();
        getData();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.fragment_repair_task_list;
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

    private void initData() {

        mAdapter = new RecycleAdapter<CustomerServiceTasksResult.Data>(UIUtil.getContext(), datas, true, R.layout.layout_repair_item, mSwipeRefreshLayout) {
            @Override
            public void convert(RecycleCommonViewHolder holder, final CustomerServiceTasksResult.Data data) {
                if(data.getFas_type().equals("1"))
                {
                    holder.setText(R.id.tv_type, "滤芯更换");
                }
                else if(data.getFas_type().equals("2"))
                {
                    holder.setText(R.id.tv_type, "设备报障");
                }
                else if(data.getFas_type().equals("3"))
                {
                    holder.setText(R.id.tv_type, "其他");
                }

                if(data.getFas_state().equals("1"))
                {
                    holder.setText(R.id.tv_statu, "进行中");
                }
                else
                {
                    holder.setText(R.id.tv_statu, "完成");
                }

                if(!StringUtil.isEmpty(data.getSpecific_reason()))
                {
                    holder.setText(R.id.tv_repair, data.getSpecific_reason());
                }

                holder.setText(R.id.tv_date, data.getFas_addtime());
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
                        getTaskDetailInfo(data.getId());
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
                                    getData();
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
                                getData();
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
    public void getData() {
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
        dao.getMyTaskList(type, SesSharedReferences.getUserId(getActivity()), page+"", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                CustomerServiceTasksResult customerServiceTasksResult = (CustomerServiceTasksResult) object;
                if(customerServiceTasksResult != null)
                {
                    showData(customerServiceTasksResult.getData());
                }
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
                if (datas.size() <= 0 && moreDatas.size() <= 0) {
                    mDiffViewHelper.showEmptyView();
                } else {
                    mDiffViewHelper.showDataView();
                }
            }
        });
    }

    /**
     * 显示数据
     * @param list
     */
    void showData(List<CustomerServiceTasksResult.Data> list)
    {
        if(list != null)
        {
            moreDatas = list;
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
        getData();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    void getTaskDetailInfo(String id)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }
        dialog.show();
        dao.getCustomerServiceTaskDetail(id, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                CustomerServiceTaskDetailResult customerServiceTaskDetailResult = (CustomerServiceTaskDetailResult)object;
                if(customerServiceTaskDetailResult != null && customerServiceTaskDetailResult.getData().size() > 0)
                {
                    CustomerServiceTaskDetailResult.Data data = customerServiceTaskDetailResult.getData().get(0);
                    if(data.getFas_type().equals("1"))
                    {
                        Intent intent = new Intent(getActivity(), AddChangeFilterTaskActivity.class);
                        intent.putExtra("obj", data);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getActivity(), AddRepairMachineTaskActivity.class);
                        intent.putExtra("obj", data);
                        intent.putExtra("type", data.getFas_type());
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }
}

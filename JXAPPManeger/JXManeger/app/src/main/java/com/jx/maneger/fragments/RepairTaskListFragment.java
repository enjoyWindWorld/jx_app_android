package com.jx.maneger.fragments;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jx.maneger.R;
import com.jx.maneger.activities.CustomerService.AddChangeFilterTaskActivity;
import com.jx.maneger.activities.CustomerService.AddRepairMachineTaskActivity;
import com.jx.maneger.adapter.RecycleAdapter;
import com.jx.maneger.adapter.RecycleCommonAdapter;
import com.jx.maneger.adapter.holder.RecycleCommonViewHolder;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.base.RHBaseFragment;
import com.jx.maneger.dao.CustomerServiceDao;
import com.jx.maneger.dao.WithdrawalDao;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.OnItemClickListener;
import com.jx.maneger.intf.OnRecycleScrollListener;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.CustomerServiceTaskDetailResult;
import com.jx.maneger.results.CustomerServiceTasksReult;
import com.jx.maneger.results.WidthdrawalRecordResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.UIUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.SpaceItemDecoration;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

import java.util.ArrayList;
import java.util.List;

public class RepairTaskListFragment extends RHBaseFragment {

    private List<CustomerServiceTasksReult.Data> datas = new ArrayList<CustomerServiceTasksReult.Data>();
    private List<CustomerServiceTasksReult.Data> moreDatas = new ArrayList<CustomerServiceTasksReult.Data>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecycleCommonAdapter mAdapter;
    ProgressWheelDialog dialog;
    int page = 1;
    private boolean isFresh;
    private DBManager dbManager;
    CustomerServiceDao dao;
    String type;//售后状态: 1 进行 200 完成

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

        mAdapter = new RecycleAdapter<CustomerServiceTasksReult.Data>(UIUtil.getContext(), datas, true, R.layout.layout_repair_item, mSwipeRefreshLayout) {
            @Override
            public void convert(RecycleCommonViewHolder holder, final CustomerServiceTasksReult.Data data) {
                if(data.getFas_type().equals("1"))
                {
                    holder.setText(R.id.tv_repair, "滤芯更换");
                }
                else if(data.getFas_type().equals("2"))
                {
                    holder.setText(R.id.tv_repair, "设备报修");
                }
                else if(data.getFas_type().equals("3"))
                {
                    holder.setText(R.id.tv_repair, "其他");
                }

                holder.setText(R.id.tv_date, data.getFas_addtime());
                holder.setText(R.id.tv_name, data.getContact_person());
                holder.setText(R.id.tv_phone, data.getContact_way());
                holder.setText(R.id.tv_addr, data.getAddress_details());
                holder.getView(R.id.img_jt).setVisibility(View.VISIBLE);

                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
                        getTaskDetail(data.getId());
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
        dao.getTasks(SesSharedReferences.getSafetyMark(UIUtil.getContext()), page+"", type, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                CustomerServiceTasksReult customerServiceTasksReult = (CustomerServiceTasksReult) object;
                showData(customerServiceTasksReult.getData());
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
     * 显示数据
     * @param list
     */
    void showData(List<CustomerServiceTasksReult.Data> list)
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

    void getTaskDetail(String id)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(getActivity())))
        {
            return;
        }

        dialog.show();
        dao.getCustomerServiceDetail(SesSharedReferences.getSafetyMark(UIUtil.getContext()), id, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                CustomerServiceTaskDetailResult customerServiceTaskDetailResult = (CustomerServiceTaskDetailResult) object;
                if(customerServiceTaskDetailResult != null && customerServiceTaskDetailResult.getData().size() > 0)
                {
                    if(customerServiceTaskDetailResult.getData().get(0).getFas_type().equals("1"))
                    {
                        Intent intent = new Intent(getActivity(), AddChangeFilterTaskActivity.class);
                        intent.putExtra("obj", customerServiceTaskDetailResult.getData().get(0));
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(getActivity(), AddRepairMachineTaskActivity.class);
                        intent.putExtra("obj", customerServiceTaskDetailResult.getData().get(0));
                        intent.putExtra("type", customerServiceTaskDetailResult.getData().get(0).getFas_type());
                        startActivity(intent);
                    }
                }
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
                }
            }
        });
    }
}

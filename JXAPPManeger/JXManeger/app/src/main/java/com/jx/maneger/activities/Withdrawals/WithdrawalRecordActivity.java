package com.jx.maneger.activities.Withdrawals;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jx.maneger.R;
import com.jx.maneger.activities.MainActivity;
import com.jx.maneger.adapter.RecycleAdapter;
import com.jx.maneger.adapter.RecycleCommonAdapter;
import com.jx.maneger.adapter.holder.RecycleCommonViewHolder;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.SubordinateDao;
import com.jx.maneger.dao.WithdrawalDao;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.OnItemClickListener;
import com.jx.maneger.intf.OnRecycleScrollListener;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.SubordinateResult;
import com.jx.maneger.results.WidthdrawalRecordResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.UIUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.SpaceItemDecoration;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提现记录界面
 * Created by Administrator on 2017/8/11.
 */

public class WithdrawalRecordActivity extends RHBaseActivity{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecycleCommonAdapter mAdapter;
    private TitleBarHelper titleBarHelper;
    private int page = 1;
    private boolean isFresh;
    private List<WidthdrawalRecordResult.Data> datas = new ArrayList<WidthdrawalRecordResult.Data>();
    private List<WidthdrawalRecordResult.Data> moreDatas = new ArrayList<WidthdrawalRecordResult.Data>();
    private ProgressWheelDialog dialog;
    private DBManager dbManager;
    private WithdrawalDao dao;
    private DecimalFormat decimalFormat;

    @Override
    protected void init() {
        decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new WithdrawalDao();
        getWithdrawalRecords(page);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_subordinate;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(WithdrawalRecordActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        ((LinearLayout) contentView.findViewById(R.id.refresh_empty_view_ll_no_refresh)).setVisibility(View.GONE);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));
        titleBarHelper.setMiddleTitleText("提现记录");
        dialog = new ProgressWheelDialog(WithdrawalRecordActivity.this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
        }
    }

    private void initData() {

        mAdapter = new RecycleAdapter<WidthdrawalRecordResult.Data>(UIUtil.getContext(), datas, true, R.layout.layout_widthdrawal_record_item, mSwipeRefreshLayout) {
            @Override
            public void convert(RecycleCommonViewHolder holder, final WidthdrawalRecordResult.Data data) {
                /**
                 * withdrawal_way
                 * -1：待提现
                 * 0：提现发起
                 * 1：审核失败
                 * 2：提现取消
                 * 3：审核成功
                 * 4：提现失败
                 * 200：提现成功
                 */
                if(data.getWithdrawal_state() == 0)//提现发起
                {
                    holder.getView(R.id.layout_reason).setVisibility(View.GONE);
                    holder.setText(R.id.tv_audit_time_title, "预计审核时间");
                    holder.getView(R.id.layout_arrive_time).setVisibility(View.GONE);
                    holder.getView(R.id.layout_reason).setVisibility(View.GONE);

                    holder.setText(R.id.tv_state, "提现发起");
                    holder.setText(R.id.tv_money, "¥"+decimalFormat.format(data.getWithdrawal_amount())+"元");
                    holder.setText(R.id.tv_mode, data.getWithdrawal_way() == 0 ? "支付宝":"其他方式");
                    holder.setText(R.id.tv_date, data.getAdd_time());
                    holder.setText(R.id.tv_start_time, data.getAdd_time());
                    holder.setText(R.id.tv_audit_time, data.getAudit_time());
                    holder.setText(R.id.tv_transaction_number, data.getWithdrawal_order());
                }
                else if(data.getWithdrawal_state() == 3)//提现审核通过
                {
                    holder.getView(R.id.layout_reason).setVisibility(View.GONE);
                    holder.setText(R.id.tv_audit_time_title, "审核时间");
                    holder.getView(R.id.layout_arrive_time).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_arrive_time_title, "预期到账时间");
                    holder.getView(R.id.layout_reason).setVisibility(View.GONE);

                    holder.setText(R.id.tv_state, "审核通过");
                    holder.setText(R.id.tv_money, "¥"+decimalFormat.format(data.getWithdrawal_amount())+"元");
                    holder.setText(R.id.tv_mode, data.getWithdrawal_way() == 0 ? "支付宝":"其他方式");
                    holder.setText(R.id.tv_date, data.getAdd_time());
                    holder.setText(R.id.tv_start_time, data.getAdd_time());
                    holder.setText(R.id.tv_arrive_time, data.getArrive_time());
                    holder.setText(R.id.tv_transaction_number, data.getWithdrawal_order());
                }
                else if(data.getWithdrawal_state() == 200)//提现到账
                {
                    holder.getView(R.id.layout_reason).setVisibility(View.GONE);
                    holder.setText(R.id.tv_audit_time_title, "审核时间");
                    holder.getView(R.id.layout_arrive_time).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_arrive_time_title, "到账时间");
                    holder.getView(R.id.layout_reason).setVisibility(View.GONE);

                    holder.setText(R.id.tv_state, "提现到账");
                    holder.setText(R.id.tv_money, "¥"+decimalFormat.format(data.getWithdrawal_amount())+"元");
                    holder.setText(R.id.tv_mode, data.getWithdrawal_way() == 0 ? "支付宝":"其他方式");
                    holder.setText(R.id.tv_date, data.getAdd_time());
                    holder.setText(R.id.tv_start_time, data.getAdd_time());
                    holder.setText(R.id.tv_audit_time, data.getAudit_time());
                    holder.setText(R.id.tv_arrive_time, data.getArrive_time());
                    holder.setText(R.id.tv_transaction_number, data.getWithdrawal_order());
                }
                else if(data.getWithdrawal_state() == 1)//审核失败
                {
                    holder.getView(R.id.layout_reason).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_audit_time_title, "审核时间");
                    holder.getView(R.id.layout_arrive_time).setVisibility(View.GONE);
                    holder.getView(R.id.layout_reason).setVisibility(View.VISIBLE);

                    holder.setText(R.id.tv_state, "审核失败");
                    holder.setText(R.id.tv_money, "¥"+decimalFormat.format(data.getWithdrawal_amount())+"元");
                    holder.setText(R.id.tv_mode, data.getWithdrawal_way() == 0 ? "支付宝":"其他方式");
                    holder.setText(R.id.tv_date, data.getAdd_time());
                    holder.setText(R.id.tv_start_time, data.getAdd_time());
                    holder.setText(R.id.tv_audit_time, data.getAudit_time());
                    holder.setText(R.id.tv_reason, StringUtil.isEmpty(data.getWithdrawal_reason()) ? "未知原因" : data.getWithdrawal_reason());
                    holder.setText(R.id.tv_transaction_number, data.getWithdrawal_order());
                }
                else if(data.getWithdrawal_state() == 4)//提现失败
                {
                    holder.getView(R.id.layout_reason).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_audit_time_title, "审核时间");
                    holder.getView(R.id.layout_arrive_time).setVisibility(View.GONE);
                    holder.getView(R.id.layout_reason).setVisibility(View.VISIBLE);

                    holder.setText(R.id.tv_state, "提现失败");
                    holder.setText(R.id.tv_money, "¥"+decimalFormat.format(data.getWithdrawal_amount())+"元");
                    holder.setText(R.id.tv_mode, data.getWithdrawal_way() == 0 ? "支付宝":"其他方式");
                    holder.setText(R.id.tv_date, data.getAdd_time());
                    holder.setText(R.id.tv_start_time, data.getAdd_time());
                    holder.setText(R.id.tv_audit_time, data.getAudit_time());
                    holder.setText(R.id.tv_reason, StringUtil.isEmpty(data.getWithdrawal_reason()) ? "未知原因" : data.getWithdrawal_reason());
                    holder.setText(R.id.tv_transaction_number, data.getWithdrawal_order());
                }
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");

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
                                    getWithdrawalRecords(page);
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
                                if(mAdapter != null)
                                {
                                    mAdapter.setHavefooter(true);
                                    mAdapter.setmLoadFinish(false);
                                }
                                isFresh = true;
                                page = 1;
                                getWithdrawalRecords(page);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    void getWithdrawalRecords(final int page)
    {
        getPreWithdrawalRecordsData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(WithdrawalRecordActivity.this)))
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
        dao.getWidthdrawalRecords(SesSharedReferences.getSafetyMark(UIUtil.getContext()), page+"", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                WidthdrawalRecordResult widthdrawalRecordResult = (WidthdrawalRecordResult)object;
                showServicesData(widthdrawalRecordResult);
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
    void getPreWithdrawalRecordsData()
    {
        if(page == 1)
        {
            Map<String, String> map = new HashMap<String, String>();
            map.put("safetyMark", SesSharedReferences.getSafetyMark(WithdrawalRecordActivity.this));
            map.put("page", "1");

            String js = dbManager.getUrlJsonData(Constant.USER_WITHDRAWAL_RECORD + StringUtil.obj2JsonStr(map));
            if(!StringUtil.isEmpty(js))
            {
                WidthdrawalRecordResult widthdrawalRecordResult = new Gson().fromJson(js, WidthdrawalRecordResult.class);
                showServicesData(widthdrawalRecordResult);
                if(widthdrawalRecordResult != null && widthdrawalRecordResult.getData().size()>0)
                {
                    isFresh = true;
                }
            }
        }
    }

    void showServicesData(WidthdrawalRecordResult subordinateResult)
    {
        if(subordinateResult != null)
        {
            moreDatas = subordinateResult.getData();
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
        getWithdrawalRecords(page);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.LOGIN_OK)
        {
            getWithdrawalRecords(page);
        }
    }
}

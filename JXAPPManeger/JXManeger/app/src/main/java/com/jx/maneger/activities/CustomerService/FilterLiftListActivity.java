package com.jx.maneger.activities.CustomerService;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.adapter.RecycleAdapter;
import com.jx.maneger.adapter.RecycleCommonAdapter;
import com.jx.maneger.adapter.holder.RecycleCommonViewHolder;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.dao.CustomerServiceDao;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.OnItemClickListener;
import com.jx.maneger.intf.OnRecycleScrollListener;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.FilterLiftsResult;
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

public class FilterLiftListActivity extends RHBaseActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecycleCommonAdapter mAdapter;
    private int page = 1;
    private boolean isFresh;
    private List<FilterLiftsResult.FilterLiftInfo> datas = new ArrayList<FilterLiftsResult.FilterLiftInfo>();
    private List<FilterLiftsResult.FilterLiftInfo> moreDatas = new ArrayList<FilterLiftsResult.FilterLiftInfo>();
    private ProgressWheelDialog dialog;
    private DBManager dbManager;
    private CustomerServiceDao dao;
    private EditText searchBox;
    private ImageView clearBtn;
    private TextView txt_search;

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new CustomerServiceDao();
        getData(page);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_filter_lift_list;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(FilterLiftListActivity.this).setMiddleTitleText("套餐滤芯状况统计")
                .setLeftImageRes(R.drawable.selector_back)
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));
        dialog = new ProgressWheelDialog(FilterLiftListActivity.this);
        clearBtn = (ImageView) contentView.findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(this);

        txt_search = (TextView) findViewById(R.id.txt_search);
        txt_search.setOnClickListener(this);

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
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        hideSoftKeyboard(searchBox);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.iv_search_clear:
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                break;
            case R.id.txt_search:
                if(!TextUtils.isEmpty(searchBox.getText().toString()))
                {
                    page = 1;
                    datas.clear();
                    moreDatas.clear();
                    searchData(searchBox.getText().toString(), page);
                }
                break;
        }
    }

    private void initData() {
        mAdapter = new RecycleAdapter<FilterLiftsResult.FilterLiftInfo>(UIUtil.getContext(), datas, true, R.layout.layout_filter_lift_item, mSwipeRefreshLayout) {
            @Override
            public void convert(RecycleCommonViewHolder holder, final FilterLiftsResult.FilterLiftInfo data) {
                holder.setText(R.id.tv_pro_name, data.getName());
                holder.setText(R.id.tv_ord_no, data.getOrd_no());
                holder.setText(R.id.tv_pro_no, data.getPro_no());
                holder.setText(R.id.tv_name, data.getOrd_receivename());
                holder.setText(R.id.tv_phone, data.getOrd_phone());
                holder.setText(R.id.tv_addr, data.getAdr_id());
                holder.setText(R.id.tv_pp, data.getFilter_state().get(0).getProportion()+"%");
                holder.setText(R.id.tv_cto, data.getFilter_state().get(1).getProportion()+"%");
                holder.setText(R.id.tv_ro, data.getFilter_state().get(2).getProportion()+"%");
                holder.setText(R.id.tv_fh, data.getFilter_state().get(3).getProportion()+"%");
                holder.setText(R.id.tv_day, data.getPro_day());
                holder.setText(R.id.tv_flow, data.getPro_restflow());
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
//                        Intent intent = new Intent(FilterLiftListActivity.this, RepairRecordActivity.class);
//                        intent.putExtra("pro_no", data.getPro_no());
//                        intent.putExtra("ord_no", data.getOrd_no());
//                        startActivity(intent);
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
                                    getData(page);
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
                                getData(page);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    void getData(final int page)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(FilterLiftListActivity.this)))
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
        dao.getfilterLifts(SesSharedReferences.getSafetyMark(UIUtil.getContext()), page+"", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                FilterLiftsResult filterLiftsResult = (FilterLiftsResult)object;
                showData(filterLiftsResult.getData());
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

    void searchData(String search, final int page)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(FilterLiftListActivity.this)))
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
        dao.searchFilterLifts(SesSharedReferences.getSafetyMark(UIUtil.getContext()), page+"", search, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                FilterLiftsResult filterLiftsResult = (FilterLiftsResult)object;
                showData(filterLiftsResult.getData());
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

    void showData(List<FilterLiftsResult.FilterLiftInfo> list)
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
        getData(page);
    }
}

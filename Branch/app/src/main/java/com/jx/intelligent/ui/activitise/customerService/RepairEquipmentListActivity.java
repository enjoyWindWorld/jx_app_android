package com.jx.intelligent.ui.activitise.customerService;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.adapter.RecycleAdapter;
import com.jx.intelligent.adapter.RecycleCommonAdapter;
import com.jx.intelligent.adapter.holder.RecycleCommonViewHolder;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CustomerServiceDao;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.OnItemClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.listener.OnRecycleScrollListener;
import com.jx.intelligent.result.GetMyWPListResult;
import com.jx.intelligent.result.GetRepairEquipmentResult;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.SpaceItemDecoration;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepairEquipmentListActivity extends RHBaseActivity {

    private List<GetRepairEquipmentResult.Data> datas = new ArrayList<GetRepairEquipmentResult.Data>();
    private List<GetRepairEquipmentResult.Data> moreDatas = new ArrayList<GetRepairEquipmentResult.Data>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecycleCommonAdapter mAdapter;
    ProgressWheelDialog dialog;
    int page = 1;
    private boolean isFresh;
    private DBManager dbManager;
    CustomerServiceDao dao;

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new CustomerServiceDao();
        getData();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_repair_equipment_list;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("选择设备")
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.srl);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));
        dialog = new ProgressWheelDialog(RepairEquipmentListActivity.this);
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

        mAdapter = new RecycleAdapter<GetRepairEquipmentResult.Data>(UIUtil.getContext(), datas, true, R.layout.layout_repair_equipment_item, mSwipeRefreshLayout) {
            @Override
            public void convert(RecycleCommonViewHolder holder, final GetRepairEquipmentResult.Data data) {
                holder.setImageByUrl(R.id.wd_jsq_icon, data.getUrl(), R.id.loading_anim_iv, R.mipmap.small_empty_img);
                holder.setText(R.id.txt_name, data.getName()+"（"+data.getColor()+"）");
                holder.setText(R.id.txt_ord_no, data.getOrd_no());
                holder.setText(R.id.txt_pro_no, data.getPro_no());
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
                        Intent intent = new Intent();
                        intent.putExtra("pro_id", data.getPro_id());
                        intent.putExtra("pro_name", data.getName());
                        intent.putExtra("ord_color", data.getColor());
                        intent.putExtra("pro_no", data.getPro_no());
                        intent.putExtra("ord_managerno", data.getOrd_managerno());
                        intent.putExtra("ord_no", data.getOrd_no());
                        setResult(Constant.SELECT_EQUIPMENT, intent);
                        finish();
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
                        runOnUiThread(new Runnable() {
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
        dao.getEquipments(SesSharedReferences.getUserId(RepairEquipmentListActivity.this), page+"", new ResponseResult() {

            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                GetRepairEquipmentResult result = (GetRepairEquipmentResult) object;
                showWPData(result);
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
            String js = dbManager.getUrlJsonData(Constant.SC_REPAIR_EQUIPMENT_LIST + StringUtil.obj2JsonStr(map));
            if(!StringUtil.isEmpty(js))
            {
                GetRepairEquipmentResult result = new Gson().fromJson(js, GetRepairEquipmentResult.class);
                showWPData(result);
                if(result != null && result.getData().size() > 0)
                {
                    isFresh = true;
                }
            }
        }
    }

    /**
     * 显示数据
     * @param result
     */
    void showWPData(GetRepairEquipmentResult result)
    {
        if(result != null)
        {
            moreDatas = result.getData();
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
                        mAdapter.notifyDataSetChanged();
                    } else {
                        datas = moreDatas;
                        initData();
                    }
                } else {
                    mAdapter.loadMore(moreDatas); //数据长度为0,表示没有更多数据
                    mAdapter.notifyDataSetChanged();
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

}

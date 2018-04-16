package com.jx.intelligent.ui.activitise.customerService;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

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
import com.jx.intelligent.result.EquipmentFilterLiftsResult;
import com.jx.intelligent.result.FaultTypeResult;
import com.jx.intelligent.util.LogUtil;
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

public class FaultTypeActivity extends RHBaseActivity {

    private CustomerServiceDao dao;
    private ProgressWheelDialog dialog;
    private DBManager dbManager;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecycleCommonAdapter mAdapter;
    private List<FaultTypeResult.Data> datas = new ArrayList<FaultTypeResult.Data>();
    //0为上架故障 1为下架故障
    private String is_shelves = "0";

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new CustomerServiceDao();
        getData();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_fault_type;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(FaultTypeActivity.this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("故障现象")
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
        dialog = new ProgressWheelDialog(FaultTypeActivity.this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
        }
    }

    void getData()
    {
        getPreAdData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }
        dialog.show();
        dao.getFaultTypes(is_shelves, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                FaultTypeResult faultTypeResult = (FaultTypeResult)object;
                showData(faultTypeResult);
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 获取故障现象的缓存数据
     */
    void getPreAdData()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("is_shelves", is_shelves);
        String js = dbManager.getUrlJsonData(Constant.SC_FAULT_TYPE + StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            FaultTypeResult faultTypeResult = new Gson().fromJson(js, FaultTypeResult.class);
            showData(faultTypeResult);
        }
    }

    void showData(FaultTypeResult faultTypeResult)
    {
        if(faultTypeResult != null && faultTypeResult.getData().size() > 0)
        {
            datas = faultTypeResult.getData();
            initData();
        }
    }

    private void initData() {

        mAdapter = new RecycleAdapter<FaultTypeResult.Data>(UIUtil.getContext(), datas, true, R.layout.layout_fault_type_item, null) {
            @Override
            public void convert(final RecycleCommonViewHolder holder, final FaultTypeResult.Data data) {

                holder.setText(R.id.txt_name, data.getFault_cause());

                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
                        Intent intent = new Intent();
                        intent.putExtra("id", data.getId());
                        intent.putExtra("name", data.getFault_cause());
                        setResult(Constant.SELECT_FAULT_TYPE, intent);
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

        mAdapter.setHavefooter(false);
        mAdapter.setmLoadFinish(false);
        mRecyclerView.setAdapter(mAdapter);
    }
}

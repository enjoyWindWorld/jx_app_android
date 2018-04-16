package com.jx.intelligent.ui.activitise.customerService;

import android.content.Intent;
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

public class RepairFilterDetailActivity extends RHBaseActivity {

    private CustomerServiceDao dao;
    ProgressWheelDialog dialog;
    private String pro_no;
    private DBManager dbManager;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mManager;
    private RecycleCommonAdapter mAdapter;
    private List<EquipmentFilterLiftsResult.EquipmentFilterLiftInfo> datas = new ArrayList<EquipmentFilterLiftsResult.EquipmentFilterLiftInfo>();
    private String proflt_life = "", filter_name = "";

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new CustomerServiceDao();
        pro_no = getIntent().getStringExtra("pro_no");
        getData();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_repair_filter_detail;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(RepairFilterDetailActivity.this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("选择滤芯")
                .setRightText("确认")
                .setLeftClickListener(this)
                .setRightClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
        dialog = new ProgressWheelDialog(RepairFilterDetailActivity.this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.titlebar_right_rl:
                if(datas.size() > 0)
                {
                    for (EquipmentFilterLiftsResult.EquipmentFilterLiftInfo info : datas)
                    {
                        if(info.isCheck())
                        {
                            proflt_life += info.getProportion()+",";
                            filter_name += info.getName()+",";
                        }
                    }

                    Intent intent = new Intent();
                    intent.putExtra("proflt_life", proflt_life);
                    intent.putExtra("filter_name", filter_name);
                    setResult(Constant.SELECT_FILTER, intent);
                    finish();
                }
                else
                {
                    ToastUtil.showToast("请选择要更换的滤芯");
                }

                break;
        }
    }

    void getData()
    {
        getPreAdData(pro_no);
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }
        dialog.show();
        dao.getEquipmentFilterLifts(pro_no, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                EquipmentFilterLiftsResult equipmentFilterLiftsResult = (EquipmentFilterLiftsResult)object;
                showData(equipmentFilterLiftsResult);
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 获取对应机器的剩余滤芯寿命的缓存数据
     * @param pro_no
     */
    void getPreAdData(String pro_no)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("pro_no", pro_no);
        String js = dbManager.getUrlJsonData(Constant.SC_REPAIR_EQUIPMENT_FILTER_LIFT + StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            EquipmentFilterLiftsResult equipmentFilterLiftsResult = new Gson().fromJson(js, EquipmentFilterLiftsResult.class);
            showData(equipmentFilterLiftsResult);
        }
    }

    void showData(EquipmentFilterLiftsResult equipmentFilterLiftsResult)
    {
        if(equipmentFilterLiftsResult != null && equipmentFilterLiftsResult.getData().size() > 0)
        {
            datas = equipmentFilterLiftsResult.getData();
            initData();
        }
    }

    private void initData() {

        mAdapter = new RecycleAdapter<EquipmentFilterLiftsResult.EquipmentFilterLiftInfo>(UIUtil.getContext(), datas, true, R.layout.layout_equipment_filter_lift_item, null) {
            @Override
            public void convert(final RecycleCommonViewHolder holder, final EquipmentFilterLiftsResult.EquipmentFilterLiftInfo data) {

                holder.setText(R.id.txt_name, data.getName());
                holder.setText(R.id.txt_lift, data.getProportion()+"%");

                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了条目");
                        if(data.isCheck())
                        {
                            ((CheckBox)holder.getView(R.id.my_checkbox)).setChecked(false);
                            data.setCheck(false);
                        }
                        else
                        {
                            ((CheckBox)holder.getView(R.id.my_checkbox)).setChecked(true);
                            data.setCheck(true);
                        }
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

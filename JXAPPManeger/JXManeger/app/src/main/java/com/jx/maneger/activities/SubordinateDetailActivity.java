package com.jx.maneger.activities;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.util.StringUtil;

/**
 * 下级界面
 * Created by Administrator on 2017/8/11.
 */

public class SubordinateDetailActivity extends RHBaseActivity{

    private TitleBarHelper titleBarHelper;
    private LinearLayout layout_order, layout_allocation_proportion;
    private String name, ord_managerno, permissions, level;

    @Override
    protected void init() {
        if(getIntent() != null)
        {
            name = getIntent().getStringExtra("name");
            ord_managerno = getIntent().getStringExtra("ord_managerno");
            permissions = getIntent().getStringExtra("permissions");
            level = getIntent().getStringExtra("level");
        }

        if(StringUtil.isEmpty(name))
        {
            ((TextView) findViewById(R.id.titlebar_center_tv)).setText("详情");
        }
        else
        {
            ((TextView) findViewById(R.id.titlebar_center_tv)).setText(name + "详情");
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_subordinate_detail;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(SubordinateDetailActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        titleBarHelper.setMiddleTitleText("下属详情");
        layout_order = (LinearLayout) contentView.findViewById(R.id.layout_order);
        layout_allocation_proportion = (LinearLayout) contentView.findViewById(R.id.layout_allocation_proportion);

        layout_order.setOnClickListener(this);
        layout_allocation_proportion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.layout_order:
                Intent intent = new Intent(SubordinateDetailActivity.this, MySubordinateOrderActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("ord_managerno", ord_managerno);
                startActivity(intent);
                break;
            case R.id.layout_allocation_proportion:
                intent = new Intent(SubordinateDetailActivity.this, UpdateAllocationProportionActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("permissions", permissions);
                intent.putExtra("level", level);
                intent.putExtra("ord_managerno", ord_managerno);
                startActivityForResult(intent, Constant.REQUEST_CODE);
                break;
        }
    }
}

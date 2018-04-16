package com.jx.intelligent.ui.activitise.customerService;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.helper.TitleBarHelper;

public class CustomerServiceTypeActivity extends RHBaseActivity {

    LinearLayout layout_change_filter, layout_equipment_repair, layout_other;

    @Override
    protected void init() {

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_customer_service_type;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(CustomerServiceTypeActivity.this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("售后")
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        layout_change_filter = (LinearLayout) contentView.findViewById(R.id.layout_change_filter);
        layout_equipment_repair = (LinearLayout) contentView.findViewById(R.id.layout_equipment_repair);
        layout_other = (LinearLayout) contentView.findViewById(R.id.layout_other);

        layout_change_filter.setOnClickListener(this);
        layout_equipment_repair.setOnClickListener(this);
        layout_other.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.layout_change_filter:
                Intent intent_change_filter = new Intent(CustomerServiceTypeActivity.this, AddChangeFilterTaskActivity.class);
                startActivity(intent_change_filter);
                break;
            case R.id.layout_equipment_repair:
                Intent intent_equipment_repair = new Intent(CustomerServiceTypeActivity.this, AddRepairMachineTaskActivity.class);
                intent_equipment_repair.putExtra("type", 1);
                startActivity(intent_equipment_repair);
                break;
            case R.id.layout_other:
                Intent intent_other = new Intent(CustomerServiceTypeActivity.this, AddRepairMachineTaskActivity.class);
                intent_other.putExtra("type", 2);
                startActivity(intent_other);
                break;
        }
    }
}

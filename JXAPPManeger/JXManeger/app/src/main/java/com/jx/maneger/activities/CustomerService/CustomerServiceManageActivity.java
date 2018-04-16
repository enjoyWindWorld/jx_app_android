package com.jx.maneger.activities.CustomerService;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.helper.TitleBarHelper;

public class CustomerServiceManageActivity extends RHBaseActivity {

    private LinearLayout layout_filter_lift, layout_task, layout_notice;

    @Override
    protected void init() {

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_customer_service_manage;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(CustomerServiceManageActivity.this).setMiddleTitleText("售后管理")
                .setLeftImageRes(R.drawable.selector_back)
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        layout_filter_lift = (LinearLayout) contentView.findViewById(R.id.layout_filter_lift);
        layout_task = (LinearLayout) contentView.findViewById(R.id.layout_task);
        layout_notice = (LinearLayout) contentView.findViewById(R.id.layout_notice);

        layout_filter_lift.setOnClickListener(this);
        layout_task.setOnClickListener(this);
        layout_notice.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.layout_filter_lift:
                Intent intent_filter_lift = new Intent(CustomerServiceManageActivity.this, FilterLiftListActivity.class);
                startActivity(intent_filter_lift);
                break;
            case R.id.layout_task:
                Intent intent_task = new Intent(CustomerServiceManageActivity.this, TaskListActivity.class);
                startActivity(intent_task);
                break;
            case R.id.layout_notice:
                Intent intent_notice = new Intent(CustomerServiceManageActivity.this, FilterLiftNoticeListActivity.class);
                startActivity(intent_notice);
                break;
        }
    }
}

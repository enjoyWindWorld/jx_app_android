package com.jx.maneger.activities.CustomerService;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.results.CustomerServiceTaskDetailResult;

/**
 * Created by Administrator on 2017/11/9.
 */

public class AddChangeFilterTaskActivity extends RHBaseActivity {

    private EditText edit_name, edit_phone, edit_addr;
    private TextView txt_equipment, txt_filter, txt_date, txt_pcd, txt_fault_description;
    private CustomerServiceTaskDetailResult.Data data;

    @Override
    protected void init() {

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_add_change_filter_task;
    }

    @Override
    protected void initTitle() {
        data = (CustomerServiceTaskDetailResult.Data)getIntent().getSerializableExtra("obj");
        new TitleBarHelper(AddChangeFilterTaskActivity.this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("滤芯更换")
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {

        edit_name = (EditText) contentView.findViewById(R.id.edit_name);
        edit_phone = (EditText) contentView.findViewById(R.id.edit_phone);
        edit_addr = (EditText) contentView.findViewById(R.id.edit_addr);

        txt_equipment = (TextView) contentView.findViewById(R.id.txt_equipment);
        txt_filter = (TextView) contentView.findViewById(R.id.txt_filter);
        txt_date = (TextView) contentView.findViewById(R.id.txt_date);
        txt_pcd = (TextView) contentView.findViewById(R.id.txt_pcd);
        txt_fault_description = (TextView) contentView.findViewById(R.id.txt_fault_description);

        if(data != null)
        {
            showTaskDetail();
        }
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

    void showTaskDetail()
    {
        edit_name.setText(data.getContact_person());
        edit_phone.setText(data.getContact_way());
        edit_addr.setText(data.getAddress_details());
        txt_equipment.setText(data.getPro_name()+"（"+data.getOrd_no()+"）");
        txt_filter.setText(data.getFilter_name());
        txt_date.setText(data.getMake_time());
        txt_pcd.setText(data.getUser_address());
        txt_fault_description.setText(data.getSpecific_reason());
        txt_equipment.setTextColor(getResources().getColor(R.color.color_333333));
        txt_filter.setTextColor(getResources().getColor(R.color.color_333333));
        txt_date.setTextColor(getResources().getColor(R.color.color_333333));
        txt_pcd.setTextColor(getResources().getColor(R.color.color_333333));
        txt_fault_description.setTextColor(getResources().getColor(R.color.color_333333));
        edit_name.setEnabled(false);
        edit_phone.setEnabled(false);
        edit_addr.setEnabled(false);
        edit_name.setTextColor(getResources().getColor(R.color.color_333333));
        edit_phone.setTextColor(getResources().getColor(R.color.color_333333));
        edit_addr.setTextColor(getResources().getColor(R.color.color_333333));
    }

}

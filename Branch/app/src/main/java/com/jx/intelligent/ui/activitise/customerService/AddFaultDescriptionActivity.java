package com.jx.intelligent.ui.activitise.customerService;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.ui.activitise.personalcenter.FeedbackActivity;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

public class AddFaultDescriptionActivity extends RHBaseActivity {

    EditText edt_fault_description;
    private String description;

    @Override
    protected void init() {
        description = getIntent().getStringExtra("description");
        if(!StringUtil.isEmpty(description))
        {
            edt_fault_description.setText(description);
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_add_fault_description;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("故障说明")
                .setRightText("确定")
                .setLeftClickListener(this)
                .setRightClickListener(this);

    }

    @Override
    protected void findView(View contentView) {
        edt_fault_description = (EditText) contentView.findViewById(R.id.edt_fault_description);
        hideSoftKeyboard(edt_fault_description);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.titlebar_right_rl:
                description = edt_fault_description.getText().toString();
                if(!StringUtil.isEmpty(description))
                {
                    Intent intent = new Intent();
                    intent.putExtra("description", description);
                    setResult(Constant.ADD_FAULT_DESCRIPTION, intent);
                    finish();
                }
                else
                {
                    ToastUtil.showToast("请描述你遇到的问题");
                }
                break;
        }
    }
}

package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

/**
 * 设置个性签名
 * Created by Administrator on 2016/11/15 0015.
 */
public class SetPerdSignActivity extends RHBaseActivity {

    ImageView titlebar_left_vertical_iv;


    EditText grxx_gxqm_srk;

    @Override
    protected void init() {

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_set_perd_sign;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("个性签名")
                .setRightText("完成")
                .setLeftClickListener(this)
                .setRightClickListener(this);

    }

    @Override
    protected void findView(View contentView) {
        grxx_gxqm_srk = (EditText) contentView.findViewById(R.id.grxx_gxqm_srk);
        hideSoftKeyboard(grxx_gxqm_srk);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:

                finish();

                break;


            case R.id.titlebar_right_rl:

                //TODO 提交修改个性签名


                goPerInfo();

                break;


        }

    }

    /**
     * 跳转到设置个人签名
     */
    private void goPerInfo() {
        if(Utils.filterEmoji(grxx_gxqm_srk.getText().toString()))
        {
            ToastUtil.showToast("请勿输入表情符号");
            return;
        }
        Intent intent = new Intent(SetPerdSignActivity.this, PerInfoActivity.class);
        intent.putExtra("grqm_string", grxx_gxqm_srk.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}

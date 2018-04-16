package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

/**
 * 设置昵称
 * Created by Administrator on 2016/11/15 0015.
 */
public class SetNicknameActivity extends RHBaseActivity {

    ImageView titlebar_left_vertical_iv;

    EditText grxx_xgnc_srk;
    LinearLayout grxx_xgnc_clean;

    @Override
    protected void init() {

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_set_nickname;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("昵称")
                .setRightText("完成")
                .setLeftClickListener(this)
                .setRightClickListener(this);

    }

    @Override
    protected void findView(View contentView) {
        grxx_xgnc_srk = (EditText) contentView.findViewById(R.id.grxx_xgnc_srk);
        grxx_xgnc_clean = (LinearLayout) contentView.findViewById(R.id.grxx_xgnc_clean);

        grxx_xgnc_clean.setOnClickListener(this);
        hideSoftKeyboard(grxx_xgnc_srk);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:

                finish();

                break;

            case R.id.titlebar_right_rl:

                //TODO 提交修改昵称请求
                goPerInfo();

                break;
            case R.id.grxx_xgnc_clean:
                grxx_xgnc_srk.setText("");
                break;


        }
    }

    /**
     * 跳转到设置昵称
     */
    private void goPerInfo() {
        if(Utils.filterEmoji(grxx_xgnc_srk.getText().toString()))
        {
            ToastUtil.showToast("请勿输入表情符号");
            return;
        }
        Intent intent = new Intent(SetNicknameActivity.this, PerInfoActivity.class);
        intent.putExtra("nc_string", grxx_xgnc_srk.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

}

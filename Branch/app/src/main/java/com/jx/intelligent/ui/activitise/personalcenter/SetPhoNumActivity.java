package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.util.SesSharedReferences;

/**
 * 绑定手机界面
 * Created by Administrator on 2016/11/15 0015.
 */
public class SetPhoNumActivity extends RHBaseActivity {

    ImageView titlebar_left_vertical_iv;
    Button grxx_bdsj_bdan;
    TextView grxx_bdsj_sjhm;

    @Override
    protected void init() {
        grxx_bdsj_sjhm.setText("手机号码："+SesSharedReferences.getPhoneNum(SetPhoNumActivity.this));
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_set_pho_nom;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("绑定手机")
                .setLeftClickListener(this);

    }

    @Override
    protected void findView(View contentView) {

        grxx_bdsj_bdan = (Button) contentView.findViewById(R.id.grxx_bdsj_bdan);
        grxx_bdsj_sjhm = (TextView) contentView.findViewById(R.id.grxx_bdsj_sjhm);
        grxx_bdsj_bdan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.grxx_bdsj_bdan:
                goSetNewPhoNum();
                break;
        }
    }

    /**
     * 跳转到用户信息界面
     */
    private void goSetNewPhoNum() {
        Intent intent = new Intent(SetPhoNumActivity.this, SetNewPhoNumActivity.class);
        startActivity(intent);
    }

}

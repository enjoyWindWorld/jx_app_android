package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.helper.TitleBarHelper;

/**
 * 账户安全
 * Created by Administrator on 2016/11/15 0015.
 */
public class ActSecyActivity extends RHBaseActivity {

    ImageView titlebar_left_vertical_iv;

    LinearLayout grxx_zfaq_sjh_jr, grxx_zfaq_xgmm_jr;


    @Override
    protected void init() {


    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_act_secy;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("账户安全")
                .setLeftClickListener(this);

    }

    @Override
    protected void findView(View contentView) {
        grxx_zfaq_sjh_jr = (LinearLayout) contentView.findViewById(R.id.grxx_zfaq_sjh_jr);
        grxx_zfaq_xgmm_jr = (LinearLayout) contentView.findViewById(R.id.grxx_zfaq_xgmm_jr);
        grxx_zfaq_sjh_jr.setOnClickListener(this);
        grxx_zfaq_xgmm_jr.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:

                finish();

                break;


            case R.id.grxx_zfaq_sjh_jr:
                goSetPhoNum();
                break;
            case R.id.grxx_zfaq_xgmm_jr:
                goChagPasw();
                break;


        }

    }


    /**
     * 跳转到绑定手机号界面
     */
    private void goSetPhoNum() {
        Intent intent = new Intent(ActSecyActivity.this, SetPhoNumActivity.class);
        startActivity(intent);

    }

    /**
     * 跳转到绑定手机号界面
     */
    private void goChagPasw() {
        Intent intent = new Intent(ActSecyActivity.this, ChagPaswActivity.class);
        startActivity(intent);

    }


}

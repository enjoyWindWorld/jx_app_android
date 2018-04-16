package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

/**
 * 分享绑定
 * Created by Administrator on 2016/11/15 0015.
 */
public class SharedBindingsActivity extends RHBaseActivity {

    ImageView titlebar_left_vertical_iv;
    LinearLayout grxx_zfaq_sjh_jr, grxx_zfaq_xgmm_jr;
    EditText edit_targetnum;
    Button fxbd_btn;
    ProgressWheelDialog dialog;
    UserCenter dao;

    public ActivityType activityType;

    public enum ActivityType {
        actionFenX, actionTianJ
    }

    @Override
    protected void init() {
        initDialog();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_shared_bindings;
    }

    @Override
    protected void initTitle() {

        Intent intent = getIntent();
        Bundle actionFenX = intent.getBundleExtra("FenX");
        Bundle actionTianJ = intent.getBundleExtra("TianJ");
        if (actionFenX != null) {
            activityType = ActivityType.actionFenX;
            new TitleBarHelper(this)
                    .setLeftImageRes(R.drawable.selector_back)
                    .setMiddleTitleText("分享绑定")
                    .setLeftClickListener(this);
        } else if (actionTianJ != null) {
            activityType = ActivityType.actionTianJ;
            new TitleBarHelper(this)
                    .setLeftImageRes(R.drawable.selector_back)
                    .setMiddleTitleText("添加设备")
                    .setLeftClickListener(this);
        }


    }

    @Override
    protected void findView(View contentView) {

        fxbd_btn = (Button) contentView.findViewById(R.id.fxbd_btn);
        fxbd_btn.setOnClickListener(this);

        if (activityType == ActivityType.actionFenX) {
            fxbd_btn.setText("确定");
        } else if (activityType == ActivityType.actionTianJ) {
            fxbd_btn.setText("绑定");
        }

        edit_targetnum = (EditText) contentView.findViewById(R.id.edit_targetnum);

//        grxx_zfaq_xgmm_jr = (LinearLayout) contentView.findViewById(R.id.grxx_zfaq_xgmm_jr);
//        grxx_zfaq_sjh_jr.setOnClickListener(this);

        dialog = new ProgressWheelDialog(SharedBindingsActivity.this);
        dao = new UserCenter();

        hideSoftKeyboard(edit_targetnum);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.fxbd_btn:
                if(StringUtil.isEmpty(edit_targetnum.getText().toString()))
                {
                    ToastUtil.showToast("请输入对方手机号");
                }
                else if(!Utils.isMobileNO(edit_targetnum.getText().toString()))
                {
                    ToastUtil.showToast("输入的手机号码格式错误");
                }
                else
                {
                    normalAlertDialog.show();
                }
                break;
        }
    }

    void shareBind(String targetNum)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dao.shareBindTask(SesSharedReferences.getUserId(SharedBindingsActivity.this), targetNum, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                ToastUtil.showToast("分享绑定成功");
                finish();
            }

            @Override
            public void resFailure(String message) {
                ToastUtil.showToast(message);
            }
        });
    }

    NormalAlertDialog normalAlertDialog;
    private void initDialog()
    {

        normalAlertDialog = new NormalAlertDialog.Builder(SharedBindingsActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("是否要分享")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("")
                .setContentTextColor(R.color.color_000000)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.color_000000)
                .setRightButtonText("确定")
                .setRightButtonTextColor(R.color.color_000000)
                .setCancelable(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                    }
                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        shareBind(edit_targetnum.getText().toString());
                    }
                })
                .build();
    }

}

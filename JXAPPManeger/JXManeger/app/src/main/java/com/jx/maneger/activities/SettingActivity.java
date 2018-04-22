package com.jx.maneger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.DialogOnClickListener;
import com.jx.maneger.update.UpdateAgent;
import com.jx.maneger.util.AppUtil;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.UIUtil;
import com.jx.maneger.view.dialog.NormalAlertDialog;

import java.util.HashMap;

/**
 * 设置界面
 */
public class SettingActivity extends RHBaseActivity {

    LinearLayout grxx_sz_xgmm_jr, grxx_sz_qc_jr, layout_share;
    RelativeLayout grxx_sz_bbgx_jr;
    TextView grxx_sz_tcdl_jr, tv_version_code;
    private UpdateAgent mUpdateAgent;
    private UpdateAgent.CheckUpdateListener mCheckUpdateListener;
    private NormalAlertDialog dialog, dialog2;
    private DBManager dbManager;
    private String partnerNumber;

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        partnerNumber = SesSharedReferences.getPartnerNumber(UIUtil.getContext());
        initUpdate();
        initDialog();
        initQCDialog();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("设置")
                .setLeftClickListener(this);

    }

    @Override
    protected void findView(View contentView) {
        layout_share = (LinearLayout) contentView.findViewById(R.id.layout_share);
        grxx_sz_xgmm_jr = (LinearLayout) contentView.findViewById(R.id.grxx_sz_xgmm_jr);
        grxx_sz_bbgx_jr = (RelativeLayout) contentView.findViewById(R.id.grxx_sz_bbgx_jr);
        grxx_sz_qc_jr = (LinearLayout) contentView.findViewById(R.id.grxx_sz_qc_jr);
        grxx_sz_tcdl_jr = (TextView) contentView.findViewById(R.id.grxx_sz_tcdl_jr);
        tv_version_code = (TextView) contentView.findViewById(R.id.tv_version_code);
        grxx_sz_bbgx_jr.setOnClickListener(this);
        grxx_sz_tcdl_jr.setOnClickListener(this);
        grxx_sz_qc_jr.setOnClickListener(this);
        grxx_sz_xgmm_jr.setOnClickListener(this);
        layout_share.setOnClickListener(this);
        tv_version_code.setText(AppUtil.getVersionName());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.grxx_sz_bbgx_jr:
                mUpdateAgent.update();
                break;
            case R.id.grxx_sz_qc_jr:
                dialog2.show();
                break;
            case R.id.grxx_sz_tcdl_jr:
                dialog.show();
                break;
            case R.id.layout_share:
                Intent share_intent = new Intent(SettingActivity.this, CustomShareActivity.class);
                startActivity(share_intent);
                break;
            case R.id.grxx_sz_xgmm_jr:
                Intent intent = new Intent(SettingActivity.this, ChagPaswActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE);
                break;
        }

    }

    private void initUpdate() {
        mUpdateAgent = UpdateAgent.getInstance(this);
        mCheckUpdateListener = new SettingActivity.CheckUpdateListener();
        mUpdateAgent.setCheckUpdateListener(mCheckUpdateListener);
        //网络可用时调用
    }

    /**
     * 检查更新接口
     */
    private class CheckUpdateListener implements UpdateAgent.CheckUpdateListener {

        @Override
        public void onUpdateReturned(boolean isCanUpdate, HashMap<String, String> responseInfos) {
            LogUtil.e("responseInfos:" + responseInfos);
            if (isCanUpdate) {
                if (responseInfos != null && responseInfos.size() > 0) {
                    //有新版本
                    mUpdateAgent.showUpdateDialog(SettingActivity.this, responseInfos);
                }
            } else {
                ToastUtil.showToast("当前是最新版本");
            }
        }
    }

    private void initDialog() {

        dialog = new NormalAlertDialog.Builder(SettingActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("是否退出登录")
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
                        dialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        dialog.dismiss();
                        //解绑个推别名
                        PushManager.getInstance().unBindAlias(SettingActivity.this, partnerNumber, true);
                        SesSharedReferences.cleanShare(SettingActivity.this);
                        setResult(Constant.LOGIN_OUT);
                        finish();
                    }
                })
                .build();
    }

    private void initQCDialog() {
        dialog2 = new NormalAlertDialog.Builder(SettingActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("是否清除缓存")
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
                        dialog2.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        dialog2.dismiss();
                        dbManager.deleteUrlJsonData();
                        isDeleted();
                    }
                })
                .build();
    }

    /**
     * 是否还有缓存数据
     */
    void isDeleted() {
        if (dbManager.getCountUrlJsonData() > 0) {
            ToastUtil.showToast("清除缓存失败");
        } else {
            ToastUtil.showToast("清除缓存成功");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.UPDATE_PWD_OK)
        {
            //解绑个推别名
            PushManager.getInstance().unBindAlias(SettingActivity.this, partnerNumber, true);
            SesSharedReferences.cleanShare(SettingActivity.this);
            setResult(Constant.LOGIN_OUT);
            finish();
        }
    }
}

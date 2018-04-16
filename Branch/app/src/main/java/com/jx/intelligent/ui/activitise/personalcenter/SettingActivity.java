package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.result.LoginResult;
import com.jx.intelligent.ui.activitise.SplashActivity;
import com.jx.intelligent.ui.activitise.share.CustomShareActivity;
import com.jx.intelligent.update.UpdateAgent;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.view.dialog.NormalAlertDialog;

import java.util.HashMap;

/**
 * 设置界面
 * Created by Administrator on 2016/11/15 0015.
 */
public class SettingActivity extends RHBaseActivity {

    ImageView titlebar_left_vertical_iv;
    LinearLayout grxx_sz_zfaq_jr, grxx_sz_bbgx_jr, grxx_sz_gy_jr, grxx_sz_fxrj_lr, grxx_sz_qc_jr;
    TextView grxx_sz_tcdl_jr;
    private UpdateAgent mUpdateAgent;
    private UpdateAgent.CheckUpdateListener mCheckUpdateListener;
    private NormalAlertDialog dialog, dialog2;
    private DBManager dbManager;
    private String userId;

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        SesSharedReferences sp =new SesSharedReferences();
        userId = sp.getUserId(UIUtil.getContext());

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
                .setRightText("")
                .setLeftClickListener(this);

    }

    @Override
    protected void findView(View contentView) {
        grxx_sz_zfaq_jr = (LinearLayout) contentView.findViewById(R.id.grxx_sz_zfaq_jr);
        grxx_sz_bbgx_jr = (LinearLayout) contentView.findViewById(R.id.grxx_sz_bbgx_jr);
        grxx_sz_fxrj_lr = (LinearLayout) contentView.findViewById(R.id.grxx_sz_fxrj_lr);
        grxx_sz_gy_jr = (LinearLayout) contentView.findViewById(R.id.grxx_sz_gy_jr);
        grxx_sz_qc_jr = (LinearLayout) contentView.findViewById(R.id.grxx_sz_qc_jr);
        grxx_sz_tcdl_jr = (TextView) contentView.findViewById(R.id.grxx_sz_tcdl_jr);
        grxx_sz_zfaq_jr.setOnClickListener(this);
        grxx_sz_fxrj_lr.setOnClickListener(this);
        grxx_sz_bbgx_jr.setOnClickListener(this);
        grxx_sz_tcdl_jr.setOnClickListener(this);
        grxx_sz_gy_jr.setOnClickListener(this);
        grxx_sz_qc_jr.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.grxx_sz_zfaq_jr:
                goActSecy();
                break;
            case R.id.grxx_sz_bbgx_jr:
                mUpdateAgent.update();
                break;
            case R.id.grxx_sz_fxrj_lr:
                goCustomShare();
                break;
            case R.id.grxx_sz_gy_jr:
                goAboutJX();
                break;
            case R.id.grxx_sz_qc_jr:
                dialog2.show();
                break;
            case R.id.grxx_sz_tcdl_jr:
                dialog.show();
                break;
        }

    }

    /**
     * 跳转到绑定手机号界面
     */
    private void goCustomShare() {
        Intent intent = new Intent(SettingActivity.this, CustomShareActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到账户安全界面
     */
    private void goActSecy() {
        Intent intent = new Intent(SettingActivity.this, ActSecyActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到账户安全界面
     */
    private void goAboutJX() {
        Intent intent = new Intent(SettingActivity.this, AboutJXActivity.class);
        startActivity(intent);
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
                        PushManager.getInstance().unBindAlias(SettingActivity.this,userId,true);
                        SesSharedReferences.cleanShare(SettingActivity.this);
                        Intent intent = new Intent(SettingActivity.this, SplashActivity.class);
                        startActivity(intent);
                        RHBaseApplication.getMainActivity().finish();
                        SettingActivity.this.finish();
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
    protected void onNewIntent(Intent intent) {
        Bundle ncBundle = intent.getBundleExtra("login_out");
        if (ncBundle != null) {
            PushManager.getInstance().unBindAlias(SettingActivity.this,userId,true);
            SesSharedReferences.cleanShare(SettingActivity.this);
            intent = new Intent(SettingActivity.this, SplashActivity.class);
            startActivity(intent);
            RHBaseApplication.getMainActivity().finish();
            SettingActivity.this.finish();
        }
    }
}

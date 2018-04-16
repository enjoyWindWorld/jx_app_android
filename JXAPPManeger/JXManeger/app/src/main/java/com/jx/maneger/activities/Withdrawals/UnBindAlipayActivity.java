package com.jx.maneger.activities.Withdrawals;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.activities.MainActivity;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.WithdrawalDao;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.DialogOnClickListener;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.GetAliPayResult;
import com.jx.maneger.results.NormalResult;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.NormalAlertDialog;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

public class UnBindAlipayActivity extends RHBaseActivity {

    private TextView tv_my_alipay, tv_my_name;
    private Button btn_unbind;
    private NormalAlertDialog dialog;
    private ProgressWheelDialog progressWheelDialog;
    private WithdrawalDao withdrawalDao;
    private int option;

    @Override
    protected void init() {
        initDialog();
        progressWheelDialog = new ProgressWheelDialog(UnBindAlipayActivity.this);
        withdrawalDao = new WithdrawalDao();
        getAlipayInfo();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_un_bind_alipay;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(UnBindAlipayActivity.this).setMiddleTitleText("已绑定")
                .setLeftImageRes(R.drawable.selector_back)
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        tv_my_alipay = (TextView) contentView.findViewById(R.id.tv_my_alipay);
        tv_my_name = (TextView) contentView.findViewById(R.id.tv_my_name);
        btn_unbind = (Button) contentView.findViewById(R.id.btn_unbind);

        btn_unbind.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.btn_unbind:
                dialog.show();
                break;
        }
    }

    private void initDialog() {
        dialog = new NormalAlertDialog.Builder(UnBindAlipayActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("提示")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("是否解绑支付宝账号")
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
                        unBindAliPayInfo();
                    }
                })
                .build();
    }

    private void unBindAliPayInfo()
    {
        option = 1;
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }
        progressWheelDialog.show();
        withdrawalDao.unBindAliPayInfo(SesSharedReferences.getSafetyMark(UnBindAlipayActivity.this), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                progressWheelDialog.dismiss();
                ToastUtil.showToast("支付宝账号解绑成功");
                setResult(Constant.UNBIND_ALIPAY);
                finish();
            }

            @Override
            public void resFailure(int statusCode, String message) {
                progressWheelDialog.dismiss();
                if(statusCode == 4)
                {
                    gotoLogin();
                }
                else
                {
                    ToastUtil.showToast(message);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.LOGIN_OK)
        {
            if(option == 1)
            {
                unBindAliPayInfo();
            }
            else
            {
                getAlipayInfo();
            }
        }
    }

    void getAlipayInfo()
    {
        option = 0;
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(UnBindAlipayActivity.this)))
        {
            return;
        }
        progressWheelDialog.show();
        withdrawalDao.getAliPayInfo(SesSharedReferences.getSafetyMark(UnBindAlipayActivity.this), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                progressWheelDialog.dismiss();
                GetAliPayResult aliPayResult = (GetAliPayResult)object;
                if(aliPayResult != null && aliPayResult.getData().size() > 0)
                {
                    tv_my_alipay.setText(aliPayResult.getData().get(0).getPay_account());
                    tv_my_name.setText(aliPayResult.getData().get(0).getPay_name());
                }
            }

            @Override
            public void resFailure(int statusCode, String message) {
                progressWheelDialog.dismiss();
                if(statusCode == 4)
                {
                    gotoLogin();
                }
                else
                {
                    ToastUtil.showToast(message);
                }
            }
        });
    }
}

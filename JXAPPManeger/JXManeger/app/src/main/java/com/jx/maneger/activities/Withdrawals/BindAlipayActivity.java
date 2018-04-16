package com.jx.maneger.activities.Withdrawals;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.NormalAlertDialog;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

public class BindAlipayActivity extends RHBaseActivity {

    private EditText edit_my_alipay, edit_my_name;
    private Button btn_bind;
    private LinearLayout layout_checkbox;
    private CheckBox checkBox;
    private NormalAlertDialog dialog;
    private ProgressWheelDialog progressWheelDialog;
    private WithdrawalDao withdrawalDao;
    private String account, name;

    @Override
    protected void init() {
        initDialog();
        progressWheelDialog = new ProgressWheelDialog(BindAlipayActivity.this);
        withdrawalDao = new WithdrawalDao();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_bind_alipay;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(BindAlipayActivity.this).setMiddleTitleText("未绑定")
                .setLeftImageRes(R.drawable.selector_back)
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        edit_my_alipay = (EditText) contentView.findViewById(R.id.edit_my_alipay);
        edit_my_name = (EditText) contentView.findViewById(R.id.edit_my_name);
        btn_bind = (Button) contentView.findViewById(R.id.btn_bind);
        layout_checkbox = (LinearLayout) contentView.findViewById(R.id.layout_checkbox);
        checkBox = (CheckBox) contentView.findViewById(R.id.checkBox);

        layout_checkbox.setOnClickListener(this);
        btn_bind.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.layout_checkbox:
                if (checkBox.isChecked())
                {
                    checkBox.setChecked(false);
                }
                else
                {
                    checkBox.setChecked(true);
                }
                break;
            case R.id.btn_bind:
                name = edit_my_name.getText().toString();
                account = edit_my_alipay.getText().toString();
                if(StringUtil.isEmpty(account))
                {
                    ToastUtil.showToast("请输入支付宝账号");
                }
                if(!Utils.isMobileNO(account) && !Utils.isEmail(account))
                {
                    ToastUtil.showToast("支付宝账号格式错误");
                }
                else if(StringUtil.isEmpty(name))
                {
                    ToastUtil.showToast("请输入真是姓名");
                }
                else if(!checkBox.isChecked())
                {
                    ToastUtil.showToast("请确认提现收款账号");
                }
                else
                {
                    dialog.show();
                }
                break;
        }
    }

    private void initDialog() {
        dialog = new NormalAlertDialog.Builder(BindAlipayActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("提示")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("本人确认支付宝账号输入无误")
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
                        bindAliPayInfo();
                    }
                })
                .build();
    }

    private void bindAliPayInfo()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(BindAlipayActivity.this)))
        {
            return;
        }

        progressWheelDialog.show();
        withdrawalDao.bindAliPayInfo(SesSharedReferences.getSafetyMark(BindAlipayActivity.this), name, account, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                progressWheelDialog.dismiss();
                ToastUtil.showToast("支付宝账号绑定成功");
                setResult(Constant.BIND_ALIPAY);
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
            bindAliPayInfo();
        }
    }
}

package com.jx.maneger.activities.Withdrawals;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.activities.MainActivity;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.WithdrawalDao;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.GetAliPayResult;
import com.jx.maneger.results.WithdrawalAmountResult;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

import java.text.DecimalFormat;

/**
 * 我的收入
 */
public class MyInComeActivity extends RHBaseActivity {

    private TextView tv_money, tv_bind_alipay;
    private RelativeLayout layout_money, layout_bind_alipay;
    private LinearLayout layout_cash_register;
    private ProgressWheelDialog progressWheelDialog;
    private WithdrawalDao withdrawalDao;
    private String withdrawalOrderNo;
    private boolean isCanWithdrawal, isBindAliPayAccount, isJurisdiction;
    private DecimalFormat decimalFormat;

    @Override
    protected void init() {
        decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        progressWheelDialog = new ProgressWheelDialog(MyInComeActivity.this);
        withdrawalDao = new WithdrawalDao();

        if(getIntent() != null)
        {
            isBindAliPayAccount = getIntent().getBooleanExtra("isBundAlipay", false);
            if(isBindAliPayAccount)
            {
                tv_bind_alipay.setText("已绑定");
            }
            else
            {
                tv_bind_alipay.setText("未绑定");
            }
        }

        getWithdrawalAmount();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_my_in_come;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(MyInComeActivity.this).setMiddleTitleText("我的收入")
                .setLeftImageRes(R.drawable.selector_back)
                .setLeftClickListener(this)
                .setRightText("提现规则")
                .setRightClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        tv_money = (TextView) contentView.findViewById(R.id.tv_money);
        tv_bind_alipay = (TextView) contentView.findViewById(R.id.tv_bind_alipay);
        layout_money = (RelativeLayout) contentView.findViewById(R.id.layout_money);
        layout_bind_alipay = (RelativeLayout) contentView.findViewById(R.id.layout_bind_alipay);
        layout_cash_register = (LinearLayout) contentView.findViewById(R.id.layout_cash_register);

        layout_money.setOnClickListener(this);
        layout_bind_alipay.setOnClickListener(this);
        layout_cash_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                if(isBindAliPayAccount)
                {
                    setResult(Constant.BIND_ALIPAY);
                }
                else
                {
                    setResult(Constant.UNBIND_ALIPAY);
                }
                finish();
                break;
            case R.id.titlebar_right_rl:
                intent = new Intent(MyInComeActivity.this, RulesActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_money:
                if(!isJurisdiction)
                {
                    ToastUtil.showToast("没有提现权限");
                }
                else if(!isBindAliPayAccount)
                {
                    ToastUtil.showToast("没有绑定支付宝");
                }
                else if(!isCanWithdrawal)
                {
                    ToastUtil.showToast("没有可提现金额");
                }
                else
                {
                    intent = new Intent(MyInComeActivity.this, WithdrawalDetailActivity.class);
                    intent.putExtra("withdrawalOrderNo", withdrawalOrderNo);
                    startActivityForResult(intent, Constant.REQUEST_CODE);
                }
                break;
            case R.id.layout_bind_alipay:
                if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
                {
                    return;
                }

                if(isBindAliPayAccount)
                {
                    intent = new Intent(MyInComeActivity.this, UnBindAlipayActivity.class);
                    startActivityForResult(intent, 100);
                }
                else
                {
                    intent = new Intent(MyInComeActivity.this, BindAlipayActivity.class);
                    startActivityForResult(intent, 100);
                }

                break;
            case R.id.layout_cash_register:
                intent = new Intent(MyInComeActivity.this, WithdrawalRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getWithdrawalAmount()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(MyInComeActivity.this)))
        {
            return;
        }
        progressWheelDialog.show();
        withdrawalDao.getWithdrawalAmount(SesSharedReferences.getSafetyMark(MyInComeActivity.this), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                progressWheelDialog.dismiss();
                WithdrawalAmountResult withdrawalAmountResult = (WithdrawalAmountResult) object;
                if(withdrawalAmountResult != null && withdrawalAmountResult.getData().size() > 0)
                {
                    withdrawalOrderNo = withdrawalAmountResult.getData().get(0).getWithdrawalOrderNo();
                    if(isBindAliPayAccount)
                    {
                        tv_money.setText("¥"+decimalFormat.format(withdrawalAmountResult.getData().get(0).getWithdrawal_total_amount())+"元");
                    }
                    else
                    {
                        tv_money.setText("¥"+decimalFormat.format(withdrawalAmountResult.getData().get(0).getWithdrawal_total_amount())+"元（未绑定支付宝）");
                    }

                    if(withdrawalAmountResult.getData().get(0).getWithdrawal_total_amount() > 0)
                    {
                        isCanWithdrawal = true;
                    }
                    else
                    {
                        isCanWithdrawal = false;
                    }

                    isBindAliPayAccount = true;
                    isJurisdiction = true;
                }
            }

            @Override
            public void resFailure(int statusCode, String message) {
                progressWheelDialog.dismiss();
                if(statusCode == 4)
                {
                    gotoLogin();
                }
                else if(statusCode == 5)//没有提现权限
                {
                    isJurisdiction = false;
                }
                else if(statusCode == 3)//没有绑定支付宝
                {
                    isJurisdiction = true;
                    isBindAliPayAccount = false;
                    tv_money.setText("¥0.00元（未绑定支付宝）");
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
        if(resultCode == Constant.BIND_ALIPAY)
        {
            isBindAliPayAccount = true;
            tv_bind_alipay.setText("已绑定");
            getWithdrawalAmount();
        }
        else if(resultCode == Constant.UNBIND_ALIPAY)
        {
            isBindAliPayAccount = false;
            tv_bind_alipay.setText("未绑定");
            getWithdrawalAmount();
        }
        else if(resultCode == Constant.LOGIN_OK || resultCode == Constant.WITHDRAWAL_OK)
        {
            getWithdrawalAmount();
        }
    }

    @Override
    public void onBackPressed() {
        if(isBindAliPayAccount)
        {
            setResult(Constant.BIND_ALIPAY);
        }
        else
        {
            setResult(Constant.UNBIND_ALIPAY);
        }
        finish();
    }
}

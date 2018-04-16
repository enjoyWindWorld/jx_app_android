package com.jx.maneger.activities.Withdrawals;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.WithdrawalDao;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.SubWithdrawalDetailResult;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

import java.text.DecimalFormat;

/**
 * 下级提现详情
 * Created by Administrator on 2017/9/14.
 */

public class SubWithdrawalDetailActivity extends RHBaseActivity{

    private TextView tv_wall_hanging_install_amount, tv_table_install_amount, tv_vertical_install_amount, tv_wall_hanging_renew_amount, tv_table_renew_amount, tv_vertical_renew_amount,
            tv_sub_service, tv_sub_renew_service, tv_no_deposit, tv_rebate_ratio;

    private ProgressWheelDialog progressWheelDialog;
    private WithdrawalDao withdrawalDao;
    private String name, withdrawal_order_no, id;
    private DecimalFormat decimalFormat;

    @Override
    protected void init() {
        decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        progressWheelDialog = new ProgressWheelDialog(SubWithdrawalDetailActivity.this);
        withdrawalDao = new WithdrawalDao();
        getDetail();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_sub_withdrawal_detail;
    }

    @Override
    protected void initTitle() {
        if(getIntent() != null)
        {
            name = getIntent().getStringExtra("name");
            withdrawal_order_no = getIntent().getStringExtra("order_no");
            id = getIntent().getStringExtra("id");
        }
        new TitleBarHelper(SubWithdrawalDetailActivity.this).setMiddleTitleText(name)
                .setLeftImageRes(R.drawable.selector_back)
                .setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        tv_wall_hanging_install_amount = (TextView) contentView.findViewById(R.id.tv_wall_hanging_install_amount);
        tv_table_install_amount = (TextView) contentView.findViewById(R.id.tv_table_install_amount);
        tv_vertical_install_amount = (TextView) contentView.findViewById(R.id.tv_vertical_install_amount);
        tv_wall_hanging_renew_amount = (TextView) contentView.findViewById(R.id.tv_wall_hanging_renew_amount);
        tv_table_renew_amount = (TextView) contentView.findViewById(R.id.tv_table_renew_amount);
        tv_vertical_renew_amount = (TextView) contentView.findViewById(R.id.tv_vertical_renew_amount);
        tv_sub_service = (TextView) contentView.findViewById(R.id.tv_sub_service);
        tv_sub_renew_service = (TextView) contentView.findViewById(R.id.tv_sub_renew_service);
        tv_no_deposit = (TextView) contentView.findViewById(R.id.tv_no_deposit);
        tv_rebate_ratio = (TextView) contentView.findViewById(R.id.tv_rebate_ratio);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
        }
    }

    void getDetail()
    {
        progressWheelDialog.show();
        withdrawalDao.getSubWithdrawalDetail(SesSharedReferences.getSafetyMark(SubWithdrawalDetailActivity.this), withdrawal_order_no, id, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                progressWheelDialog.dismiss();
                SubWithdrawalDetailResult subWithdrawalDetailResult = (SubWithdrawalDetailResult) object;
                showDetail(subWithdrawalDetailResult);
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
            getDetail();
        }
    }

    void showDetail( SubWithdrawalDetailResult subWithdrawalDetailResult)
    {
        if(subWithdrawalDetailResult != null && subWithdrawalDetailResult.getData().size() > 0)
        {
            SubWithdrawalDetailResult.Data data = subWithdrawalDetailResult.getData().get(0);
            tv_wall_hanging_install_amount.setText(data.getWall()+"台");
            tv_table_install_amount.setText(data.getDesktop()+"台");
            tv_vertical_install_amount.setText(data.getVertical()+"台");
            tv_wall_hanging_renew_amount.setText(data.getWall_renew()+"单");
            tv_table_renew_amount.setText(data.getDesktop_renew()+"单");
            tv_vertical_renew_amount.setText(data.getVertical_renew()+"单");

            tv_sub_service.setText(decimalFormat.format(data.getService_fee())+"元");
            tv_sub_renew_service.setText(decimalFormat.format(data.getRenewal())+"元");
            tv_no_deposit.setText(decimalFormat.format(data.getTotal_money())+"元");
            tv_rebate_ratio.setText(decimalFormat.format(data.getBy_tkr_rebates()));
        }
    }
}

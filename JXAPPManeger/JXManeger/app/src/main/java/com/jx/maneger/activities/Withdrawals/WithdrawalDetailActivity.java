package com.jx.maneger.activities.Withdrawals;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.activities.MainActivity;
import com.jx.maneger.adapter.RecycleAdapter;
import com.jx.maneger.adapter.RecycleCommonAdapter;
import com.jx.maneger.adapter.holder.RecycleCommonViewHolder;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.WithdrawalDao;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.DialogOnClickListener;
import com.jx.maneger.intf.OnItemClickListener;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.intf.WithdrawalDialogOnClickListener;
import com.jx.maneger.results.NormalResult;
import com.jx.maneger.results.SalesAmountResult;
import com.jx.maneger.results.WithdrawalAmountResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.UIUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.SpaceItemDecoration;
import com.jx.maneger.view.dialog.NormalAlertDialog;
import com.jx.maneger.view.dialog.ProgressWheelDialog;
import com.jx.maneger.view.dialog.withdrawalAlertDialog;

import java.text.DecimalFormat;

public class WithdrawalDetailActivity extends RHBaseActivity {

    private RecyclerView mRecyclerView;
    private RecycleCommonAdapter mAdapter;
    private RecyclerView.LayoutManager mManager;

    private TextView tv_money, tv_wall_hanging_install_amount, tv_table_install_amount, tv_vertical_install_amount,
            tv_wall_hanging_renew_amount, tv_table_renew_amount, tv_vertical_renew_amount, tv_contract, tv_rebate, tv_renew_rebate,
            tv_install_rebate, tv_subordinate_rebate, titlebar_right_tv, tv_rebate_ratio, tv_Installationfee_ratio, tv_odd_numbers,
            tv_partner_number, tv_date, tv_purchase_total, tv_renew_total;

    private ProgressWheelDialog progressWheelDialog;
    private NormalAlertDialog normalAlertDialog;
    private withdrawalAlertDialog mWithdrawalAlertDialog;
    private WithdrawalDao withdrawalDao;
    private TitleBarHelper titleBarHelper;
    private int option;
    private String withdrawalOrderNo, no_reason, no_state;
    private int state;
    private DecimalFormat decimalFormat;
    private boolean isOther;

    @Override
    protected void init() {
        decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        if(getIntent() != null)
        {
            withdrawalOrderNo = getIntent().getStringExtra("withdrawalOrderNo");
            isOther = getIntent().getBooleanExtra("isOther", false);
        }
        initNormalAlertDialog();
        progressWheelDialog = new ProgressWheelDialog(WithdrawalDetailActivity.this);
        withdrawalDao = new WithdrawalDao();
        if(!StringUtil.isEmpty(withdrawalOrderNo))
        {
            getSalesAmount();
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_withdrawal_detail;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(WithdrawalDetailActivity.this).setMiddleTitleText("提现金额")
                .setLeftImageRes(R.drawable.selector_back)
                .setLeftClickListener(this)
                .setRightClickListener(this);
        titlebar_right_tv = titleBarHelper.getmRightTv();
    }

    @Override
    protected void findView(View contentView) {
        tv_money = (TextView) contentView.findViewById(R.id.tv_money);
        tv_wall_hanging_install_amount = (TextView) contentView.findViewById(R.id.tv_wall_hanging_install_amount);
        tv_table_install_amount = (TextView) contentView.findViewById(R.id.tv_table_install_amount);
        tv_vertical_install_amount = (TextView) contentView.findViewById(R.id.tv_vertical_install_amount);
        tv_wall_hanging_renew_amount = (TextView) contentView.findViewById(R.id.tv_wall_hanging_renew_amount);
        tv_table_renew_amount = (TextView) contentView.findViewById(R.id.tv_table_renew_amount);
        tv_vertical_renew_amount = (TextView) contentView.findViewById(R.id.tv_vertical_renew_amount);
        tv_contract = (TextView) contentView.findViewById(R.id.tv_contract);
        tv_rebate = (TextView) contentView.findViewById(R.id.tv_rebate);
        tv_renew_rebate = (TextView) contentView.findViewById(R.id.tv_renew_rebate);
        tv_install_rebate = (TextView) contentView.findViewById(R.id.tv_install_rebate);
        tv_subordinate_rebate = (TextView) contentView.findViewById(R.id.tv_subordinate_rebate);
        tv_rebate_ratio = (TextView) contentView.findViewById(R.id.tv_rebate_ratio);
        tv_Installationfee_ratio = (TextView) contentView.findViewById(R.id.tv_Installationfee_ratio);

        tv_odd_numbers = (TextView) contentView.findViewById(R.id.tv_odd_numbers);
        tv_partner_number = (TextView) contentView.findViewById(R.id.tv_partner_number);
        tv_date = (TextView) contentView.findViewById(R.id.tv_date);
        tv_purchase_total = (TextView) contentView.findViewById(R.id.tv_purchase_total);
        tv_renew_total = (TextView) contentView.findViewById(R.id.tv_renew_total);

        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.rcy);
        mManager = new LinearLayoutManager(WithdrawalDetailActivity.this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.titlebar_right_rl:
                if (state == 0)
                {
                    mWithdrawalAlertDialog.show();
                }
                else if(state == -1)
                {
                    normalAlertDialog.show();
                }
                break;
        }
    }

    private void initNormalAlertDialog() {

        normalAlertDialog = new NormalAlertDialog.Builder(WithdrawalDetailActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("提示")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("是否提现")
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
                        doWithdrawal(withdrawalOrderNo);
                    }
                })
                .build();
    }

    private void initWithdrawalAlertDialog() {

        mWithdrawalAlertDialog = new withdrawalAlertDialog.Builder(WithdrawalDetailActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("提示")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("提现单"+withdrawalOrderNo+"审核")
                .setContentTextColor(R.color.color_000000)
                .setCancelButtonText("取消")
                .setCancelButtonTextColor(R.color.color_000000)
                .setOkButtonText("通过")
                .setOkButtonTextColor(R.color.color_000000)
                .setNoButtonText("拒绝")
                .setNoButtonTextColor(R.color.color_000000)
                .setCancelable(true)
                .setOnclickListener(new WithdrawalDialogOnClickListener() {
                    @Override
                    public void clickOkButton(View view, String reason) {
                        mWithdrawalAlertDialog.dismiss();
                        no_reason = reason;
                        no_state = "3";
                        withdrawalaudit();
                    }

                    @Override
                    public void clickNoButton(View view, String reason) {
                        if(!StringUtil.isEmpty(reason))
                        {
                            mWithdrawalAlertDialog.dismiss();
                            no_reason = reason;
                            no_state = "1";
                            withdrawalaudit();
                        }
                        else
                        {
                            ToastUtil.showToast("请输入审核不通的原因");
                        }
                    }

                    @Override
                    public void clickCancelButton(View view) {
                        mWithdrawalAlertDialog.dismiss();
                    }
                })
                .build();
    }

    private void getSalesAmount()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(WithdrawalDetailActivity.this)))
        {
            return;
        }

        option = 1;
        progressWheelDialog.show();
        withdrawalDao.getSalesAmount(SesSharedReferences.getSafetyMark(WithdrawalDetailActivity.this), withdrawalOrderNo, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                progressWheelDialog.dismiss();
                SalesAmountResult salesAmountResult = (SalesAmountResult) object;
                if(salesAmountResult != null && salesAmountResult.getData().size() > 0)
                {
                    showWithdrawalDetail(salesAmountResult);
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

    private void doWithdrawal(String withdrawal_order_no)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(WithdrawalDetailActivity.this)))
        {
            return;
        }

        option = 2;
        progressWheelDialog.show();
        withdrawalDao.doWithdrawal(SesSharedReferences.getSafetyMark(WithdrawalDetailActivity.this), withdrawal_order_no, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                progressWheelDialog.dismiss();
                NormalResult normalResult = (NormalResult) object;
                if(normalResult != null)
                {
                    ToastUtil.showToast("提现成功，请耐心等待审核结果。");
                    setResult(Constant.WITHDRAWAL_OK);
                    finish();
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

    private void showWithdrawalDetail(SalesAmountResult salesAmountResult)
    {
        SalesAmountResult.Data data = salesAmountResult.getData().get(0);
        tv_money.setText("¥"+decimalFormat.format(data.getWithdrawal_total_amount())+"元");
        tv_wall_hanging_install_amount.setText(data.getWall()+"台");
        tv_table_install_amount.setText(data.getDesktop()+"台");
        tv_vertical_install_amount.setText(data.getVertical()+"台");
        tv_wall_hanging_renew_amount.setText(data.getWall_renew()+"单");
        tv_table_renew_amount.setText(data.getDesktop_renew()+"单");
        tv_vertical_renew_amount.setText(data.getVertical_renew()+"单");

        if(data.getIspact() == 0)
        {
            tv_contract.setText("否");
        }
        else
        {
            tv_contract.setText("是");
        }

        tv_odd_numbers.setText(data.getWithdrawal_order_no());
        tv_partner_number.setText(data.getUser_number());
        tv_date.setText(data.getSales_time());
        tv_purchase_total.setText(decimalFormat.format(data.getBuy_combined())+"元");
        tv_renew_total.setText(decimalFormat.format(data.getRenewal_combined())+"元");
        tv_rebate_ratio.setText(decimalFormat.format(data.getWdl_fee()));
        tv_Installationfee_ratio.setText(decimalFormat.format(data.getRwl_install()));
        tv_rebate.setText(decimalFormat.format(data.getService_fee())+"元");
        tv_renew_rebate.setText(decimalFormat.format(data.getRenewal())+"元");
        tv_install_rebate.setText(decimalFormat.format(data.getInstallation())+"元");
        tv_subordinate_rebate.setText("合计"+decimalFormat.format(data.getLower_rebate())+"元");

        state = data.getState();

        if(state == 0 && isOther)
        {
            titlebar_right_tv.setVisibility(View.VISIBLE);
            titlebar_right_tv.setText("审核");
            initWithdrawalAlertDialog();
        }
        else if(state == -1 && !isOther)
        {
            titlebar_right_tv.setVisibility(View.VISIBLE);
            titlebar_right_tv.setText("提现");
        }
        showSubordinates(salesAmountResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.LOGIN_OK)
        {
            switch (option)
            {
                case 1:
                    getSalesAmount();
                    break;
                case 2:
                    doWithdrawal(withdrawalOrderNo);
                    break;
                case 3:
                    withdrawalaudit();
                    break;
            }
        }
    }

    private void showSubordinates(final SalesAmountResult salesAmountResult) {
        mAdapter = new RecycleAdapter<SalesAmountResult.Data.Subordinate>(UIUtil.getContext(), salesAmountResult.getData().get(0).getDirect_subordinates(), true, R.layout.layout_subordinate_item, null) {
            @Override
            public void convert(RecycleCommonViewHolder holder, SalesAmountResult.Data.Subordinate data) {
                holder.setText(R.id.tv_name, data.getName());
                holder.setText(R.id.tv_code, "编号："+data.getNumber());
                holder.setText(R.id.tv_rebate, "返利比例："+data.getRebates());
                holder.setText(R.id.tv_total, "合计："+decimalFormat.format(data.getMoney()));
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(WithdrawalDetailActivity.this, SubWithdrawalDetailActivity.class);
                        intent.putExtra("name", salesAmountResult.getData().get(0).getDirect_subordinates().get(position).getName());
                        intent.putExtra("order_no", withdrawalOrderNo);
                        intent.putExtra("id", salesAmountResult.getData().get(0).getDirect_subordinates().get(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            protected void opHeader(RecycleCommonViewHolder holder) {
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了头");
                    }
                });
            }
        };
        mAdapter.setHavefooter(false);
        mRecyclerView.setAdapter(mAdapter);
    }

    void withdrawalaudit()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(WithdrawalDetailActivity.this)))
        {
            return;
        }
        option = 3;
        progressWheelDialog.show();
        withdrawalDao.withdrawalaudit(SesSharedReferences.getSafetyMark(WithdrawalDetailActivity.this), withdrawalOrderNo, no_state, no_reason, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                progressWheelDialog.dismiss();
                ToastUtil.showToast("审核完成！");
                finish();
            }

            @Override
            public void resFailure(int statusCode, String message) {
                progressWheelDialog.dismiss();
                ToastUtil.showToast(message);
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

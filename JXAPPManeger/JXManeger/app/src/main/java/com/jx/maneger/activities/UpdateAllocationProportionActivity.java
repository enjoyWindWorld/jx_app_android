package com.jx.maneger.activities;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.primitives.Floats;
import com.google.gson.Gson;
import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.SubordinateDao;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.CashWithdrawalRatioResult;
import com.jx.maneger.results.OrderDetailResult;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class UpdateAllocationProportionActivity extends RHBaseActivity {

    private EditText edit_rebate, edit_install_equipment;
    private TextView tv_amount, titlebar_right_tv;
    private String name, permissions, par_level, ord_managerno;
    private TitleBarHelper titleBarHelper;
    private float rebate, install_equipment, total_rebate;
    private boolean isUpdate;
    private SubordinateDao subordinateDao;
    private ProgressWheelDialog dialog;
    private DBManager dbManager;
    private DecimalFormat decimalFormat;

    @Override
    protected void init() {
        decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        dialog = new ProgressWheelDialog(UpdateAllocationProportionActivity.this);
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        subordinateDao = new SubordinateDao();
        getCashWithdrawalRatioData();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_update_allocation_proportion;
    }

    @Override
    protected void initTitle() {

        if(getIntent() != null)
        {
            name = getIntent().getStringExtra("name");
            permissions = getIntent().getStringExtra("permissions");
            par_level = getIntent().getStringExtra("level");
            ord_managerno = getIntent().getStringExtra("ord_managerno");
        }

        titleBarHelper = new TitleBarHelper(UpdateAllocationProportionActivity.this);
        titleBarHelper.setMiddleTitleText(name+"比例修改");
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setLeftClickListener(this);

        if("0".equals(permissions))
        {
            titleBarHelper.setRightClickListener(this);
        }

        titlebar_right_tv = titleBarHelper.getmRightTv();
        titlebar_right_tv.setVisibility(View.VISIBLE);
        titlebar_right_tv.setText("编辑");
    }

    @Override
    protected void findView(View contentView) {
        edit_rebate = (EditText)contentView.findViewById(R.id.edit_rebate);
        edit_install_equipment = (EditText)contentView.findViewById(R.id.edit_install_equipment);
        tv_amount = (TextView) contentView.findViewById(R.id.tv_amount);

        edit_rebate.addTextChangedListener(mRebateTextWatcher);
        edit_install_equipment.addTextChangedListener(mInstallEquipmentTextWatcher);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.titlebar_right_rl:
                if(!isUpdate)
                {
                    isUpdate = true;
                    titlebar_right_tv.setText("完成");
                    edit_rebate.setEnabled(true);
                    edit_install_equipment.setEnabled(true);

                }
                else
                {
                    if(total_rebate > 1)
                    {
                        ToastUtil.showToast("提现比例合计不能大于1");
                        return;
                    }
                    isUpdate = false;
                    titlebar_right_tv.setText("编辑");
                    edit_rebate.setEnabled(false);
                    edit_install_equipment.setEnabled(false);
                    updateCashWithdrawalRatioData();
                }
                break;
        }
    }

    TextWatcher mRebateTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {
            try
            {
                rebate = Float.parseFloat(s.toString());
            }
            catch (Exception e)
            {
                rebate = 0;
            }
            total_rebate = rebate + install_equipment;
            tv_amount.setText(decimalFormat.format(total_rebate));
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = edit_rebate.getSelectionStart();
            editEnd = edit_rebate.getSelectionEnd();
            if (temp.length() > 10) {
                s.delete(editStart-1, editEnd);
                int tempSelection = editStart;
                edit_rebate.setText(s);
                edit_rebate.setSelection(tempSelection);
            }
        }
    };

    TextWatcher mInstallEquipmentTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                      int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                                  int arg3) {
            try
            {
                install_equipment = Float.parseFloat(s.toString());
            }
            catch (Exception e)
            {
                install_equipment = 0;
            }
            total_rebate = rebate + install_equipment;
            tv_amount.setText(decimalFormat.format(total_rebate));
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = edit_install_equipment.getSelectionStart();
            editEnd = edit_install_equipment.getSelectionEnd();
            if (temp.length() > 10) {
                s.delete(editStart-1, editEnd);
                int tempSelection = editStart;
                edit_install_equipment.setText(s);
                edit_install_equipment.setSelection(tempSelection);
            }
        }
    };

    void getCashWithdrawalRatioData()
    {
        getPreCashWithdrawalRatioData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(UpdateAllocationProportionActivity.this)))
        {
            return;
        }

        dialog.show();
        subordinateDao.getCashWithdrawalRatio(SesSharedReferences.getSafetyMark(UpdateAllocationProportionActivity.this), par_level, ord_managerno, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                CashWithdrawalRatioResult cashWithdrawalRatioResult = (CashWithdrawalRatioResult) object;
                sowCashWithdrawalRatioData(cashWithdrawalRatioResult);
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
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

    /**
     * 获取缓存下来的的数据
     */
    void getPreCashWithdrawalRatioData()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", SesSharedReferences.getSafetyMark(UpdateAllocationProportionActivity.this));
        map.put("par_level", par_level);
        map.put("username", ord_managerno);

        String js = dbManager.getUrlJsonData(Constant.USER_GET_CASH_WITHRAWAL_RRATIO + StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            CashWithdrawalRatioResult cashWithdrawalRatioResult = new Gson().fromJson(js, CashWithdrawalRatioResult.class);
            sowCashWithdrawalRatioData(cashWithdrawalRatioResult);
        }
    }

    void sowCashWithdrawalRatioData(CashWithdrawalRatioResult cashWithdrawalRatioResult)
    {
        if(cashWithdrawalRatioResult != null && cashWithdrawalRatioResult.getData() != null && cashWithdrawalRatioResult.getData().size() > 0)
        {
            rebate = cashWithdrawalRatioResult.getData().get(0).getService_fee();
            install_equipment = cashWithdrawalRatioResult.getData().get(0).getInstall();
            total_rebate = cashWithdrawalRatioResult.getData().get(0).getTotal_rebate();

            edit_rebate.setText(decimalFormat.format(rebate));
            edit_install_equipment.setText(decimalFormat.format(install_equipment));
            tv_amount.setText(decimalFormat.format(total_rebate));
        }
    }

    void updateCashWithdrawalRatioData()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(UpdateAllocationProportionActivity.this)))
        {
            return;
        }

        dialog.show();
        subordinateDao.updateCashWithdrawalRatio(SesSharedReferences.getSafetyMark(UpdateAllocationProportionActivity.this), ord_managerno, rebate + "", install_equipment + "", total_rebate + "", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ToastUtil.showToast("修改成功！");
                finish();
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
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

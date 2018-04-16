package com.jx.maneger.activities.loginAndRegister;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.jx.maneger.R;
import com.jx.maneger.activities.SettingActivity;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.LoginAndRegister;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.DialogOnClickListener;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.GetCodeResult;
import com.jx.maneger.util.KeyboardUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.NormalAlertDialog;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

/**
 * 验证手机界面
 */

public class VerifyPhoneActivity extends RHBaseActivity {

    LinearLayout layout_back;
    EditText edt_account, edt_code;
    TextView txt_get_code;
    Button btn_next;
    LoginAndRegister loginAndRegister;
    GetCodeResult getCodeResult;
    String account, phone;
    ProgressWheelDialog dialog;
    private NormalAlertDialog normalAlertDialog;

    Handler handler  = new Handler();
    Runnable updateThread =  new Runnable(){
        @Override
        public void run() {
            txt_get_code.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(TIME_COUNT_DOWN, 1000);
        }
    };

    //计时标志
    private final int TIME_COUNT_DOWN = 4;
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case TIME_COUNT_DOWN:
                    Constant.TOTALTIME--;
                    if (Constant.TOTALTIME <= 0) {
                        txt_get_code.setText("获取短信验证码");
                        txt_get_code.setEnabled(true);
                        txt_get_code.setTextColor(getResources().getColor(R.color.color_1bb6ef));
                        Constant.TOTALTIME = 120;
                    } else {
                        txt_get_code.setText(Constant.TOTALTIME + "秒后重新获取验证码");
                        txt_get_code.setEnabled(false);
                        txt_get_code.setTextColor(getResources().getColor(R.color.color_333333));
                        mHandler.sendEmptyMessageDelayed(TIME_COUNT_DOWN, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void init() {
        loginAndRegister = new LoginAndRegister();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_verify_phone;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(VerifyPhoneActivity.this).setHideTitleBar();
    }

    @Override
    protected void findView(View contentView) {
        dialog = new ProgressWheelDialog(VerifyPhoneActivity.this);
        layout_back = (LinearLayout)contentView.findViewById(R.id.layout_back);
        edt_account = (EditText) contentView.findViewById(R.id.edt_phone);
        edt_code = (EditText) contentView.findViewById(R.id.edt_code);
        txt_get_code = (TextView) contentView.findViewById(R.id.txt_get_code);
        btn_next = (Button) contentView.findViewById(R.id.btn_next);

        hideSoftKeyboard(edt_account);
        hideSoftKeyboard(edt_code);

        layout_back.setOnClickListener(this);
        txt_get_code.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.layout_back:
                finish();
                break;
            case R.id.txt_get_code:
                account = edt_account.getText().toString();
                if(StringUtil.isEmpty(account))
                {
                    ToastUtil.showToast("请输入产品经理编号");
                }
                else
                {
                    getCode(account);
                    KeyboardUtil.hideSoftKeyboard(txt_get_code);
                }
                break;
            case R.id.btn_next:
                String code = edt_code.getText().toString();
                account = edt_account.getText().toString();
                if(StringUtil.isEmpty(account))
                {
                    ToastUtil.showToast("请输入产品经理编号");
                }
                else if(getCodeResult == null || getCodeResult.getData().size()<=0)
                {
                    ToastUtil.showToast("请获取验证码");
                }
                else if(StringUtil.isEmpty(code))
                {
                    ToastUtil.showToast("请输入验证码");
                }
                else
                {
                    checkCode(account, code);
                }
                break;
        }
    }

    void getCode(String account)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        loginAndRegister.getCodeTask(account, "1",  new ResponseResult(){

            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ToastUtil.showToast("获取验证码成功");
                handler.postDelayed(updateThread, 0);
                getCodeResult = (GetCodeResult)object;
                if(getCodeResult != null && getCodeResult.getData().size() > 0)
                {
                    phone = getCodeResult.getData().get(0).getPhone();
                    initDialog();
                    normalAlertDialog.show();
                }
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    void checkCode(final String username, String code)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        loginAndRegister.checkCodeTask(username, code, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                Intent intent = new Intent(VerifyPhoneActivity.this, ResetPasswordActivity.class);
                intent.putExtra("account", account);
                intent.putExtra("type", "1");
                startActivityForResult(intent, Constant.REQUEST_CODE);
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.REGISTER_OK)
        {
            Intent intent = new Intent();
            setResult(Constant.REGISTER_OK, intent);
            finish();
        }
    }

    private void initDialog() {
        normalAlertDialog = new NormalAlertDialog.Builder(VerifyPhoneActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("提示")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("验证码已成功发送至"+phone)
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
                    }
                })
                .build();
    }
}

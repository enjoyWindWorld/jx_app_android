package com.jx.intelligent.ui.activitise.loginAndRegister;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.LoginAndRegister;
import com.jx.intelligent.util.KeyboardUtil;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetCodeResult;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

/**
 * 验证手机界面
 */

public class VerifyPhoneActivity extends RHBaseActivity{

    LinearLayout layout_back;
    EditText edt_phone, edt_code;
    TextView txt_get_code;
    Button btn_next;
    LoginAndRegister loginAndRegister;
    GetCodeResult getCodeResult;
    String phone;
    ProgressWheelDialog dialog;

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
        edt_phone = (EditText) contentView.findViewById(R.id.edt_phone);
        edt_code = (EditText) contentView.findViewById(R.id.edt_code);
        txt_get_code = (TextView) contentView.findViewById(R.id.txt_get_code);
        btn_next = (Button) contentView.findViewById(R.id.btn_next);

        hideSoftKeyboard(edt_phone);
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
                phone = edt_phone.getText().toString();
                if(StringUtil.isEmpty(phone))
                {
                    ToastUtil.showToast("请输入手机号码");
                }
                else if(!Utils.isMobileNO(phone))
                {
                    ToastUtil.showToast("请输入手机号码格式错误");
                }
                else
                {
                    getCode(phone);
                    KeyboardUtil.hideSoftKeyboard(txt_get_code);
                }
                break;
            case R.id.btn_next:
                String code = edt_code.getText().toString();
                String phone = edt_phone.getText().toString();
                if(getCodeResult == null || getCodeResult.getData().size()<=0)
                {
                    ToastUtil.showToast("请获取验证码");
                }
                else if(StringUtil.isEmpty(phone))
                {
                    ToastUtil.showToast("请输入手机号码");
                }
                else if(!Utils.isMobileNO(phone))
                {
                    ToastUtil.showToast("请输入手机号码格式错误");
                }
                else if(StringUtil.isEmpty(code))
                {
                    ToastUtil.showToast("请输入验证码");
                }
                else
                {
                    checkCode(phone, code);
                }
                break;
        }
    }

    void getCode(String phone)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        loginAndRegister.getCodeTask(phone, "1",  new ResponseResult(){

            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ToastUtil.showToast("获取验证码成功");
                handler.postDelayed(updateThread, 0);
                getCodeResult = (GetCodeResult)object;
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }
    void checkCode(final String phone, String code)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        loginAndRegister.checkCodeTask(phone, code, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                Intent intent = new Intent(VerifyPhoneActivity.this, ResetPasswordActivity.class);
                intent.putExtra("phone", phone);
                intent.putExtra("type", "1");
                startActivityForResult(intent, Constant.REQUEST_CODE);
            }

            @Override
            public void resFailure(String message) {
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
}

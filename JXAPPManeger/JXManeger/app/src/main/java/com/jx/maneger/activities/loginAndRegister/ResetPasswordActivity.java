package com.jx.maneger.activities.loginAndRegister;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.LoginAndRegister;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

/**
 * 重置密码
 */

public class ResetPasswordActivity extends RHBaseActivity {

    LinearLayout layout_back;
    EditText edt_pwd;
    Button btn_ok;
    TextView txt_title;
    LoginAndRegister loginAndRegister;
    String account;
    String type;
    ProgressWheelDialog dialog;


    @Override
    protected void init() {
        loginAndRegister = new LoginAndRegister();
        if(getIntent() != null)
        {
            account = getIntent().getStringExtra("account");
            type = getIntent().getStringExtra("type");
            if("0".equals(type))
            {
                txt_title.setText("设置密码");
            }
            else
            {
                txt_title.setText(getResources().getText(R.string.reset_title));
            }
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(ResetPasswordActivity.this).setHideTitleBar();
    }

    @Override
    protected void findView(View contentView) {
        dialog = new ProgressWheelDialog(ResetPasswordActivity.this);
        layout_back = (LinearLayout)contentView.findViewById(R.id.layout_back);
        edt_pwd = (EditText) contentView.findViewById(R.id.edt_pwd);
        btn_ok = (Button) contentView.findViewById(R.id.btn_ok);
        txt_title = (TextView)contentView.findViewById(R.id.txt_title);

        hideSoftKeyboard(edt_pwd);

        layout_back.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_ok:
                String pwd = edt_pwd.getText().toString();
                if(StringUtil.isEmpty(pwd))
                {
                    ToastUtil.showToast("请输入密码");
                }
                else if(!Utils.isPass(pwd))
                {
                    ToastUtil.showToast("密码只能是大小字母和数字组成");
                }
                else if(pwd.trim().length()<6 || pwd.trim().length()>16)
                {
                    ToastUtil.showToast("密码只能是6到16位");
                }
                else
                {
                    if("0".equals(type))
                    {
                        register(account, pwd);
                    }
                    else
                    {
                        resetPwd(account, pwd);
                    }
                }
                break;
        }
    }

    void resetPwd(final String account, String password)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        loginAndRegister.resetPassWordTask(account, "qqqqxxxxpppp", password, new ResponseResult(){
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                SesSharedReferences.setAccount(ResetPasswordActivity.this, account);
                ToastUtil.showToast("重置成功");
                Intent intent = new Intent();
                setResult(Constant.REGISTER_OK, intent);
                finish();
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    void register(final String account, String password)
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        loginAndRegister.RegisterTask(account, password,  new ResponseResult(){
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                SesSharedReferences.setAccount(ResetPasswordActivity.this, account);
                ToastUtil.showToast("注册成功");
                Intent intent = new Intent();
                setResult(Constant.REGISTER_OK, intent);
                finish();
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }
}
package com.jx.maneger.activities.loginAndRegister;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jx.maneger.R;
import com.jx.maneger.activities.MainActivity;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.LoginAndRegister;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.LoginResult;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.ProgressWheelDialog;

/**
 * @创建者： weifei
 * @项目名: jx
 * @创建时间： 2016/11/10 5:50
 * @描述： ${TODO} 登录页
 */

public class LoginActivity extends RHBaseActivity {
    EditText edt_account, edt_password;
    Button btn_login;
    TextView btn_register;
    RelativeLayout layout_forget;
    LoginAndRegister dao;
    String account;
    String password;
    int flag;
    ProgressWheelDialog dialog;

    @Override
    protected void init() {
        dao = new LoginAndRegister();
        if(getIntent() != null)
        {
            flag = getIntent().getIntExtra("flag", -1);
        }

        if(!StringUtil.isEmpty(SesSharedReferences.getAccount(LoginActivity.this)))
        {
            edt_account.setText(SesSharedReferences.getAccount(LoginActivity.this));
        }


    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(this).setHideTitleBar();
    }

    @Override
    protected void findView(View contentView) {
        edt_account = (EditText) contentView.findViewById(R.id.edt_phone);
        edt_password = (EditText) contentView.findViewById(R.id.edt_password);
        btn_login = (Button) contentView.findViewById(R.id.btn_login);
        btn_register = (TextView) contentView.findViewById(R.id.btn_register);
        layout_forget = (RelativeLayout) contentView.findViewById(R.id.layout_forget);
        dialog = new ProgressWheelDialog(LoginActivity.this);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        layout_forget.setOnClickListener(this);

        hideSoftKeyboard(edt_account);
        hideSoftKeyboard(edt_password);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_login:
                account = edt_account.getText().toString();
                password = edt_password.getText().toString();
                if (StringUtil.isEmpty(account)) {
                    ToastUtil.showToast("请输入合伙人编号");
                } else if (StringUtil.isEmpty(password)) {
                    ToastUtil.showToast("请输入密码");
                } else {
                    login(account, password);
                }
                break;
            case R.id.btn_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE);
                break;
            case R.id.layout_forget:
                intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE);
                break;
        }
    }



    void login(final String mAccount, String password) {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        dao.loginTask(mAccount, password, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                LoginResult loginResult = (LoginResult) object;

                if(loginResult != null && loginResult.getData().size()>0)
                {
                    LoginResult.Data data = loginResult.getData().get(0);
                    SesSharedReferences.setUsrLoginState(LoginActivity.this, true);
                    SesSharedReferences.setAccount(LoginActivity.this, mAccount);
                    SesSharedReferences.setPartnerNumber(LoginActivity.this, data.getPartnerNumber());
                    SesSharedReferences.setSafetyMark(LoginActivity.this, data.getSafetyMark());

                    if(flag > 0)//这是先要登录
                    {
                        Intent intent = new Intent();
                        setResult(Constant.LOGIN_OK, intent);
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }
                else
                {
                    ToastUtil.showToast("登录失败");
                }
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
            if(!StringUtil.isEmpty(SesSharedReferences.getAccount(LoginActivity.this)))
            {
                edt_account.setText(SesSharedReferences.getAccount(LoginActivity.this));
            }
        }
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再点击一次将退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            Intent intent = new Intent();
            setResult(Constant.EXIT_APP, intent);
            finish();
        }
    }
}

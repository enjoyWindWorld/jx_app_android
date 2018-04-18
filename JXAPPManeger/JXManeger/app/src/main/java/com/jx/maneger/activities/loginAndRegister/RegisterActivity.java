package com.jx.maneger.activities.loginAndRegister;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.LoginAndRegister;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.view.dialog.ProgressWheelDialog;


/**
 * @创建者： weifei
 * @项目名: jx
 * @创建时间： 2016/11/11 10:37
 * @描述： ${TODO} 注册页
 */
public class RegisterActivity extends RHBaseActivity {

    LinearLayout layout_back;
    EditText recommen_code, recommen_name, order_no, applicant_name, code, phone, address;
    CheckBox checkBox;
    Button btn_register;
    LoginAndRegister loginAndRegister;
    ProgressWheelDialog dialog;

    @Override
    protected void init() {
        loginAndRegister = new LoginAndRegister();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(this).setHideTitleBar();
    }

    @Override
    protected void findView(View contentView) {
        dialog = new ProgressWheelDialog(RegisterActivity.this);
        layout_back = (LinearLayout)contentView.findViewById(R.id.layout_back);
        recommen_code = (EditText)contentView.findViewById(R.id.recommen_code);
        recommen_name = (EditText)contentView.findViewById(R.id.recommen_name);
        order_no = (EditText)contentView.findViewById(R.id.order_no);
        applicant_name = (EditText)contentView.findViewById(R.id.applicant_name);
        code = (EditText)contentView.findViewById(R.id.code);
        phone = (EditText)contentView.findViewById(R.id.phone);
        address = (EditText)contentView.findViewById(R.id.address);
        btn_register = (Button)contentView.findViewById(R.id.btn_register);

        layout_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_register:

                break;
            case R.id.txt_get_code:

                break;
        }
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

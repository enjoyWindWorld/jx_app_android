package com.jx.maneger.activities.loginAndRegister;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseActivity;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.LoginAndRegister;
import com.jx.maneger.helper.TitleBarHelper;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.view.dialog.ProgressWheelDialog;


/**
 * @创建者： weifei
 * @项目名: jx
 * @创建时间： 2016/11/11 10:37
 * @描述： ${TODO} 注册页
 */
public class RegisterActivity extends RHBaseActivity {

    RelativeLayout layout_back;
    EditText recommen_code, recommen_name, order_no, applicant_name, code, phone, address;
    LinearLayout layout_agree;
    ImageView img_agree;
    Button btn_register;
    LoginAndRegister loginAndRegister;
    ProgressWheelDialog dialog;
    private boolean isAgree;

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
        layout_back = (RelativeLayout)contentView.findViewById(R.id.layout_back);
        recommen_code = (EditText)contentView.findViewById(R.id.recommen_code);
        recommen_name = (EditText)contentView.findViewById(R.id.recommen_name);
        order_no = (EditText)contentView.findViewById(R.id.order_no);
        applicant_name = (EditText)contentView.findViewById(R.id.applicant_name);
        code = (EditText)contentView.findViewById(R.id.code);
        phone = (EditText)contentView.findViewById(R.id.phone);
        address = (EditText)contentView.findViewById(R.id.address);
        btn_register = (Button)contentView.findViewById(R.id.btn_register);
        layout_agree = (LinearLayout)contentView.findViewById(R.id.layout_agree);
        img_agree = (ImageView)contentView.findViewById(R.id.img_agree);

        layout_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        layout_agree.setOnClickListener(this);
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
                if(!isAgree)
                {
                    ToastUtil.showToast("请同意本APP用户协议和法律协议");
                }
                else
                {

                }
                break;
            case R.id.layout_agree:
                Intent intent = new Intent(RegisterActivity.this, RegisterAgreeActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.REGISTER_AGREE)
        {
            isAgree = true;
            img_agree.setBackgroundResource(R.mipmap.zf_check_t);
        }
    }
}

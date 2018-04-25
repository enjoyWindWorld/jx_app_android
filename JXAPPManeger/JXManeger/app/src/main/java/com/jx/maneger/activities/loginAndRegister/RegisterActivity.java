package com.jx.maneger.activities.loginAndRegister;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.jx.maneger.results.NormalResult;
import com.jx.maneger.results.RegisterResult;
import com.jx.maneger.util.KeyboardUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.ProgressWheelDialog;
import com.jx.maneger.view.wheelCityView.CitySelectPopWindow;

import java.util.List;
import java.util.Map;


/**
 * @创建者： weifei
 * @项目名: jx
 * @创建时间： 2016/11/11 10:37
 * @描述： ${TODO} 注册页
 */
public class RegisterActivity extends RHBaseActivity {

    RelativeLayout layout_back;
    EditText recommen_code, recommen_name, order_no, applicant_name, code, phone, address, pcd;
    LinearLayout layout_agree, layout_pcd;
    ImageView img_agree;
    Button btn_register;
    LoginAndRegister loginAndRegister;
    ProgressWheelDialog dialog;
    LoginAndRegister dao;
    private boolean isAgree;
    private CitySelectPopWindow menuWindow;
    private String select, sheng = "", shi = "", qu = "";

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
        dao = new LoginAndRegister();
        dialog = new ProgressWheelDialog(RegisterActivity.this);
        layout_back = (RelativeLayout)contentView.findViewById(R.id.layout_back);
        recommen_code = (EditText)contentView.findViewById(R.id.recommen_code);
        recommen_name = (EditText)contentView.findViewById(R.id.recommen_name);
        order_no = (EditText)contentView.findViewById(R.id.order_no);
        applicant_name = (EditText)contentView.findViewById(R.id.applicant_name);
        code = (EditText)contentView.findViewById(R.id.code);
        phone = (EditText)contentView.findViewById(R.id.phone);
        address = (EditText)contentView.findViewById(R.id.address);
        pcd = (EditText)contentView.findViewById(R.id.pcd);
        btn_register = (Button)contentView.findViewById(R.id.btn_register);
        layout_agree = (LinearLayout)contentView.findViewById(R.id.layout_agree);
        layout_pcd = (LinearLayout)contentView.findViewById(R.id.layout_pcd);
        img_agree = (ImageView)contentView.findViewById(R.id.img_agree);

        layout_back.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        layout_agree.setOnClickListener(this);
        layout_pcd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.layout_back:
                finish();
                break;
            case R.id.layout_pcd:
                menuWindow = new CitySelectPopWindow(RegisterActivity.this, itemsOnClick);
                menuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                menuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                //显示窗口
                //设置layout在PopupWindow中显示的位置
                menuWindow.showAtLocation(RegisterActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                KeyboardUtil.hideSoftKeyboard(pcd);
                break;
            case R.id.btn_register:
                if(StringUtil.isEmpty(recommen_code.getText().toString()))
                {
                    ToastUtil.showToast("请输入推荐人代码");
                }
                else if(StringUtil.isEmpty(recommen_name.getText().toString()))
                {
                    ToastUtil.showToast("请输入推荐人姓名");
                }
                else if(StringUtil.isEmpty(order_no.getText().toString()))
                {
                    ToastUtil.showToast("请输入申请人订单号");
                }
                else if(StringUtil.isEmpty(applicant_name.getText().toString()))
                {
                    ToastUtil.showToast("请输入申请人姓名");
                }
                else if(StringUtil.isEmpty(code.getText().toString()))
                {
                    ToastUtil.showToast("请输入身份证号码");
                }
                else if(StringUtil.isEmpty(phone.getText().toString()))
                {
                    ToastUtil.showToast("请输入手机号码");
                }
                else if(StringUtil.isEmpty(pcd.getText().toString()))
                {
                    ToastUtil.showToast("请选择省市区");
                }
                else if(StringUtil.isEmpty(address.getText().toString()))
                {
                    ToastUtil.showToast("请输入家庭住址");
                }
                else if(!isAgree)
                {
                    ToastUtil.showToast("请同意本APP用户协议和法律协议");
                }
                else
                {
                    register(recommen_code.getText().toString(), recommen_name.getText().toString(), order_no.getText().toString(), applicant_name.getText().toString(), code.getText().toString(), phone.getText().toString(), address.getText().toString(), sheng, shi, qu);
                }
                break;
            case R.id.layout_agree:
                Intent intent = new Intent(RegisterActivity.this, RegisterAgreeActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }

    void register(String RecommenderCode, String NameReferee, String ord_no, String nameApplicant, String cardNumber, String mobilePhone, String homeAddress, String s_province, String s_city, String s_county) {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        dao.registerTask(RecommenderCode, NameReferee, nameApplicant, ord_no, cardNumber, mobilePhone, homeAddress, s_province, s_city, s_county, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                RegisterResult registerResult = (RegisterResult) object;

                if(registerResult != null && registerResult.getData().size()>0)
                {
                    SesSharedReferences.setAccount(RegisterActivity.this, registerResult.getData().get(0).getId());
                    ToastUtil.showToast("注册成功");
                    setResult(Constant.REGISTER_OK);
                    finish();
                }
                else
                {
                    ToastUtil.showToast("注册失败");
                }
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    //为弹出窗口实现监听类
    public View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.grxx_xbxz_qd:
                    List<Map<String, String>> datas = menuWindow.getPpWindowSelect();
                    select = datas.get(1).get("addrStr");
                    pcd.setText(select);
                    sheng = menuWindow.getSheng();
                    shi = menuWindow.getShi();
                    qu = menuWindow.getQu();
                    break;
            }
        }
    };

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

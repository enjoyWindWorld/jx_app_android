package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.igexin.sdk.PushManager;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.LoginAndRegister;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.ui.activitise.SplashActivity;
import com.jx.intelligent.util.KeyboardUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.GetCodeResult;
import com.jx.intelligent.ui.activitise.MainActivity;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

/**
 * 绑定新手机
 * Created by Administrator on 2016/11/15 0015.
 */
public class SetNewPhoNumActivity extends RHBaseActivity {

    ImageView titlebar_left_vertical_iv;
    Button grxx_bdxsj_bdan, grxx_bdxsj_hqyzm;
    EditText grxx_bdxsj_yzmsrk, grxx_bdxsj_sjsrk;
    LoginAndRegister loginAndRegister;
    String phone = "";
    UserCenter userCenter;
    boolean isGetCode = false;
    GetCodeResult getCodeResult;
    private ProgressWheelDialog dialog;
    private String userId;

    Handler handler  = new Handler();
    Runnable updateThread =  new Runnable(){
        @Override
        public void run() {
            grxx_bdxsj_hqyzm.setEnabled(false);
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
                        grxx_bdxsj_hqyzm.setText("获取短信验证码");
                        grxx_bdxsj_hqyzm.setEnabled(true);
                        grxx_bdxsj_hqyzm.setBackgroundResource(R.drawable.grxx_xgmm_anbg);
                        Constant.TOTALTIME = 120;
                    } else {
                        grxx_bdxsj_hqyzm.setText(Constant.TOTALTIME + "秒后重新获取验证码");
                        grxx_bdxsj_hqyzm.setEnabled(false);
                        grxx_bdxsj_hqyzm.setBackgroundResource(R.color.alpha_gray);
                        mHandler.sendEmptyMessageDelayed(TIME_COUNT_DOWN, 1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void init() {
        loginAndRegister = new LoginAndRegister();
        userCenter = new UserCenter();
        SesSharedReferences sp =new SesSharedReferences();
        userId = sp.getUserId(UIUtil.getContext());
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_set_new_pho_num;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("绑定新手机号")
                .setLeftClickListener(this);

    }

    @Override
    protected void findView(View contentView) {

        grxx_bdxsj_bdan = (Button) contentView.findViewById(R.id.grxx_bdxsj_bdan);
        grxx_bdxsj_bdan.setOnClickListener(this);
        grxx_bdxsj_hqyzm = (Button) contentView.findViewById(R.id.grxx_bdxsj_hqyzm);
        grxx_bdxsj_hqyzm.setOnClickListener(this);
        grxx_bdxsj_yzmsrk = (EditText) contentView.findViewById(R.id.grxx_bdxsj_yzmsrk);
        grxx_bdxsj_sjsrk = (EditText) contentView.findViewById(R.id.grxx_bdxsj_sjsrk);
        dialog = new ProgressWheelDialog(SetNewPhoNumActivity.this);

        hideSoftKeyboard(grxx_bdxsj_yzmsrk);
        hideSoftKeyboard(grxx_bdxsj_sjsrk);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.grxx_bdxsj_hqyzm:
                //TODO 获取验证码
                phone = grxx_bdxsj_sjsrk.getText().toString();
                if (StringUtil.isEmpty(phone)) {
                    ToastUtil.showToast("请输入手机号码");
                } else if (!Utils.isMobileNO(phone)) {
                    ToastUtil.showToast("请输入手机号码格式错误");
                } else {
                    getCode(SesSharedReferences.getPhoneNum(SetNewPhoNumActivity.this));
                    KeyboardUtil.hideSoftKeyboard(grxx_bdxsj_hqyzm);
                }
                break;
            case R.id.grxx_bdxsj_bdan:
                //TODO 绑定新手机请求
                phone = grxx_bdxsj_sjsrk.getText().toString();
                if (StringUtil.isEmpty(phone)) {
                    ToastUtil.showToast("请输入手机号码");
                } else if (!Utils.isMobileNO(phone)) {
                    ToastUtil.showToast("请输入手机号码格式错误");
                } else if (!isGetCode) {
                    ToastUtil.showToast("请先获取验证码");
                }else if(StringUtil.isEmpty(grxx_bdxsj_yzmsrk.getText().toString())) {
                    ToastUtil.showToast("请输入验证码");
                } else {
                    checkCode(SesSharedReferences.getPhoneNum(SetNewPhoNumActivity.this), grxx_bdxsj_yzmsrk.getText().toString());
                }
                break;
        }
    }

    //验证手机验证码
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
                commitBDNewPhone();
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 获取绑定手机新手机号码验证码
     *
     * @param phone
     */
    void getCode(String phone) {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        loginAndRegister.getCodeTask(phone, "1", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ToastUtil.showToast("获取验证码成功");
                handler.postDelayed(updateThread, 0);
                getCodeResult = (GetCodeResult) object;
                isGetCode = true;
            }
            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }


    /**
     * 提交绑定新手机号码请求
     */
    void commitBDNewPhone() {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        userCenter.commitBDNewPhoneTask(SesSharedReferences.getUserId(SetNewPhoNumActivity.this), phone, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                ToastUtil.showToast("账号已失效，请重新登录。");
//                SesSharedReferences.setPhoneNum(SetNewPhoNumActivity.this, phone);
//                RHBaseApplication.getInstance().setNotReadMsg(0);
//                RHBaseApplication.getMainActivity().main_rb_newhome.setChecked(true);
//                SesSharedReferences.cleanShare(SetNewPhoNumActivity.this);
//                Intent intent_main = new Intent(SetNewPhoNumActivity.this, MainActivity.class);
//                startActivity(intent_main);

                Intent intent = new Intent(SetNewPhoNumActivity.this, SettingActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("login_out", bundle);
                startActivity(intent);
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }
}

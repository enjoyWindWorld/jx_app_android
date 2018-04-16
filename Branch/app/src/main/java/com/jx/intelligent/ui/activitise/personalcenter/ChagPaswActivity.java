package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.igexin.sdk.PushManager;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.dao.LoginAndRegister;
import com.jx.intelligent.ui.activitise.SplashActivity;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.ui.activitise.MainActivity;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

/**
 * 修改密码
 * Created by Administrator on 2016/11/15 0015.
 */
public class ChagPaswActivity extends RHBaseActivity {

    private static final String TAG = "ChagPaswActivity";
    ImageView titlebar_left_vertical_iv;
    EditText grxx_xgmm_jmm_srk, grxx_xgmm_xmm_srk, grxx_xgmm_rxmm_srk;
    Button grxx_xgmm_xgmm_an;
    private String userId;

    LoginAndRegister loginAndRegisterDao;
    private ProgressWheelDialog dialog;

    @Override
    protected void init() {
        loginAndRegisterDao = new LoginAndRegister();
        SesSharedReferences sp =new SesSharedReferences();
        userId = sp.getUserId(UIUtil.getContext());
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_chag_paswd;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("修改密码")
                .setLeftClickListener(this);

    }

    @Override
    protected void findView(View contentView) {

        grxx_xgmm_jmm_srk = (EditText) contentView.findViewById(R.id.grxx_xgmm_jmm_srk);
        grxx_xgmm_xmm_srk = (EditText) contentView.findViewById(R.id.grxx_xgmm_xmm_srk);
        grxx_xgmm_rxmm_srk = (EditText) contentView.findViewById(R.id.grxx_xgmm_rxmm_srk);
        grxx_xgmm_xgmm_an = (Button) contentView.findViewById(R.id.grxx_xgmm_xgmm_an);
        grxx_xgmm_xgmm_an.setOnClickListener(this);
        dialog = new ProgressWheelDialog(ChagPaswActivity.this);

        hideSoftKeyboard(grxx_xgmm_jmm_srk);
        hideSoftKeyboard(grxx_xgmm_xmm_srk);
        hideSoftKeyboard(grxx_xgmm_rxmm_srk);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.grxx_xgmm_xgmm_an:
                String pwdj = grxx_xgmm_jmm_srk.getText().toString();
                String pwdx = grxx_xgmm_xmm_srk.getText().toString();
                String pwdrx = grxx_xgmm_rxmm_srk.getText().toString();
                if (StringUtil.isEmpty(pwdj)) {
                    ToastUtil.showToast("请输入密码");
                } else if (!Utils.isPass(pwdj)) {
                    ToastUtil.showToast("密码只能是大小字母和数字组成");
                } else if (pwdj.trim().length() < 6 || pwdj.trim().length() > 16) {
                    ToastUtil.showToast("密码只能是6到16位");
                } else if (StringUtil.isEmpty(pwdx)) {
                    ToastUtil.showToast("请输入新密码");
                } else if (!Utils.isPass(pwdx)) {
                    ToastUtil.showToast("新密码只能是大小字母和数字组成");
                } else if (pwdx.trim().length() < 6 || pwdx.trim().length() > 16) {
                    ToastUtil.showToast("新密码只能是6到16位");
                }else if (StringUtil.isEmpty(pwdrx)) {
                    ToastUtil.showToast("请输入新密码");
                } else if (!Utils.isPass(pwdrx)) {
                    ToastUtil.showToast("新密码只能是大小字母和数字组成");
                } else if (pwdrx.trim().length() < 6 || pwdrx.trim().length() > 16) {
                    ToastUtil.showToast("新密码只能是6到16位");
                }else if (pwdj.equals(pwdrx) || pwdj.equals(pwdx)) {
                    ToastUtil.showToast("旧密码和新密码不能相同");
                } else if (!pwdx.equals(pwdrx)) {
                    ToastUtil.showToast("两次输入的新密码不相同");
                } else {
                    resetPassWord();
                }
                break;
        }
    }


    public void resetPassWord() {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        String phone = SesSharedReferences.getPhoneNum(ChagPaswActivity.this);
        loginAndRegisterDao.resetPassWordTask(phone, grxx_xgmm_jmm_srk.getText().toString().trim(),
                grxx_xgmm_xmm_srk.getText().toString().trim(),
                new ResponseResult() {
                    @Override
                    public void resSuccess(Object object) {
                        dialog.dismiss();
                        ToastUtil.showToast("修改密码成功");
//                        RHBaseApplication.getInstance().setNotReadMsg(0);
//                        RHBaseApplication.getMainActivity().main_rb_newhome.setChecked(true);
//                        SesSharedReferences.cleanShare(ChagPaswActivity.this);
//                        Intent intent_main = new Intent(ChagPaswActivity.this, MainActivity.class);
//                        startActivity(intent_main);

                        Intent intent = new Intent(ChagPaswActivity.this, SettingActivity.class);
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

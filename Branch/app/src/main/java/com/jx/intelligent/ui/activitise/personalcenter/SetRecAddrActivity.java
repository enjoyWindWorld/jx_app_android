package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.util.KeyboardUtil;
import com.jx.intelligent.view.CitySelectPopWindow;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置设置收货地址
 * Created by Administrator on 2016/11/15 0015.
 */
public class SetRecAddrActivity extends RHBaseActivity {

    private static final String TAG = "SetRecAddrActivity";
    ImageView titlebar_left_vertical_iv;

    CitySelectPopWindow menuWindow;
    LinearLayout grxx_tjdz_xzdz, grxx_dz_szmr_chekb_lr, grxx_dz_dq_lr;
    EditText grxx_dz_lxm_edt, grxx_dz_phonom_edt, grxx_dz_qxdz_edt;
    TextView grxx_dz_dq;
    CheckBox grxx_dz_szmr_chekb;
    UserCenter userCenter;

    private AddrType addrType;

    String id;
    String name;
    String phone;
    String area;
    String detail;
    String code;
    String isdefault;

    String newAddrCode;

    boolean isSelectCheck;
    private ProgressWheelDialog dialog;


    public enum AddrType {
        ADD, UPDATE
    }


    @Override
    protected void init() {

        userCenter = new UserCenter();

        Intent intent = getIntent();
        Bundle addBundle = intent.getBundleExtra("add");
        if (addBundle != null) {
            addrType = AddrType.ADD;
        }
        Bundle updateBundle = intent.getBundleExtra("update");
        if (updateBundle != null) {
            addrType = AddrType.UPDATE;
            id = updateBundle.getString("id");
            name = updateBundle.getString("name");
            phone = updateBundle.getString("phone");
            area = updateBundle.getString("area");
            detail = updateBundle.getString("detail");
            code = updateBundle.getString("code");
            isdefault = updateBundle.getString("isdefault");

            grxx_dz_lxm_edt.setText(name);
            grxx_dz_phonom_edt.setText(phone);
            grxx_dz_dq.setText(area);
            grxx_dz_qxdz_edt.setText(detail);

            if (isdefault.equals("0")) {
                isSelectCheck = true;
                grxx_dz_szmr_chekb.setChecked(true);
            } else {
                isSelectCheck = false;
                grxx_dz_szmr_chekb.setChecked(false);
            }
        }

        hideSoftKeyboard(grxx_dz_lxm_edt);
        hideSoftKeyboard(grxx_dz_phonom_edt);
        hideSoftKeyboard(grxx_dz_qxdz_edt);
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_set_rec_addr;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftText("取消")
                .setMiddleTitleText("修改地址")
                .setRightText("确定")
                .setLeftClickListener(this)
                .setRightClickListener(this);

    }

    @Override
    protected void findView(View contentView) {

        grxx_dz_lxm_edt = (EditText) contentView.findViewById(R.id.grxx_dz_lxm_edt);
        grxx_dz_phonom_edt = (EditText) contentView.findViewById(R.id.grxx_dz_phonom_edt);
        grxx_dz_dq = (TextView) contentView.findViewById(R.id.grxx_dz_dq);
        grxx_dz_qxdz_edt = (EditText) contentView.findViewById(R.id.grxx_dz_qxdz_edt);

        grxx_tjdz_xzdz = (LinearLayout) contentView.findViewById(R.id.grxx_tjdz_xzdz);
        grxx_tjdz_xzdz.setOnClickListener(this);
        grxx_dz_dq_lr = (LinearLayout) contentView.findViewById(R.id.grxx_dz_dq_lr);
        grxx_dz_dq_lr.setOnClickListener(this);
        grxx_dz_szmr_chekb_lr = (LinearLayout) contentView.findViewById(R.id.grxx_dz_szmr_chekb_lr);
        grxx_dz_szmr_chekb_lr.setOnClickListener(this);
        grxx_dz_szmr_chekb = (CheckBox) contentView.findViewById(R.id.grxx_dz_szmr_chekb);

        dialog = new ProgressWheelDialog(SetRecAddrActivity.this);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;

            case R.id.grxx_dz_dq_lr:
                menuWindow = new CitySelectPopWindow(SetRecAddrActivity.this, itemsOnClick);
                menuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                menuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                //显示窗口
                //设置layout在PopupWindow中显示的位置
                menuWindow.showAtLocation(SetRecAddrActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                KeyboardUtil.hideSoftKeyboard(grxx_dz_dq);
                break;
            case R.id.titlebar_right_rl:
                addAndUpdateHomeAddr();
                break;
            case R.id.grxx_dz_szmr_chekb_lr:
                if (grxx_dz_szmr_chekb.isChecked()) {
                    grxx_dz_szmr_chekb.setChecked(false);
                } else {
                    grxx_dz_szmr_chekb.setChecked(true);
                }
                break;
        }
    }

    String sheng, shi, qu, addr;
    //为弹出窗口实现监听类
    public View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.grxx_xbxz_qd:
                    List<Map<String, String>> select = menuWindow.getPpWindowSelect();
                    Log.e(TAG, "~~~select.sa=" + select.size());
                    sheng = menuWindow.getSheng();
                    shi = menuWindow.getShi();
                    if(shi.equals("市辖区") || shi.equals("县"))
                    {
                        shi = "";
                    }
                    qu = menuWindow.getQu();

                    if(!StringUtil.isEmpty(sheng))
                    {
                        addr = sheng + "-";
                    }

                    if(!StringUtil.isEmpty(shi))
                    {
                        addr += shi + "-";
                    }

                    if(!StringUtil.isEmpty(qu))
                    {
                        addr += qu;
                    }

                    grxx_dz_dq.setText(addr);
                    newAddrCode = select.get(0).get("addrCode");
                    break;
            }
        }


    };


    void addAndUpdateHomeAddr() {
        Map<String, String> map = new HashMap<String, String>();
        if (addrType == AddrType.ADD) {
            map.put("userid", SesSharedReferences.getUserId(SetRecAddrActivity.this));
            String lxm = grxx_dz_lxm_edt.getText().toString().trim();
            String phon = grxx_dz_phonom_edt.getText().toString().trim();
            String dz = grxx_dz_dq.getText().toString().trim();
            String dz_qx = grxx_dz_qxdz_edt.getText().toString().trim();
            boolean isNewCheckd = grxx_dz_szmr_chekb.isChecked();

            map.put("name", lxm);
            map.put("phone", phon);
            map.put("area", dz);
            map.put("detail", dz_qx);
            map.put("code", newAddrCode);
            map.put("isdefault", isNewCheckd == true ? "0" : "1");
            if (lxm.equals("")) {

                //TODO 提示联系人不能为空
                ToastUtil.showToast("联系人不能为空");

            } else if (dz.equals("")) {

                //TODO 提示地址不能为空
                ToastUtil.showToast("地址不能为空");
            } else if (dz_qx.equals("")) {

                //TODO　提示详细地址不能为空
                ToastUtil.showToast("详细地址不能为空");

            } else if (!Utils.isMobileNO(phon)) {

                //TODO　提示手机号码格式不正确
                ToastUtil.showToast("手机号码格式不正确！");

            } else if(Utils.filterEmoji(lxm) || Utils.filterEmoji(dz) || Utils.filterEmoji(dz_qx)){
                ToastUtil.showToast("请勿输入表情符号");
            } else {
                if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
                {
                    return;
                }

                dialog.show();
                userCenter.addHomeAddrTask(map, new ResponseResult() {
                    @Override
                    public void resSuccess(Object object) {
                        ToastUtil.showToast("添加地址成功");
                        dialog.dismiss();
                        setResult(Constant.ADD_UPDATE_HOME_ADDR);
                        finish();
                    }
                    @Override
                    public void resFailure(String message) {
                        ToastUtil.showToast(message);
                        dialog.dismiss();
                    }
                });
            }
        } else if (addrType == AddrType.UPDATE) {
            map.put("id", id);
            map.put("userid", SesSharedReferences.getUserId(SetRecAddrActivity.this));
            String lxm = grxx_dz_lxm_edt.getText().toString().trim();
            String phon = grxx_dz_phonom_edt.getText().toString().trim();
            String dz = grxx_dz_dq.getText().toString().trim();
            String dz_qx = grxx_dz_qxdz_edt.getText().toString().trim();
            boolean isNewCheckd = grxx_dz_szmr_chekb.isChecked();

            if (!lxm.equals(name) || !phon.equals(phone) || !dz.equals(area) || !dz_qx.equals(detail) || isSelectCheck != isNewCheckd) {
                map.put("name", lxm);
                map.put("phone", phon);
                map.put("area", dz);
                map.put("detail", dz_qx);
                if (newAddrCode != null) {
                    map.put("code", newAddrCode);
                } else {
                    map.put("code", code);
                }
                map.put("isdefault", isNewCheckd == true ? "0" : "1");

                if (lxm.equals("")) {

                    //TODO 提示联系人不能为空
                    ToastUtil.showToast("联系人不能为空");

                } else if (dz.equals("")) {

                    //TODO 提示地址不能为空
                    ToastUtil.showToast("地址不能为空");
                } else if (dz_qx.equals("")) {

                    //TODO　提示详细地址不能为空
                    ToastUtil.showToast("详细地址不能为空");

                } else if (!Utils.isMobileNO(phon)) {

                    //TODO　提示手机号码格式不正确
                    ToastUtil.showToast("手机号码格式不正确！");

                } else if(Utils.filterEmoji(lxm) || Utils.filterEmoji(dz) || Utils.filterEmoji(dz_qx)){
                    ToastUtil.showToast("请勿输入表情符号");
                }
                else {
                    if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
                    {
                        return;
                    }

                    dialog.show();
                    userCenter.addHomeAddrTask(map, new ResponseResult() {
                        @Override
                        public void resSuccess(Object object) {
                            ToastUtil.showToast("修改地址成功");
                            dialog.dismiss();
                            setResult(Constant.ADD_UPDATE_HOME_ADDR);
                            finish();
                        }

                        @Override
                        public void resFailure(String message) {
                            ToastUtil.showToast(message);
                            dialog.dismiss();
                        }
                    });
                }
            }
        }
    }
}


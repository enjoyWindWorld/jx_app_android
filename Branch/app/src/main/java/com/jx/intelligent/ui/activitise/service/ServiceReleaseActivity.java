package com.jx.intelligent.ui.activitise.service;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CommunityService;
import com.jx.intelligent.result.ServiceReleaseResult;
import com.jx.intelligent.ui.activitise.payment.BuyDetailsActivity;
import com.jx.intelligent.ui.activitise.payment.DetailsPaymentActivity;
import com.jx.intelligent.ui.activitise.personalcenter.ErWeiMaActivity;
import com.jx.intelligent.util.KeyboardUtil;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.enums.DateShowType;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.util.DateUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.DateAndTimerPicker;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016/11/17 0017.
 * 服务发布界面
 */

public class ServiceReleaseActivity extends RHBaseActivity {

    private ImageView img_scanning;
    private RelativeLayout layout_content, layout_star_time, layout_end_time;
    private LinearLayout layout_location;
    private TextView txt_star_time, txt_end_time, txt_content;
    private Button btn_release;
    private EditText edit_company, edit_phone, edit_promoter, edit_name;
    private TextView txt_location;
    private String address, url, content, categoryid, sheng = "", shi = "", qu = "", detailAddr = "", longitude = "", latitude = "";
    private String starTime, endTime;
    private boolean isSelectorStar = true;
    private CommunityService dao;
    private ProgressWheelDialog dialog;

    @Override
    protected void init() {
        if(getIntent() != null)
        {
            categoryid = getIntent().getStringExtra("categoryid");
        }
        dao = new CommunityService();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_service_release;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(ServiceReleaseActivity.this).setMiddleTitleText(R.string.service_release_title)
                .setLeftImageRes(R.drawable.selector_back)
                .setLeftClickListener(this)
                .setRightText("发布须知")
                .setRightClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        layout_content = (RelativeLayout) contentView.findViewById(R.id.layout_content);
        layout_star_time = (RelativeLayout) contentView.findViewById(R.id.layout_star_time);
        layout_end_time = (RelativeLayout) contentView.findViewById(R.id.layout_end_time);
        layout_location = (LinearLayout) contentView.findViewById(R.id.layout_location);
        btn_release = (Button) contentView.findViewById(R.id.btn_release);
        txt_location = (TextView) contentView.findViewById(R.id.txt_location);
        edit_company = (EditText) contentView.findViewById(R.id.edit_company);
        edit_phone = (EditText) contentView.findViewById(R.id.edit_phone);
        edit_name = (EditText) contentView.findViewById(R.id.edit_name);
        edit_promoter = (EditText) contentView.findViewById(R.id.edit_promoter);
        txt_star_time = (TextView) contentView.findViewById(R.id.txt_star_time);
        txt_end_time = (TextView) contentView.findViewById(R.id.txt_end_time);
        txt_content = (TextView) contentView.findViewById(R.id.txt_content);
        img_scanning = (ImageView) contentView.findViewById(R.id.img_scanning);

        hideSoftKeyboard(edit_company);
        hideSoftKeyboard(edit_phone);

        layout_star_time.setOnClickListener(this);
        layout_end_time.setOnClickListener(this);
        layout_content.setOnClickListener(this);
        btn_release.setOnClickListener(this);
        layout_location.setOnClickListener(this);
        img_scanning.setOnClickListener(this);
        dialog = new ProgressWheelDialog(ServiceReleaseActivity.this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        switch (view.getId())
        {
            case R.id.img_scanning:
                intent = new Intent(ServiceReleaseActivity.this, CaptureActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE);
                break;
            case R.id.titlebar_left_rl:
                finish();
                KeyboardUtil.hideSoftKeyboard(edit_name);
                break;
            case R.id.layout_content:
                intent = new Intent(ServiceReleaseActivity.this, ServiceAddContentActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("categoryid", categoryid);
                intent.putExtra("content", content);
                startActivityForResult(intent, Constant.REQUEST_CODE);
                break;
            case R.id.layout_star_time:
                showDatePicker();
                isSelectorStar = true;
                break;
            case R.id.layout_end_time:
                showDatePicker();
                isSelectorStar = false;
                break;
            case R.id.btn_release:
                if(StringUtil.isEmpty(address))
                {
                    ToastUtil.showToast(R.string.service_release_hit_location);
                }
                else if(StringUtil.isEmpty(url) || StringUtil.isEmpty(categoryid) || StringUtil.isEmpty(content))
                {
                    ToastUtil.showToast(R.string.service_release_service_type);
                }
                else if(StringUtil.isEmpty(edit_company.getText().toString()))
                {
                    ToastUtil.showToast(R.string.service_release_service_company);
                }
                else if(Utils.filterEmoji(edit_company.getText().toString()))
                {
                    ToastUtil.showToast("请勿输入表情内容");
                }
                else if(StringUtil.isEmpty(starTime))
                {
                    ToastUtil.showToast(R.string.service_release_service_star_time);
                }
                else if(StringUtil.isEmpty(endTime))
                {
                    ToastUtil.showToast(R.string.service_release_service_end_time);
                }
//                else if(!DateUtil.judgeCurrTimeNoYear(year+"-"+month+"-"+day+" "+starTime+":"+second))
//                {
//                    ToastUtil.showToast("服务开始时间要大于当前时间");
//                }
                else if(!DateUtil.judgeTime2Time(year+"-"+month+"-"+day+" "+starTime+":"+second, year+"-"+month+"-"+day+" "+endTime+":"+second))
                {
                    ToastUtil.showToast("服务截止时间要大于服务开始时间");
                }
                else if(StringUtil.isEmpty(edit_phone.getText().toString()))
                {
                    ToastUtil.showToast(R.string.service_release_service_tel);
                }
                else if(!Utils.isMobileNO(edit_phone.getText().toString()))
                {
                    ToastUtil.showToast("输入的手机号码格式有误");
                }
                else if(StringUtil.isEmpty(edit_name.getText().toString()))
                {
                    ToastUtil.showToast(R.string.service_release_service_name);
                }
                else
                {
                    btn_release.setEnabled(false);
                    serviceRelease();
                }
                break;
            case R.id.layout_location:
                intent = new Intent(ServiceReleaseActivity.this, ServiceAddAddressActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE);
                break;
            case R.id.titlebar_right_rl:
                intent = new Intent(ServiceReleaseActivity.this, BuyDetailsActivity.class);
                intent.putExtra("flag", 1);
                startActivity(intent);
                break;
        }
    }

    String year, month, day, second;

    /**
     * 显示时间控件
     */
    public void showDatePicker(){
        DateAndTimerPicker.Builder builder = new DateAndTimerPicker.Builder(this, DateShowType.HM);

        DateAndTimerPicker picker = builder.setOnDateAndTimeSelectedListener(new DateAndTimerPicker.OnDateAndTimeSelectedListener() {
            @Override
            public void onDateAndTimeSelected(String[] dates) {
                Calendar c = Calendar.getInstance(); //获取当前的日期
                year = c.get(Calendar.YEAR)+"";
                month = c.get(Calendar.MONTH)+"";
                day = c.get(Calendar.DAY_OF_MONTH)+"";
                second = c.get(Calendar.SECOND)+"";

                if(isSelectorStar)
                {
                    starTime = dates[3]+":"+dates[4];
                    txt_star_time.setText(starTime);
                    txt_star_time.setTextColor(getResources().getColor(R.color.color_1bb6ef));
                }
                else
                {
                    endTime = dates[3]+":"+dates[4];
                    txt_end_time.setText(endTime);
                    txt_end_time.setTextColor(getResources().getColor(R.color.color_1bb6ef));
                }
            }
        }).create();
        picker.show();
    }

    void serviceRelease()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        dao.serviceReleaseTask(SesSharedReferences.getUserId(ServiceReleaseActivity.this),
                edit_promoter.getText().toString(),
                categoryid,
                edit_phone.getText().toString(),
                StringUtil.isEmpty(longitude) ? "0" : longitude,
                StringUtil.isEmpty(latitude) ? "0" : latitude,
                address,
                content,
                edit_company.getText().toString(),
                starTime,
                endTime,
                url,
                edit_name.getText().toString(),
                new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                ServiceReleaseResult serviceReleaseResult = (ServiceReleaseResult) object;
                dialog.dismiss();
                btn_release.setEnabled(true);
                if(serviceReleaseResult != null && serviceReleaseResult.getData().size() > 0)
                {
                    if("1".equals(serviceReleaseResult.getData().get(0).getIsPush()))//不用付费
                    {
                        Intent intent = new Intent();
                        intent.putExtra("categoryid", categoryid);
                        setResult(RESULT_OK, intent);
                        finish();
                        KeyboardUtil.hideSoftKeyboard(edit_name);
                    }
                    else
                    {
                        Intent intent = new Intent(ServiceReleaseActivity.this, DetailsPaymentActivity.class);
                        intent.putExtra("serviceTag", "3");
                        intent.putExtra("ServiceReleaseResult", serviceReleaseResult);
                        startActivityForResult(intent, Constant.REQUEST_CODE);
                    }
                }
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                btn_release.setEnabled(true);
                ToastUtil.showToast(message);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.GET_HOME_ADDR_OK && data != null)
        {
            sheng = data.getStringExtra("sheng");
            shi = data.getStringExtra("shi");
            qu = data.getStringExtra("qu");
            detailAddr = data.getStringExtra("detailAddr");
            longitude = data.getStringExtra("longitude");
            latitude = data.getStringExtra("latitude");

            if(!StringUtil.isEmpty(sheng))
            {
                address = sheng + "-";
            }

            if(!StringUtil.isEmpty(shi))
            {
                address += shi + "-";
            }

            if(!StringUtil.isEmpty(qu))
            {
                address += qu + " ";
            }

            if(!StringUtil.isEmpty(detailAddr))
            {
                address += detailAddr;
            }

            txt_location.setText(address);
        }
        else if(resultCode == Constant.GET_SERVICE_CONTENT_OK && data != null)
        {
            url = data.getStringExtra("url");
            content = data.getStringExtra("content");
            categoryid = data.getStringExtra("categoryid");
            txt_content.setText("查看分类及内容");
        }
        else if(resultCode ==Constant.PAY_OK)
        {
            Intent intent = new Intent();
            intent.putExtra("categoryid", categoryid);
            setResult(RESULT_OK, intent);
            finish();
            KeyboardUtil.hideSoftKeyboard(edit_name);
        }
        else if (resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            String result= bundle.getString(CodeUtils.RESULT_STRING);
            int result_type = bundle.getInt(CodeUtils.RESULT_TYPE);
            if(result_type == CodeUtils.RESULT_SUCCESS)
            {
                if(result.startsWith("jxsmart"))
                {
                    String[] strs = result.split("://");
                    if(strs.length >= 3)
                    {
                        if(strs[1].equals("promotion"))
                        {
                            edit_promoter.setText(StringUtil.decode(strs[2]));
                        }
                        else
                        {
                            ToastUtil.showToast("对不起，扫描的不是推广码");
                        }
                    }
                }
                else
                {
                    ToastUtil.showToast("对不起，扫描的不是净喜的二维码");
                }
            }
            else
            {
                ToastUtil.showToast("扫描出错");
            }
        }
    }
}

package com.jx.intelligent.ui.activitise.service;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.jx.intelligent.R;
import com.jx.intelligent.adapter.jxAdapter.ReportTypeAdapter;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.bean.ReportTypeBean;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CommunityService;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.ServiceDetailInfoResult;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.view.wheelCityView.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 韦飞 on 2017/6/28 0028.
 * 服务举报界面
 */

public class ServiceReportActivity extends RHBaseActivity implements AdapterView.OnItemClickListener {

    private TitleBarHelper titleBarHelper;
    private MyListView my_listview;
    private EditText edt_content;
    private ReportTypeAdapter reportTypeAdapter;
    private NormalAlertDialog msgDialog;
    private ProgressWheelDialog progressWheelDialog;
    private List<ReportTypeBean> reports = new ArrayList<ReportTypeBean>();
    private String[] report_str = {"色情低俗","广告骚扰","诱惑分享","谣言","政治敏感","违法（暴力恐怖、违禁品等）","侵权","售假", "其他"};
    private CommunityService dao;
    private String pubid;
    private String cause;
    private String content;//上个页面的内容
    private int index;

    @Override
    protected void init() {

        if(getIntent() != null)
        {
            pubid = getIntent().getStringExtra("pubid");
            content = getIntent().getStringExtra("content");
        }

        for (int i = 0; i < report_str.length; i ++)
        {
            ReportTypeBean reportTypeBean = new ReportTypeBean();
            reportTypeBean.setType(report_str[i]);
            if(i == 0)
            {
                reportTypeBean.setFlag(1);
            }
            reports.add(reportTypeBean);
        }
        showReports();
        dao = new CommunityService();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_service_report;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(ServiceReportActivity.this);
        titleBarHelper.setMiddleTitleText(R.string.service_report_title);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleBarHelper.setRightText("举报");
        titleBarHelper.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("是否要举报该服务？", " ", "取消", "确定");
                msgDialog.show();
            }
        });
    }

    @Override
    protected void findView(View contentView) {
        msgDialog = new NormalAlertDialog(new NormalAlertDialog.Builder(ServiceReportActivity.this));
        progressWheelDialog = new ProgressWheelDialog(ServiceReportActivity.this);
        my_listview = (MyListView)contentView.findViewById(R.id.my_listview);
        edt_content = (EditText) contentView.findViewById(R.id.edt_content);
        my_listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        for (ReportTypeBean reportTypeBean : reports)
        {
            reportTypeBean.setFlag(0);
        }

        reports.get(i).setFlag(1);
        showReports();

        if(reports.get(i).getType().equals("其他"))
        {
            edt_content.setVisibility(View.VISIBLE);
        }
        else
        {
            edt_content.setVisibility(View.GONE);
        }

        index = i;
    }

    private void showReports()
    {
        reportTypeAdapter = new ReportTypeAdapter(ServiceReportActivity.this, reports);
        my_listview.setAdapter(reportTypeAdapter);
    }

    void serviceReport()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        if("其他".equals(report_str[index]))
        {
            cause = edt_content.getText().toString();
        }
        else
        {
            cause = report_str[index];
        }

        if(StringUtil.isEmpty(cause))
        {
            ToastUtil.showToast("请填写您的举报内容");
            return;
        }

        progressWheelDialog.show();
        dao.serviceReport(SesSharedReferences.getUserId(ServiceReportActivity.this), pubid, SesSharedReferences.getNickName(ServiceReportActivity.this), content, cause, SesSharedReferences.getPhoneNum(ServiceReportActivity.this), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                ToastUtil.showToast("举报成功");
                progressWheelDialog.dismiss();
                finish();
            }

            @Override
            public void resFailure(String message) {
                ToastUtil.showToast(message);
                progressWheelDialog.dismiss();
            }
        });
    }

    /**
     *
     * @param title
     * @param titleCancle
     * @param titleOK
     */
    private void showDialog(String title, String content, String titleCancle, String titleOK)
    {
        msgDialog = new NormalAlertDialog.Builder(ServiceReportActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText(title)
                .setTitleTextColor(R.color.color_000000)
                .setContentText(content)
                .setContentTextColor(R.color.color_000000)
                .setLeftButtonText(titleCancle)
                .setLeftButtonTextColor(R.color.color_000000)
                .setRightButtonText(titleOK)
                .setRightButtonTextColor(R.color.color_000000)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        msgDialog.dismiss();
                    }
                    @Override
                    public void clickRightButton(View view) {
                        serviceReport();
                        msgDialog.dismiss();
                    }
                })
                .build();
    }

}

package com.jx.intelligent.ui.activitise.service;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.navi.model.NaviLatLng;
import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CommunityService;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.ServiceDetailInfoResult;
import com.jx.intelligent.ui.activitise.loginAndRegister.LoginActivity;
import com.jx.intelligent.util.ScreenSizeUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.BannerView;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/16 0016.
 * 服务详情页
 */

public class ServiceDetailActivity extends RHBaseActivity{

    private BannerView my_banner;
    private RelativeLayout layout_img, layout_report, layot_location;
    private ImageView img_call, img_msm;
    private TextView txt_name, txt_location, txt_date, txt_content, txt_shopper, txt_visit, txt_consultation, txt_distance;
    private TitleBarHelper titleBarHelper;
    private NormalAlertDialog msgDialog;
    private String pubId, title;
    private double sLongitude, sLatitude, eLongitude, eLatitude;
    private CommunityService dao;
    private ServiceDetailInfoResult.Data data;
    private ProgressWheelDialog progressWheelDialog;
    private DBManager dbManager;
    private SelectNaviPopWindow menuWindow;
    private int pub_id;

    @Override
    protected void init() {
        if(getIntent() != null)
        {
            pubId = getIntent().getStringExtra("pubId");
            title = getIntent().getStringExtra("title");
            pub_id = getIntent().getIntExtra("Pub_id",0);

            if(pubId==null) {
                pubId=pub_id+"";
            }

        }
        dbManager = new DBManager(ServiceDetailActivity.this);
        dbManager.copyDBFile();
        titleBarHelper.setMiddleTitleText("详情");
        dao = new CommunityService();
        if(RHBaseApplication.getInstance().getLoc() != null)
        {
            getServiceDetailInfo(RHBaseApplication.getInstance().getLoc().getLongitude()+"", RHBaseApplication.getInstance().getLoc().getLatitude()+"");
            sLatitude = RHBaseApplication.getInstance().getLoc().getLatitude();
            sLongitude = RHBaseApplication.getInstance().getLoc().getLongitude();
        }
        else
        {
            getServiceDetailInfo("0", "0");
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_service_detail;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(ServiceDetailActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void findView(View contentView) {
        my_banner = (BannerView) contentView.findViewById(R.id.my_banner);
        layout_img = (RelativeLayout)contentView.findViewById(R.id.layout_img);
        layot_location = (RelativeLayout)contentView.findViewById(R.id.layot_location);
        img_call = (ImageView) contentView.findViewById(R.id.img_call);
        img_msm = (ImageView) contentView.findViewById(R.id.img_msn);
        txt_name = (TextView)contentView.findViewById(R.id.txt_name);
        txt_location = (TextView)contentView.findViewById(R.id.txt_location);
        txt_date = (TextView)contentView.findViewById(R.id.txt_date);
        txt_content = (TextView)contentView.findViewById(R.id.txt_content);
        txt_shopper = (TextView)contentView.findViewById(R.id.tx_shopper);
        layout_report = (RelativeLayout) contentView.findViewById(R.id.layout_report);
        txt_visit = (TextView)contentView.findViewById(R.id.txt_visit);
        txt_consultation = (TextView)contentView.findViewById(R.id.txt_consultation);
        txt_distance = (TextView)contentView.findViewById(R.id.txt_distance);
        msgDialog = new NormalAlertDialog(new NormalAlertDialog.Builder(ServiceDetailActivity.this));
        progressWheelDialog = new ProgressWheelDialog(ServiceDetailActivity.this);

        img_call.setOnClickListener(this);
        img_msm.setOnClickListener(this);
        layout_report.setOnClickListener(this);
        layot_location.setOnClickListener(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenSizeUtil.getInstance(ServiceDetailActivity.this).getScreenWidth(),ScreenSizeUtil.getInstance(ServiceDetailActivity.this).getScreenWidth());
        layout_img.setLayoutParams(params);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.img_call:
                if(data != null && !StringUtil.isEmpty(data.getPhoneNum()))
                {
                    showDialog(2, "是否拨号？", data.getPhoneNum(), "取消", "拨号");
                    msgDialog.show();
                }
                break;
            case R.id.img_msn:
                if(data != null && !StringUtil.isEmpty(data.getPhoneNum()))
                {
                    showDialog(1, "是否发送短信？", data.getPhoneNum(), "取消", "发送");
                    msgDialog.show();
                }
                break;
            case R.id.layout_report:
                if(Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
                {
                    Intent intent;
                    if(SesSharedReferences.getUsrLoginState(ServiceDetailActivity.this))
                    {
                        intent = new Intent(ServiceDetailActivity.this, ServiceReportActivity.class);
                        intent.putExtra("pubid", pubId);
                        intent.putExtra("content", data.getContent());
                        startActivityForResult(intent, Constant.REQUEST_CODE);
                    }
                    else
                    {
                        intent = new Intent(ServiceDetailActivity.this, LoginActivity.class);
                        intent.putExtra("flag", Constant.LOGIN_FLAG);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.layot_location:
                menuWindow = new SelectNaviPopWindow(ServiceDetailActivity.this, new NaviLatLng(sLatitude, sLongitude), new NaviLatLng(eLatitude, eLongitude));
                menuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                menuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                //显示窗口
                //设置layout在PopupWindow中显示的位置
                menuWindow.showAtLocation(ServiceDetailActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }

    /**
     *
     * @param type 1:发短信 2:打电话 3:举报
     * @param title
     * @param titleCancle
     * @param titleOK
     */
    private void showDialog(final int type, String title, String content, String titleCancle, String titleOK)
    {
        msgDialog = new NormalAlertDialog.Builder(ServiceDetailActivity.this)
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
                        if(type == 1)
                        {
                            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+data.getPhoneNum()));
                            intent.putExtra("sms_body", "您好，我对您在净喜智能发布的“"+data.getName()+"”很感兴趣，想和您详细了解一下。");
                            startActivityForResult(intent, Constant.MSM);
                            msgDialog.dismiss();
                        }
                        else if(type == 2)
                        {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+data.getPhoneNum()));
                            startActivityForResult(intent, Constant.CALL);
                            msgDialog.dismiss();
                        }
                    }
                })
                .build();
    }

    //服务咨询
    void servceInquiries()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        progressWheelDialog.show();
        dao.serviceInquiries(pubId, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                txt_consultation.setText("咨询"+(Integer.parseInt( data.getInquiries())+1)+"次");
                progressWheelDialog.dismiss();
            }

            @Override
            public void resFailure(String message) {
                ToastUtil.showToast(message);
                progressWheelDialog.dismiss();
            }
        });
    }

    void getServiceDetailInfo(String longitude, String latitude)
    {
        getPreCache(pubId, longitude, latitude);
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        progressWheelDialog.show();
        dao.getServiceDetailTask(pubId, longitude, latitude, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                progressWheelDialog.dismiss();
                ServiceDetailInfoResult serviceDetailInfoResult = (ServiceDetailInfoResult)object;
                showDataView(serviceDetailInfoResult);
            }

            @Override
            public void resFailure(String message) {
                ToastUtil.showToast(message);
                progressWheelDialog.dismiss();
            }
        });
    }

    /**
     * 先获取缓存数据
     */
    public void getPreCache(String pubId, String userlong, String userlat )
    {
        DBManager dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();

        Map<String, String> map = new HashMap<String, String>();
        map.put("pubId", pubId);
        map.put("userlong", userlong);
        map.put("userlat", userlat);

        String js = dbManager.getUrlJsonData(Constant.HOME_SERVICE_DETAIL_URL + StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            ServiceDetailInfoResult serviceDetailInfoResult = new Gson().fromJson(js, ServiceDetailInfoResult.class);
            showDataView(serviceDetailInfoResult);
        }
    }

    /**
     * 显示数据
     * @param serviceDetailInfoResult
     */
    void showDataView(ServiceDetailInfoResult serviceDetailInfoResult)
    {
        if(serviceDetailInfoResult != null && serviceDetailInfoResult.getData().size() > 0)
        {
            data = serviceDetailInfoResult.getData().get(0);
            showInfo();
        }
        else
        {
            ToastUtil.showToast("获取服务详情失败");
        }
    }

    void showInfo()
    {
        txt_shopper.setText(data.getPubName());
        txt_visit.setText("浏览量"+data.getTraffic()+"次");
        txt_consultation.setText("咨询"+data.getInquiries()+"次");
        try
        {
            if(Integer.parseInt(data.getDistance()) < 1000)
            {
                txt_distance.setText(data.getDistance()+" m");
            }
            else
            {
                txt_distance.setText(Integer.parseInt(data.getDistance())/1000+" km");
            }
        }
        catch (Exception e)
        {

        }
        txt_name.setText(data.getName().trim());
        txt_location.setText(data.getAddress());
        txt_date.setText(data.getVaildtime()+" - "+data.getInvildtime());
        txt_content.setText(data.getContent());
        showAdDataView(data.getUrl().split(","));
        if(StringUtil.isEmpty(data.getMerchantlong()))
        {
            eLongitude = 0;
        }
        else
        {
            eLongitude = Double.parseDouble(data.getMerchantlong());
        }

        if(StringUtil.isEmpty(data.getMerchantlat()))
        {
            eLatitude = 0;
        }
        else
        {
            eLatitude = Double.parseDouble(data.getMerchantlat());
        }

//        getLatlon(data.getAddress().replaceAll(" ","-"));
    }

    /**
     * 显示广告数据
     */
    void showAdDataView(String[] img_url)
    {
        if(img_url != null && img_url.length > 0)
        {
            my_banner.setImagesRes(new int[]{});
            my_banner.removeAllViewsInLayout();
            List<String> imgUrlLis = new ArrayList<String>();
            for (int a = 0; a < img_url.length; a++) {
                imgUrlLis.add(img_url[a]);
            }
            my_banner.setImagesUrl(imgUrlLis);
            my_banner.startAutoScroll();
        }
        else
        {
            my_banner.removeAllViewsInLayout();
            my_banner.setImagesRes(new int[]{R.mipmap.img_empty_banner});
        }

        my_banner.setViewSize(ScreenSizeUtil.getInstance(ServiceDetailActivity.this).getScreenWidth(), ScreenSizeUtil.getInstance(ServiceDetailActivity.this).getScreenWidth());
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, final Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if(requestcode == Constant.CALL || requestcode == Constant.MSM)
        {
            servceInquiries();
        }
    }


}

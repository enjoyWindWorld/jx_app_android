package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.ServicePriceResult;
import com.jx.intelligent.ui.activitise.payment.SetReNewInfoActivity;
import com.jx.intelligent.util.DateUtil;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/15 0015.
 * 服务费
 */

public class ServicePriceActivity extends RHBaseActivity{

    TextView txt_name, txt_phone, txt_code, txt_type, txt_star_time, txt_end_time, txt_flow, txt_date;
    LinearLayout layout_flow, layout_end_time;
    Button btn_pay;
    TitleBarHelper titleBarHelper;
    private ProgressWheelDialog dialog;
    UserCenter dao;
    private String ord_no, payType, productId;
    ServicePriceResult servicePriceResult;
    private String proNo, type;
    private DBManager dbManager;
    private TextView txt_order;

    @Override
    protected void init() {
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        if(getIntent() != null)
        {
            proNo = getIntent().getStringExtra("pro_no");
            type = getIntent().getStringExtra("type");
        }
        if(!StringUtil.isEmpty(proNo))
        {
            dao = new UserCenter();
            getDetailInfo();
        }

        //别人的机器
        if("1".equals(type))
        {
            btn_pay.setVisibility(View.GONE);
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_service_price;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(ServicePriceActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        txt_date = (TextView)contentView.findViewById(R.id.txt_date);
        txt_name = (TextView)contentView.findViewById(R.id.txt_name);
        txt_phone = (TextView)contentView.findViewById(R.id.txt_phone);
        txt_code = (TextView)contentView.findViewById(R.id.txt_code);
        txt_type = (TextView)contentView.findViewById(R.id.txt_type);
        //王云修改 订单编号
        txt_order = (TextView) contentView.findViewById(R.id.txt_order);
        txt_star_time = (TextView)contentView.findViewById(R.id.txt_star_time);
        txt_end_time = (TextView)contentView.findViewById(R.id.txt_end_time);
        txt_flow = (TextView)contentView.findViewById(R.id.txt_flow);
        layout_flow = (LinearLayout) contentView.findViewById(R.id.layout_flow);
        layout_end_time = (LinearLayout) contentView.findViewById(R.id.layout_end_time);
        btn_pay = (Button) contentView.findViewById(R.id.btn_pay);
        dialog = new ProgressWheelDialog(ServicePriceActivity.this);
        btn_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.btn_pay:
                if(Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) && servicePriceResult != null && servicePriceResult.getData().size()>0)
                {
                    Intent intent = new Intent(ServicePriceActivity.this, SetReNewInfoActivity.class);
                    intent.putExtra("ord_no", ord_no);
                    intent.putExtra("payType", payType);
                    intent.putExtra("productId", productId);
                    startActivity(intent);
                }
                break;
        }
    }

    void getDetailInfo()
    {
        getPreData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        dao.getMyWaterPurifierServiceDetailTask(proNo, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                servicePriceResult = (ServicePriceResult)object;
                showDetailData();
            }

            @Override
            public void resFailure(String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 获取缓存下来的服务续费详情的数据
     */
    void getPreData()
    {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("pro_no", proNo);
        String js = dbManager.getUrlJsonData(Constant.USER_GET_WP_SERVICE_DETAIL_INFO + StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            servicePriceResult = new Gson().fromJson(js, ServicePriceResult.class);
            showDetailData();
        }
    }

    /**
     * 显示服务续费详情的数据
     */
    void showDetailData()
    {
        if(servicePriceResult != null && servicePriceResult.getData().size()>0)
        {
            showInfo(servicePriceResult.getData().get(0));
        }
        else
        {
            ToastUtil.showToast("获取净水机服务信息失败");
        }
    }


    void showInfo(ServicePriceResult.Data data)
    {
        ord_no = data.getOrd_no();
        payType = data.getType();
        productId = data.getProductId();
        txt_date.setText(DateUtil.getTodayDate());
        txt_name.setText(data.getName());
        txt_phone.setText(data.getPhone());
        //订单编号
        txt_order.setText(data.getOrd_no());
        txt_code.setText(data.getPro_no());
        txt_star_time.setText(data.getPro_addtime());
        txt_type.setText(data.getPro_addtime()
                +data.getPro_name()
                +"（"+data.getOrd_color()+"）"
                +(data.getType().equals("0")?"包年费用":"流量预支")
                +data.getOrd_price()
                +"元");
        titleBarHelper.setMiddleTitleText(data.getType().equals("0")?"包年服务费用":"流量服务费用");
        if(data.getType().equals("0"))
        {
            layout_end_time.setVisibility(View.VISIBLE);
            layout_flow.setVisibility(View.GONE);
            txt_end_time.setText(data.getPro_invalidtime());
        }
        else
        {
            layout_end_time.setVisibility(View.GONE);
            layout_flow.setVisibility(View.VISIBLE);
            txt_flow.setText(data.getPro_hasflow()+"升");
        }
    }
}

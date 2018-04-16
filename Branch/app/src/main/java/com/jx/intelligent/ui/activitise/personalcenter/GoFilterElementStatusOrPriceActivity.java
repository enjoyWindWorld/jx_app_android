package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.result.GetMyWPListResult;

/**
 * Created by Administrator on 2016/12/15 0015.
 * 点击净水器进入的子界面
 */

public class GoFilterElementStatusOrPriceActivity extends RHBaseActivity {

    RelativeLayout layout_status, layout_price;
    TitleBarHelper titleBarHelper;
    GetMyWPListResult.WaterPurifierBean waterPurifierBean;
    TextView txt_type;
    String type;

    @Override
    protected void init() {
        if(getIntent() != null)
        {
            type = getIntent().getStringExtra("type");
            waterPurifierBean = (GetMyWPListResult.WaterPurifierBean)getIntent().getSerializableExtra("obj");
            if(waterPurifierBean != null)
            {
                titleBarHelper.setMiddleTitleText(waterPurifierBean.getName()+"（"+waterPurifierBean.getColor()+"）");
                if(waterPurifierBean.getOrd_protypeid().equals("0"))
                {
                    txt_type.setText("包年费用余量查询");
                }
                else
                {
                    txt_type.setText("流量费用余量查询");
                }
            }
        }
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_status_price;
    }

    @Override
    protected void initTitle() {
        titleBarHelper = new TitleBarHelper(GoFilterElementStatusOrPriceActivity.this);
        titleBarHelper.setLeftImageRes(R.drawable.selector_back);
        titleBarHelper.setLeftClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.layout_status:
                goFilterElementStatus();
                break;
            case R.id.layout_price:
                goServicePrice();
                break;
        }
    }

    @Override
    protected void findView(View contentView) {
        layout_status = (RelativeLayout)contentView.findViewById(R.id.layout_status);
        layout_price = (RelativeLayout)contentView.findViewById(R.id.layout_price);
        txt_type = (TextView) contentView.findViewById(R.id.txt_type);

        layout_status.setOnClickListener(this);
        layout_price.setOnClickListener(this);
    }

    private void goFilterElementStatus() {
        Intent intent = new Intent(GoFilterElementStatusOrPriceActivity.this, FilterElementStatusActivity.class);
        intent.putExtra("pro_id", waterPurifierBean.getPro_no());
        startActivity(intent);
    }

    private void goServicePrice() {
        Intent intent = new Intent(GoFilterElementStatusOrPriceActivity.this, ServicePriceActivity.class);
        intent.putExtra("pro_no", waterPurifierBean.getPro_no());
        intent.putExtra("type", type);
        startActivity(intent);
    }
}

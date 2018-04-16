package com.jx.intelligent.ui.activitise.service;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.view.CitySelectPopWindow;
import com.jx.intelligent.util.KeyboardUtil;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 添加服务地址界面
 */
public class ServiceAddAddressActivity extends RHBaseActivity {

    private CitySelectPopWindow menuWindow;
    private RelativeLayout layout_pcd;
    private EditText edit_addr;
    private Button btn_ok;
    private TextView txt_pcd;
    private String select, sheng = "", shi = "", qu = "";

    @Override
    protected void init() {

    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_service_add_addr;
    }

    @Override
    protected void initTitle() {
        new TitleBarHelper(ServiceAddAddressActivity.this).setMiddleTitleText(R.string.service_release_addr_title).setLeftImageRes(R.drawable.selector_back).setLeftClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        layout_pcd = (RelativeLayout) contentView.findViewById(R.id.layout_pcd);
        edit_addr = (EditText) contentView.findViewById(R.id.edit_addr);
        btn_ok = (Button) contentView.findViewById(R.id.btn_ok);
        txt_pcd = (TextView) contentView.findViewById(R.id.txt_pcd);

        hideSoftKeyboard(edit_addr);

        layout_pcd.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.layout_pcd:
                menuWindow = new CitySelectPopWindow(ServiceAddAddressActivity.this, itemsOnClick);
                menuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                menuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                //显示窗口
                //设置layout在PopupWindow中显示的位置
                menuWindow.showAtLocation(ServiceAddAddressActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                KeyboardUtil.hideSoftKeyboard(txt_pcd);
                break;
            case R.id.btn_ok:
                if (StringUtil.isEmpty(select)) {
                    ToastUtil.showToast(R.string.service_release_service_pcd);
                } else if (StringUtil.isEmpty(edit_addr.getText().toString())) {
                    ToastUtil.showToast(R.string.service_release_service_detail_addr);
                } else if(Utils.filterEmoji(edit_addr.getText().toString())) {
                    ToastUtil.showToast("请勿输入表情符号");
                } else {
                    String addr = sheng+shi+qu+edit_addr.getText().toString();
                    getLatlon(addr.replaceAll(" ", "-"));//有空格，定位不到的
                }
                break;
            case R.id.titlebar_left_rl:
                finish();
                break;
        }
    }

    //为弹出窗口实现监听类
    public View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.grxx_xbxz_qd:
                    List<Map<String, String>> datas = menuWindow.getPpWindowSelect();
                    select = datas.get(1).get("addrStr");
                    txt_pcd.setText(select);
                    txt_pcd.setTextColor(getResources().getColor(R.color.color_333333));
                    sheng = menuWindow.getSheng();
                    shi = menuWindow.getShi();
                    if(shi.equals("市辖区") || shi.equals("县"))
                    {
                        shi = "";
                    }
                    qu = menuWindow.getQu();
                    break;
            }
        }
    };

    /**
     * 响应地理编码
     */
    public void getLatlon(String strAddress) {
        Geocoder geocoder = new Geocoder(ServiceAddAddressActivity.this, Locale.getDefault());
        List<Address> geoResults = null;
        try {
            geoResults = geocoder.getFromLocationName(strAddress, 1);
            while (geoResults.size()==0) {
                geoResults = geocoder.getFromLocationName(strAddress, 1);
            }
            if (geoResults.size()>0) {
                Address addr = geoResults.get(0);
                System.out.println("经纬度："+addr.getLongitude()+"；"+addr.getLatitude());
                Intent intent = new Intent();
                intent.putExtra("sheng", sheng);
                intent.putExtra("shi", shi);
                intent.putExtra("qu", qu);
                intent.putExtra("detailAddr", edit_addr.getText().toString());
                intent.putExtra("longitude", addr.getLongitude()+"");
                intent.putExtra("latitude", addr.getLatitude()+"");
                setResult(Constant.GET_HOME_ADDR_OK, intent);
                finish();
            }
        } catch (Exception e) {
            System.out.println("定位异常："+e.getMessage());
        }
    }
}

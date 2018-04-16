package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseActivity;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.ui.activitise.payment.WyPaymentDetailActivity;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.adapter.jxAdapter.HomeAddrAdapter;
import com.jx.intelligent.result.GetAddrResult;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 家庭地址
 * Created by Administrator on 2016/11/15 0015.
 */
public class HomeAddrActivity extends RHBaseActivity {

    private static final String TAG = "HomeAddrActivity";
    ImageView titlebar_left_vertical_iv;
    ListView grxx_jtdz_lb;
    HomeAddrAdapter homeAddrAdapter;
    private List<GetAddrResult.HomeAddrBean> data;
    private int flag = -1;//大于0代表填写支付信息过来要家庭住址的
    GetAddrResult getAddrResult;
    UserCenter userCenter;
    private ProgressWheelDialog dialog;
    private NormalAlertDialog normalAlertDialog;
    private DBManager dbManager;
    int my_id, my_item;

    @Override
    protected void init() {

        if (getIntent() != null) {
            flag = getIntent().getIntExtra("flag", -1);
        }
        grxx_jtdz_lb.setDividerHeight(0);
        data = new ArrayList<>();

        homeAddrAdapter = new HomeAddrAdapter(this, data);
        grxx_jtdz_lb.setAdapter(homeAddrAdapter);
        grxx_jtdz_lb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (flag > 0) {
                    Intent intent = new Intent(HomeAddrActivity.this, WyPaymentDetailActivity.class);
                    intent.putExtra("obj", getAddrResult.getData().get(i));
                    setResult(Constant.GET_HOME_ADDR_OK, intent);
                    finish();
                }
            }
        });

        homeAddrAdapter.setItemSzmrdzOnclickListen(new HomeAddrAdapter.myOnClickListen() {
            @Override
            public void onItemClick(int item) {
                GetAddrResult.HomeAddrBean homeAddrBean = getAddrResult.getData().get(item);
                updateDefaultHomeAddr(homeAddrBean);
            }
        });
        homeAddrAdapter.setItemScOnclickListen(new HomeAddrAdapter.myOnClickListen() {
            @Override
            public void onItemClick(int item) {
                my_id = getAddrResult.getData().get(item).getId();
                my_item = item;
                normalAlertDialog.show();
            }
        });
        homeAddrAdapter.setItemXgOnclickListen(new HomeAddrAdapter.myOnClickListen() {
            @Override
            public void onItemClick(int item) {
                GetAddrResult.HomeAddrBean homeAddrBean = getAddrResult.getData().get(item);
                 goSetRecAddrForUpdate(homeAddrBean);
            }
        });

        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        userCenter = new UserCenter();
        getHomeAddrList();
        initDialog();
    }

    @Override
    protected int setContentLayout() {
        return R.layout.activity_home_addr;
    }

    @Override
    protected void initTitle() {

        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("家庭住址")
                .setRightText("新增")
                .setLeftClickListener(this).
                setRightClickListener(this);
    }

    @Override
    protected void findView(View contentView) {
        grxx_jtdz_lb = (ListView) contentView.findViewById(R.id.grxx_jtdz_lb);
        dialog = new ProgressWheelDialog(HomeAddrActivity.this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle bundle = intent.getBundleExtra("getAddr");
        if (bundle != null) {
            getHomeAddrList();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                if (flag > 0)
                {
                    if(getAddrResult != null && getAddrResult.getData().size() > 0)
                    {
                        finish();
                    }
                    else
                    {
                        Intent intent = new Intent(HomeAddrActivity.this, WyPaymentDetailActivity.class);
                        setResult(Constant.ALL_HOME_ADDR_DELETE, intent);
                    }
                }
                finish();
                break;
            case R.id.titlebar_right_rl:
                goSetRecAddrForAdd();
                break;
        }
    }

    /**
     * 跳转修改用户地址界面
     */
    private void goSetRecAddrForUpdate(GetAddrResult.HomeAddrBean homeAddrBean) {
        Intent intent = new Intent(HomeAddrActivity.this, SetRecAddrActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", homeAddrBean.getId() + "");
        bundle.putString("name", homeAddrBean.getName());
        bundle.putString("phone", homeAddrBean.getPhone());
        bundle.putString("area", homeAddrBean.getArea());
        bundle.putString("detail", homeAddrBean.getDetail());
        bundle.putString("code", homeAddrBean.getCode());
        bundle.putString("isdefault", homeAddrBean.getIsdefault());
        intent.putExtra("update", bundle);
        startActivityForResult(intent, 100);
    }

    /**
     * 跳转到添加用户地址界面
     */

    private void goSetRecAddrForAdd() {
        Intent intent = new Intent(HomeAddrActivity.this, SetRecAddrActivity.class);
        intent.putExtra("add", new Bundle());
        startActivityForResult(intent, 100);
    }


    void getHomeAddrList() {
        getPreHomwAddressData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            if(getAddrResult == null || getAddrResult.getData().size()<=0)
            {
                mDiffViewHelper.showEmptyView();
            }
            else
            {
                mDiffViewHelper.showDataView();
            }
            return;
        }

        if(!dialog.isShowing())
        {
            dialog.show();
        }
        userCenter.getHomeAddrListTask(SesSharedReferences.getUserId(HomeAddrActivity.this), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                getAddrResult = (GetAddrResult) object;
                showHomeAddressData();
            }

            @Override
            public void resFailure(String message) {
                if(getAddrResult == null || getAddrResult.getData().size()<=0)
                {
                    mDiffViewHelper.showEmptyView();
                }
                else
                {
                    mDiffViewHelper.showDataView();
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * 获取缓存下来的收货地址数据
     */
    void getPreHomwAddressData()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", SesSharedReferences.getUserId(HomeAddrActivity.this));

        String js = dbManager.getUrlJsonData(Constant.USER_GET_ADDR_LIST_WP + StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            getAddrResult = new Gson().fromJson(js, GetAddrResult.class);
            showHomeAddressData();
        }
    }

    /**
     * 显示收货地址数据
     */
    void showHomeAddressData()
    {
        if(getAddrResult == null || getAddrResult.getData().size()<=0)
        {
            mDiffViewHelper.showEmptyView();
        }
        else
        {
            mDiffViewHelper.showDataView();
            homeAddrAdapter.setData(getAddrResult.getData());
            homeAddrAdapter.notifyDataSetChanged();
        }
    }

    void updateDefaultHomeAddr(GetAddrResult.HomeAddrBean homeAddrBean) {

        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", homeAddrBean.getId()+"");
        map.put("userid", SesSharedReferences.getUserId(HomeAddrActivity.this));
        map.put("name", homeAddrBean.getName());
        map.put("phone", homeAddrBean.getPhone());
        map.put("area", homeAddrBean.getArea());
        map.put("detail", homeAddrBean.getDetail());
        map.put("code", homeAddrBean.getCode());
        map.put("isdefault", homeAddrBean.isdefault.equals("0") ? "1" : "0");

        userCenter.addHomeAddrTask(map, new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                ToastUtil.showToast("设置默认地址成功");
                getAddrResult.getData().clear();
                getHomeAddrList();
            }

            @Override
            public void resFailure(String message) {
                ToastUtil.showToast(message);
                dialog.dismiss();
            }
        });
    }

    /**
     * 删除地址
     */
    void commitDelAddr() {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            if(getAddrResult == null || getAddrResult.getData().size() <= 0)
            {
                mDiffViewHelper.showEmptyView();
            }
            else
            {
                mDiffViewHelper.showDataView();
            }
            return;
        }

        dialog.show();
        userCenter.commitDelAddrTask(my_id + "", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                getAddrResult.getData().remove(my_item);
                if(getAddrResult == null || getAddrResult.getData().size() <= 0)
                {
                    mDiffViewHelper.showEmptyView();
                }
                else
                {
                    mDiffViewHelper.showDataView();
                    homeAddrAdapter.setData(getAddrResult.getData());
                    homeAddrAdapter.notifyDataSetChanged();
                }
                ToastUtil.showToast("删除地址成功");
                dialog.dismiss();
            }

            @Override
            public void resFailure(String message) {
                ToastUtil.showToast(message);
                if(getAddrResult == null || getAddrResult.getData().size() <= 0)
                {
                    mDiffViewHelper.showEmptyView();
                }
                else
                {
                    mDiffViewHelper.showDataView();
                }
                dialog.dismiss();
            }
        });
    }

    private void initDialog()
    {
        normalAlertDialog = new NormalAlertDialog.Builder(HomeAddrActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("是否要删除家庭住址")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("")
                .setContentTextColor(R.color.color_000000)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.color_000000)
                .setRightButtonText("确定")
                .setRightButtonTextColor(R.color.color_000000)
                .setCancelable(true)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        normalAlertDialog.dismiss();
                    }
                    @Override
                    public void clickRightButton(View view) {
                        normalAlertDialog.dismiss();
                        commitDelAddr();
                    }
                })
                .build();
    }

    @Override
    public void emptyRetryRefreshListener() {
        super.emptyRetryRefreshListener();
        getHomeAddrList();
    }

    @Override
    public void onBackPressed() {
        if (flag > 0)
        {
            if(getAddrResult != null && getAddrResult.getData().size() > 0)
            {
                finish();
            }
            else
            {
                Intent intent = new Intent(HomeAddrActivity.this, WyPaymentDetailActivity.class);
                setResult(Constant.ALL_HOME_ADDR_DELETE, intent);
            }
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.ADD_UPDATE_HOME_ADDR)
        {
            getHomeAddrList();
        }
    }
}

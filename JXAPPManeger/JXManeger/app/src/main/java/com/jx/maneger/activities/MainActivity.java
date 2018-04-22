package com.jx.maneger.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jx.maneger.R;
import com.jx.maneger.activities.CustomerService.CustomerServiceManageActivity;
import com.jx.maneger.activities.Withdrawals.MyInComeActivity;
import com.jx.maneger.activities.loginAndRegister.LoginActivity;
import com.jx.maneger.adapter.RecycleAdapter;
import com.jx.maneger.adapter.RecycleCommonAdapter;
import com.jx.maneger.adapter.holder.RecycleCommonViewHolder;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.LoginAndRegister;
import com.jx.maneger.dao.MessageDao;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.intf.OnItemClickListener;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.HomeTextResult;
import com.jx.maneger.results.IndexOptionsBean;
import com.jx.maneger.results.LoginResult;
import com.jx.maneger.results.MessageNoReadResult;
import com.jx.maneger.runtimepermission.PermissionsChecker;
import com.jx.maneger.update.UpdateAgent;
import com.jx.maneger.util.LogUtil;
import com.jx.maneger.util.SesSharedReferences;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.UIUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.SpaceItemDecoration;
import com.jx.maneger.view.dialog.ProgressWheelDialog;
import com.jx.maneger.view.wheelCityView.AutoVerticalScrollTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecycleCommonAdapter mAdapter;
    private RecyclerView.LayoutManager mManager;
    private List<IndexOptionsBean> mList;
    private TextView tv_name, tv_code, tv_level, tv_up_name, tv_up_code, tv_remind;
    private LoginAndRegister dao;
    private MessageDao messageDao;
    private DBManager dbManager;
    private ProgressWheelDialog dialog;
    private boolean isBundAlipay;
    private UpdateAgent mUpdateAgent;
    private UpdateAgent.CheckUpdateListener mCheckUpdateListener;
    private AutoVerticalScrollTextView mTv_scrallow;
    private String news_content;
    private String news_type_name;
    private String news_url;
    private boolean isRunning=true;
    private ArrayList<HomeTextResult.DataBean> textDatas = new ArrayList<>();
    private HomeTextResult TextResut;
    private List<HomeTextResult.DataBean> mScrollTextDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_level = (TextView) findViewById(R.id.tv_level);
        tv_up_name = (TextView) findViewById(R.id.tv_up_name);
        tv_up_code = (TextView) findViewById(R.id.tv_up_code);
        tv_remind = (TextView) findViewById(R.id.tv_remind);
        mTv_scrallow = (AutoVerticalScrollTextView) findViewById(R.id.tv_shuizhi);

        //下拉刷新
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        //渐变颜色
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.color_15aa5a), UIUtil.getColor(R.color.color_ff0000));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rcy);
        mManager = new GridLayoutManager(MainActivity.this, 3);
        mRecyclerView.setLayoutManager(mManager);
        //设置条目间隔
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
        dialog = new ProgressWheelDialog(MainActivity.this);

        initOptionsData();
        showOptions();
        dao = new LoginAndRegister();
        messageDao = new MessageDao();
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        getPartnerInfo();
        getAdv();
        initUpdate();

        new Thread(){
            @Override
            public void run() {
                while (isRunning){
                    if(textDatas != null && textDatas.size() > 0)
                    {
                        for(int i=0;i<textDatas.size();i++){
                            if(textDatas != null && textDatas.size() > 0)
                            {
                                news_content = textDatas.get(i).getNews_content();
                                news_type_name = textDatas.get(i).getNews_type_name();
                                news_url = textDatas.get(i).getNews_url();
                            }
                            SystemClock.sleep(3000);
                            handler.sendEmptyMessage(199);
                        }
                    }
                }
            }
        }.start();

        mChecker = new PermissionsChecker(this);
        isRequireCheck = true;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 199) {
                mTv_scrallow.next();
                //用Spannable 来实现 一个TextView中显示不同颜色的字体
                String text=news_type_name+"     "+news_content;
                Spannable span = new SpannableString(text);
                span.setSpan(new ForegroundColorSpan(Color.parseColor("#F49F28")), 0,7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                span.setSpan(new ForegroundColorSpan(Color.parseColor("#666666")), 7, text.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTv_scrallow.setText(span);
                mTv_scrallow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转浏览器
                        goWebJX(news_url);
                    }
                });
            }
        }
    };

    /**
     * 跳转到浏览器界面
     */
    private void goWebJX(String url) {
        if(!StringUtil.isEmpty(url))
        {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }
    }

    private void initUpdate() {
        mUpdateAgent = UpdateAgent.getInstance(this);
        mCheckUpdateListener = new CheckUpdateListener();
        mUpdateAgent.setCheckUpdateListener(mCheckUpdateListener);
        //网络可用时调用
        mUpdateAgent.update();
    }

    /**
     * 检查更新接口
     */
    private class CheckUpdateListener implements UpdateAgent.CheckUpdateListener {
        @Override
        public void onUpdateReturned(boolean isCanUpdate, HashMap<String, String> responseInfos) {
            LogUtil.e("responseInfos:" + responseInfos);
            if (isCanUpdate) {
                if (responseInfos != null && responseInfos.size() > 0) {
                    //有新版本
                    mUpdateAgent.showUpdateDialog(MainActivity.this, responseInfos);
                }
            }
        }
    }

    private void initOptionsData()
    {
        mList = new ArrayList<IndexOptionsBean>();
        IndexOptionsBean indexOptionsBean = new IndexOptionsBean();
        indexOptionsBean.setId(0);
        indexOptionsBean.setTitle("订单管理");
        indexOptionsBean.setResourceId(R.mipmap.icon_myorder);
        mList.add(indexOptionsBean);

        indexOptionsBean = new IndexOptionsBean();
        indexOptionsBean.setId(1);
        indexOptionsBean.setTitle("我的收入");
        indexOptionsBean.setResourceId(R.mipmap.icon_myincome);
        mList.add(indexOptionsBean);

        indexOptionsBean = new IndexOptionsBean();
        indexOptionsBean.setId(2);
        indexOptionsBean.setTitle("我的e家");
        indexOptionsBean.setResourceId(R.mipmap.icon_subordinate);
        mList.add(indexOptionsBean);

//        indexOptionsBean = new IndexOptionsBean();
//        indexOptionsBean.setId(3);
//        indexOptionsBean.setTitle("售后管理");
//        indexOptionsBean.setResourceId(R.mipmap.repair);
//        mList.add(indexOptionsBean);

        indexOptionsBean = new IndexOptionsBean();
        indexOptionsBean.setId(3);
        indexOptionsBean.setTitle("我的消息");
        indexOptionsBean.setResourceId(R.mipmap.icon_message);
        mList.add(indexOptionsBean);

        indexOptionsBean = new IndexOptionsBean();
        indexOptionsBean.setId(4);
        indexOptionsBean.setTitle("设置");
        indexOptionsBean.setResourceId(R.mipmap.icon_setting);
        mList.add(indexOptionsBean);
    }

    private void showUserInfo(LoginResult loginResult)
    {
        if(loginResult != null && loginResult.getData().size() > 0)
        {
            LoginResult.Data data = loginResult.getData().get(0);
            tv_name.setText(data.getUsername());
            tv_code.setText(data.getPartnerNumber());
            switch (data.getLevel())
            {
                case "1"://省
                    tv_level.setText("运营商");
                    break;
                case "2"://市
                    tv_level.setText("创客");
                    break;
//                case "3"://区
//                    tv_level.setText("区级");
//                    break;
//                case "4"://产品经理
//                    tv_level.setText("产品经理");
//                    break;
//                case "-1"://区县代
//                    tv_level.setText("区县代");
//                    break;
//                case "-2"://体验店
//                    tv_level.setText("体验店");
//                    break;
//                case "-3"://合伙人
//                    tv_level.setText("合伙人");
//                    break;
                default:
                    tv_level.setText("创客");
                    break;
            }
            tv_up_name.setText(data.getParParentName());
            tv_up_code.setText(data.getParParentid());

            if(data.getOriginalpassword().equals("1"))
            {
                tv_remind.setText("当前密码为初始密码,建议及时修改密码！");
                tv_remind.setVisibility(View.VISIBLE);
            }
            else if(data.getUnboundedalipay().equals("1"))
            {
                tv_remind.setText("支付宝当前没有绑定,建议及时绑定！");
                tv_remind.setVisibility(View.VISIBLE);
            }
            else
            {
                tv_remind.setVisibility(View.GONE);
            }

            if(data.getUnboundedalipay().equals("1"))
            {
                isBundAlipay = false;
            }
            else
            {
                isBundAlipay = true;
            }
        }
    }

    private void showOptions() {

        mAdapter = new RecycleAdapter<IndexOptionsBean>(UIUtil.getContext(), mList, true, R.layout.layout_home_service_item, null) {
            @Override
            public void convert(RecycleCommonViewHolder holder, IndexOptionsBean data) {
                holder.setImageById(R.id.my_img, data.getResourceId(), R.id.loading_anim_iv, R.mipmap.img_service_type_empty);
                holder.setText(R.id.my_txt, data.getTitle());
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if(position  == 0)
                        {
                            Intent intent = new Intent(MainActivity.this, MyOrderActivity.class);
                            startActivityForResult(intent, Constant.REQUEST_MY_ORDER);
                        }
                        else if(position == 1)
                        {
                            Intent intent = new Intent(MainActivity.this, MyInComeActivity.class);
                            intent.putExtra("isBundAlipay", isBundAlipay);
                            startActivityForResult(intent, Constant.REQUEST_MY_INCOME);
                        }
                        else if(position == 2)
                        {
                            Intent intent = new Intent(MainActivity.this, SubordinateManegeActivity.class);
                            startActivityForResult(intent, Constant.REQUEST_MY_SUBORDINATE);
                        }
//                        else if(position == 3)
//                        {
//                            Intent msg_intent = new Intent(MainActivity.this, CustomerServiceManageActivity.class);
//                            startActivityForResult(msg_intent, Constant.REQUEST_MESSAGE);
//                        }
                        else if(position == 3)
                        {
                            Intent msg_intent = new Intent(MainActivity.this, MessageActivity.class);
                            startActivityForResult(msg_intent, Constant.REQUEST_MESSAGE);
                        }
                        else if(position == 4)
                        {
                            Intent setting_intent = new Intent(MainActivity.this, SettingActivity.class);
                            startActivityForResult(setting_intent, Constant.REQUEST_MY_SETTING);
                        }
                    }
                });
            }

            @Override
            protected void opHeader(RecycleCommonViewHolder holder) {
                holder.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        LogUtil.e("点击了头");
                    }
                });
            }
        };
        mAdapter.setHavefooter(false);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 获取缓存下来的订单详情的数据
     */
    void getPreOrderDetailData()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("safetyMark", SesSharedReferences.getSafetyMark(MainActivity.this));

        String js = dbManager.getUrlJsonData(Constant.USER_INFO_URL + StringUtil.obj2JsonStr(map));
        if(!StringUtil.isEmpty(js))
        {
            LoginResult loginResult = new Gson().fromJson(js, LoginResult.class);
            showUserInfo(loginResult);
        }
    }

    private void getPartnerInfo()
    {
        getPreOrderDetailData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(MainActivity.this)))
        {
            return;
        }
        dialog.show();
        dao.getUserInfoTask(SesSharedReferences.getSafetyMark(MainActivity.this), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                showUserInfo((LoginResult) object);
                getMessagAmount();
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
                if(statusCode == 4)
                {
                    gotoLogin();
                }
                else
                {
                    ToastUtil.showToast(message);
                }
            }
        });
    }

    private void getAdv()
    {
        /**
         * 网络获取首页 滚动TextView的数据
         */
        dao.getScrollTextViewTask(0+"", new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                TextResut = (HomeTextResult) object;
                mScrollTextDatas = TextResut.getData();
                if(textDatas!=null&&textDatas.size()>0) {
                    textDatas.clear();
                }
                for (int i=0;i<mScrollTextDatas.size();i++){
                    HomeTextResult.DataBean dataBean = mScrollTextDatas.get(i);

                    textDatas.add(dataBean);
                }
            }

            @Override
            public void resFailure(int statusCode, String message) {

            }
        });
    }

    private void getMessagAmount()
    {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()) || StringUtil.isEmpty(SesSharedReferences.getSafetyMark(MainActivity.this)))
        {
            return;
        }
        messageDao.getNoReadMsgTask(SesSharedReferences.getSafetyMark(MainActivity.this), new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                MessageNoReadResult messageNoReadResult = (MessageNoReadResult) object;
                if(messageNoReadResult != null && messageNoReadResult.getData().size() > 0)
                {
                    changeMsgAmount(messageNoReadResult.getData().get(0).getNumber()+"", 3);
                }
            }

            @Override
            public void resFailure(int statusCode, String message) {
                if(statusCode == 4)
                {
                    gotoLogin();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Constant.LOGIN_OUT)
        {
            Intent intent =  new Intent(MainActivity.this, SplashActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
        else if(resultCode == Constant.LOGIN_OK)
        {
            getPartnerInfo();
        }
        else if(resultCode == Constant.BIND_ALIPAY)
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    getPartnerInfo();
                }
            }, 600);
        }
        else if(resultCode == Constant.UNBIND_ALIPAY)
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    getPartnerInfo();
                }
            }, 600);
        }
        else if(requestCode == Constant.REQUEST_MESSAGE)
        {
            getPartnerInfo();
        }
        else if(requestCode == Constant.REQUEST_MY_ORDER)
        {
            getPartnerInfo();
        }
        else if(requestCode == Constant.REQUEST_MY_SUBORDINATE)
        {
            getPartnerInfo();
        }
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtil.showToast("再点击一次将退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            exitApp();
        }
    }

    //退出app
    public void exitApp() {
        MainActivity.this.finish();
        System.exit(0);
        Process.killProcess(Process.myPid());
    }

    @Override
    public void onRefresh() {
        getPartnerInfo();
        getAdv();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected void gotoLogin()
    {
        Intent intent =  new Intent(MainActivity.this, LoginActivity.class);
        intent.putExtra("flag", 1);
        startActivityForResult(intent, Constant.REQUEST_CODE);
    }

    void changeMsgAmount(String amount, int index)
    {
        for (int i = 0; i < mRecyclerView.getChildCount(); i ++)
        {
            if(i == index)
            {
                LinearLayout parents = (LinearLayout) mRecyclerView.getChildAt(index);
                FrameLayout frameLayout_dot = (FrameLayout)parents.findViewById(R.id.layout_dot);
                if("0".equals(amount))
                {
                    frameLayout_dot.setVisibility(View.GONE);
                }
                else
                {
                    frameLayout_dot.setVisibility(View.VISIBLE);
                    TextView tv_amount = (TextView) parents.findViewById(R.id.txt_amount);
                    tv_amount.setText(amount);
                }
            }
        }
    }

    //===============权限处理===================
    private static final int PERMISSION_REQUEST_CODE = 0;
    // 系统权限管理页面的参数
    private static final String EXTRA_PERMISSIONS = "me.chunyu.clwang.permission.extra_permission";
    // 权限参数
    private static final String PACKAGE_URL_SCHEME = "package:";
    // 方案
    private PermissionsChecker mChecker;
    // 权限检测器
    private boolean isRequireCheck;
    // 是否需要系统权限检测
    private AlertDialog alertDialog;

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            String[] permissions = getPermissions();
            if (mChecker.lacksPermissions(permissions)) {
                requestPermissions(permissions);// 请求权限
            } else {
                allPermissionsGranted(); // 全部权限都已获取
            }
        } else {
            isRequireCheck = true;
        }
    }

    // 返回传递的权限参数
    private String[] getPermissions() {
        String[] mPermissionList = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_LOGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.SET_DEBUG_APP,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_APN_SETTINGS,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        return mPermissionList;
    }

    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {

    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            allPermissionsGranted();
        } else {
            isRequireCheck = false;
            showMissingPermissionDialog();
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {

        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setMessage("申请权限异常，将影响App正常运作，点击确定进入权限管理")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startAppSettings();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
//                            exitApp();
                        }
                    }).create();
        }

        if (alertDialog != null && !alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }

}

package com.jx.intelligent.ui.activitise;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.MessageNoReadResult;
import com.jx.intelligent.runtimepermission.PermissionsChecker;
import com.jx.intelligent.ui.fragments.NewHomeFragment;
import com.jx.intelligent.ui.fragments.ServiceFragment;
import com.jx.intelligent.ui.fragments.UserFragment;
import com.jx.intelligent.ui.fragments.VideoFragment;
import com.jx.intelligent.update.UpdateAgent;
import com.jx.intelligent.util.LogUtil;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.StatusBarUtil;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.UIUtil;
import com.jx.intelligent.util.Utils;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private VideoFragment mVideoFragment;
    private ServiceFragment mServiceFragment;
    private UserFragment mUserFragment;
    private int fragmentPosition;
    public RadioButton mian_rb_mine;

    private UpdateAgent mUpdateAgent;
    private UpdateAgent.CheckUpdateListener mCheckUpdateListener;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private UserCenter userCenter;
    ProgressWheelDialog dialog;
    private NewHomeFragment mNewHomeFragment;
    private RadioButton main_rb_newhome;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private RadioGroup radioGroup;
    //fragment返回键接口

    private boolean isInterception = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //我的
        mian_rb_mine = (RadioButton) findViewById(R.id.mian_rb_mine);
        //首页
        main_rb_newhome = (RadioButton) findViewById(R.id.main_rb_newhome);

        dialog = new ProgressWheelDialog(MainActivity.this);
        setFragment(0);
        radioGroup = (RadioGroup) findViewById(R.id.main_rg);
        radioGroup.setOnCheckedChangeListener(this);
        StatusBarUtil.setColor(this, UIUtil.getColor(R.color.color_1bb6ef));
        userCenter = new UserCenter();
        getNotReadMsg();
        initUpdate();
        mChecker = new PermissionsChecker(this);
        isRequireCheck = true;
        RHBaseApplication.setMainActivity(MainActivity.this);


//        String alias ="151";
//        PushManager.getInstance().bindAlias(MainActivity.this, alias);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }





    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.main_rb_newhome:
                setFragment(0);
                break;
            case R.id.main_rb_service:
                setFragment(1);
                break;
            case R.id.main_rb_video:
                setFragment(2);
                break;
            case R.id.mian_rb_mine:
                setFragment(3);
                break;
        }
    }

    /**
     * @param trans
     */
    private void hideFragments(FragmentTransaction trans) {
        if (mServiceFragment != null) {
            trans.hide(mServiceFragment);
        }
        if (mUserFragment != null) {
            trans.hide(mUserFragment);
        }
        if (mNewHomeFragment != null) {
            trans.hide(mNewHomeFragment);
        }
        if (mVideoFragment != null) {
            trans.hide(mVideoFragment);
        }
    }

    private void setFragment(int position) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        setAnimation(position, transaction);
        hideFragments(transaction);
        switch (position) {
            case 0:
                if(mVideoFragment!=null){
                    mVideoFragment.stopVideo();
                }
                if (mNewHomeFragment == null) {
                    mNewHomeFragment = new NewHomeFragment();

                    transaction.add(R.id.main_container_fl, mNewHomeFragment);
                } else {
                    transaction.show(mNewHomeFragment);
                }
                changeMineBtn(false);
                break;
            case 1:
                if(mVideoFragment!=null){
                    mVideoFragment.stopVideo();
                }
                if (mServiceFragment == null) {

                    mServiceFragment = new ServiceFragment();
                    transaction.add(R.id.main_container_fl, mServiceFragment);
                } else {
                    transaction.show(mServiceFragment);
                }
                changeMineBtn(false);
                break;
            case 2:
                if (mVideoFragment == null) {
                    mVideoFragment = new VideoFragment();
                    transaction.add(R.id.main_container_fl, mVideoFragment);
                } else {
                    transaction.show(mVideoFragment);
                }
                changeMineBtn(false);
                break;
            case 3:
                if(mVideoFragment!=null){
                    mVideoFragment.stopVideo();
                }
                if (mUserFragment == null) {

                    mUserFragment = new UserFragment();
                    transaction.add(R.id.main_container_fl, mUserFragment);
                } else {
                    transaction.show(mUserFragment);
                }
                changeMineBtn(true);
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    /**
     * @param position
     * @param transaction
     */
    private void setAnimation(int position, FragmentTransaction transaction) {

//        if (position > fragmentPosition) {
//            transaction.setCustomAnimations(R.anim.slide_in_from_left,
//                    R.anim.abc_fade_out);
//
//        } else if (position < fragmentPosition) {
//            transaction.setCustomAnimations(R.anim.slide_in_from_right,
//                    R.anim.abc_fade_out);
//        }

        fragmentPosition = position;
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再点击一次将退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            exitApp();
        }
    }

    private void initUpdate() {
        mUpdateAgent = UpdateAgent.getInstance(this);
        mCheckUpdateListener = new CheckUpdateListener();
        mUpdateAgent.setCheckUpdateListener(mCheckUpdateListener);
        mUpdateAgent.update();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class CheckUpdateListener implements UpdateAgent.CheckUpdateListener {

        @Override
        public void onUpdateReturned(boolean isCanUpdate, HashMap<String, String> responseInfos) {
            LogUtil.e("responseInfos:" + responseInfos);
            if (isCanUpdate) {
                if (responseInfos != null && responseInfos.size() > 0) {
                    mUpdateAgent.showUpdateDialog(MainActivity.this, responseInfos);
                }
            } else {
//                ToastUtil.showToast("当前是最新版本");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.LOGIN_FLAG && resultCode == Constant.LOGIN_OK) {

            goUser();

        } else if (requestCode == Constant.LOGIN_FLAG) {

            main_rb_newhome.setChecked(true);//登录返回切会首页
        }
    }

    public void goHome() {

        if (mNewHomeFragment == null) {
            mNewHomeFragment = new NewHomeFragment();
            transaction.add(R.id.main_container_fl, mNewHomeFragment);
        } else {
            transaction.show(mNewHomeFragment);
        }
        main_rb_newhome.setChecked(true);
    }

    public void goUser() {
        main_rb_newhome.setChecked(true);
        if (mUserFragment == null) {
            mUserFragment = new UserFragment();
            transaction.add(R.id.main_container_fl, mUserFragment);
        } else {
            transaction.show(mUserFragment);
            refreshUserCenter();
        }
        mian_rb_mine.setChecked(true);
    }

    public void refreshUserCenter() {
        mUserFragment.refActivityData();
    }

    /**
     * 变换我的按钮背景
     *
     * @param isSelect 是否选中
     */
    public void changeMineBtn(boolean isSelect) {
        int noReadMsg = RHBaseApplication.getInstance().getNotReadMsg();
        if (SesSharedReferences.getUsrLoginState(MainActivity.this) && noReadMsg > 0 && isSelect)//有新消息且选中
        {
            Drawable topDrawable = getResources().getDrawable(R.mipmap.mine_dot_selector);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            mian_rb_mine.setCompoundDrawables(null, topDrawable, null, null);
            if (mian_rb_mine.isChecked()) {
                mian_rb_mine.setChecked(true);
            }
        } else if (SesSharedReferences.getUsrLoginState(MainActivity.this) && noReadMsg > 0 && !isSelect)//有新消息且没选中
        {
            Drawable topDrawable = getResources().getDrawable(R.mipmap.mine_dot_normal);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            mian_rb_mine.setCompoundDrawables(null, topDrawable, null, null);
            if (!mian_rb_mine.isChecked()) {
                mian_rb_mine.setChecked(false);
            }
        } else if (noReadMsg <= 0 && isSelect)//没有新消息且选中
        {
            Drawable topDrawable = getResources().getDrawable(R.mipmap.shouye_mineselect);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            mian_rb_mine.setCompoundDrawables(null, topDrawable, null, null);
            if (mian_rb_mine.isChecked()) {
                mian_rb_mine.setChecked(true);
            }
        } else if (noReadMsg <= 0 && !isSelect)//没有新消息且没选中
        {
            Drawable topDrawable = getResources().getDrawable(R.mipmap.shouye_minenormal);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
            mian_rb_mine.setCompoundDrawables(null, topDrawable, null, null);
            if (!mian_rb_mine.isChecked()) {
                mian_rb_mine.setChecked(false);
            }
        }
    }

    /**
     * 获取未读消息
     */
    public void getNotReadMsg() {
        if (!StringUtil.isEmpty(SesSharedReferences.getUserId(MainActivity.this))) {
            if (!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext())) {
                return;
            }

            dialog.show();
            userCenter.getNoReadMsgTask(SesSharedReferences.getUserId(MainActivity.this), new ResponseResult() {
                @Override
                public void resSuccess(Object object) {
                    dialog.dismiss();
                    MessageNoReadResult messageNoReadResult = (MessageNoReadResult) object;
                    if (messageNoReadResult != null && messageNoReadResult.getData().size() > 0) {
                        if (messageNoReadResult.getData().get(0).getNumber() > 0) {
                            RHBaseApplication.getInstance().setNotReadMsg(messageNoReadResult.getData().get(0).getNumber());
                            changeMineBtn(false);
                        }
                    }
                }

                @Override
                public void resFailure(String message) {
                    dialog.dismiss();
                }
            });
        }
    }

    //退出app
    public void exitApp() {
        System.exit(0);
        Process.killProcess(Process.myPid());
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
        return new String[]{
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        };
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

    public void HideRadioGroup(){
        radioGroup.setVisibility(View.GONE);
    }
}

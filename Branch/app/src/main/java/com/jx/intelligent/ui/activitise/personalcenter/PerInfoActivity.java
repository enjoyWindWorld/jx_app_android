package com.jx.intelligent.ui.activitise.personalcenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.CommunityService;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.view.dialog.NormalAlertDialog;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.helper.GlideHelper;
import com.jx.intelligent.helper.TitleBarHelper;
import com.jx.intelligent.intf.DialogOnClickListener;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.LoginResult;
import com.jx.intelligent.result.UploadImgResult;
import com.jx.intelligent.ui.activitise.service.ClipViewActivity;
import com.jx.intelligent.util.CommonUtils;
import com.jx.intelligent.util.SDCardUtils;
import com.jx.intelligent.util.SesSharedReferences;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;

import java.io.File;

/**
 * 个人信息
 * Created by Administrator on 2016/11/15 0015.
 */
public class PerInfoActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "PerInfoActivity";
    ImageView titlebar_left_vertical_iv, grxx_tx_img;

    RelativeLayout grxx_xgtx_jr, grxx_xgxb_jr, grxx_xgjtdz_jr;
    SexSelectPopWindow menuWindow;
    ProgressWheelDialog dialog;
    NormalAlertDialog dialog2;
    private String logo, url;
    LinearLayout grxx_xgnc_jr, grxx_xggrqm_jr;
    private Uri mImageUri, uri;
    TextView grxx_xgxb_text, grxx_xgnc_text, grxx_xggrqm_text;
    String gxqmString;
    UserCenter userCenter;
    boolean isUpdate;//是否修改过信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        new TitleBarHelper(this)
                .setLeftImageRes(R.drawable.selector_back)
                .setMiddleTitleText("个人信息")
                .setRightText("")
                .setLeftClickListener(this);
        userCenter = new UserCenter();

        grxx_xgtx_jr = (RelativeLayout) findViewById(R.id.grxx_xgtx_jr);
        grxx_tx_img = (ImageView) findViewById(R.id.grxx_tx_img);
        grxx_xgnc_jr = (LinearLayout) findViewById(R.id.grxx_xgnc_jr);
        grxx_xgxb_jr = (RelativeLayout) findViewById(R.id.grxx_xgxb_jr);
        grxx_xggrqm_jr = (LinearLayout) findViewById(R.id.grxx_xggrqm_jr);
        //我的推广码
        grxx_xgjtdz_jr = (RelativeLayout) findViewById(R.id.grxx_xgjtdz_jr);
        grxx_xgxb_text = (TextView) findViewById(R.id.grxx_xgxb_text);
        grxx_xgnc_text = (TextView) findViewById(R.id.grxx_xgnc_text);
        grxx_xggrqm_text = (TextView) findViewById(R.id.grxx_xggrqm_text);

        LoginResult loginResult = RHBaseApplication.getInstance().getLoginResult();
        if (loginResult != null && loginResult.getData().size() > 0) {

            grxx_xgnc_text.setText(loginResult.getData().get(0).getNickname());

            if (loginResult.getData().get(0).getSex().equals("0")) {
                grxx_xgxb_text.setText("女");
            } else if (loginResult.getData().get(0).getSex().equals("1")) {
                grxx_xgxb_text.setText("男");
            } else {
                grxx_xgxb_text.setText("保密");
            }

            grxx_xggrqm_text.setText(loginResult.getData().get(0).getSign());
            GlideHelper.setImageView(PerInfoActivity.this, loginResult.getData().get(0).getUserImg(), grxx_tx_img);
        }

        grxx_xgtx_jr.setOnClickListener(this);
        grxx_xgnc_jr.setOnClickListener(this);
        grxx_xgxb_jr.setOnClickListener(this);
        grxx_xggrqm_jr.setOnClickListener(this);
        //我的推广码点击事件
        grxx_xgjtdz_jr.setOnClickListener(this);
        initDialog();
        dialog = new ProgressWheelDialog(PerInfoActivity.this);
    }

    private void initDialog() {
        dialog2 = new NormalAlertDialog.Builder(PerInfoActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(true)
                .setTitleText("温馨提示")
                .setTitleTextColor(R.color.color_000000)
                .setContentText("请选择照片")
                .setContentTextColor(R.color.color_000000)
                .setLeftButtonText("相册")
                .setLeftButtonTextColor(R.color.color_000000)
                .setRightButtonText("拍照")
                .setCancelable(true)
                .setRightButtonTextColor(R.color.color_000000)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        getPhotoByLocal();
                        dialog2.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        createPicture();
                        dialog2.dismiss();
                    }
                })
                .build();
    }

    protected void createPicture() {

        getRootDir();
        Intent camera = new Intent();
        camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(camera, Constant.TAKE_PHOTO);

    }

    protected void getPhotoByLocal() {

        getRootDir();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.TAKE_LOCAL_PICTURE);

    }

    private void getRootDir() {

        File file = new File(SDCardUtils.getAbsRootDir(PerInfoActivity.this) + File.separator
                + "ImageAuth");

        if (!file.exists()) {
            file.mkdir();
        } else {

        }
        mImageUri = Uri.parse("file://" + SDCardUtils.getAbsRootDir(PerInfoActivity.this)
                + File.separator + "ImageAuth" + File.separator + "content.jpg");
    }

    /**
     * 拍照上传
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == Constant.TAKE_LOCAL_PICTURE) {
            startPhotoZoom(data.getData());
        }
        if (requestCode == Constant.TAKE_LOCAL_PICTURE_CROP) {
            disPlayImg(logo);

        }
        if (requestCode == Constant.TAKE_PHOTO) {
            startPhotoZoom(mImageUri);
        }

        if(requestCode == Constant.UPDATE_NICK_NAME && data != null)
        {
            String nc_string = data.getStringExtra("nc_string");
            if (nc_string != null) {
                grxx_xgnc_text.setText(nc_string);
                LoginResult loginResult = RHBaseApplication.getInstance().getLoginResult();
                if (loginResult.getData().size() <= 0 || loginResult.getData().get(0).getNickname() == null || !loginResult.getData().get(0).getNickname().equals(nc_string)) {
                    updateUserNickName();
                }
            }
        }

        if(requestCode == Constant.UPDATE_SIGN && data != null)
        {
            String grqm_string = data.getStringExtra("grqm_string");
            if (grqm_string != null) {
                gxqmString = grqm_string;
                grxx_xggrqm_text.setText(grqm_string);
                LoginResult loginResult = RHBaseApplication.getInstance().getLoginResult();
                if (loginResult.getData().size() <=0 || loginResult.getData().get(0).getSign() == null || !loginResult.getData().get(0).getSign().equals(grqm_string)) {
                    updateUserSign();
                }
            }
        }

        //图片剪切时的返回
        if (requestCode == Constant.CLIP_CODE) {
            if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
            {
                return;
            }
            logo = data.getStringExtra("value");
            //将图片上传到服务器
            new CommunityService().upload(logo, new ResponseResult() {
                @Override
                public void resSuccess(Object object) {
                    UploadImgResult data = (UploadImgResult)object;
                    if(data != null && data.getData().size()>0)
                    {
                        url = data.getData().get(0).getImgUrl();
                        updateUserHeadImg();
                    }
                    else
                    {
                        ToastUtil.showToast("图片上传失败");
                    }
                }

                @Override
                public void resFailure(String message) {
                    ToastUtil.showToast(message);
                }
            });
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        if (uri == null) {
            return;
        }

        this.uri = uri;

        if (weHavePermissionToReadContacts()) {
            logo = CommonUtils.getPath(this, uri);
            gotoClipViewActivity(logo);
        } else {
            requestReadContactsPermissionFirst();
        }
    }


    public void requestReadContactsPermissionFirst() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestForResultContactsPermission();
        } else {
            requestForResultContactsPermission();
        }
    }

    public void requestForResultContactsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }

    /**
     * 跳转到裁剪页面
     *
     * @param url
     */
    private void gotoClipViewActivity(String url) {
        Intent intent = new Intent(this, ClipViewActivity.class);
        intent.putExtra("value", url);
        startActivityForResult(intent, Constant.CLIP_CODE);
    }

    protected void disPlayImg(String strUrl) {
        GlideHelper.setImageView(PerInfoActivity.this, strUrl, grxx_tx_img);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.grxx_xbxz_qd:
                    int select = menuWindow.getPpWindowSelect();
                    if (select == 0) {
                        grxx_xgxb_text.setText("女");
                    } else if (select == 1) {
                        grxx_xgxb_text.setText("男");
                    } else {
                        grxx_xgxb_text.setText("保密");
                    }

                    LoginResult loginResult = RHBaseApplication.getInstance().getLoginResult();
                    if (loginResult.getData().size()<=0 || loginResult.getData().get(0).getSex() == null || !loginResult.getData().get(0).getSex().equals(select + "")) {
                        updateUserSex(select);
                    }
                    break;
            }
        }

    };

    //==========申请权限的==========
    public boolean weHavePermissionToReadContacts() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    /**
     * 跳转到设置昵称
     */
    private void goSetNickname() {
        Intent intent = new Intent(PerInfoActivity.this, SetNicknameActivity.class);
        startActivityForResult(intent, Constant.UPDATE_NICK_NAME);
    }


    /**
     * 跳转到设置个人签名
     */
    private void goSetPerdSign() {
        Intent intent = new Intent(PerInfoActivity.this, SetPerdSignActivity.class);
        startActivityForResult(intent, Constant.UPDATE_SIGN);
    }

    /**
     * 跳转到二维码
     */
    private void goErWeiMa() {
        Intent intent = new Intent(PerInfoActivity.this, ErWeiMaActivity.class);
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isUpdate)
        {
            RHBaseApplication.getInstance().getMainActivity().refreshUserCenter();
        }
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                if(isUpdate)
                {
                    RHBaseApplication.getInstance().getMainActivity().refreshUserCenter();
                }
                finish();
                break;
            case R.id.grxx_xgtx_jr:
                dialog2.show();
                break;
            case R.id.grxx_xgnc_jr:
                goSetNickname();
                break;
            case R.id.grxx_xgxb_jr:
                menuWindow = new SexSelectPopWindow(PerInfoActivity.this, itemsOnClick);
                menuWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                menuWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                //显示窗口
                //设置layout在PopupWindow中显示的位置
                menuWindow.showAtLocation(PerInfoActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.grxx_xggrqm_jr:
                goSetPerdSign();
                break;
            //我的推广码
            case R.id.grxx_xgjtdz_jr:
                goErWeiMa();
                break;


        }
    }

    /**
     * 修改用户昵称
     */
    void updateUserNickName() {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        userCenter.updateUserNickNameTask(SesSharedReferences.getUserId(PerInfoActivity.this),
                grxx_xgnc_text.getText().toString().trim(), new ResponseResult() {
                    @Override
                    public void resSuccess(Object object) {
                        RHBaseApplication.getInstance().getLoginResult().getData().get(0).setNickname(grxx_xgnc_text.getText().toString());
                        ToastUtil.showToast("修改用户昵称成功");
                        isUpdate = true;
                        dialog.dismiss();
                    }

                    @Override
                    public void resFailure(String message) {
                        ToastUtil.showToast(message);
                        dialog.dismiss();
                    }
                });

    }

    /**
     * 修改用户性别
     */
    void updateUserSex(final int sexCode) {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        userCenter.updateUserSexTask(SesSharedReferences.getUserId(PerInfoActivity.this),
                sexCode + "", new ResponseResult() {
                    @Override
                    public void resSuccess(Object object) {
                        dialog.dismiss();
                        RHBaseApplication.getInstance().getLoginResult().getData().get(0).setSex(sexCode+"");
                        ToastUtil.showToast("修改用户性别成功");
                        isUpdate = true;
                    }

                    @Override
                    public void resFailure(String message) {
                        dialog.dismiss();
                        ToastUtil.showToast(message);
                    }
                });

    }

    /**
     * 修改用户签名
     */
    void updateUserSign() {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        userCenter.updateUserSignTask(SesSharedReferences.getUserId(PerInfoActivity.this),
                grxx_xggrqm_text.getText().toString(), new ResponseResult() {
                    @Override
                    public void resSuccess(Object object) {
                        dialog.dismiss();
                        RHBaseApplication.getInstance().getLoginResult().getData().get(0).setSign(grxx_xggrqm_text.getText().toString());
                        ToastUtil.showToast("修改个性签名成功");
                        isUpdate = true;
                    }

                    @Override
                    public void resFailure(String message) {
                        dialog.dismiss();
                        ToastUtil.showToast(message);
                    }
                });
    }

    void updateUserHeadImg() {
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        userCenter.setUserHeadImg(SesSharedReferences.getUserId(PerInfoActivity.this),
                url, new ResponseResult() {
                    @Override
                    public void resSuccess(Object object) {
                        RHBaseApplication.getInstance().getLoginResult().getData().get(0).setUserImg(url);
                        ToastUtil.showToast("修改头像成功");
                        isUpdate = true;
                        disPlayImg(logo);
                    }

                    @Override
                    public void resFailure(String message) {
                        ToastUtil.showToast(message);
                    }
                });
    }
}

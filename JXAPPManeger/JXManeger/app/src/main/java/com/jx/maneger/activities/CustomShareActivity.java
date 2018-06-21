package com.jx.maneger.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jx.maneger.R;
import com.jx.maneger.base.RHBaseApplication;
import com.jx.maneger.constant.Constant;
import com.jx.maneger.dao.LoginAndRegister;
import com.jx.maneger.db.DBManager;
import com.jx.maneger.intf.ResponseResult;
import com.jx.maneger.results.ShareContentResult;
import com.jx.maneger.util.StringUtil;
import com.jx.maneger.util.ToastUtil;
import com.jx.maneger.util.Utils;
import com.jx.maneger.view.dialog.ProgressWheelDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.SocializeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 自定义的分享界面
 * Created by Administrator on 2016/11/15 0015.
 */
public class CustomShareActivity extends Activity implements View.OnClickListener {
    LinearLayout share_wx_lr, share_wxpyq_lr, share_qqkj_lr, share_xlwb_lr, share_qq_lr;
    ImageView share_cancel;
    LoginAndRegister dao;
    private ProgressWheelDialog dialog;

    private UMImage imageurl, imagelocal;
    private UMVideo video;
    private UMusic music;
    private UMEmoji emoji;
    private File file;
    private ShareContentResult shareContentResult;
    private DBManager dbManager;

//    String imageurl = "http://mobile.umeng.com/images/pic/home/social/img-1.png";
    String videourl = "http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html";
    String musicurl = "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_share);
        share_cancel = (ImageView) findViewById(R.id.share_cancel);
        share_cancel.setOnClickListener(this);
        share_wx_lr = (LinearLayout) findViewById(R.id.share_wx_lr);
        share_wx_lr.setOnClickListener(this);
        share_wxpyq_lr = (LinearLayout) findViewById(R.id.share_wxpyq_lr);
        share_wxpyq_lr.setOnClickListener(this);
        share_qqkj_lr = (LinearLayout) findViewById(R.id.share_qqkj_lr);
        share_qqkj_lr.setOnClickListener(this);
        share_xlwb_lr = (LinearLayout) findViewById(R.id.share_xlwb_lr);
        share_xlwb_lr.setOnClickListener(this);
        share_qq_lr = (LinearLayout) findViewById(R.id.share_qq_lr);
        share_qq_lr.setOnClickListener(this);
        dialog = new ProgressWheelDialog(CustomShareActivity.this);
        dbManager = new DBManager(RHBaseApplication.getInstance().getApplicationContext());
        dbManager.copyDBFile();
        dao = new LoginAndRegister();
        getShareContent();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_left_rl:
                finish();
                break;
            case R.id.share_cancel:
                finish();
                break;
            case R.id.share_wx_lr:
                if(shareContentResult != null && shareContentResult.getData().size()>0) {
                    initMedia();
                    goShareDetail(SHARE_MEDIA.WEIXIN.toSnsPlatform());
                }
                //分享到微信
                break;
            case R.id.share_wxpyq_lr:
                if(shareContentResult != null && shareContentResult.getData().size()>0) {
                    initMedia();
                    goShareDetail(SHARE_MEDIA.WEIXIN_CIRCLE.toSnsPlatform());
                }
                //分享到微信朋友圈
                break;
            case R.id.share_qqkj_lr:
                if(shareContentResult != null && shareContentResult.getData().size()>0) {
                    initMedia();
                    goShareDetail(SHARE_MEDIA.QZONE.toSnsPlatform());
                }
                //分享到QQ空间
                break;
            case R.id.share_xlwb_lr:
                if(shareContentResult != null && shareContentResult.getData().size()>0)
                {
                    initMedia();
                    goShareDetail(SHARE_MEDIA.SINA.toSnsPlatform());
                }
                //分享到新浪微博
                break;
            case R.id.share_qq_lr:
                if(shareContentResult != null && shareContentResult.getData().size()>0)
                {
                    initMedia();
                    goShareDetail(SHARE_MEDIA.QQ.toSnsPlatform());
                }
                //分享到QQ
                break;
        }
    }

    private void goShareDetail(SnsPlatform mPlatform) {
        UMWeb web = new UMWeb (shareContentResult.getData().get(0).getApkurl());
//        UMWeb web = new UMWeb ("http://www.szjxzn.tech:8080/jx/apk/150848058161166899.apk");
        web.setTitle(getResources().getString(R.string.app_name));//标题
        web.setThumb(imagelocal);  //缩略图
        web.setDescription(shareContentResult.getData().get(0).getContent());//描述

        new ShareAction(CustomShareActivity.this)
                .withMedia(web)
                .setPlatform(mPlatform.mPlatform)
                .setCallback(shareListener).share();
    }


    private void initMedia() {
        imageurl = new UMImage(this, shareContentResult.getData().get(0).getImgurl());
        imageurl.setThumb(new UMImage(this, R.mipmap.icon_jx));
        imagelocal = new UMImage(this, R.mipmap.icon_jx);
        imagelocal.setThumb(new UMImage(this, R.mipmap.icon_jx));
        music = new UMusic(musicurl);
        video = new UMVideo(videourl);
        music.setTitle("This is music title");
        music.setThumb(new UMImage(this, R.mipmap.icon_jx));
        music.setDescription("my description");
        //init video
        video.setThumb(new UMImage(this, R.mipmap.icon_jx));
        video.setTitle("This is video title");
        video.setDescription("my description");
        emoji = new UMEmoji(this, "http://img5.imgtn.bdimg.com/it/u=2749190246,3857616763&fm=21&gp=0.jpg");
        emoji.setThumb(imagelocal);
        file = new File(this.getFilesDir() + "test.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (SocializeUtils.File2byte(file).length <= 0) {
            String content = "U-share分享";
            byte[] contentInBytes = content.getBytes();
            try {
                FileOutputStream fop = new FileOutputStream(file);
                fop.write(contentInBytes);
                fop.flush();
                fop.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtil.showToast("分享成功");
            finish();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.showToast("分享失败");
            finish();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtil.showToast("取消分享");
            finish();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    void getShareContent()
    {
        getPreShareData();
        if(!Utils.isNetworkAvailable(RHBaseApplication.getInstance().getApplicationContext()))
        {
            return;
        }

        dialog.show();
        dao.geShareContentTask(new ResponseResult() {
            @Override
            public void resSuccess(Object object) {
                dialog.dismiss();
                shareContentResult = (ShareContentResult) object;
            }

            @Override
            public void resFailure(int statusCode, String message) {
                dialog.dismiss();
                ToastUtil.showToast(message);
            }
        });
    }

    /**
     * 获取缓存下来的分享的数据
     */
    void getPreShareData()
    {
        String js = dbManager.getUrlJsonData(Constant.USER_SHARE_CONTENT_URL);
        if(!StringUtil.isEmpty(js))
        {
            shareContentResult = new Gson().fromJson(js, ShareContentResult.class);
        }
    }



}

package com.jx.intelligent.ui.activitise.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jx.intelligent.R;
import com.jx.intelligent.base.RHBaseApplication;
import com.jx.intelligent.constant.Constant;
import com.jx.intelligent.dao.UserCenter;
import com.jx.intelligent.db.DBManager;
import com.jx.intelligent.view.dialog.ProgressWheelDialog;
import com.jx.intelligent.intf.ResponseResult;
import com.jx.intelligent.result.ShareContentResult;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.jx.intelligent.util.Utils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMEmoji;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
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
    UserCenter dao;
    private ProgressWheelDialog dialog;

    private UMImage imageurl, imagelocal;
    private UMVideo video;
    private UMusic music;
    private UMEmoji emoji;
    private File file;
    private ShareContentResult shareContentResult;
    private DBManager dbManager;

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
        dao = new UserCenter();
        getShareContent();
    }

//    @Override
//    protected void init() {
//
//
//    }
//
//    @Override
//    protected int setContentLayout() {
//        return R.layout.activity_custom_share;
//    }

//    @Override
//    protected void initTitle() {
//
//        new TitleBarHelper(this)
//                .setHideTitleBar();
//
//    }

//    @Override
//    protected void findView(View contentView) {
//        share_cancel = (ImageView) contentView.findViewById(R.id.share_cancel);
//        share_cancel.setOnClickListener(this);
//        share_wx_lr = (LinearLayout) contentView.findViewById(R.id.share_wx_lr);
//        share_wx_lr.setOnClickListener(this);
//        share_wxpyq_lr = (LinearLayout) contentView.findViewById(R.id.share_wxpyq_lr);
//        share_wxpyq_lr.setOnClickListener(this);
//        share_qqkj_lr = (LinearLayout) contentView.findViewById(R.id.share_qqkj_lr);
//        share_qqkj_lr.setOnClickListener(this);
//        share_xlwb_lr = (LinearLayout) contentView.findViewById(R.id.share_xlwb_lr);
//        share_xlwb_lr.setOnClickListener(this);
//
//
//    }

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


    /**
     * 跳转到绑定手机号界面
     */
    private void goShareDetail(SnsPlatform mPlatform) {
        new ShareAction(CustomShareActivity.this)
                .withText(shareContentResult.getData().get(0).getContent())
                .withMedia(imagelocal)
                .withTargetUrl(shareContentResult.getData().get(0).getApkurl())
                .withTitle(shareContentResult.getData().get(0).getTitle())
                .setPlatform(mPlatform.mPlatform)
                .setCallback(shareListener).share();
    }


    private void initMedia() {
        imageurl = new UMImage(this, shareContentResult.getData().get(0).getImgurl());
        imageurl.setThumb(new UMImage(this, R.mipmap.icon_jx));
        imagelocal = new UMImage(this, R.mipmap.icon_jx);
        imagelocal.setThumb(new UMImage(this, R.mipmap.icon_jx));
        music = new UMusic(Defaultcontent.musicurl);
        video = new UMVideo(Defaultcontent.videourl);
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
            public void resFailure(String message) {
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

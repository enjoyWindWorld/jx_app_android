package com.jx.intelligent.ui.activitise.personalcenter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jx.intelligent.R;
import com.jx.intelligent.util.StringUtil;
import com.jx.intelligent.util.ToastUtil;
import com.superplayer.library.SuperPlayer;

/**
 * 全屏播放广告视频
 * Created by Administrator on 2016/11/15 0015.
 */
public class VideoPlayActivity extends AppCompatActivity implements View.OnClickListener, SuperPlayer.OnNetChangeListener {

    ImageView titlebar_left_vertical_iv;
    MyOnclickList myOnclickList;
    SuperPlayer player;
    RelativeLayout titlebar_left_rl;
    private boolean isLive;
    private String url;
    private String play_url;
    private int currentPosition;
    private String title;
    private int fullScreen_currentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        play_url = getIntent().getStringExtra("play_url");
        currentPosition = getIntent().getIntExtra("currentPosition", 0);
        title = getIntent().getStringExtra("title");
        if(!StringUtil.isEmpty(play_url) && !StringUtil.isEmpty(title))
        {
            initPlayer();
        }
        else
        {
            ToastUtil.showToast("暂无视频资源！");
        }
    }


    /**
     * 初始化播放器
     */
    private void initPlayer() {
        player = (SuperPlayer) findViewById(R.id.view_super_player);

        player.setShowTopControl(false);

        if (isLive) {
            player.setLive(true);//设置该地址是直播的地址
        }
        player.setNetChangeListener(true)//设置监听手机网络的变化
                .setOnNetChangeListener(this)//实现网络变化的回调
                .onPrepared(new SuperPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared() {
                        /**
                         * 监听视频是否已经准备完成开始播放。（可以在这里处理视频封面的显示跟隐藏）
                         */
                    }
                }).onComplete(new Runnable() {
            @Override
            public void run() {
                /**
                 * 监听视频是否已经播放完成了。（可以在这里处理视频播放完成进行的操作）
                 */
            }
        }).onInfo(new SuperPlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                /**
                 * 监听视频的相关信息。
                 */

            }
        }).onError(new SuperPlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
                /**
                 * 监听视频播放失败的回调
                 */

            }
        }).setTitle(title)//设置视频的titleName
                .play(play_url,currentPosition);//开始播放视频

        player.setScaleType(SuperPlayer.SCALETYPE_FITXY);
        player.setFullScreenOnly(true);



    }

    /**
     * 网络链接监听类
     */
    @Override
    public void onWifi() {
        Toast.makeText(this, "当前网络环境是WIFI", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMobile() {
        Toast.makeText(this, "当前网络环境是手机网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisConnect() {
        Toast.makeText(this, "网络链接断开", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoAvailable() {
        Toast.makeText(this, "无网络链接", Toast.LENGTH_SHORT).show();
    }


    /**
     * 下面的这几个Activity的生命状态很重要
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {

            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {

    }


    class MyOnclickList implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.titlebar_left_rl:


                    finish();

                    break;


                case R.id.grxx_zfaq_sjh_jr:
                    goSetPhoNum();
                    break;
                case R.id.grxx_zfaq_xgmm_jr:
                    goChagPasw();
                    break;


            }


        }

    }


    /**
     * 跳转到绑定手机号界面
     */
    private void goSetPhoNum() {
        Intent intent = new Intent(VideoPlayActivity.this, SetPhoNumActivity.class);
        startActivity(intent);

    }

    /**
     * 跳转到绑定手机号界面
     */
    private void goChagPasw() {
        Intent intent = new Intent(VideoPlayActivity.this, ChagPaswActivity.class);
        startActivity(intent);

    }

}

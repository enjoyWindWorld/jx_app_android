package com.jx.intelligent.ui.activitise.share;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.jx.intelligent.R;
import com.superplayer.library.SuperPlayer;
import com.superplayer.library.SuperPlayerManage;

/**
 * Created by Administrator on 2017/7/13.
 */

public class PlayerActivity extends Activity {
    private SuperPlayer player;
    private String play_url;
    private int currentPosition;
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_activity);
        initData();

        initPlayer();

    }

    private void initData() {
        play_url = getIntent().getStringExtra("play_url");
        currentPosition = getIntent().getIntExtra("currentPosition", 0);


    }
    private void initPlayer() {
        player = SuperPlayerManage.getSuperManage().initialize(this);
        player.setShowTopControl(false).setSupportGesture(false);
        mFrameLayout = (FrameLayout) findViewById(R.id.adapter_super_video);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int widthPixels = metrics.widthPixels;
//        int heightPixels = metrics.heightPixels;
//
//        mFrameLayout.getLayoutParams().width=widthPixels;
//        mFrameLayout.getLayoutParams().height=heightPixels;
                mFrameLayout.removeAllViews();
        player.showView(R.id.adapter_player_control);
        mFrameLayout.addView(player);

        player.play(play_url,currentPosition);//开始播放视频
        player.setScaleType(SuperPlayer.SCALETYPE_FITXY);
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

            finish();
            return;
        }
        super.onBackPressed();
    }

//    @Override
//    public void finish() {
//        super.finish();
//    }
}

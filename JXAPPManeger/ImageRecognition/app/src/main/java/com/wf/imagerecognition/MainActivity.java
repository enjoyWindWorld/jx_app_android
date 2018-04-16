package com.wf.imagerecognition;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private SurfaceView adv_sfv;
	private SurfaceHolder holder;
	private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adv_sfv = (SurfaceView) findViewById(R.id.adv_sfv);
        adv_sfv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initMedia();
    }

    private void initMedia() {
        holder = adv_sfv.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        player = MediaPlayer.create(MainActivity.this, R.raw.video);
        player.setOnCompletionListener(this);
        player.setOnPreparedListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        // 在指定了MediaPlayer播放的容器后，使用prepare或者prepareAsync来准备播放了
        try
        {
            player.prepareAsync();
            player.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Toast.makeText(MainActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}

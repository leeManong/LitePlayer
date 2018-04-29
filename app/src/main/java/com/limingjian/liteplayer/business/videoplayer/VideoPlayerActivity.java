package com.limingjian.liteplayer.business.videoplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.limingjian.liteplayer.R;

import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayerStandard;

public class VideoPlayerActivity extends AppCompatActivity {

    private String mData;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        mData = intent.getStringExtra("data");
        mTitle = intent.getStringExtra("title");
    }

    private void initView() {
        JZVideoPlayerStandard.startFullscreen(this,JZVideoPlayerStandard.class,mData,mTitle);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayerStandard.releaseAllVideos();
    }
}

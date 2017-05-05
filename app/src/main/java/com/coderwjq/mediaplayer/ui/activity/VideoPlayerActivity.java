package com.coderwjq.mediaplayer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.coderwjq.mediaplayer.R;
import com.coderwjq.mediaplayer.bean.VideoItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created by coderwjq on 2017/5/5 8:52.
 * @Desc
 */

public class VideoPlayerActivity extends BaseActivity {
    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.video_player_iv_pause)
    ImageView mVideoPlayerIvPause;
    @BindView(R.id.tv_video_name)
    TextView mTvVideoName;

    public static void startActivity(Activity srcActivity, VideoItem videoItem) {
        Intent intent = new Intent();
        intent.setClass(srcActivity, VideoPlayerActivity.class);
        intent.putExtra("video_item", videoItem);
        srcActivity.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_player;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        VideoItem videoItem = getIntent().getParcelableExtra("video_item");
        mVideoView.setVideoURI(Uri.parse(videoItem.getPath()));
        mTvVideoName.setText(videoItem.getTitle());
    }

    @Override
    protected void initListener() {
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
                updateButtonPlayerState();
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.video_player_iv_pause})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.video_player_iv_pause:
                updatePlayerState();
                break;
        }
    }

    private void updatePlayerState() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        } else {
            mVideoView.start();
        }
        updateButtonPlayerState();
    }

    private void updateButtonPlayerState() {
        if (mVideoView.isPlaying()) {
            mVideoPlayerIvPause.setImageResource(R.drawable.video_pause_player);
        } else {
            mVideoPlayerIvPause.setImageResource(R.drawable.video_play_selector);
        }
    }
}

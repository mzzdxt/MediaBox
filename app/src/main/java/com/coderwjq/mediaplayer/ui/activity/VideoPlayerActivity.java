package com.coderwjq.mediaplayer.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coderwjq.mediaplayer.R;
import com.coderwjq.mediaplayer.bean.VideoItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

/**
 * @Created by coderwjq on 2017/5/5 8:52.
 * @Desc
 */

public class VideoPlayerActivity extends BaseActivity {
    private static final String TAG = "VideoPlayerActivity";

    private static final int MSG_UPDATE_SYSTEM_TIME = 0;
    private static final int MSG_UPDATE_POSITION = 1;
    private static final int MSG_HIDE_CONTROLLER = 2;

    @BindView(R.id.video_view)
    VideoView mVideoView;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.video_player_iv_pause)
    ImageView mVideoPlayerIvPause;
    @BindView(R.id.tv_video_name)
    TextView mTvVideoName;
    @BindView(R.id.ll_top_controller)
    LinearLayout mLlTopController;
    @BindView(R.id.ll_bottom_controller)
    LinearLayout mLlBottomController;
    @BindView(R.id.tv_battery_level)
    TextView mTvBatteryLevel;
    @BindView(R.id.tv_system_time)
    TextView mTvSystemTime;
    private boolean isControllerShowing = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_SYSTEM_TIME:
                    break;
                case MSG_UPDATE_POSITION:
                    break;
                case MSG_HIDE_CONTROLLER:
                    hideController();
                    break;
            }
        }
    };
    private GestureDetector mGestureDetector;
    private BatteryChangeReceiver mBatteryChangeReceiver;

    public static void invoke(Activity srcActivity, VideoItem videoItem) {
        Intent intent = new Intent();
        intent.setClass(srcActivity, VideoPlayerActivity.class);
        intent.putExtra("video_item", videoItem);
        srcActivity.startActivity(intent);
    }


    /**
     * 显示播放器的控制面板
     */
    private void showController() {
        ViewCompat.animate(mLlTopController).translationY(0);
        ViewCompat.animate(mLlBottomController).translationY(0);

        isControllerShowing = true;
    }

    /**
     * 隐藏播放器的控制面板
     */
    private void hideController() {
        ViewCompat.animate(mLlTopController).translationY(-mLlTopController.getHeight());
        ViewCompat.animate(mLlBottomController).translationY(mLlBottomController.getHeight());

        isControllerShowing = false;
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
        // 初始化Vitamio框架
        Vitamio.isInitialized(getApplicationContext());

        VideoItem videoItem = getIntent().getParcelableExtra("video_item");
        mVideoView.setVideoURI(Uri.parse(videoItem.getPath()));
        mTvVideoName.setText(videoItem.getTitle());

        // 初始化的时候隐藏控制面板
        hideControllerWhenInit();
    }

    private void hideControllerWhenInit() {
        // getMeasuredHeight()是在measure方法执行后就能获取到控件的高度，获取的结果可能不准确
        mLlTopController.measure(0, 0);
        ViewCompat.animate(mLlTopController).translationY(-mLlTopController.getMeasuredHeight());

        // getHeight()需要在onLayout以后才可以获得控件的高度，为准确值
        mLlBottomController.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlBottomController.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ViewCompat.animate(mLlBottomController).translationY(mLlBottomController.getHeight());
            }
        });

        isControllerShowing = false;
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

        // 添加手势监听
        mGestureDetector = new GestureDetector(this, new OnVideoGestureListener());
        // 添加电量监听
        addBatteryChangeReceiver();
    }

    private void addBatteryChangeReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        if (mBatteryChangeReceiver == null) {
            mBatteryChangeReceiver = new BatteryChangeReceiver();
        }
        registerReceiver(mBatteryChangeReceiver, filter);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void hideOrShowController() {
        if (isControllerShowing) {
            hideController();
        } else {
            showController();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeBatteryChangeReceiver();
    }

    private void removeBatteryChangeReceiver() {
        if (mBatteryChangeReceiver != null) {
            unregisterReceiver(mBatteryChangeReceiver);
        }
    }

    private class BatteryChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取电量信息
            int level = intent.getIntExtra("level", 0);
            Log.e(TAG, "onReceive: currentBatteryLevel:" + level);
            // 更新电量
            mTvBatteryLevel.setText(level + "%");
        }
    }

    private final class OnVideoGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            hideOrShowController();
            return super.onSingleTapConfirmed(e);
        }
    }
}

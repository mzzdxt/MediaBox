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
import android.os.PersistableBundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.coderwjq.mediaplayer.R;
import com.coderwjq.mediaplayer.bean.VideoItem;
import com.coderwjq.mediaplayer.utils.ActivityBrightnessManager;
import com.coderwjq.mediaplayer.utils.AudioUtils;
import com.coderwjq.mediaplayer.utils.FilterUtils;
import com.coderwjq.mediaplayer.utils.SPUtils;
import com.coderwjq.mediaplayer.utils.StringUtils;
import com.litesuits.common.assist.Toastor;

import java.util.ArrayList;

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
    public static final String SCREEN_BRIGHTNESS = "screen_brightness";
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
    @BindView(R.id.iv_mute)
    ImageView mIvMute;
    @BindView(R.id.sb_volume_controller)
    SeekBar mSbVolumeController;
    @BindView(R.id.tv_play_time)
    TextView mTvPlayTime;
    @BindView(R.id.sb_play_progress)
    SeekBar mSbPlayProgress;
    @BindView(R.id.tv_total_time)
    TextView mTvTotalTime;
    @BindView(R.id.btn_pre_video)
    ImageView mBtnPreVideo;
    @BindView(R.id.btn_next_video)
    ImageView mBtnNextVideo;
    @BindView(R.id.btn_full_screen)
    ImageView mBtnFullScreen;
    private boolean isControllerShowing = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_SYSTEM_TIME:
                    addSystemTimeUpdate();
                    break;
                case MSG_UPDATE_POSITION:
                    startUpdatePlayTime();
                    break;
                case MSG_HIDE_CONTROLLER:
                    hideController();
                    break;
            }
        }
    };
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!fromUser) {
                return;
            }

            switch (seekBar.getId()) {
                case R.id.sb_volume_controller:
                    // 修改当前的音量值
                    setCurrentVolume(progress);
                    break;
                case R.id.sb_play_progress:
                    // 修改播放进度
                    updatePosition(progress);
                    mVideoView.seekTo(progress);
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeMessages(MSG_UPDATE_POSITION);
            mHandler.removeMessages(MSG_HIDE_CONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.sendEmptyMessage(MSG_UPDATE_POSITION);
            notifyHideController();
        }
    };
    private GestureDetector mGestureDetector;
    private BatteryChangeReceiver mBatteryChangeReceiver;
    private int mNormalVolume;
    private float mStartY;
    private int mStartVolume;
    private float mStartBrightness;
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mVideoView.start();
            updateButtonPlayerState();

            // 更新播放时间
            mTvTotalTime.setText(StringUtils.formatDuration(mVideoView.getDuration()));
            mSbPlayProgress.setMax((int) mVideoView.getDuration());
            startUpdatePlayTime();
        }
    };
    private ArrayList<VideoItem> mVideoItemList;
    private int mPosition;
    private Toastor mToastor;
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mHandler.removeMessages(MSG_UPDATE_POSITION);

            playNextVideo();
        }
    };
    private int mMediaMaxVolume;

    public static void invoke(Activity srcActivity, VideoItem videoItem) {
        Intent intent = new Intent();
        intent.setClass(srcActivity, VideoPlayerActivity.class);
        intent.putExtra("video_item", videoItem);
        srcActivity.startActivity(intent);
    }

    public static void invoke(Activity srcActivity, ArrayList<VideoItem> videoItemList, int position) {
        Intent intent = new Intent();
        intent.setClass(srcActivity, VideoPlayerActivity.class);
        intent.putParcelableArrayListExtra("video_item_list", videoItemList);
        intent.putExtra("position", position);
        srcActivity.startActivity(intent);
    }

    private void notifyHideController() {
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_CONTROLLER, 5000);
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

        // 添加系统时间更新
        addSystemTimeUpdate();

        mToastor = new Toastor(this);
    }

    @Override
    protected void initData() {
        // 初始化Vitamio框架
        Vitamio.isInitialized(getApplicationContext());

        mVideoItemList = getIntent().getParcelableArrayListExtra("video_item_list");
        mPosition = getIntent().getIntExtra("position", -1);
        playItem();

        // 初始化的时候隐藏控制面板
        hideControllerWhenInit();

        // 初始化声音相关数据
        initAudioData();

        // 初始化默认屏幕亮度
        initScreenBrightness();
    }

    private void playItem() {
        VideoItem videoItem = mVideoItemList.get(mPosition);
        mVideoView.setVideoURI(Uri.parse(videoItem.getPath()));
        mTvVideoName.setText(videoItem.getTitle());

        updatePreAndNextButton();
    }

    private void updatePreAndNextButton() {
        mBtnPreVideo.setEnabled(mPosition != 0);
        mBtnNextVideo.setEnabled(mPosition != mVideoItemList.size() - 1);
    }

    private void startUpdatePlayTime() {
        // 获取当前播放进度
        long currentPosition = mVideoView.getCurrentPosition();

        // 更新UI
        updatePosition(currentPosition);

        // 发送延迟更新消息
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_POSITION, 500);
    }

    private void updatePosition(long currentPosition) {
        mTvPlayTime.setText(StringUtils.formatDuration(currentPosition));
        mSbPlayProgress.setProgress((int) currentPosition);
    }

    private void initScreenBrightness() {
        float defaultBrightness = SPUtils.getSingleton(getApplicationContext()).getFloat(SCREEN_BRIGHTNESS);
        Log.e(TAG, "initScreenBrightness: 初始化默认屏幕亮度:" + defaultBrightness);
        setScreenBrightness(defaultBrightness);
    }

    private float getScreenBrightness() {
        float activityBrightness = ActivityBrightnessManager.getActivityBrightness(VideoPlayerActivity.this);
        return activityBrightness == -1.0f ? 1.0f : activityBrightness;
    }

    private void setScreenBrightness(float value) {
        value = FilterUtils.brightnessFilter(value);

        ActivityBrightnessManager.setActivityBrightness(value, VideoPlayerActivity.this);
        saveCurrentBrightness();
    }

    private void initAudioData() {
        mMediaMaxVolume = AudioUtils.getSingleton(this).getMaxMediaVolume();
        int mediaCurrentVolume = AudioUtils.getSingleton(this).getCurrentMediaVolume();
        Log.e(TAG, "initAudioData: mediaMaxVolume:" + mMediaMaxVolume + " mediaCurrentVolume:" + mediaCurrentVolume);
        // 设置声音进度条的最大值
        mSbVolumeController.setMax(mMediaMaxVolume);
        // 设置声音进度条的当前值
        mSbVolumeController.setProgress(mediaCurrentVolume);
    }

    private void hideControllerWhenInit() {
        // getMeasuredHeight()是在measure方法执行后就能获取到控件的高度，获取的结果可能不准确
        // mLlTopController.measure(0, 0);

        // getHeight()需要在onLayout以后才可以获得控件的高度，为准确值
        mLlBottomController.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLlBottomController.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ViewCompat.animate(mLlTopController).translationY(-mLlTopController.getHeight());
                ViewCompat.animate(mLlBottomController).translationY(mLlBottomController.getHeight());
            }
        });

        isControllerShowing = false;
    }

    @Override
    protected void initListener() {
        mVideoView.setOnPreparedListener(mOnPreparedListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);

        // 添加手势监听
        mGestureDetector = new GestureDetector(this, new OnVideoGestureListener());
        // 添加电量监听
        addBatteryChangeReceiver();
        // 添加声音控制条的监听
        mSbVolumeController.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        mSbPlayProgress.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
    }

    private void addSystemTimeUpdate() {
        mTvSystemTime.setText(StringUtils.formatSystemTime());
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_SYSTEM_TIME, 500);
    }

    private void addBatteryChangeReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        if (mBatteryChangeReceiver == null) {
            mBatteryChangeReceiver = new BatteryChangeReceiver();
        }
        registerReceiver(mBatteryChangeReceiver, filter);
    }

    @OnClick({R.id.btn_back, R.id.video_player_iv_pause, R.id.iv_mute,
            R.id.btn_pre_video, R.id.btn_next_video, R.id.btn_full_screen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.video_player_iv_pause:
                updatePlayerState();
                break;
            case R.id.iv_mute:
                switchMuteStatus();
                break;
            case R.id.btn_pre_video:
                playPreVideo();
                break;
            case R.id.btn_next_video:
                playNextVideo();
                break;
            case R.id.btn_full_screen:
                break;
        }
    }

    private void playNextVideo() {
        if (mPosition != mVideoItemList.size() - 1) {
            mPosition++;
            playItem();
        } else {
            // 播放完最后一个文件直接退出
            finish();
        }

        updatePreAndNextButton();
    }

    private void playPreVideo() {
        if (mPosition != 0) {
            mPosition--;
            playItem();
        }

        updatePreAndNextButton();
    }

    /**
     * 切换静音按钮状态
     */
    private void switchMuteStatus() {
        if (getCurrentVolume() != 0) {
            // 当前非静音状态
            mNormalVolume = getCurrentVolume();
            setCurrentVolume(0);
        } else {
            // 当前为静音状态
            setCurrentVolume(mNormalVolume);
        }
    }

    private int getCurrentVolume() {
        return AudioUtils.getSingleton(getApplicationContext()).getCurrentMediaVolume();
    }

    private void setCurrentVolume(int progress) {
        AudioUtils.getSingleton(getApplicationContext()).setCurrentMediaVolume(progress);
        // 修改当前进度值
        mSbVolumeController.setProgress(progress);
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

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = event.getY();
                mStartVolume = getCurrentVolume();
                mStartBrightness = getScreenBrightness();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = event.getY();
                // 手指划过屏幕的距离
                float offsetY = currentY - mStartY;
                // 手指划过屏幕的百分比
                float movePercent = offsetY / getScreenHeight();
                if (event.getX() < getScreenWidth() / 2) {
                    // 处理屏幕亮度
                    adjustBrightness(movePercent);
                } else {
                    // 处理音量
                    adjustVolume(movePercent);
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private void adjustBrightness(float movePercent) {
        float finalBrightness = mStartBrightness - movePercent;
        finalBrightness = FilterUtils.brightnessFilter(finalBrightness);
        setScreenBrightness(finalBrightness);
        mToastor.showSingletonToast("当前屏幕亮度：" + (int) (finalBrightness * 100) + "%");
    }

    private void adjustVolume(float movePercent) {
        int offsetVolume = (int) (movePercent * mMediaMaxVolume);
        int finalVolume = mStartVolume - offsetVolume;
        finalVolume = FilterUtils.volumeFilter(finalVolume, mMediaMaxVolume);
        setCurrentVolume(finalVolume);
        mToastor.showSingletonToast("当前音量：" + finalVolume * 100 / mMediaMaxVolume + "%");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }


    private void hideOrShowController() {
        if (isControllerShowing) {
            hideController();
        } else {
            showController();

            notifyHideController();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除电量监听
        removeBatteryChangeReceiver();
        mHandler.removeCallbacksAndMessages(null);
    }

    private void removeBatteryChangeReceiver() {
        if (mBatteryChangeReceiver != null) {
            unregisterReceiver(mBatteryChangeReceiver);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void saveCurrentBrightness() {
        float screenBrightness = getScreenBrightness();
        SPUtils.getSingleton(getApplicationContext()).putFloat(SCREEN_BRIGHTNESS, screenBrightness);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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

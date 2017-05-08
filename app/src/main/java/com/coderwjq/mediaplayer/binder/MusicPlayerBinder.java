package com.coderwjq.mediaplayer.binder;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;

import com.coderwjq.mediaplayer.bean.MusicItem;
import com.coderwjq.mediaplayer.common.Constant;
import com.coderwjq.mediaplayer.utils.SPUtils;
import com.litesuits.common.assist.Toastor;
import com.litesuits.common.utils.RandomUtil;

import java.io.IOException;
import java.util.ArrayList;

import static com.coderwjq.mediaplayer.common.Constant.PLAYMODE_ALL_REPEAT;
import static com.coderwjq.mediaplayer.common.Constant.PLAYMODE_RANDOM;
import static com.coderwjq.mediaplayer.common.Constant.PLAYMODE_SINGLE_REPEAT;

/**
 * @Created by coderwjq on 2017/5/8 10:30.
 * @Desc
 */

public class MusicPlayerBinder extends Binder {
    private static final int START_PLAY_MUSIC = 0;
    private static final int COMPLETE_PLAY_MUSIC = 1;
    private final Toastor mToastor;
    private MediaPlayer mMediaPlayer;
    private ArrayList<MusicItem> mMusicItems;
    private int mCurrentPosotion;
    private Context mContext;

    private int mPlayMode;

    public MusicPlayerBinder(Context context) {
        mContext = context;
        mToastor = new Toastor(context);
    }

    public void init(ArrayList<MusicItem> musicItems, int currentPosotion, int playMode) {
        mMusicItems = musicItems;
        mCurrentPosotion = currentPosotion;
        mPlayMode = playMode;
    }

    public void playMusicItem() {
        // 非空校验
        if (mMusicItems == null || mMusicItems.size() == 0 || mCurrentPosotion == -1) {
            new RuntimeException("播放数据不能为空");
            return;
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(mMusicItems.get(mCurrentPosotion).getPath());
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 通知界面更新
                    notifyUIToRefresh(START_PLAY_MUSIC);
                    // 开始播放
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 通知界面播放完毕
                    notifyUIToRefresh(COMPLETE_PLAY_MUSIC);
                    // 根据用户设置，自动播放下一首
                    autoPlayNextMusic();
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void autoPlayNextMusic() {
        // 根据播放模式切换歌曲
        switch (mPlayMode) {
            case PLAYMODE_ALL_REPEAT:
                mCurrentPosotion++;
                mCurrentPosotion = mCurrentPosotion % (mMusicItems.size() - 1);
                break;
            case PLAYMODE_SINGLE_REPEAT:
                break;
            case PLAYMODE_RANDOM:
                mCurrentPosotion = RandomUtil.getRandom(mMusicItems.size() - 1);
                break;
        }
        playMusicItem();
    }

    private void notifyUIToRefresh(int state) {
        switch (state) {
            case START_PLAY_MUSIC:
                refreshCurrentMusic();
                break;
            case COMPLETE_PLAY_MUSIC:
                mContext.sendBroadcast(new Intent(Constant.ACTION_MUSIC_COMPLETED));
                break;
        }
    }

    public void refreshCurrentMusic() {
        MusicItem musicItem = mMusicItems.get(mCurrentPosotion);
        Intent intent = new Intent(Constant.ACTION_MUSIC_PREPARED);
        intent.putExtra("music_item", musicItem);
        mContext.sendBroadcast(intent);
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public void playPreMusic() {
        if (mCurrentPosotion != 0) {
            mCurrentPosotion--;
            playMusicItem();
        } else {
            mToastor.showSingletonToast("已经是第一首歌曲了");
        }
    }

    public void playNextMusic() {
        if (mCurrentPosotion != mMusicItems.size() - 1) {
            mCurrentPosotion++;
            playMusicItem();
        } else {
            mToastor.showSingletonToast("已经是最后首歌曲了");
        }
    }

    public void seekTo(int msec) {
        mMediaPlayer.seekTo(msec);
    }

    public void switchPlayMode() {
        switch (mPlayMode) {
            case PLAYMODE_ALL_REPEAT:
                mPlayMode = PLAYMODE_SINGLE_REPEAT;
                break;
            case PLAYMODE_SINGLE_REPEAT:
                mPlayMode = PLAYMODE_RANDOM;
                break;
            case PLAYMODE_RANDOM:
                mPlayMode = PLAYMODE_ALL_REPEAT;
                break;
        }
        SPUtils.getSingleton(mContext).putInt("music_play_mode", mPlayMode);
    }

    public int getPlayMode() {
        return mPlayMode;
    }

    public void switchPlayButton() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else {
            mMediaPlayer.start();
        }
    }
}

package com.coderwjq.mediaplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.coderwjq.mediaplayer.bean.MusicItem;
import com.coderwjq.mediaplayer.binder.MusicPlayerBinder;
import com.coderwjq.mediaplayer.utils.SPUtils;

import java.util.ArrayList;

public class MusicPlayerService extends Service {

    private static final String TAG = "MusicPlayerService";
    private int mPlayMode;
    private int mCurrentPosition = -1;
    private MusicPlayerBinder mBinder;
    private ArrayList<MusicItem> mMusicItems;

    public MusicPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: intent = [" + intent + "]");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");

        mBinder = new MusicPlayerBinder(this);
        // 初始化播放模式
        mPlayMode = SPUtils.getSingleton(this).getInt("music_play_mode");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        // 获取到播放数据
        int position = intent.getIntExtra("position", -1);
        if (mCurrentPosition == position) {
            // 当前歌曲正在播放
            mBinder.refreshCurrentMusic();
        } else {
            // 播放新的歌曲
            mCurrentPosition = position;
            mMusicItems = intent.getParcelableArrayListExtra("music_list");
            mBinder.init(mMusicItems, mCurrentPosition, mPlayMode);
            mBinder.playMusicItem();
        }
        return super.onStartCommand(intent, flags, startId);
    }

}

package com.coderwjq.mediaplayer.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * @Created by coderwjq on 2017/5/6 9:01.
 * @Desc 音频管理相关的工具类
 */

public class AudioUtils {
    private static AudioUtils singleton;
    private Context mContext;
    private AudioManager mAudioManager;

    private AudioUtils(Context context) {
        this.mContext = context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public static AudioUtils getSingleton(Context context) {
        if (singleton == null) {
            synchronized (AudioUtils.class) {
                if (singleton == null) {
                    singleton = new AudioUtils(context);
                }
            }
        }
        return singleton;
    }

    /**
     * @return 多媒体声音的最大值
     */
    public int getMaxMediaVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * @return 多媒体声音的当前值
     */
    public int getCurrentMediaVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void setCurrentMediaVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }
}

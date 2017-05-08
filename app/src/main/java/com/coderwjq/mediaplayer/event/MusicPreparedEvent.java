package com.coderwjq.mediaplayer.event;

import com.coderwjq.mediaplayer.bean.MusicItem;

/**
 * @Created by coderwjq on 2017/5/8 15:56.
 * @Desc
 */

public class MusicPreparedEvent {
    private MusicItem mMusicItem;

    public MusicPreparedEvent(MusicItem musicItem) {
        mMusicItem = musicItem;
    }

    public MusicItem getMusicItem() {
        return mMusicItem;
    }

    public void setMusicItem(MusicItem musicItem) {
        mMusicItem = musicItem;
    }
}

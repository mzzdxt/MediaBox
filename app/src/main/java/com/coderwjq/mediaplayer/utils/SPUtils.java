package com.coderwjq.mediaplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Created by coderwjq on 2017/5/6 10:22.
 * @Desc
 */

public class SPUtils {
    private static SPUtils singleton;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private SPUtils(Context context) {
        this.mContext = context;
        mSharedPreferences = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static SPUtils getSingleton(Context context) {
        if (singleton == null) {
            synchronized (AudioUtils.class) {
                if (singleton == null) {
                    singleton = new SPUtils(context);
                }
            }
        }
        return singleton;
    }

    public void putFloat(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).commit();
    }

    public float getFloat(String key) {
        return mSharedPreferences.getFloat(key, -1.0f);
    }

}

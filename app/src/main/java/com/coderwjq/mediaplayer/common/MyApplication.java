package com.coderwjq.mediaplayer.common;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Process;

import com.litesuits.common.assist.Toastor;

/**
 * Created by coderwjq on 2017/3/20.
 */

public class MyApplication extends Application {

    public static Context mContext = null;
    public static Handler mHandler = null;
    public static Thread mMainThread = null;
    public static int mMainThreadId = 1;
    public static Toastor mToaster = null;
    public static AssetManager mAssetManager = null;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        mHandler = new Handler();
        mMainThread = Thread.currentThread();
        mMainThreadId = Process.myTid();
        mToaster = new Toastor(getApplicationContext());
        mAssetManager = mContext.getAssets();
    }

}

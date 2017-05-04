package com.coderwjq.mediaplayer.ui.activity;

import android.os.Handler;

import com.coderwjq.mediaplayer.R;

public class SplashActivity extends BaseActivity {
    @Override
    protected void initEvent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.start(SplashActivity.this);
                finish();
            }
        }, 2000);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }
}

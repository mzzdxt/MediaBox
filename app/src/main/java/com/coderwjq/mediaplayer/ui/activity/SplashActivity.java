package com.coderwjq.mediaplayer.ui.activity;

import android.Manifest;
import android.os.Handler;

import com.coderwjq.mediaplayer.R;
import com.litesuits.common.assist.Toastor;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity {

    private Toastor mToastor;

    @Override
    protected void initListener() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            mToastor.showSingletonToast("没有权限读取文件");
                        }
                        startMainActivity();
                    }
                });
    }

    private void startMainActivity() {
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
        mToastor = new Toastor(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void pendingAnimation() {
        super.pendingAnimation();
        // Activity切换的淡入淡出效果
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

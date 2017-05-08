package com.coderwjq.mediaplayer.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.coderwjq.mediaplayer.R;
import com.coderwjq.mediaplayer.adapter.MainActivityPagerAdatper;
import com.coderwjq.mediaplayer.service.MusicPlayerService;
import com.coderwjq.mediaplayer.ui.fragment.MusicFragment;
import com.coderwjq.mediaplayer.ui.fragment.VideoFragment;
import com.coderwjq.mediaplayer.utils.ServiceStateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.main_tv_video)
    TextView mMainTvVideo;
    @BindView(R.id.main_tv_audio)
    TextView mMainTvAudio;
    @BindView(R.id.main_indicate_line)
    View mMainIndicateLine;
    @BindView(R.id.main_viewpager)
    ViewPager mMainViewpager;
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            updateTab(position, mMainTvVideo, 0);
            updateTab(position, mMainTvAudio, 1);

            // 更新底部状态指示器
            float translation = position * mMainIndicateLine.getWidth();
            ViewCompat.animate(mMainIndicateLine).translationX(translation);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private MainActivityPagerAdatper mMainActivityPagerAdatper;
    private List<Fragment> mFragmentList;

    public static void start(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initListener() {
        mMainActivityPagerAdatper = new MainActivityPagerAdatper(getSupportFragmentManager(), mFragmentList);
        mMainViewpager.setAdapter(mMainActivityPagerAdatper);
        mMainViewpager.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    protected void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new VideoFragment());
        mFragmentList.add(new MusicFragment());
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        // 设置指示器宽度为屏幕的一半
        mMainIndicateLine.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels / 2;
        updateTab(0, mMainTvVideo, 0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void updateTab(int position, TextView textView, int tabPosition) {
        int selectedColor = getResources().getColor(R.color.main_title_text_selected_color, null);
        int normalColor = getResources().getColor(R.color.main_title_text_normal_color, null);

        textView.setTextColor(position == tabPosition ? selectedColor : normalColor);

        ViewCompat.animate(textView).scaleX(position == tabPosition ? 1.3f : 1.0f)
                .scaleY(position == tabPosition ? 1.3f : 1.0f);
    }

    @OnClick({R.id.main_tv_video, R.id.main_tv_audio})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tv_video:
                mMainViewpager.setCurrentItem(0);
                break;
            case R.id.main_tv_audio:
                mMainViewpager.setCurrentItem(1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopMusicPlayerService();
    }

    private void stopMusicPlayerService() {
        boolean musicState = ServiceStateUtils.isServiceRunning(MainActivity.this, "com.coderwjq.mediaplayer.service.MusicPlayerService");

        if (!musicState) {
            return;
        }

        stopService(new Intent(MainActivity.this, MusicPlayerService.class));
    }
}

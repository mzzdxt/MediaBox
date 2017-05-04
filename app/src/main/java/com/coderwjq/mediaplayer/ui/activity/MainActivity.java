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
import com.coderwjq.mediaplayer.ui.fragment.MusicFragment;
import com.coderwjq.mediaplayer.ui.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
            // 获得起始位置
            int startX = position * mMainIndicateLine.getWidth();

            // 获得偏移位置
            int offsetX = (int) (positionOffset * mMainIndicateLine.getWidth());

            // 求得移动距离
            int translationX = startX + offsetX;

            // 移动指示器
            ViewCompat.animate(mMainIndicateLine).translationX(translationX);
        }

        @Override
        public void onPageSelected(int position) {
            updateTab(position, mMainTvVideo, 0);
            updateTab(position, mMainTvAudio, 1);
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
    protected void initEvent() {
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
}

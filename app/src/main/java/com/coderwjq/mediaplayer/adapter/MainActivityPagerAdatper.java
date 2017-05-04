package com.coderwjq.mediaplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @Created by coderwjq on 2017/5/4 12:06.
 * @Desc
 */

public class MainActivityPagerAdatper extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    public MainActivityPagerAdatper(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}

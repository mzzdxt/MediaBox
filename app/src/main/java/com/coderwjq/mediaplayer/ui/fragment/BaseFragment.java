package com.coderwjq.mediaplayer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Created by coderwjq on 2017/5/4 11:27.
 * @Desc
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), getLayoutId(), null);
        initView(view);
        initData();
        initEvent();
        return view;
    }

    protected abstract void initEvent();

    protected abstract void initData();

    protected abstract void initView(View view);

    protected abstract int getLayoutId();
}

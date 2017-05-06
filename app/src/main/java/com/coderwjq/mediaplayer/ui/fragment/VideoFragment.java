package com.coderwjq.mediaplayer.ui.fragment;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore.Video.Media;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.coderwjq.mediaplayer.R;
import com.coderwjq.mediaplayer.adapter.VideoListAdapter;
import com.coderwjq.mediaplayer.bean.VideoItem;
import com.coderwjq.mediaplayer.db.MediaAsyncQueryHandler;
import com.coderwjq.mediaplayer.ui.activity.VideoPlayerActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @Created by coderwjq on 2017/5/4 11:29.
 * @Desc
 */

public class VideoFragment extends BaseFragment {
    private static final String TAG = "VideoFragment";

    @BindView(R.id.common_list_view)
    ListView mCommonListView;
    private VideoListAdapter mVideoListAdapter;

    @Override
    protected void initListener() {
        mCommonListView.setAdapter(mVideoListAdapter);
        mCommonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取到被点击的数据
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                ArrayList<VideoItem> listData = VideoItem.getListData(cursor);
                // 打开新界面进行播放
                VideoPlayerActivity.invoke(getActivity(), listData, position);
            }
        });
    }

    @Override
    protected void initData() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        AsyncQueryHandler asyncQueryHandler = new MediaAsyncQueryHandler(contentResolver);

        mVideoListAdapter = new VideoListAdapter(getActivity(), null);
        asyncQueryHandler.startQuery(0, mVideoListAdapter,
                Media.EXTERNAL_CONTENT_URI,
                new String[]{Media._ID, Media.TITLE, Media.SIZE, Media.DURATION, Media.DATA},
                null, null, null);
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

}

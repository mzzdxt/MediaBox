package com.coderwjq.mediaplayer.ui.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.coderwjq.mediaplayer.R;
import com.coderwjq.mediaplayer.adapter.MusicListAdapter;
import com.coderwjq.mediaplayer.bean.MusicItem;
import com.coderwjq.mediaplayer.db.MediaAsyncQueryHandler;
import com.coderwjq.mediaplayer.ui.activity.MusicPlayerActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @Created by coderwjq on 2017/5/4 11:30.
 * @Desc
 */

public class MusicFragment extends BaseFragment {
    private static final String TAG = "MusicFragment";
    @BindView(R.id.common_list_view)
    ListView mCommonListView;
    private MusicListAdapter mMusicListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {
        ContentResolver contentResolver = getActivity().getContentResolver();

        MediaAsyncQueryHandler mediaAsyncQueryHandler = new MediaAsyncQueryHandler(contentResolver);

        mMusicListAdapter = new MusicListAdapter(getActivity(), null);
        mediaAsyncQueryHandler.startQuery(1, mMusicListAdapter, Media.EXTERNAL_CONTENT_URI,
                new String[]{Media._ID, Media.DATA, Media.ARTIST, Media.TITLE, Media.DURATION, Media.SIZE},
                null, null, null);
    }

    @Override
    protected void initListener() {
        mCommonListView.setAdapter(mMusicListAdapter);
        mCommonListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                ArrayList<MusicItem> listData = MusicItem.getListData(cursor);
                MusicPlayerActivity.invoke(getActivity(), listData, position);
                Log.e(TAG, "onItemClick: " + listData.get(position));
            }
        });
    }


}

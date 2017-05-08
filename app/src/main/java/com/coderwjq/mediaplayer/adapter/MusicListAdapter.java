package com.coderwjq.mediaplayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coderwjq.mediaplayer.R;
import com.coderwjq.mediaplayer.bean.MusicItem;
import com.coderwjq.mediaplayer.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created by coderwjq on 2017/5/6 18:20.
 * @Desc
 */

public class MusicListAdapter extends CursorAdapter {

    public MusicListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public MusicListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public MusicListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_music_list, null);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        MusicItem musicItem = new MusicItem(cursor);
        viewHolder.mTvMusicTitle.setText(musicItem.getTitle());
        viewHolder.mTvMusicArtist.setText(musicItem.getArtist());
        viewHolder.mTvMusicDuration.setText(StringUtils.formatDuration(musicItem.getDuration()));
    }

    class ViewHolder {
        @BindView(R.id.tv_music_title)
        TextView mTvMusicTitle;
        @BindView(R.id.tv_music_artist)
        TextView mTvMusicArtist;
        @BindView(R.id.tv_music_duration)
        TextView mTvMusicDuration;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

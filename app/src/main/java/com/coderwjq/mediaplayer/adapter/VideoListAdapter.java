package com.coderwjq.mediaplayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coderwjq.mediaplayer.R;
import com.coderwjq.mediaplayer.bean.VideoItem;
import com.coderwjq.mediaplayer.utils.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created by coderwjq on 2017/5/4 16:55.
 * @Desc
 */

public class VideoListAdapter extends CursorAdapter {
    public VideoListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public VideoListAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public VideoListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_video_list, null);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        VideoItem videoItem = new VideoItem(cursor);

        viewHolder.mMainListItemTvDuration.setText(StringUtils.formatDuration(videoItem.getDuration()));
        viewHolder.mMainListItemTvSize.setText(Formatter.formatFileSize(context, videoItem.getSize()));
        viewHolder.mMainListItemTvTitle.setText(videoItem.getTitle());
    }

    static class ViewHolder {
        @BindView(R.id.main_list_item_tv_title)
        TextView mMainListItemTvTitle;
        @BindView(R.id.main_list_item_tv_duration)
        TextView mMainListItemTvDuration;
        @BindView(R.id.main_list_item_tv_size)
        TextView mMainListItemTvSize;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

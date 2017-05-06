package com.coderwjq.mediaplayer.bean;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * @Created by coderwjq on 2017/5/4 17:32.
 * @Desc
 */

public class VideoItem implements Parcelable {
    public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>() {
        @Override
        public VideoItem createFromParcel(Parcel source) {
            return new VideoItem(source);
        }

        @Override
        public VideoItem[] newArray(int size) {
            return new VideoItem[size];
        }
    };
    private String title;
    private int duration;
    private int size;
    private String path;

    public VideoItem() {
    }

    /**
     * 从cursor中解析出对象
     *
     * @param cursor
     */
    public VideoItem(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            new RuntimeException("null cursor");
        }

        this.title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
        this.duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
        this.size = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
        this.path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
    }

    public VideoItem(String title, int duration, int size, String path) {
        this.title = title;
        this.duration = duration;
        this.size = size;
        this.path = path;
    }

    protected VideoItem(Parcel in) {
        this.title = in.readString();
        this.duration = in.readInt();
        this.path = in.readString();
        this.size = in.readInt();
    }

    /**
     * 从cursor中解析出对象集合
     *
     * @param cursor
     */
    public static ArrayList<VideoItem> getListData(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            new RuntimeException("null cursor");
        }
        ArrayList<VideoItem> videoItems = new ArrayList<>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            VideoItem videoItem = new VideoItem(cursor);
            videoItems.add(videoItem);
        }
        return videoItems;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.duration);
        dest.writeString(this.path);
        dest.writeInt(this.size);
    }
}

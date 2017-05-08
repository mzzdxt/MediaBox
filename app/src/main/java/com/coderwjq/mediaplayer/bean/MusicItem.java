package com.coderwjq.mediaplayer.bean;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * @Created by coderwjq on 2017/5/7 14:07.
 * @Desc
 */

public class MusicItem implements Parcelable {
    public static final Parcelable.Creator<MusicItem> CREATOR = new Parcelable.Creator<MusicItem>() {
        @Override
        public MusicItem createFromParcel(Parcel source) {
            return new MusicItem(source);
        }

        @Override
        public MusicItem[] newArray(int size) {
            return new MusicItem[size];
        }
    };
    /**
     * Media.DATA, Media.ARTIST, Media.TITLE, Media.DURATION, Media.SIZE
     */
    private String title;
    private String artist;
    private long duration;
    private String path;
    private long size;

    public MusicItem(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            new RuntimeException("null cursor");
        }

        this.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        this.duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        this.size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
        this.path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        this.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
    }

    public MusicItem(String title, String artist, long duration, String path, long size) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
        this.size = size;
    }

    protected MusicItem(Parcel in) {
        this.title = in.readString();
        this.artist = in.readString();
        this.duration = in.readLong();
        this.path = in.readString();
        this.size = in.readLong();
    }

    /**
     * 从cursor中解析出对象集合
     *
     * @param cursor
     */
    public static ArrayList<MusicItem> getListData(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            new RuntimeException("null cursor");
        }
        ArrayList<MusicItem> musicItems = new ArrayList<>();
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            MusicItem musicItem = new MusicItem(cursor);
            musicItems.add(musicItem);
        }
        return musicItems;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "MusicItem{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", path='" + path + '\'' +
                ", size=" + size +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.artist);
        dest.writeLong(this.duration);
        dest.writeString(this.path);
        dest.writeLong(this.size);
    }
}

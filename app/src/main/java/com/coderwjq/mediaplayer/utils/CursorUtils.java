package com.coderwjq.mediaplayer.utils;

import android.database.Cursor;

import com.litesuits.android.log.Log;

public class CursorUtils {
    private static final String TAG = "CursorUtils";

    /**
     * 打印cursor里的内容
     */
    public static void printCursor(Cursor cursor) {
        Log.e(TAG, "CursorUtils.printCursor.查询到的数据个数为:" + cursor.getCount());
        while (cursor.moveToNext()) {
            Log.e(TAG, "===================================");
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Log.e(TAG, "CursorUtils.printCursor.name=" + cursor.getColumnName(i) + ";value=" + cursor.getString(i));
            }
        }
    }
}

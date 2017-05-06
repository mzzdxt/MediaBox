package com.coderwjq.mediaplayer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    private static final int HOUR = 60 * 60 * 1000;
    private static final int MIN = 60 * 1000;
    private static final int SEC = 1000;

    public static String formatDuration(int duration) {
        String time = null;

        // 计算小时数
        int hour = duration / HOUR;

        // 计算分钟数
        int min = (duration % HOUR) / MIN;

        // 计算秒数
        int sec = (duration % MIN) / SEC;

        if (hour == 0) {
            // 01:01
            time = String.format("%02d:%02d", min, sec);
        } else {
            // 01:01:01
            time = String.format("%02d:%02d:%02d", hour, min, sec);
        }

        return time;
    }

    public static String formatDuration(long duration) {
        String time = null;

        // 计算小时数
        long hour = duration / HOUR;

        // 计算分钟数
        long min = (duration % HOUR) / MIN;

        // 计算秒数
        long sec = (duration % MIN) / SEC;

        if (hour == 0) {
            // 01:01
            time = String.format("%02d:%02d", min, sec);
        } else {
            // 01:01:01
            time = String.format("%02d:%02d:%02d", hour, min, sec);
        }

        return time;
    }

    public static String formatSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }
}

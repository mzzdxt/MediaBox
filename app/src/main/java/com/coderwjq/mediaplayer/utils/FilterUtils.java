package com.coderwjq.mediaplayer.utils;

/**
 * @Created by coderwjq on 2017/5/6 10:54.
 * @Desc 数据过滤工具
 */

public class FilterUtils {
    public static float brightnessFilter(float value) {
        if (value <= 0.0f) {
            value = 0.0f;
        } else if (value >= 1.0f) {
            value = 1.0f;
        }
        return value;
    }

    public static int volumeFilter(int value, int maxValue) {
        if (value <= 0) {
            value = 0;
        } else if (value >= maxValue) {
            value = maxValue;
        }
        return value;
    }
}

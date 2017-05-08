package com.coderwjq.mediaplayer.utils;

import android.graphics.Color;

import com.coderwjq.mediaplayer.common.MyApplication;
import com.litesuits.common.utils.RandomUtil;

/**
 * @Created by coderwjq on 2017/5/8 16:41.
 * @Desc
 */

public class UIUtils {
    public static int dp2px(int dp) {
        float density = MyApplication.mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    public static int px2dp(int px) {
        float density = MyApplication.mContext.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);
    }

    public static int getRandomColor() {
        return Color.rgb(RandomUtil.getRandom(40, 200), RandomUtil.getRandom(40, 200), RandomUtil.getRandom(40, 200));
    }

}

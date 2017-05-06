package com.coderwjq.mediaplayer.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @Created by coderwjq on 2017/5/6 10:18.
 * @Desc 单个Activity屏幕亮度调整的工具类
 */

public class ActivityBrightnessManager {
    /**
     * 设置当前activity的屏幕亮度
     *
     * @param paramFloat 0-1.0f
     * @param activity   需要调整亮度的activity
     */
    public static void setActivityBrightness(float paramFloat, Activity activity) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams params = localWindow.getAttributes();
        params.screenBrightness = paramFloat;
        localWindow.setAttributes(params);
    }

    /**
     * 获取当前activity的屏幕亮度
     *
     * @param activity 当前的activity对象
     * @return 亮度值范围为0-0.1f，如果为-1.0，则亮度与全局同步。
     */
    public static float getActivityBrightness(Activity activity) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams params = localWindow.getAttributes();
        return params.screenBrightness;
    }

    /**
     setActivityBrightness()方法
     1、这里设置传入的参数是0-1.0f。
     2、一旦传入参数，则当前的亮度属性完全独立，不会受到全局亮度属性的影响，并且没有自动模式。
     3、当传入参数-1.0f，当前的activity就又会重新使用全局的亮度属性。

     getActivityBrightness()方法
     1、在没有设置属性值之前，默认返回的是-1.0。代表使用全局亮度属性。
     2、设置属性值之后，返回的是当前属性值。
     */
}

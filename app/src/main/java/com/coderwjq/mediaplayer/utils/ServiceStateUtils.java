package com.coderwjq.mediaplayer.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 动态判断服务的运行状态
 */
public class ServiceStateUtils {

    public static boolean isServiceRunning(Context context, String clazz) {
        //获取到设备的相关服务
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取正在运行的前100个服务
        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : serviceInfos) {
            if (clazz.equals(info.service.getClassName())) {
                //说明服务开启啦
                return true;

            }
        }
        return false;
    }
}

package com.example.taobaounion.utils;

import android.util.Log;

/**
 * @author WuChangJian
 * @date 2020/6/16 21:46
 */
public class LogUtils {
    private static int currentLev = 4;
    private static final int DEBUG_LEV = 4;
    private static final int INFO_LEV = 3;
    private static final int WARNING_LEV = 2;
    private static final int ERROR_LEV = 1;

    public static void d(Object object, String msg) {
        if (currentLev >= DEBUG_LEV) {
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }
    public static void i(Object object, String msg) {
        if (currentLev >= INFO_LEV) {
            Log.i(object.getClass().getSimpleName(), msg);
        }
    }
    public static void w(Object object, String msg) {
        if (currentLev >= WARNING_LEV) {
            Log.w(object.getClass().getSimpleName(), msg);
        }
    }
    public static void e(Object object, String msg) {
        if (currentLev >= ERROR_LEV) {
            Log.e(object.getClass().getSimpleName(), msg);
        }
    }
}

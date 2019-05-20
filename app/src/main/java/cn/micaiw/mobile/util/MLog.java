package cn.micaiw.mobile.util;

import android.content.Context;
import android.util.Log;

/**
 * 作者：凌涛 on 2018/5/21 16:34
 * 邮箱：771548229@qq..com
 */
public class MLog {

    private static final boolean isDebug = true;
    private static String TAG = "lingtao";

    public static void log(String tag, String content) {
        TAG = tag;
        if (isDebug) {
            Log.d(TAG, content);
        }
    }

    public static void log(String content) {
        if (isDebug) {
            Log.d(TAG, content);
        }
    }

}

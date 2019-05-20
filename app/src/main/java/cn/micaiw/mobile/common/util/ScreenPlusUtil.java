package cn.micaiw.mobile.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.changelcai.mothership.view.util.ScreenUtil;

import java.lang.reflect.Field;

/**
 * Created by ckerv on 2018/1/22.
 */

public class ScreenPlusUtil {


    public static int getScreenHeight(Context context) {
        return ScreenUtil.getScreenHeight(context);
    }

    public static int getScreenWidth(Context context) {
        return ScreenUtil.getScreenWidth(context);
    }

    public static int getNavigationBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

}

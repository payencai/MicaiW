package cn.micaiw.mobile.common.util;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Created by ckerv on 2018/1/8.
 */

public class DrawableUtil {

    public static int[] getDrawableResIds(Context context, int originArrayId) {
        TypedArray ar = context.getResources().obtainTypedArray(originArrayId);
        int len = ar.length();
        int[] resIds = new int[len];
        for (int i = 0; i < len; i++)
            resIds[i] = ar.getResourceId(i, 0);
        ar.recycle();
        return resIds;
    }
}

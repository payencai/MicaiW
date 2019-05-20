package cn.micaiw.mobile.common.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ckerv on 2018/1/16.
 */

public class ActivityAnimationUtil {

    public static void startActivityWithAnim(Activity context, Class toStratAc, int animResId, int exitAnim, Bundle bundle) {
        Intent intent = new Intent(context, toStratAc);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        context.overridePendingTransition(animResId, exitAnim);
    }
}

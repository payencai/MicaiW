package cn.micaiw.mobile.base.listener;

import android.view.View;

/**
 * Created by ckerv on 2018/2/8.
 */

public interface OnItemSubviewClickListener<T> {

    void onClick(View v, int pos, T model);
}

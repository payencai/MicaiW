package cn.micaiw.mobile.base.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.changelcai.mothership.component.fragment.dialog.MSDialogFragmentHelper;

/**
 * Created by ckerv on 2018/1/7.
 */

public class DialogHelper extends MSDialogFragmentHelper {

    public static Dialog showCustomDialog(View view, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(view);
        builder.setCancelable(cancelable);
        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static Dialog showCustomDialog(View view, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(view);
        builder.setCancelable(true);
        builder.setPositiveButton("确定", positiveListener);
        builder.setNegativeButton("取消", negativeListener);
        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

}

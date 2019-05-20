package cn.micaiw.mobile.base.component;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.changelcai.mothership.component.activity.MSBaseListActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.micaiw.mobile.MyApplication;
import cn.micaiw.mobile.base.util.DialogHelper;

/**
 * Created by ckerv on 2018/1/12.
 */

public abstract class BaseListActivity extends MSBaseListActivity {

    private Unbinder mUnbinder;
    private DialogFragment mLoadingDialog;

    @Override
    protected void init(Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    protected void showLoading(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = DialogHelper.showProgress(getSupportFragmentManager(), msg, true);
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyApplication.restoreAppCxt(this);
    }
}

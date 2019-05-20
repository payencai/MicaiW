package cn.micaiw.mobile.base.component;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.changelcai.mothership.component.fragment.MSBaseListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.micaiw.mobile.base.util.DialogHelper;

/**
 * Created by ckerv on 2018/1/7.
 */

public abstract class BaseListFragment<E> extends MSBaseListFragment {

    private Unbinder mUnbinder;
    private DialogFragment mLoadingDialog;
    private List<E> mItems;


    @Override
    final protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, getContentView());
        mItems = new ArrayList<>();
    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        mUnbinder.unbind();
    }

    protected void showLoading(String msg) {
        boolean isDestroy;
        if (Build.VERSION.SDK_INT > 16) {
            isDestroy = getActivity().isDestroyed();
        } else {
            isDestroy = getActivity().isFinishing();
        }
        if (mLoadingDialog == null && !isDestroy)
            mLoadingDialog = DialogHelper.showProgress(getChildFragmentManager(), msg, true);
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    protected List<E> getItemList() {
        return mItems;
    }

}

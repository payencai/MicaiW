package cn.micaiw.mobile.common.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import cn.micaiw.mobile.base.component.BaseActivity;


/**
 * Created by ckerv on 2018/1/7.
 */

public abstract class CommonTitleBarActivity extends BaseActivity {

    protected ViewGroup mTitleBar;


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mTitleBar = (ViewGroup) findViewById(getTitleBarId());
    }

    /**
     * TitleBar里面的具体控件由子类业务定制。
     * @return
     */
    protected abstract int getTitleBarId();

    protected ViewGroup getTitleBar() {
        return mTitleBar;
    }
}

package cn.micaiw.mobile.common.activity;

import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.micaiw.mobile.base.component.BaseMultiTabActivity;
import cn.micaiw.mobile.view.CommonTabBean;

/**
 * 通用的多Tab的Activity，Tab实体为{@link CommonTabBean}
 * 子类定制Tab的名称、选中图标和未选中图标。
 * Created by ckerv on 2018/1/7.
 */

public abstract class CommonMultiTabActivity extends BaseMultiTabActivity<CommonTabBean> {

    protected ViewGroup mTitleBar;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTitleBar = (ViewGroup) findViewById(getTitleBarId());
    }

    @Override
    protected ArrayList<CommonTabBean> initTabList() {
        ArrayList<CommonTabBean> tabList = new ArrayList<>();
        String[] tabNames = getTabNames();
        for (int i = 0; i < tabNames.length; i++) {
            tabList.add(new CommonTabBean(tabNames[i], getTabSelectedIcons()[i], getTabUnSelectedIcons()[i]));
        }
        return tabList;
    }

    protected abstract String[] getTabNames();

    protected abstract int[] getTabSelectedIcons();

    protected abstract int[] getTabUnSelectedIcons();

    /**
     * TitleBar里面的具体控件由子类业务定制。
     * @return
     */
    protected abstract int getTitleBarId();

    protected ViewGroup getTitleBar() {
        return mTitleBar;
    }
}

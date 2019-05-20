package cn.micaiw.mobile.common.activity;

import java.util.ArrayList;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.view.CommonTabBean;

/**
 * Tab只有一个文案的通用多Tab的Activity,
 * 注意：布局通用，Tab统一在上，ViewPager在Tab之下。
 * Created by ckerv on 2018/1/7.
 */

public abstract class CommonMultiTabOnlyTextActivity extends CommonMultiTabActivity {

    @Override
    protected int[] getTabUnSelectedIcons() {
        return new int[0];
    }

    @Override
    protected int[] getTabSelectedIcons() {
        return new int[0];
    }

    /**
     * 重写
     * @return
     */
    @Override
    protected ArrayList<CommonTabBean> initTabList() {
        ArrayList<CommonTabBean> tabList = new ArrayList<>();
        String[] tabNames = getTabNames();
        for (int i = 0; i < tabNames.length; i++) {
            tabList.add(new CommonTabBean(tabNames[i]));
        }
        return tabList;
    }

    @Override
    protected int getViewPagerId() {
        return R.id.common_mt_vp;
    }

    @Override
    protected int getCommonTabLayoutId() {
        return R.id.common_mt_tab_layout;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_common_multi_tab_onlytext;
    }

    @Override
    protected int getTitleBarId() {
        return R.id.common_mt_titlebar;
    }
}

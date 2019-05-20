package cn.micaiw.mobile.base.component;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import cn.micaiw.mobile.common.customview.CommonFragmentPagerAdapter;

/**
 * 多个Fragment结合Tab的Activity父类。
 *
 * Base原则：
 *      1.FragmentList由实现子类添加；
 *      2.TabList由实现子类添加；
 *      3.ViewPager与CommonTabLayout只实现基本联动，
 *          1）若要实现对Tab的点击监听，请子类实现{@link BaseMultiTabActivity#onTabSelect(int)}
 *             和{@link BaseMultiTabActivity#onTabReselect(int)}方法；
 *          2）若要实现对ViewPager的监听，则直接在子类调用mViewPager.addOnPageChangeListener(OnPageChangeListener)方法。
 * Created by ckerv on 2018/1/7.
 */

public abstract class BaseMultiTabActivity<T extends CustomTabEntity> extends BaseActivity {

    protected ViewPager mViewPager;
    protected CommonTabLayout mCommonTabLayout;

    private FragmentManager mFragmentManager;
    private List<Fragment> mFragmentsList;
    private ArrayList<T> mTabList;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        mViewPager = (ViewPager) findViewById(getViewPagerId());
        mCommonTabLayout = (CommonTabLayout) findViewById(getCommonTabLayoutId());

        mFragmentManager = getSupportFragmentManager();

        mFragmentsList = initFragmentList();
        mTabList = initTabList();

        initViewPager();
        initCommonTabLayout();
    }

    private void initViewPager() {
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(mFragmentManager, mFragmentsList);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCommonTabLayout.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(mTabList.size());
    }


    private void initCommonTabLayout() {
        mCommonTabLayout.setTabData((ArrayList<CustomTabEntity>) mTabList);
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
                BaseMultiTabActivity.this.onTabSelect(position);
            }

            @Override
            public void onTabReselect(int position) {
                BaseMultiTabActivity.this.onTabReselect(position);
            }
        });
        mCommonTabLayout.setCurrentTab(0);
    }

    protected void onTabSelect(int position) {

    }

    protected void onTabReselect(int postion) {

    }

    protected abstract int getViewPagerId();

    protected abstract int getCommonTabLayoutId();

    protected abstract List<Fragment> initFragmentList();

    protected abstract ArrayList<T> initTabList();
}

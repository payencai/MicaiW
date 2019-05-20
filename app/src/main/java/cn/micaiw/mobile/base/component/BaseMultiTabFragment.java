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
 * Created by ckerv on 2018/1/8.
 */

public abstract class BaseMultiTabFragment<T extends CustomTabEntity> extends BaseFragment {
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

        mFragmentManager = getActivity().getSupportFragmentManager();

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
                BaseMultiTabFragment.this.onTabSelect(position);
            }

            @Override
            public void onTabReselect(int position) {
                BaseMultiTabFragment.this.onTabReselect(position);
            }
        });
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

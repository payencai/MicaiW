package cn.micaiw.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.adapter.MyFragmentAdapter;
import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.base.component.CommonFragment;

/**
 * Created by Administrator on 2018/3/21 0021.
 * P2P页
 */

public class MoreP2PFragment extends BaseFragment {
//    @BindView(R.id.more_p2p_tab_layout)
//    SlidingTabLayout mCommonTabLayout;
//    @BindView(R.id.more_p2p_vp)
//    ViewPager mViewPager;
//
//    private List<Fragment> mFragments;
//
//    @Override
//    protected int getContentViewId() {
//        return R.layout.fragment_more_p2p;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        super.init(savedInstanceState);
//        initFragmentList();
//        initViewPager();
//        initTabLayout();
//    }
//
//    private void initViewPager() {
//        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getFragmentManager(), mFragments);
//        mViewPager.setAdapter(adapter);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                mCommonTabLayout.setCurrentTab(position);
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//        mViewPager.setCurrentItem(0);
//        mViewPager.setOffscreenPageLimit(Constans.LCL_TAB_NAMES.length);
//    }
//
//    private void initTabLayout() {
//        mCommonTabLayout.setViewPager(mViewPager, Constans.LCL_TAB_NAMES);
//        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelect(int position) {
//                mViewPager.setCurrentItem(position);
//            }
//
//            @Override
//            public void onTabReselect(int position) {
//            }
//        });
//    }
//
//    protected void initFragmentList() {
//        mFragments = new ArrayList<>();
//        mFragments.add(new P2PListFragment());
//        mFragments.add(new P2PListFragment());
//        mFragments.add(new P2PListFragment());
//        mFragments.add(new P2PListFragment());
//        mFragments.add(new P2PListFragment());
//        mFragments.add(new P2PListFragment());
//        mFragments.add(new P2PListFragment());
//        mFragments.add(new P2PListFragment());
//    }

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.p2p_tab_layout)
    SlidingTabLayout mCommonTabLayout;
    private String[] titles = new String[]{"养生", "保健", "男性", "女性", "呼吸道", "保健", "男性", "女性", "呼吸道", "保健", "男性", "女性", "呼吸道"};

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_p2_pfragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initViewPager();
        initTabLayout();
    }


    private void initViewPager() {
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            list.add(CommonFragment.getInstance(titles[i] + i, i));
        }
        MyFragmentAdapter adapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager(), list, titles);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(titles.length);//预加载
    }

    private void initTabLayout() {
        mCommonTabLayout.setViewPager(viewPager, titles);
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }
}

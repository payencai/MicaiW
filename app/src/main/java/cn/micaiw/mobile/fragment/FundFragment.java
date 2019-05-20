package cn.micaiw.mobile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.adapter.MyFragmentAdapter;
import cn.micaiw.mobile.base.component.BaseFragment;

public class FundFragment extends BaseFragment {

    @BindView(R.id.viewpager_fund)
    ViewPager viewPager;
    @BindView(R.id.p2p_tab_layout_fund)
    SlidingTabLayout mCommonTabLayout;
    private String[] fundTitles = new String[]{"米财推荐", "公募基金", "基金排行", "热销基金", "私募专区", "定投专区", "基金社区", "投教基地"};

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_p2_pfragment3;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initViewPager();
        initTabLayout();
    }


    private void initViewPager() {
        List<Fragment> list = new ArrayList<>();
        for (int i = 0; i < fundTitles.length; i++) {
            P2PItemFundFragment instance = P2PItemFundFragment.getInstance(fundTitles[i], 1);
            list.add(instance);
//            list.add(new MyFragment());
        }
        MyFragmentAdapter adapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager(), list, fundTitles);
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
        viewPager.setOffscreenPageLimit(fundTitles.length);//预缓存
    }

    private void initTabLayout() {
        mCommonTabLayout.setViewPager(viewPager, fundTitles);
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

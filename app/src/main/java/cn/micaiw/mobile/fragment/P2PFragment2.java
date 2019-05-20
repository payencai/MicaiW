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
import cn.micaiw.mobile.adapter.MyFragmentAdapter2;
import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.base.component.CommonFragment;
import cn.micaiw.mobile.base.component.CommonFragment2;

public class P2PFragment2 extends BaseFragment {

    //    @BindView(R.id.tabLayout)
//    TabLayout tabLayout;
    @BindView(R.id.viewpager2)
    ViewPager viewPager2;
    @BindView(R.id.p2p_tab_layout2)
    SlidingTabLayout mCommonTabLayout2;
    private String[] titles = new String[]{"全部","高评分", "大额区", "小额区", "高返利", "存管系", "风投系", "上市系", "民营系"};

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_p2_pfragment2;
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
            list.add(P2PPageFragment.getInstance(titles[i], 2));
        }
        MyFragmentAdapter adapter = new MyFragmentAdapter(getActivity().getSupportFragmentManager(), list, titles);
        viewPager2.setAdapter(adapter);
        viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCommonTabLayout2.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager2.setCurrentItem(0);
        viewPager2.setOffscreenPageLimit(titles.length);//预缓存
    }

    private void initTabLayout() {
        mCommonTabLayout2.setViewPager(viewPager2, titles);
        mCommonTabLayout2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager2.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }
}

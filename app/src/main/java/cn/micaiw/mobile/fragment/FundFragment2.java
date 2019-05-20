package cn.micaiw.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.adapter.MyFragmentAdapter;
import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.common.commonRv.base.Cell;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.common.commonRv.fragment.AbsBaseFragment;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.FundBean;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

public class FundFragment2 extends BaseFragment {
    @BindView(R.id.viewpager_fund2)
    ViewPager viewPager;
    @BindView(R.id.p2p_tab_layout_fund2)
    SlidingTabLayout mCommonTabLayout;
    private String[] fundTitles = new String[]{"米财推荐", "公募基金", "基金排行", "热销基金", "私募专区", "定投专区", "基金社区", "投教基地"};

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_p2_pfragment4;
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
            P2PItemFundFragment instance = P2PItemFundFragment.getInstance(fundTitles[i], 2);
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

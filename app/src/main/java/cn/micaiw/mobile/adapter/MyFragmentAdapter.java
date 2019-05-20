package cn.micaiw.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.base.component.CommonFragment;

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    private String[] titles;

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> list, String[] titles) {
        super(fm);
        this.list = list;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}

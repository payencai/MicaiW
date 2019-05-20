package cn.micaiw.mobile.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.adapter.MyFragmentAdapter;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.fragment.NewsFragment;

public class MCaiHeadlineActivity extends BaseActivity {

    @BindView(R.id.lineTablayout)
    TabLayout lineTablayout;
    @BindView(R.id.lineViewpager)
    ViewPager lineViewpager;


    private static final String[] titles = new String[]{"今日要闻", "理财", "基金",};
    private List<Fragment> mFragments;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_mcai_headline;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mFragments = new ArrayList<>();

        mFragments.add(NewsFragment.getInstance(1, 1));
        mFragments.add(NewsFragment.getInstance(1, 2));
        mFragments.add(NewsFragment.getInstance(1, 3));
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), mFragments, titles);
        lineViewpager.setAdapter(adapter);
        lineViewpager.setOffscreenPageLimit(titles.length);
        lineTablayout.setupWithViewPager(lineViewpager);
    }

    @OnClick({R.id.backImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;

        }
    }


}

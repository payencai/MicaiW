package cn.micaiw.mobile.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.fragment.DetailFragment;

public class IntegraexChangelActivity extends BaseActivity {


    @BindView(R.id.fragment_container)
    FrameLayout fragment_container;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_integraex_changel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        DetailFragment fragment = new DetailFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragment).commit();
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

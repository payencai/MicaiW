package cn.micaiw.mobile.activity;

import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;

public class UseProtocolActivity extends BaseActivity {


    @Override
    protected int getContentViewId() {
        return R.layout.activity_use_protocol;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }


    @OnClick(R.id.backImg)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
        }

    }
}

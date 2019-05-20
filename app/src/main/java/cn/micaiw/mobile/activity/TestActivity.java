package cn.micaiw.mobile.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.custom.ScrollerLayout;
import cn.micaiw.mobile.custom.SmartScrollView;
import cn.micaiw.mobile.fragment.HomePageFragment;
import cn.micaiw.mobile.util.ToaskUtil;

public class TestActivity extends AppCompatActivity implements SmartScrollView.ISmartScrollChangedListener {
    private ScrollerLayout mScrollerLayout;
    @BindViews({R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6})
    List<TextView> mViewList = new ArrayList<>();

    boolean isRunning = true;
    SmartScrollView smartScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        smartScrollView = (SmartScrollView) findViewById(R.id.smartScrollView);
        smartScrollView.setScanScrollChangedListener(this);
        mScrollerLayout = (ScrollerLayout) findViewById(R.id.scrollerLayoutLL);
        String[] contents = {"我的剑，就是你的剑!",
                "俺也是从石头里蹦出来得!",
                "人在塔在!",
                "犯我德邦者，虽远必诛!",
                "我会让你看看什么叫残忍!",
                "我的大刀早已饥渴难耐了!"};
        for (int i = 0; i < mViewList.size(); i++) {
            mViewList.get(i).setText(contents[i]);
        }

        findViewById(R.id.profile_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoundTelActivity.stateBoundActivity(TestActivity.this, "qqid", 0);
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Override
    public void onScrolledToBottom() {
        ToaskUtil.showToast(this, "滑动到底部");
    }

    @Override
    public void onScrolledToTop() {
        ToaskUtil.showToast(this, "滑动到顶部");
    }
}

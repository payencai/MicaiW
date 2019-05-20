package cn.micaiw.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import butterknife.BindView;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.fragment.AllPlatfromFragment;
import cn.micaiw.mobile.util.ToaskUtil;

public class AllPlatfromInfoActivity extends BaseActivity implements AllPlatfromFragment.OnItemClickListener {

    @BindView(R.id.frameLayoutRv)
    FrameLayout frameLayoutRv;

    @BindView(R.id.backImg)
    ImageView backImg;
    private AllPlatfromFragment mFragment;

    public int companyType;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_all_platfrom_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        companyType = getIntent().getIntExtra("companyType", 1);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mFragment = new AllPlatfromFragment();
        mFragment.setOnitemclicklistener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayoutRv, mFragment).commit();
    }


    @Override
    public void onItemClick(int id, String platfromName) {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        intent.putExtra("platformName", platfromName);
        setResult(0, intent);
        finish();
    }
}

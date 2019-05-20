package cn.micaiw.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;

/**
 * 基金详情页
 */
public class FundDetailsActivity extends BaseActivity {


    @BindView(R.id.titleHead)
    TextView titleHead;
    @BindView(R.id.platformBenner)
    ImageView platformBennerImg;
    @BindView(R.id.platformName)
    TextView platformNameText;
    @BindView(R.id.brightSpot)
    TextView brightSpotText;
    @BindView(R.id.annualRate)
    TextView annualRateText;
    @BindView(R.id.platformUrl)
    Button platformUrlBtn;
    @BindView(R.id.platformDetailsImg)
    ImageView platformDetailsImgView;
    private String mPlatformUrl;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_fund_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getIntent();
        String platformName = intent.getStringExtra("platformName");
        String annualRate = intent.getStringExtra("annualRate");
        String platformBenner = intent.getStringExtra("platformBenner");
        String platformDetailsImg = intent.getStringExtra("platformDetailsImg");
        mPlatformUrl = intent.getStringExtra("platformUrl");
        String brightSpot = intent.getStringExtra("brightSpot");
        titleHead.setText(platformName);
        annualRateText.setText("+" + annualRate + "%");
        Glide.with(this).load(platformBenner).placeholder(R.mipmap.logo).error(R.mipmap.logo).into(platformBennerImg);
        Glide.with(this).load(platformDetailsImg).placeholder(R.mipmap.logo).error(R.mipmap.logo).into(platformDetailsImgView);
        brightSpotText.setText(brightSpot);
    }

    @OnClick({R.id.backImg, R.id.share,R.id.platformUrl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.share:
                break;
            case R.id.platformUrl:
                WebViewActivity.starUi(this, mPlatformUrl, "平台官网");
                break;
        }
    }

}

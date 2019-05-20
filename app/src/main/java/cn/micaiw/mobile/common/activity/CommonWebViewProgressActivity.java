package cn.micaiw.mobile.common.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.changelcai.mothership.utils.Check;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseWebViewProgressActivity;

/**
 * Created by ckerv on 2018/1/22.
 */

public class CommonWebViewProgressActivity extends BaseWebViewProgressActivity {
    protected static final String KEY_TITLE = "title";
    protected static final String KEY_URL = "url";
    @BindView(R.id.titlebar_tv_title)
    TextView mTvTitle;
    @BindView(R.id.titlebar_btn_left)
    ImageButton mIvBack;

    protected String mUrl;
    protected String mTitle;


    public static void startActivity(Context context, String title, String url) {
        Intent i = new Intent(context, CommonWebViewProgressActivity.class);
        i.putExtra(KEY_TITLE, title);
        i.putExtra(KEY_URL, url);
        context.startActivity(i);
    }

    @Override
    protected void initVariables() {
        super.initVariables();
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(KEY_TITLE);
        mUrl = intent.getStringExtra(KEY_URL);
    }

    @Override
    protected void initViews() {
        super.initViews();
        if (Check.isEmpty(mTitle)) mTitle = "网页链接";

        //这里好像有时候会报空指针,不知道是不是ButterKnife的BUG?
        if (mTvTitle == null) mTvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
        mTvTitle.setText(mTitle);
        if (mIvBack == null) mIvBack = (ImageButton) findViewById(R.id.titlebar_btn_left);
        mIvBack.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initWebViewSetting(WebSettings settings) {
        super.initWebViewSetting(settings);
        //第三方网页中 如优酷会用到DOMStorage来存储信息,如果不能存储则无法转跳
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        //默认Client 留在APP内启动,而不是启动外部浏览器
        getWebView().setWebViewClient(new WebViewClient());
    }


    @Override
    protected int getProgressBarId() {
        return R.id.web_pb_loading;
    }

    @Override
    protected int getWebViewParentLayoutId() {
        return R.id.web_ll_root;
    }

    @Override
    protected int getWebViewId() {
        return R.id.web_wv_view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_common_webview_progress;
    }

    @Override
    protected void loadData() {
        super.loadData();
        getWebView().loadUrl(mUrl);
    }


    @OnClick(R.id.titlebar_btn_left)
    public void onClickBack() {
        finish();
    }


}

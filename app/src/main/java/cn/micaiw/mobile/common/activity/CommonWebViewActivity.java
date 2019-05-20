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
import cn.micaiw.mobile.base.component.BaseWebViewActivity;

/**
 * Created by ckerv on 2018/1/22.
 */

public class CommonWebViewActivity extends BaseWebViewActivity {

    protected static final String KEY_TITLE = "title";
    protected static final String KEY_URL = "url";
    @BindView(R.id.titlebar_tv_title)
    TextView mTvTitle;
    @BindView(R.id.titlebar_btn_left)
    ImageButton mIvBack;

    private String mUrl;
    private String mTitle;

    public static void startActivity(Context context, String title, String url) {
        Intent i = new Intent(context, CommonWebViewActivity.class);
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
        mTvTitle.setText(mTitle);
        mIvBack.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initWebViewSetting(WebSettings settings) {
        super.initWebViewSetting(settings);
        //第三方网页中 如优酷会用到DOMStorage来存储信息,如果不能存储则无法转跳
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        getWebView().setWebViewClient(new WebViewClient());
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
        return R.layout.activity_common_webview;
    }

    @Override
    protected void loadData() {
        super.loadData();
        getWebView().loadUrl(mUrl);
    }

    @OnClick(R.id.titlebar_btn_left)
    public void onClickBack() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(getWebView().canGoBack()) {
            getWebView().goBack();
        } else {
            super.onBackPressed();
        }
    }
}

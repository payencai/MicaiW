package cn.micaiw.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;


/**
 * 新闻详情页面(H5页面)
 */
public class WebViewActivity extends BaseActivity {

    @BindView(R.id.webViewFrameLayout)
    FrameLayout webViewFrameLayout;
    @BindView(R.id.titlebar_btn_left)
    ImageButton titlebarBtnLeft;
    @BindView(R.id.titlebar_tv_title)
    TextView titlebarTvTitle;
    @BindView(R.id.newsTitle)
    LinearLayout newsTitle;

    @BindView(R.id.contentTitle)
    TextView contentTitle;
    @BindView(R.id.watchNumber)
    TextView watchNumber;
    @BindView(R.id.newsTime)
    TextView newsTime;


    private String mUrl = "http://www.baidu.com";
    private WebView webView;
    private String mTitle = "";

    /**
     * 启动打印页面
     *
     * @param url 要打开的网页
     */
    public static void starUi(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_news_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        titlebarTvTitle.setVisibility(View.VISIBLE);
        webView = new WebView(getApplicationContext());
        webViewFrameLayout.addView(webView);
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = "http://www.baidu.com";
        }
        mTitle = intent.getStringExtra("title");
        if (mTitle.equals("米财头条")) {
            newsTitle.setVisibility(View.VISIBLE);
            String string_contentTitle = intent.getStringExtra("contentTitle");
            String string_watchNumber = intent.getStringExtra("watchNumber");
            String string_newsTime = intent.getStringExtra("newsTime");
            contentTitle.setText(string_contentTitle);
            watchNumber.setText(string_watchNumber);
            newsTime.setText(string_newsTime);

        } else {
            newsTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mTitle)) {
            titlebarTvTitle.setText(mTitle);
        }
        initSetting();
    }

    private void initSetting() {
        WebSettings ws = webView.getSettings();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        //下面这一句不能少不然打不开https网页
        ws.setDomStorageEnabled(true);
        //允许javascript执行
        ws.setJavaScriptEnabled(true);
        //加载一个服务端网页
        webView.loadUrl(mUrl);
        //加载一个本地网页
//        webView.loadUrl("file:///android_asset/jm/index.html");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //开始加载网页时回调
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            //网页加载结束时回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

        });
    }

    @OnClick({R.id.titlebar_btn_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_btn_left:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //如果网页可以后退，则网页后退
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }
}

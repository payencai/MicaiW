package cn.micaiw.mobile.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;

public class WebUrlActivity extends BaseActivity {
    private FrameLayout mFrameLayout;
    private WebView mWebView;
    private String url;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_web_url;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        url=getIntent().getStringExtra("url");
        if(!url.contains("http://")||!url.contains("https://"))
            url= "http://"+url;
        mFrameLayout= (FrameLayout) findViewById(R.id.mywebViewFrame);
        mWebView=new WebView(getApplicationContext());
        mFrameLayout.addView(mWebView);
        initSetting();
    }

    private void initSetting() {
        WebSettings ws = mWebView.getSettings();
        //允许javascript执行
        ws.setJavaScriptEnabled(true);
        //加载一个服务端网页
        mWebView.loadUrl(url);
        //加载一个本地网页
//        webView.loadUrl("file:///android_asset/jm/index.html");

        mWebView.setWebViewClient(new WebViewClient() {
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
        mWebView.setWebChromeClient(new WebChromeClient() {
            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

        });
    }

    @Override
    public void onBackPressed() {
        //如果网页可以后退，则网页后退
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
    }
}

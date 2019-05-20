package cn.micaiw.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;

public class PlatformIntroduceActivity extends BaseActivity {

    @BindView(R.id.PlatformIntroduce)
    FrameLayout webViewFrameLayout;

    @BindView(R.id.versionName)
    TextView versionNameText;

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
        return R.layout.activity_platform_introduce;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        webView = new WebView(getApplicationContext());
//        webViewFrameLayout.addView(webView);
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = "http://www.baidu.com";
        }
        mTitle = intent.getStringExtra("title");
        if (!TextUtils.isEmpty(mTitle)) {
//            titlebarTvTitle.setText(mTitle);
        }
        String versionName = getVersionName(this);
        if (!TextUtils.isEmpty(versionName)) {
            versionNameText.setText("版本号V" + versionName);
        }
//        initSetting();
    }

    private void initSetting() {
        WebSettings ws = webView.getSettings();
        //允许javascript执行
        ws.setJavaScriptEnabled(true);
        //加载一个服务端网页
        webView.loadUrl(mUrl);
        //加载一个本地网页
//        webView.loadUrl("file:///android_asset/jm/index.html");

        webView.setWebViewClient(new WebViewClient() {
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

    @OnClick({R.id.backImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
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

    //获取版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //获取版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }
    //通过PackageInfo得到的想要启动的应用的包名
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pInfo = null;

        try {
            //通过PackageManager可以得到PackageInfo
            PackageManager pManager = context.getPackageManager();
            pInfo = pManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pInfo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }
}

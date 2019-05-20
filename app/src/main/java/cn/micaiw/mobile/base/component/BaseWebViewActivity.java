package cn.micaiw.mobile.base.component;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by ckerv on 2018/1/22.
 */

public abstract class BaseWebViewActivity extends BaseActivity {
    private ViewGroup mViewGroup;
    private WebView mWebView;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initVariables();
        initViews();
        if (mWebView != null) {
            initWebViewSetting(mWebView.getSettings());
            loadData();
        }
    }

    /**
     * 获得webview的父layout，用于移除子view
     *
     * @return
     */
    protected abstract int getWebViewParentLayoutId();

    /**
     * 获得webview的在布局中的id
     *
     * @return
     */
    protected abstract int getWebViewId();

    protected void initVariables() {
    }

    protected void initViews() {
        mViewGroup = (ViewGroup) findViewById(getWebViewParentLayoutId());
        mWebView = (WebView) findViewById(getWebViewId());
    }

    protected void initWebViewSetting(WebSettings settings) {
        //常用配置

        /*// 自适应屏幕
        // LayoutAlgorithm.NARROW_COLUMNS 可能的话使所有列的宽度不超过屏幕宽度
        // LayoutAlgorithm.NORMAL 正常显示不做任何渲染
        // LayoutAlgorithm.SINGLE_COLUMN 使所有内容放大webview等宽的一列中
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 可任意比例缩放
        settings.setUseWideViewPort(true);
        // setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        settings.setLoadWithOverviewMode(true);*/

        /*// 设置可以支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        // 隐藏缩放工具
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }*/

        /*// 允许与js交互
        settings.setJavaScriptEnabled(true);

        // 保存密码
        settings.setSavePassword(true);
        // 保存表单数据
        settings.setSaveFormData(true);
        // 启用地理定位
        settings.setGeolocationEnabled(true);
        // 设置定位的数据库路径
        settings.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        settings.setDomStorageEnabled(true);
        settings.setSupportMultipleWindows(true);*/
    }

    protected void loadData() {
    }

    protected WebView getWebView() {
        return mWebView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onResume();
        }
        mWebView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebView.onPause();
        }
        mWebView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewGroup != null) {
            // 防止缩放因为缩放工具而产生crash，Receiver not registered: android.widget.ZoomButtonsController
            mViewGroup.removeAllViews();
        }
        if (mWebView != null) {
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }
}

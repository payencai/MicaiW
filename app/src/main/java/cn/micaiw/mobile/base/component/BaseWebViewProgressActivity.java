package cn.micaiw.mobile.base.component;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by ckerv on 2018/1/22.
 */

public abstract class BaseWebViewProgressActivity extends BaseWebViewActivity{
    protected ProgressBar mPbLoading;
    //默认的ProgressChromeClient
    protected WebChromeClient mProgressChromeClient;

    @Override
    protected void initViews() {
        super.initViews();
        mPbLoading = (ProgressBar) findViewById(getProgressBarId());
        mProgressChromeClient = new ProgressChromeClient();
    }

    protected abstract int getProgressBarId();

    @Override
    protected void initWebViewSetting(WebSettings settings) {
        super.initWebViewSetting(settings);
        getWebView().setWebChromeClient(mProgressChromeClient);
    }


    protected void onProgress(WebView view, int progress) {
        if (mPbLoading.getVisibility() == View.GONE)
            mPbLoading.setVisibility(View.VISIBLE);
        mPbLoading.setProgress(progress);
        if (progress >= 100) {
            mPbLoading.setVisibility(View.GONE);
        }
    }


    public class ProgressChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            onProgress(view, newProgress);
        }
    }
}

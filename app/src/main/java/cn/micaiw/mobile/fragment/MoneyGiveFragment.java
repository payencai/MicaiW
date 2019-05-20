package cn.micaiw.mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import butterknife.BindView;
import cn.micaiw.mobile.MyApplication;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.WebNextActivity;
import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.view.ItemSwitchButton;

/**
 * 作者：凌涛 on 2018/11/21 16:29
 * 邮箱：771548229@qq..com
 */
public class MoneyGiveFragment extends BaseFragment{
    @BindView(R.id.titlebar_swicth_btn)
    ItemSwitchButton mItemSwitchButton;
    @BindView(R.id.titlebar_msg_btn)
    ImageView mImageView;
    @BindView(R.id.web1)
    WebView web1;
    @BindView(R.id.web2)
    WebView web2;
    @BindView(R.id.web3)
    WebView web3;
    String baseurl1="";
    String baseurl2="";
    String baseurl3="";
    int userId ;
    String userName;
    String token;
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_money_give;
    }

    public MoneyGiveFragment() {
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        Log.e("resume","resume");
        if(!TextUtils.isEmpty(UserInfoSharedPre.getIntance(getContext()).getToken())){
            userId =  UserInfoSharedPre.getIntance(getContext()).getUserId();
            userName= UserInfoSharedPre.getIntance(getContext()).getUserName();
            token = UserInfoSharedPre.getIntance(getContext()).getToken();
            baseurl1="http://www.micaiw.cn/micaih5/credit.html?token="
                    +token
                    +"&userId="
                    +userId
                    +"&userName="
                    +userName;
            baseurl2="http://www.micaiw.cn/micaih5/insurance.html?token="
                    +token
                    +"&userId="
                    +userId
                    +"&userName="
                    +userName;
            baseurl3="http://www.micaiw.cn/micaih5/matters.html?token="
                    +token
                    +"&userId="
                    +userId
                    +"&userName="
                    +userName;
        }
        web1.loadUrl(baseurl1);
        web2.loadUrl(baseurl2);
        web3.loadUrl(baseurl3);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        int userId =  UserInfoSharedPre.getIntance(getContext()).getUserId();
        String userName=UserInfoSharedPre.getIntance(getContext()).getUserName();
        String token = UserInfoSharedPre.getIntance(getContext()).getToken();
        baseurl1="http://www.micaiw.cn/micaih5/credit.html?token="
                +token
                +"&userId="
                +userId
                +"&userName="
                +userName;
         baseurl2="http://www.micaiw.cn/micaih5/insurance.html?token="
                +token
                +"&userId="
                +userId
                +"&userName="
                +userName;
         baseurl3="http://www.micaiw.cn/micaih5/matters.html?token="
                +token
                +"&userId="
                +userId
                +"&userName="
                +userName;
        Log.e("url",baseurl1);

        mItemSwitchButton.setSwitchBtnText("信用卡","保险","理财");
        mItemSwitchButton.setSwicthBtnClickListener(new ItemSwitchButton.OnSwicthClickListener() {
            @Override
            public void onSwicthClick(String target) {
                Log.e("target",target);
                if("信用卡".equals(target)){
                    web1.setVisibility(View.VISIBLE);
                    web2.setVisibility(View.GONE);
                    web3.setVisibility(View.GONE);
                }else if("保险".equals(target)){
                    web1.setVisibility(View.GONE);
                    web2.setVisibility(View.VISIBLE);
                    web3.setVisibility(View.GONE);
                }else if("理财".equals(target)){
                    web1.setVisibility(View.GONE);
                    web2.setVisibility(View.GONE);
                    web3.setVisibility(View.VISIBLE);
                }

            }
        });
        initWeb1(web1);
        initWeb1(web2);
        initWeb1(web3);
        mImageView.setVisibility(View.GONE);
    }
    private void showSelectWebview(String  target){

    }
    private void initWeb1(WebView mWebView){
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("KeithXiaoY","开始加载");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("KeithXiaoY","加载结束");
            }

            // 链接跳转都会走这个方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean isjump=true;
                Log.d("KeithXiaoY","Url："+ url );
                if(url.contains("pushTosecondWeb")){
                    Context context=view.getContext();
                    Intent intent=new Intent(context,WebNextActivity.class);
                    intent.putExtra("url",url);
                    context.startActivity(intent);
                    //isjump=false;
                }
                //view.loadUrl(url);// 强制在当前 WebView 中加载 url
                return isjump;
            }
        });



    }
}

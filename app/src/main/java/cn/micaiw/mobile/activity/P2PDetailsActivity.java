package cn.micaiw.mobile.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.constant.Config;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.InvestmentRecordBean;
import cn.micaiw.mobile.entity.P2PDetailsBean;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ToaskUtil;
import cn.micaiw.mobile.util.Util;

public class P2PDetailsActivity extends BaseActivity {

    @BindView(R.id.P2PDetailsRv)
    RecyclerView P2PDetailsRv;
    @BindView(R.id.returnMoneyBtn)
    TextView returnMoneyBtn;
    @BindView(R.id.titleHead)
    TextView titleHead;
    @BindView(R.id.attentionZoom)
    TextView attentionZoom;
    @BindView(R.id.attentionZoom2)
    TextView attentionZoom2;
    @BindView(R.id.detailsContentItem)
    LinearLayout detailsContentItem;
    @BindView(R.id.investmentRecordRv)
    RecyclerView investmentRecordRv;

    private String mPlatformUrl;
    private int mPtpPlatformId;
    private RVBaseAdapter<P2PDetailsBean> mBeanRVBaseAdapter;
    private RVBaseAdapter<InvestmentRecordBean> mRecordBeanRVBaseAdapter;
    private String mPlatformName;
    private ArrayList<String> mLabelList;
    private String mContent;
    private String mRiskScore;
    private String mAnnualRate;
    private String avgAnnualRate;
    private int mUserNumber;
    private String mPlatformLogo;
    private int mIsFirst;
    private int companyType;

    private Tencent mTencent;
    private IWXAPI api;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_p2_pdetails;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTencent = Tencent.createInstance(Config.QQ_APP_ID, this.getApplicationContext());
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
        api.registerApp(Config.APP_ID);
        Intent intent = getIntent();
        mPlatformLogo = intent.getStringExtra("platformLogo");
        mPlatformName = intent.getStringExtra("platformName");//名称
        mLabelList = intent.getStringArrayListExtra("labelList");//标签名称
        mPlatformUrl = intent.getStringExtra("platformUrl");//直达连接
        Log.d("mPlatformUrl", "init: " + mPlatformUrl);
        mContent = intent.getStringExtra("content");//内容
        mRiskScore = intent.getStringExtra("riskScore");
        mAnnualRate = intent.getStringExtra("annualRate");
        avgAnnualRate = intent.getStringExtra("avgAnnualRate");
        mUserNumber = intent.getIntExtra("userNumber", 0);
        mIsFirst = intent.getIntExtra("isFirst", 1);
        companyType = intent.getIntExtra("companyType", 1);
        titleHead.setText(mPlatformName);
        mPtpPlatformId = intent.getIntExtra("ptpPlatformId", -1);//平台id

        mBeanRVBaseAdapter = new RVBaseAdapter<P2PDetailsBean>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }
        };
        P2PDetailsBean head = new P2PDetailsBean();
        head.TYPE = 0;
        head.platformLogo = mPlatformLogo;
        head.platformName = mPlatformName;
        head.annualRate = mAnnualRate;
        head.labelList = mLabelList;
        head.platformUrl = mPlatformUrl;
        head.content = mContent;
        head.riskScore = mRiskScore;
        head.avgAnnualRate = avgAnnualRate;
        head.userNumber = mUserNumber;
        head.isFirst = mIsFirst;
        head.setPtpPlatformId(mPtpPlatformId);
        mBeanRVBaseAdapter.add(head);//添加一个头

        P2PDetailsRv.setLayoutManager(new LinearLayoutManager(this));
        P2PDetailsRv.setAdapter(mBeanRVBaseAdapter);

        mRecordBeanRVBaseAdapter = new RVBaseAdapter<InvestmentRecordBean>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }
        };
        investmentRecordRv.setLayoutManager(new LinearLayoutManager(this));
        investmentRecordRv.setAdapter(mRecordBeanRVBaseAdapter);
        if (companyType == 1) {
            requestSchemeData(PlatformContans.User.sGetInvestmentSchemeByPtpIdForApp);
        } else {
            requestSchemeData(PlatformContans.User.sGetFundSchemeByFundIdForApp);
        }
        if (companyType == 1) {

            requestRecordData(PlatformContans.User.sGetPtpRecordByPlatformId);
        } else {
            requestRecordData(PlatformContans.User.sGetFundRecordByPlatformId );
        }

    }

    @OnClick({R.id.backImg, R.id.returnMoneyBtn, R.id.through, R.id.share, R.id.attentionZoom, R.id.attentionZoom2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.returnMoneyBtn:
                String token = UserInfoSharedPre.getIntance(this).getToken();
                if (TextUtils.isEmpty(token)) {
                    ToaskUtil.showToast(this, "请登录后填写");
                    return;
                }
                Intent intent = new Intent(this, WriteReturnMoneyActivity.class);
                intent.putExtra("companyType", companyType);
                startActivity(intent);
                break;
            case R.id.through:
                WebViewActivity.starUi(this, mPlatformUrl, "平台官网");
                break;
            case R.id.share:
                setBackgroundDrakValue(0.5f);
                share(view);
                break;
            case R.id.attentionZoom:
                if (detailsContentItem.getVisibility() == View.GONE) {
                    attentionZoom.setText("点击收起");
                    detailsContentItem.setVisibility(View.VISIBLE);
                } else {
                    attentionZoom.setText("点击展开");
                    detailsContentItem.setVisibility(View.GONE);
                }
                break;
            case R.id.attentionZoom2:
                if (investmentRecordRv.getVisibility() == View.GONE) {
                    attentionZoom2.setText("点击收起");
                    investmentRecordRv.setVisibility(View.VISIBLE);
                } else {
                    attentionZoom2.setText("点击展开");
                    investmentRecordRv.setVisibility(View.GONE);
                }
                break;
        }
    }

    //获取投资方案
    private void requestSchemeData(String url) {
        Map<String, Object> paramsMap = new HashMap<>();
        if (mPtpPlatformId == -1) {
            ToaskUtil.showToast(this, "数据出错");
            return;
        }
        String userName = UserInfoSharedPre.getIntance(this).getUserName();
        if (companyType == 1) {
            paramsMap.put("ptpPlatformId", mPtpPlatformId);
        } else {
            paramsMap.put("fundPlatformId", mPtpPlatformId);

        }
        paramsMap.put("userName", userName);

        HttpProxy.obtain().get(url, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("ptpPlatformId", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        Gson gson = new Gson();
                        List<P2PDetailsBean> list = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            P2PDetailsBean bean = gson.fromJson(item.toString(), P2PDetailsBean.class);
                            bean.TYPE = 1;
                            list.add(bean);
                        }
                        mBeanRVBaseAdapter.addAll(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToaskUtil.showToast(P2PDetailsActivity.this, "登录过期");
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void requestRecordData(String url) {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> token = new HashMap<>();
        params.put("platformId", mPtpPlatformId);
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        String tokenValue = intance.getToken();
        if (TextUtils.isEmpty(tokenValue)) {
            ToaskUtil.showToast(this, "还未登录");
            return;
        }
        token.put("token", tokenValue);
        HttpProxy.obtain().get(url, params, token, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("platformIdniniubi", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        List<InvestmentRecordBean> list = new ArrayList<>();
                        Gson gson = new Gson();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            InvestmentRecordBean bean = gson.fromJson(item.toString(), InvestmentRecordBean.class);
                            list.add(bean);
                        }
                        mRecordBeanRVBaseAdapter.addAll(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    public void share(View view) {
        View shareView = LayoutInflater.from(this).inflate(R.layout.share_layout, null);
        PopupWindow sharePw = new PopupWindow(shareView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        handleView(shareView, sharePw);
        sharePw.setFocusable(true);
        sharePw.setBackgroundDrawable(new BitmapDrawable());
        sharePw.setOutsideTouchable(true);
        sharePw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundDrakValue(1.0f);
            }
        });
        sharePw.setAnimationStyle(R.style.pwStyle);
        //第一个参数可以取当前页面的任意一个View
        //第二个参数表示pw从哪一个方向显示出来
        //3、4表示pw的偏移量
        sharePw.showAtLocation(view, Gravity.BOTTOM, 0, 0);

    }

    private void handleView(View view, final PopupWindow sharePw) {
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        final String tel = intance.getUserName();
//        final String shaUrl = "http://120.78.77.138:8080/miH5/index.html?agencyAccount=" + tel;
        String shareUrlByBg = intance.getIosDownloadPath();
        final String shaUrl = shareUrlByBg;
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePw.dismiss();
            }
        });
        view.findViewById(R.id.wxFriends).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                shareToWxFriends(shaUrl, SendMessageToWX.Req.WXSceneSession, tel);
                sharePw.dismiss();
            }
        });
        view.findViewById(R.id.wxCircle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToWxFriends(shaUrl, SendMessageToWX.Req.WXSceneTimeline, tel);
                sharePw.dismiss();
            }
        });
        view.findViewById(R.id.qqFriends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickShare(shaUrl);
                sharePw.dismiss();
            }
        });
        view.findViewById(R.id.qqZone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareToQzone(shaUrl);
                sharePw.dismiss();
            }
        });

    }

    protected void setBackgroundDrakValue(float value) {
        if (value > 1) {
            value = 1;
        }
        if (value < 0) {
            value = 0;
        }
        Window mWindow = getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = value;
        mWindow.setAttributes(params);

    }

    private IUiListener mIUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            String s = o.toString();
            Log.d("onComplete", "onComplete: " + s);
        }

        @Override
        public void onError(UiError uiError) {
            ToaskUtil.showToast(P2PDetailsActivity.this, "授权异常");
        }

        @Override
        public void onCancel() {

        }
    };

    //分享到QQ好友
    private void onClickShare(String url) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "米财钱包");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "您的好友邀请您下载，米财钱包app。");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "米财钱包");
//        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        mTencent.shareToQQ(this, params, mIUiListener);
    }

    //分享到qq空间
    private void shareToQzone(String url) {
//分享类型
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "米财钱包");
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "您的好友邀请您下载，米财钱包app。");
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, new ArrayList<String>());
        mTencent.shareToQzone(this, params, mIUiListener);
    }

    //分享到微信好友
    private void shareToWxFriends(String url, int type, String tel) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "米财钱包";

//        msg.description = tel + "邀请您下载,米财钱包app";
        msg.description = "您的好友邀请您下载，米财钱包app。";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.share_logo);
        msg.thumbData = Util.bmpToByteArray(bitmap, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        api.sendReq(req);


    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}

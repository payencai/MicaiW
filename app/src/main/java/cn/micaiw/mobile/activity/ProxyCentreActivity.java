package cn.micaiw.mobile.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.adapter.MyFragmentAdapter;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.Config;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.fragment.ActivityRuleFragment;
import cn.micaiw.mobile.fragment.MyInviteFragment;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ToaskUtil;
import cn.micaiw.mobile.util.Util;

public class ProxyCentreActivity extends BaseActivity {


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.totalAward)
    TextView mTotalAward;
    @BindView(R.id.totalNumber)
    TextView mTotalNumber;
    @BindView(R.id.totalInvestment)
    TextView mTotalInvestment;
    @BindView(R.id.waitAward)
    TextView mWaitAward;

    private Tencent mTencent;
    private IWXAPI api;

    private ActivityRuleFragment mRuleFragment;
    private MyInviteFragment mInviteFragment;

    private static final String[] titles = new String[]{"活动规则", "我的邀请"};
    private int mCash = 0;
    private String mRegulation = "";

    @Override
    protected int getContentViewId() {
        return R.layout.activity_proxy_centre;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTencent = Tencent.createInstance(Config.QQ_APP_ID, this);
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
        api.registerApp(Config.APP_ID);
        requestProxyData();
    }

    @OnClick({R.id.backImg, R.id.inviteFriends})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.inviteFriends:
                setBackgroundDrakValue(0.5f);
                share(view);
                break;
            default:
                break;
        }
    }

    private void requestProxyData() {
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> token = new HashMap<>();
        String tokenValue = UserInfoSharedPre.getIntance(this).getToken();
        String userName = UserInfoSharedPre.getIntance(this).getUserName();
        if (TextUtils.isEmpty(tokenValue) || TextUtils.isEmpty(userName)) {
            ToaskUtil.showToast(this, "未登录");
            return;
        }
        paramsMap.put("userName", userName);
        token.put("token", tokenValue);
        HttpProxy.obtain().get(PlatformContans.AgencyForA.sGetAgencyDataById, paramsMap, token, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("sGetAgencyDataById", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONObject item = object.getJSONObject("data");
                        int totalAward = item.getInt("totalAward");//总奖金
                        int totalNumber = item.getInt("totalNumber");//总人数
                        int totalInvestment = item.getInt("totalInvestment");
                        int waitAward = item.getInt("waitAward");//等待的奖励
                        mTotalAward.setText(totalAward + "");
                        mTotalNumber.setText(totalNumber + "");
                        mTotalInvestment.setText(totalInvestment + "");
                        mWaitAward.setText(waitAward + "");
                        JSONObject agencyCenter = item.getJSONObject("agencyCenter");
                        mCash = agencyCenter.getInt("cash");
                        mRegulation = agencyCenter.getString("regulation");
                        initView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    String a = "1、邀请好友注册得5元红包奖励 2、邀请好友通过米财钱包投资得10元红包奖励";
//                    ToaskUtil.showToast(ProxyCentreActivity.this, "");
//                    UserInfoSharedPre.getIntance(ProxyCentreActivity.this).clearUserInfo();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void initView() {
        List<Fragment> fragmentList = new ArrayList<>();
        if (mRuleFragment == null) {
            mRuleFragment = ActivityRuleFragment.getInstance(mRegulation, mCash);
        }
        if (mInviteFragment == null) {
            mInviteFragment = new MyInviteFragment();
        }
        fragmentList.add(mRuleFragment);
        fragmentList.add(mInviteFragment);
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
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
//        final String shaUrl = "http://120.78.77.138:8080/miH5/index.html?agencyAccount=" + tel;
        final String tel = intance.getUserName();
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
            ToaskUtil.showToast(ProxyCentreActivity.this, "授权异常");
        }

        @Override
        public void onCancel() {

        }
    };
    private IUiListener mZoneListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            String s = o.toString();
            Log.d("caonima", "onComplete: " + s);
        }

        @Override
        public void onError(UiError uiError) {
            ToaskUtil.showToast(ProxyCentreActivity.this, "授权异常");

            Log.d("caonima", "onError: " + uiError.errorCode);
            Log.d("caonima", "onError: " + uiError.errorDetail);
            Log.d("caonima", "onError: " + uiError.errorDetail);
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


        mTencent.shareToQzone(this, params, mZoneListener);
    }

    //分享到微信好友
    private void shareToWxFriends(String url, int type, String tel) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "米财钱包";

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

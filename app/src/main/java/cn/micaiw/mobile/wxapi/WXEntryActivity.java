package cn.micaiw.mobile.wxapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.micaiw.mobile.MyApplication;
import cn.micaiw.mobile.constant.Config;
import cn.micaiw.mobile.entity.WxInfo;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private static final int WX_LOGIN = 1;
    private static IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
        iwxapi.registerApp(Config.APP_ID);
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d("BaseResp", "onReq: 你妹夫");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        /*微信登录为getType为1，分享网页为2*/
        Log.d("BaseResp", "onResp: ?????");
        Log.d("BaseResp", "" + baseResp.getType());
        Log.d("BaseResp", "" + baseResp.errStr);
        Log.d("BaseResp", "" + baseResp.openId);
        Log.d("BaseResp", "" + baseResp.transaction);
        Log.d("BaseResp", "" + baseResp.errCode);
        Log.d("BaseResp", "" + baseResp.checkArgs());
        if (baseResp.getType() == WX_LOGIN) {
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    ToaskUtil.showToast(this, "授权成功");
                    String code = String.valueOf(resp.code);
                    requestOpenId(Config.APP_ID, Config.APP_SECRET, code);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    ToaskUtil.showToast(this, "拒绝授权");
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ToaskUtil.showToast(this, "用户取消");
                    finish();
                    break;
            }
        }
        if (baseResp.getType() == 2) {//网页分享
            if (baseResp.errCode == 0) {
                ToaskUtil.showToast(this, "分享成功");
            } else {
                ToaskUtil.showToast(this, "分享失败");
            }
            finish();
        }
    }

    public static IWXAPI getWXAPI() {
        if (iwxapi == null) {
            iwxapi = WXAPIFactory.createWXAPI(MyApplication.getContext(), Config.APP_ID, true);
            iwxapi.registerApp(Config.APP_ID);
        }
        return iwxapi;
    }

    public static boolean isInstall() {
        if (!iwxapi.isWXAppInstalled()) {
            ToaskUtil.showToast(MyApplication.getContext(),
                    "请先安装微信应用");
            return false;
        }
// else if (!iwxapi.isWXAppSupportAPI()) {
//            ToaskUtil.showToast(MyApplication.getContext(), "请先更新微信应用");
//            return false;
//        }
        return true;
    }

    public void requestOpenId(String appid, String secret, String code) {
        //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        HttpProxy.obtain().get(url, null, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("WXEntryActivity", result);
                try {
                    JSONObject object = new JSONObject(result);
                    String openid = object.getString("openid");
                    if (!TextUtils.isEmpty(openid)) {
                        WxInfo wxInfo = new WxInfo();
                        wxInfo.openId = openid;
                        EventBus.getDefault().post(wxInfo);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToaskUtil.showToast(WXEntryActivity.this, "登录异常");
                    finish();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d("WXEntryActivity", "onFailure: " + error);
            }
        });
    }

}

package cn.micaiw.mobile.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.micaiw.mobile.MyApplication;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.Config;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.custom.KyLoadingBuilder;
import cn.micaiw.mobile.entity.BoundResultInfo;
import cn.micaiw.mobile.entity.UserInfo;
import cn.micaiw.mobile.entity.WxInfo;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class LoginActivity extends BaseActivity {
    //    String APP_ID = "wxa5fefb7214b6814d";
    String APP_SECRET = "75a2411032debefca3451449a988304c";
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.saveBtn)
    TextView saveBtn;
    @BindView(R.id.obtianAuthCode)
    TextView obtianAuthCode;
    @BindView(R.id.pswLogin)
    TextView pswLogin;
    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.QQLogin)
    ImageView QQLogin;
    @BindView(R.id.WXLogin)
    ImageView WXLogin;
    @BindView(R.id.editPhoneNumber)
    EditText editPhoneNumber;
    @BindView(R.id.findPasswordImg)
    ImageView findPasswordImg;
    @BindView(R.id.editPswInput)
    EditText editPswInput;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.submit)
    Button submit;
    private Context ctx;
    private ProgressDialog dialog;
    //    private UMShareAPI mShareAPI = null;
//    private SHARE_MEDIA platform = null;
    private Unbinder mUnbinder;

    //登录类型，0为密码登录，1为短信验证登录，{2为微信登录，3为QQ登录}默认为密码登录
    private int loginType = 0;

    //请求验证码的参数
    Map<String, Object> requeAuthParamsMap = new HashMap<>();
    //登录接口参数
    Map<String, Object> loginParamsMap = new HashMap<>();
    private KyLoadingBuilder mOpenLoadView;
    private Tencent mTencent;
    private IWXAPI api;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
        api.registerApp(Config.APP_ID);
        EventBus.getDefault().register(this);
        mUnbinder = ButterKnife.bind(this);//使用butterknife
        backImg.setVisibility(View.VISIBLE);
        title.setText("登录");
        initView(loginType);

        initTencent();
    }

    private void initTencent() {
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(Config.QQ_APP_ID, this.getApplicationContext());
        // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
        // 初始化视图
    }


    private void initView(int loginType) {
        switch (loginType) {
            case 0://密码登录
                pswLogin.setText("验证码登录");
                obtianAuthCode.setVisibility(View.GONE);
                editPswInput.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                findPasswordImg.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

                break;
            case 1://短信验证登录
                pswLogin.setText("密码登录");
                obtianAuthCode.setVisibility(View.VISIBLE);
                editPswInput.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                findPasswordImg.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        saveBtn.setVisibility(View.GONE);
        messageImg.setVisibility(View.GONE);

        ctx = this;
        dialog = new ProgressDialog(ctx);
//        mShareAPI = UMShareAPI.get(this);
    }


    private void apply() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }

    private void submitRequest(String value) {
//        Request<String> request = null;
//        request = NoHttp.createStringRequest(PlatformContans.rootUrl + PlatformContans.Urls.SystemUrls.login, RequestMethod.POST);
//        if (request != null) {
//            request.add("openid",value);
//            request(0, request, httpListener, true, true);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        EventBus.getDefault().unregister(this);
//        mHandler.removeMessages(REQUE_AGAIN_AUTH_CODE);
//        mHandler.removeCallbacksAndMessages(null);
    }

    @OnClick({R.id.backImg, R.id.obtianAuthCode, R.id.pswLogin, R.id.QQLogin, R.id.WXLogin, R.id.submit, R.id.findPasswordImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.obtianAuthCode://获取验证码
                if (MyApplication.isDebug) {
                    Intent intent = new Intent(this, VerifyRegisterActivity.class);
                    intent.putExtra("telephone", "18878554275");
                    intent.putExtra("comeInType", 1);
                    startActivityForResult(intent, 0);
                } else {
                    if (checkPhoneNumber()) {//获取验证码之前验证手机号码已经注册
                        obtianAuthCode.setEnabled(false);
                        isRegister();
                    }
                }

                break;
            case R.id.pswLogin:
                if (loginType == 0) {
                    loginType = 1;
                } else if (loginType == 1) {
                    loginType = 0;
                }
                initView(loginType);
                break;
            case R.id.QQLogin:
//                loginByThird(SHARE_MEDIA.QQ);
                if (!mTencent.isSessionValid()) {
                    //注销登录 mTencent.logout(this);
                    mOpenLoadView = openLoadView("请稍等...");
                    mTencent.login(this, "all", mIUiListener);
                }

                break;
            case R.id.WXLogin:
//                mOpenLoadView = openLoadView("请稍等...");
                setWXLogin();
//                WxTextShare("测试文本", 0);
                break;
            case R.id.submit:
                if (MyApplication.isDebug) {
                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    if (checkPhoneNumber()) {
                        if (checkPassword()) {
                            submit.setEnabled(false);
                            mOpenLoadView = openLoadView("");
                            String name = editPhoneNumber.getEditableText().toString();
                            String psw = editPswInput.getEditableText().toString();
                            loginByAccount(name, psw);
                        }
                    }
                }
                break;
            case R.id.findPasswordImg:
                String string = editPhoneNumber.getEditableText().toString();
                if (TextUtils.isEmpty(string)) {
                    ToaskUtil.showToast(this, "请输入手机号码");
                    return;
                }
                if (string.length() != 11) {
                    ToaskUtil.showToast(this, "号码无效");
                    return;
                }
                Intent intent = new Intent(this, RetrievePswActivity.class);
                intent.putExtra("tel", string);
                startActivity(intent);
                break;
        }
    }

    //通过短信验证码登录
    private void LoginByAuth() {
//        ToaskUtil.showToast(this, "验证码已发送");

        obtianAuthCode();

    }

    private void loginByAccount(String userName, final String password) {
        Map<String, Object> hasMap = new HashMap<>();
        hasMap.put("userName", userName);
        hasMap.put("password", password);
        HttpProxy.obtain().post(PlatformContans.User.sUserLogin, hasMap, new ICallBack() {
            @Override
            public void OnSuccess(final String result) {
                //{"resultCode":1,"message":"登录失败，用户名或密码错误或账号已停用！"}


                try {
                    JSONObject object = new JSONObject(result);
                    final int resultCode = object.optInt("resultCode");
                    final String message = object.optString("message");
                    final JSONObject data = object.optJSONObject("data");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            submit.setEnabled(true);
                            if (mOpenLoadView != null) {
                                closeLoadView(mOpenLoadView);
                            }
                            ToaskUtil.showToast(LoginActivity.this, message);
                            if (resultCode == 0) {
                                //保存用户信息
                                UserInfo userInfo = new Gson().fromJson(data.toString(), UserInfo.class);
                                userInfo.setPassword(password);
                                MyApplication.token=userInfo.getToken();
                                MyApplication.userId=userInfo.getId();
                                MyApplication.userName=userInfo.getUserName();
                                UserInfoSharedPre sharedPre = UserInfoSharedPre.getIntance(LoginActivity.this);
                                sharedPre.saveUserInfo(userInfo, true);
                                sharedPre.setUserIsLogin(true);
                                Log.e("loginby", "OnSuccess: " + result);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                EventBus.getDefault().post("已经登录");
                                finish();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submit.setEnabled(true);
                        if (mOpenLoadView != null) {
                            closeLoadView(mOpenLoadView);
                        }
                        ToaskUtil.showToast(LoginActivity.this, "请检查网络");
                    }
                });
            }
        });
    }

//    private void loginByThird(SHARE_MEDIA media) {
//        UMShareAPI.get(this).getPlatformInfo(this, media, authListener);
//    }

    /**
     * 获取验证码
     */
    private void obtianAuthCode() {
        obtianAuthCode.setEnabled(false);
        final String tel = editPhoneNumber.getEditableText().toString();
        requeAuthParamsMap.put("telephone", tel);
        HttpProxy.obtain().get(PlatformContans.User.sHead + PlatformContans.User.sGetVerificationCode,
                requeAuthParamsMap, new ICallBack() {
                    @Override
                    public void OnSuccess(String result) {
                        //{"resultCode":0,"message":"验证码已发送！"}
                        Log.d("OnSuccess", "OnSuccess: " + result);
                        try {
                            JSONObject object = new JSONObject(result);
                            int resultCode = object.optInt("resultCode");
                            String message = object.optString("message");
                            ToaskUtil.showToast(LoginActivity.this, message);
                            if (resultCode == 0) {
                                Intent intent = new Intent(LoginActivity.this, VerifyRegisterActivity.class).putExtra("telephone", tel);
                                intent.putExtra("comeInType", 1);//短信验证登录
                                startActivityForResult(intent, 0);
                            }
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ToaskUtil.showToast(LoginActivity.this, message);
//                                }
//                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.d("lingtao", "onFailure: " + error);
                    }

                });

    }

    private boolean checkPhoneNumber() {
        String str = editPhoneNumber.getEditableText().toString();
        if (TextUtils.isEmpty(str)) {
            ToaskUtil.showToast(this, "号码不能为空");
            return false;
        }
//        if (!PhoneUtil.isPhoneNumber(str)) {
//            ToaskUtil.showToast(this, "请输入正确的手机号码");
//            return false;
//        }
        return true;
    }

    private boolean checkPassword() {
        if (TextUtils.isEmpty(editPswInput.getEditableText().toString())) {
            ToaskUtil.showToast(this, "请输入密码");
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        obtianAuthCode.setEnabled(true);
        submit.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        if (data != null) {
            switch (requestCode) {
                case 0:
                    obtianAuthCode.setEnabled(true);
                    int loginType = data.getIntExtra("loginType", -1);
                    String userName = data.getStringExtra("userName");
                    String code = data.getStringExtra("code");
                    String result = data.getStringExtra("result");
                    this.loginType = loginType;
                    initView(this.loginType);
                    loginParamsMap.put("userName", userName);
                    loginParamsMap.put("password", code);

                    try {
                        if (MyApplication.isDebug) {
                            result = LoginActivity.result;
                        }
                        JSONObject object = new JSONObject(result);
                        JSONObject userData = object.optJSONObject("data");
                        //做短信验证的登录
                        UserInfo userInfo = new Gson().fromJson(userData.toString(), UserInfo.class);
                        UserInfoSharedPre.getIntance(this).saveUserInfo(userInfo, false);
                        Log.d("nanaling", "onActivityResult: " + userData.toString());
                        EventBus.getDefault().post("已经登录");
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public void isRegister() {
        Map<String, Object> has = new HashMap<>();
        has.put("userName", editPhoneNumber.getEditableText().toString());
        HttpProxy.obtain().get(PlatformContans.User.sGetUserByUserName, has, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    final int resultCode = object.optInt("resultCode");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            obtianAuthCode.setEnabled(true);
                            if (resultCode == 0) {//已经存在
                                LoginByAuth();
                            } else {
                                showRegisterDialog();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String error) {
                Log.d("lingtao", "onFailure: " + error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToaskUtil.showToast(LoginActivity.this, "请检查网络");
                        obtianAuthCode.setEnabled(true);
                    }
                });
            }
        });
    }

    private void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(editPhoneNumber.getEditableText().toString() + "未注册，是否注册？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("tel", editPhoneNumber.getEditableText().toString());
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        //创建一个对话框并显示
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    /**
     * 微信登录
     */
    private void setWXLogin() {

        if (api != null && api.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            api.sendReq(req);
        } else {
            Toast.makeText(this, "您尚未安装微信", Toast.LENGTH_SHORT).show();
        }
    }


    private IUiListener mIUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            String s = o.toString();
            try {
                if (mOpenLoadView != null) {
                    mOpenLoadView.dismiss();
                }
                Log.d("onComplete", "onComplete: " + s);
                JSONObject object = new JSONObject(s);
                String openid = object.getString("openid");

                requestQQLogin(PlatformContans.User.sLoginByQq, openid, 0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            if (mOpenLoadView != null) {
                mOpenLoadView.dismiss();
            }
            ToaskUtil.showToast(LoginActivity.this, "授权异常");
        }

        @Override
        public void onCancel() {
            if (mOpenLoadView != null) {
                mOpenLoadView.dismiss();
            }
        }
    };

    private void requestQQLogin(String url, final String qqid, final int intoType) {
        Map<String, Object> params = new HashMap<>();
        if (intoType == 0) {
            params.put("qqId", qqid);
        } else {
            params.put("openId", qqid);
        }
        HttpProxy.obtain().post(url, params, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                //{"resultCode":1,"message":"此QQ未绑定过用户账号，请先绑定再登录"}
                MLog.log("LoginUserInfo", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 1) {
                        ToaskUtil.showToast(LoginActivity.this, message);
                        BoundTelActivity.stateBoundActivity(LoginActivity.this, qqid, intoType);
                    } else if (resultCode == 0) {
                        JSONObject data = object.optJSONObject("data");
                        //做短信验证的登录
                        //保存用户信息
                        UserInfo userInfo = new Gson().fromJson(data.toString(), UserInfo.class);
                        MyApplication.token=userInfo.getToken();
                        MyApplication.userId=userInfo.getId();
                        MyApplication.userName=userInfo.getUserName();
                        UserInfoSharedPre sharedPre = UserInfoSharedPre.getIntance(LoginActivity.this);
                        sharedPre.saveUserInfo(userInfo, false);
                        sharedPre.setUserIsLogin(true);
                        EventBus.getDefault().post("已经登录");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

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

    public static String result = "{\"resultCode\":0,\"message\":\"登录成功\",\"data\":{\"id\":5,\"name\":\"MIC18680243413\",\"userName\":\"18680243413\",\"password\":null,\"openId\":null,\"qqId\":null,\"createTime\":\"2018-05-16 11:39:04\",\"endLoginTime\":\"2018-05-16 11:43:31\",\"state\":1,\"avatar\":null,\"avatarKey\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJqd3QiLCJpYXQiOjE1MjY0NDIyMTEsInN1YiI6IntcInNpZ25TdGF0ZVwiOjAsXCJzdGF0ZVwiOjEsXCJ1c2VySWRcIjo1LFwidXNlck5hbWVcIjpcIjE4NjgwMjQzNDEzXCJ9IiwiZXhwIjoxNTI2NDQ1ODExfQ.IqtWtXpAeeXtw7eyl3lDsk3f03DP8s-7NQJgBtk9qaE\",\"wxId\":null,\"qq\":null,\"lastLoginIp\":\"112.96.173.37\",\"ip\":\"112.96.173.37\",\"lastSignTime\":null,\"signState\":0,\"continueSign\":0,\"agencyAccount\":null}}\n";

    @Subscribe
    public void isBound(BoundResultInfo resultInfo) {
        Log.d("isBound", "isBound: 同意登录");
        boolean b = resultInfo.isSucceed;
        if (b) {
            Log.d("isBound", "isBound: 同意登录22222");
            ToaskUtil.showToast(this, "绑定成功");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            EventBus.getDefault().post("已经登录");
            finish();
        }
    }

    public void WxTextShare(String text, int judge) {
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;
        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = textObject;
        message.description = text;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = message;
        req.scene = judge;//分享类型，0为好友 1为朋友圈
        IWXAPI api = WXAPIFactory.createWXAPI(this, Config.APP_ID, true);
        api.registerApp(Config.APP_ID);
        api.sendReq(req);
    }

    @Subscribe
    public void getWxOpenId(WxInfo wxInfo) {
        requestQQLogin(PlatformContans.User.sLoginByWechat, wxInfo.openId, 1);
    }

}

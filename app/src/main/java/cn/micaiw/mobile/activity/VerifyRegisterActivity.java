package cn.micaiw.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.MyApplication;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.common.customview.VerificationCodeInput;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.custom.KyLoadingBuilder;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.ToaskUtil;

public class VerifyRegisterActivity extends BaseActivity implements VerificationCodeInput.Listener {

    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.showPswImg)
    ImageView showPswImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.showPhoneNumber)
    TextView showPhoneNumber;
    @BindView(R.id.inputVerify)
    VerificationCodeInput inputVerify;
    @BindView(R.id.againSendVerify)
    TextView againSendVerify;
    @BindView(R.id.inputPsw)
    EditText inputPsw;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.view1)
    View view1;

    private boolean isShowPsw = false;//是否显示密码
    private String mTelephone;
    public static final int REQUE_AGAIN_AUTH_CODE = 0;
    public String mInputActhCode = "";//用户输入的验证码
    //添加用户的参数
    Map<String, Object> mAddUserParams = new HashMap<>();

    private int comeInType = 1;//进入类型，0为注册获取验证码，1为验证码登录
    private KyLoadingBuilder mOpenLoadView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_verify_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        backImg.setVisibility(View.VISIBLE);
        title.setText("填写验证码");
        inputVerify.setOnCompleteListener(this);
        inputVerify.setEnabled(true);

        Intent intent = getIntent();
        mTelephone = intent.getStringExtra("telephone");
        comeInType = intent.getIntExtra("comeInType", 1);

        if (!TextUtils.isEmpty(mTelephone)) {
            showPhoneNumber.setText(mTelephone);
            againSendVerify.setEnabled(false);
            mHandler.sendEmptyMessage(REQUE_AGAIN_AUTH_CODE);
        }
        if (comeInType == 0) {
            inputPsw.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
        } else {
            inputPsw.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
        }
        messageImg.setVisibility(View.GONE);
        inputPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
//                if (TextUtils.isEmpty(s)) {
//                    showPswImg.setVisibility(View.GONE);
//                } else {
//                    showPswImg.setVisibility(View.VISIBLE);
//                }
                if (s.length() == 26) {
                    ToaskUtil.showToast(VerifyRegisterActivity.this, "密码长度不能大于26");
                }
            }
        });

    }

    @OnClick({R.id.backImg, R.id.next, R.id.showPswImg, R.id.againSendVerify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.next:
                if (MyApplication.isDebug) {
                    Intent data = new Intent();
                    data.putExtra("message", "注册成功");
                    setResult(0, data);
                    finish();
                } else {
                    if (checkFrom()) {
                        next.setEnabled(false);
                        mOpenLoadView = openLoadView("");
                        if (comeInType == 0) {//注册
                            register();
                        } else {//短信验证码登录
                            requestRegister();
                        }
                    }
                }
                break;
            case R.id.showPswImg:
//                if (isShowPsw) {
//                    isShowPsw = false;
//                    //如果选中，显示密码
//                    inputPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
//                } else {
//                    isShowPsw = true;
//                    //否则隐藏密码
//                    inputPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                }

                break;
            case R.id.againSendVerify:
                againSendVerify.setEnabled(false);
                mHandler.sendEmptyMessage(REQUE_AGAIN_AUTH_CODE);
                obtianAuthCode();
                break;
        }
    }


    //获取验证码
    private void obtianAuthCode() {
        Map<String, Object> headParamsMap = new HashMap<>();
        headParamsMap.put("telephone", mTelephone);
        HttpProxy.obtain().get(PlatformContans.User.sHead + PlatformContans.User.sGetVerificationCode,
                headParamsMap, new ICallBack() {
                    @Override
                    public void OnSuccess(String result) {
                        //{"resultCode":0,"message":"验证码已发送！"}
                        try {
                            JSONObject object = new JSONObject(result);
                            int resultCode = object.optInt("resultCode");
                            final String message = object.optString("message");
                            ToaskUtil.showToast(VerifyRegisterActivity.this, message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                    }

                });

    }

    //注册
    private void register() {
        final String cachePassword = inputPsw.getEditableText().toString().replace(" ", "");
        final String inputActhCode = mInputActhCode;
        Map<String, Object> hasMap = new HashMap<>();
        hasMap.put("userName", mTelephone);
        hasMap.put("password", cachePassword);
        hasMap.put("code", inputActhCode);
        HttpProxy.obtain().post(PlatformContans.User.sAddUser, hasMap, new ICallBack() {
            @Override
            public void OnSuccess(final String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    final int resultCode = object.optInt("resultCode");
                    final String message = object.optString("message");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            next.setEnabled(true);
                            if (mOpenLoadView != null) {
                                closeLoadView(mOpenLoadView);
                            }
                            if (resultCode != 0) {
                                ToaskUtil.showToast(VerifyRegisterActivity.this, message);
                            } else {
                                //{"resultCode":0,"message":"添加成功"}
                                ToaskUtil.showToast(VerifyRegisterActivity.this, message);
                                Intent data = new Intent();
                                data.putExtra("message", message);
                                setResult(0, data);
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        next.setEnabled(true);
                        if (mOpenLoadView != null) {
                            closeLoadView(mOpenLoadView);
                        }
                        ToaskUtil.showToast(VerifyRegisterActivity.this, "请检查网络");
                    }
                });
            }
        });
    }


    //手机验证码登录
    private void requestRegister() {
        final String inputActhCode = mInputActhCode;
        mAddUserParams.put("userName", mTelephone);
        mAddUserParams.put("code", inputActhCode);
        HttpProxy.obtain().post(PlatformContans.User.sUserLoginByCode, mAddUserParams, new ICallBack() {
            @Override
            public void OnSuccess(final String result) {
                Log.d("lingtao", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    final int resultCode = object.optInt("resultCode");
                    final String message = object.optString("message");
//                    JSONObject data = object.optJSONObject("data");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            next.setEnabled(true);
                            if (mOpenLoadView != null) {
                                closeLoadView(mOpenLoadView);
                            }
                            if (resultCode != 0) {
                                ToaskUtil.showToast(VerifyRegisterActivity.this, message);
                            } else {
                                //{"resultCode":0,"message":"添加成功"}
//                                ToaskUtil.showToast(VerifyRegisterActivity.this, message);
                                Intent data = new Intent();
                                data.putExtra("loginType", 0);
                                data.putExtra("result", result);
                                data.putExtra("message", message);
                                data.putExtra("userName", mTelephone);
//                                data.putExtra("password", cachePassword);
                                data.putExtra("code", inputActhCode);
                                setResult(0, data);
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
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        next.setEnabled(true);
                        if (mOpenLoadView != null) {
                            closeLoadView(mOpenLoadView);
                        }
                        ToaskUtil.showToast(VerifyRegisterActivity.this, "请检查网络");
                    }
                });
            }
        });
    }

    private boolean checkFrom() {
        if (TextUtils.isEmpty(mInputActhCode)) {
            ToaskUtil.showToast(this, "请输入验证码");
            return false;
        }
        if (mInputActhCode.length() < 6) {
            ToaskUtil.showToast(this, "请输入完整验证码");
            return false;
        }
        if (comeInType == 0) {//手机注册，需要密码是否符合
            String intpuPassword = inputPsw.getEditableText().toString().replace(" ", "");
            if (TextUtils.isEmpty(intpuPassword)) {
                ToaskUtil.showToast(this, "请输入密码");
                return false;
            }
            if (intpuPassword.length() < 6) {
                ToaskUtil.showToast(this, "密码长度不能小于6位");
                return false;
            }

        }
//        else {//验证码登录，不需要验证密码
//            return true;
//        }
        return true;
    }

//    /**
//     * 打开loading
//     */
//    public void openLoadView() {
//        mLoadingBuilder = new KyLoadingBuilder(this);
//        mLoadingBuilder.setIcon(R.mipmap.logo);
//        mLoadingBuilder.setText("正在加载中...");
//        //builder.setOutsideTouchable(false);
//        //builder.setBackTouchable(true);
//        mLoadingBuilder.show();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private MyHandler mHandler = new MyHandler(this);

    @Override
    public void onComplete(String content) {
        mInputActhCode = content;
    }

    public static class MyHandler extends Handler {
        private WeakReference<Activity> activity;
        public int mCountDown = 59;

        public MyHandler(Activity activity) {
            this.activity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VerifyRegisterActivity activity = (VerifyRegisterActivity) this.activity.get();
            if (activity != null) {
                switch (msg.what) {
                    case REQUE_AGAIN_AUTH_CODE:
                        activity.againSendVerify.setText("重新获取(" + mCountDown + ")");
                        mCountDown--;
                        if (mCountDown <= 0) {
                            mCountDown = 10;
                            activity.againSendVerify.setEnabled(true);
                            activity.againSendVerify.setText("重新获取");
                        } else {
                            sendEmptyMessageDelayed(REQUE_AGAIN_AUTH_CODE, 1000);
                        }
                        break;
                }
            }
        }
    }
}

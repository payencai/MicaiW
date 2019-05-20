package cn.micaiw.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.custom.KyLoadingBuilder;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

public class RetrievePswActivity extends BaseActivity {

    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.boundPhoneNumber)
    TextView boundPhoneNumber;
    @BindView(R.id.authInput)
    EditText authInput;
    @BindView(R.id.passwrodInput)
    EditText passwrodInput;
    private String mTelephone;

    @BindView(R.id.sendAuth)
    TextView sendAuth;
    private KyLoadingBuilder mOpenLoadView;
    private KyLoadingBuilder mLoadView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_retrieve_psw;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        title.setText("找回登录密码");
        mTelephone = getIntent().getStringExtra("tel");
        initTelView();
        backImg.setVisibility(View.VISIBLE);
        messageImg.setVisibility(View.GONE);
    }

    private void initTelView() {
        if (!TextUtils.isEmpty(mTelephone)) {
            //当前手机号：176****7788\n请输入短信验证码重设密码
            boundPhoneNumber.setText("当前手机号：" + hiddenTel(mTelephone) + "\n请输入短信验证码重设密码");
        } else {
            ToaskUtil.showToast(this, "手机号码出错");
            finish();
        }
    }

    private String hiddenTel(String telephone) {
        if (telephone.length() == 11) {
            String one = telephone.substring(0, 3);
            String two = telephone.substring(7, 11);
            return one + "****" + two;
        }
        return "";
    }


    @OnClick({R.id.backImg, R.id.submit, R.id.sendAuth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.sendAuth:
                if (checkPhoneNumber()) {
                    obtianAuthCode();
                }
                break;
            case R.id.submit:
                if (checkAuth()) {
                    if (checkPassword()) {
                        mOpenLoadView = openLoadView("");
                        requeFindPsw();
                    }
                }
                break;
        }
    }

    private boolean checkPassword() {
        String intpuPassword = passwrodInput.getEditableText().toString().replace(" ", "");
        if (TextUtils.isEmpty(intpuPassword)) {
            ToaskUtil.showToast(this, "请输入密码");
            return false;
        }
        if (intpuPassword.length() < 6) {
            ToaskUtil.showToast(this, "密码长度不能小于6位");
            return false;
        }
        return true;
    }

    private boolean checkPhoneNumber() {
        if (TextUtils.isEmpty(mTelephone)) {
            ToaskUtil.showToast(this, "号码出错");
            finish();
            return false;
        }
        if (mTelephone.length() != 11) {
            ToaskUtil.showToast(this, "号码出错");
            finish();
            return false;
        }
        return true;
    }

    /**
     * 获取验证码
     */
    private void obtianAuthCode() {
        sendAuth.setEnabled(false);
        Map<String, Object> requeAuthParamsMap = new HashMap<>();
        requeAuthParamsMap.put("telephone", mTelephone);
        mLoadView = openLoadView("验证码已发送");
        HttpProxy.obtain().get(PlatformContans.User.sHead + PlatformContans.User.sGetVerificationCode,
                requeAuthParamsMap, new ICallBack() {
                    @Override
                    public void OnSuccess(String result) {
                        if (mLoadView != null) {
                            mLoadView.dismiss();
                        }
                        //{"resultCode":0,"message":"验证码已发送！"}
                        Log.d("OnSuccess", "OnSuccess: " + result);
                        try {
                            JSONObject object = new JSONObject(result);
                            int resultCode = object.optInt("resultCode");
                            final String message = object.optString("message");
                            ToaskUtil.showToast(RetrievePswActivity.this, message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        if (mLoadView != null) {
                            mLoadView.dismiss();
                        }
                        Log.d("lingtao", "onFailure: " + error);
                    }

                });

    }

    private boolean checkAuth() {
        String authString = authInput.getEditableText().toString();
        if (TextUtils.isEmpty(authString)) {
            ToaskUtil.showToast(this, "请输入验证码");
            return false;
        }
        if (authString.length() < 6) {
            ToaskUtil.showToast(this, "请输入完整验证码");
            return false;
        }
        return true;
    }

    private void requeFindPsw() {
        String intpuPassword = passwrodInput.getEditableText().toString().replace(" ", "");
        String authString = authInput.getEditableText().toString();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userName", mTelephone);
        paramsMap.put("password", intpuPassword);
        paramsMap.put("code", authString);

        HttpProxy.obtain().post(PlatformContans.User.sFindPasswordByUserName, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("sFindPasswordBy", result);
                if (mOpenLoadView != null) {
                    closeLoadView(mOpenLoadView);
                }
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    ToaskUtil.showToast(RetrievePswActivity.this, message);
                    if (resultCode == 0) {
                        Intent data = new Intent();
                        setResult(0, data);
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                if (mOpenLoadView != null) {
                    closeLoadView(mOpenLoadView);
                }
            }
        });
    }

}

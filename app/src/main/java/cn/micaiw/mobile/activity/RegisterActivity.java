package cn.micaiw.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.MyApplication;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.PhoneUtil;
import cn.micaiw.mobile.util.ToaskUtil;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.editPhoneNumber)
    EditText editPhoneNumber;
    @BindView(R.id.next)
    Button next;


    private String mTel;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        backImg.setVisibility(View.VISIBLE);
        messageImg.setVisibility(View.GONE);
        title.setText("注册");
        mTel = getIntent().getStringExtra("tel");
        if (!TextUtils.isEmpty(mTel)) {
            editPhoneNumber.setText(mTel);
        }

    }


    @OnClick({R.id.backImg, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.next:
                if (MyApplication.isDebug) {
                    Intent intent = new Intent(this, VerifyRegisterActivity.class).putExtra("telephone", "18878554275");
                    intent.putExtra("comeInType", 0);
                    startActivityForResult(intent, 0);
                } else {
                    if (checkForm()) {
                        next.setEnabled(false);
                        //验证账户是否已经存在
                        isRegister();
                    }
                }
                break;

        }
    }

    private void next() {
        ToaskUtil.showToast(this, "验证码已发送");
        String tel = editPhoneNumber.getEditableText().toString();
        obtianAuthCode(tel);
        Intent intent = new Intent(this, VerifyRegisterActivity.class).putExtra("telephone", tel);
        intent.putExtra("comeInType", 0);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        next.setEnabled(true);
    }

    //验证用户是否已经存在
    public void isRegister() {
        Map<String, Object> has = new HashMap<>();
        has.put("userName", editPhoneNumber.getEditableText().toString());
        HttpProxy.obtain().get(PlatformContans.User.sGetUserByUserName, has, new ICallBack() {
            @Override
            public void OnSuccess(final String result) {
                try {
                    Log.d("isRegister", "OnSuccess: " + result);
                    JSONObject object = new JSONObject(result);
                    final int resultCode = object.optInt("resultCode");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            next.setEnabled(true);
                            if (resultCode == 0) {//已经存在
                                ToaskUtil.showToast(RegisterActivity.this, "用户已经存在");
                            } else {
                                next();
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
                        ToaskUtil.showToast(RegisterActivity.this, "请检查网络");
                        next.setEnabled(true);
                    }
                });
            }
        });
    }

    private void obtianAuthCode(String tel) {
        Map<String, Object> hasMap = new HashMap<>();
        hasMap.put("telephone", tel);
        HttpProxy.obtain().get(PlatformContans.User.sHead + PlatformContans.User.sGetVerificationCode,
                hasMap, new ICallBack() {
                    @Override
                    public void OnSuccess(String result) {
                        //{"resultCode":0,"message":"验证码已发送！"}
                        Log.d("lingtao", "OnSuccess: " + result);
                        try {
                            JSONObject object = new JSONObject(result);
                            int resultCode = object.optInt("resultCode");
                            final String message = object.optString("message");
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

    private boolean checkForm() {
        String str = editPhoneNumber.getEditableText().toString();
        if (TextUtils.isEmpty(str)) {
            ToaskUtil.showToast(this, "请输入号码");
            return false;
        }
//        if (!PhoneUtil.isPhoneNumber(str)) {
        if (str.length() != 11) {
            ToaskUtil.showToast(this, "请输入正确的手机号码");
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 0) {
                next.setEnabled(true);
                String message = data.getStringExtra("message");
                ToaskUtil.showToast(this, message);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
    }
}

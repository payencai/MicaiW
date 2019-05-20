package cn.micaiw.mobile.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.custom.KyLoadingBuilder;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ActivityManager;
import cn.micaiw.mobile.util.ToaskUtil;

public class AmendLoginPswActivity extends BaseActivity {

    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.messageImg)
    ImageView messageImg;

    @BindView(R.id.boundPhoneNumber)
    TextView boundPhoneNumber;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.primevalPassword)
    EditText primevalPassword;
    @BindView(R.id.newPassword)
    EditText newPassword;
    private String mTel;
    private int count = 5;//原始密码修改错误次数
    private KyLoadingBuilder mLoadView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_amend_login_psw;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        title.setText("修改登录密码");
        backImg.setVisibility(View.VISIBLE);
        messageImg.setVisibility(View.GONE);
        initView();
    }

    private void initView() {
        mTel = getIntent().getStringExtra("tel");
        if (!TextUtils.isEmpty(mTel)) {
            boundPhoneNumber.setText("当前手机号：" + mTel);
        }

    }

    @OnClick({R.id.backImg, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.submit:
                if (checkAll()) {
                    requestData();
                }
                break;
        }
    }

    private boolean checkAll() {
        String password = primevalPassword.getEditableText().toString();
        String pwd = newPassword.getEditableText().toString();
        String savePassword = UserInfoSharedPre.getIntance(this).getPassword();

        if (TextUtils.isEmpty(password)) {
            ToaskUtil.showToast(this, "请输入原始密码");
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToaskUtil.showToast(this, "请输入新密码");
            return false;
        }
        if (pwd.length() < 6) {
            ToaskUtil.showToast(this, "密码长度必须大于6位");
            return false;
        }
        if (!password.equals(savePassword)) {
            count--;
            ToaskUtil.showToast(this, "原始密码错误,当前还可以输入:" + count + "次");
            if (count <= 0) {
                ToaskUtil.showToast(this, "操作频繁，休息一下吧");
                finish();
                return false;
            }
            return false;
        }

        return true;
    }

    private void requestData() {
        String password = primevalPassword.getEditableText().toString();
        String pwd = newPassword.getEditableText().toString();
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> tokenMap = new HashMap<>();
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        String token = intance.getToken();
        if (TextUtils.isEmpty(token)) {
            ToaskUtil.showToast(this, "您还未登录");
            return;
        }
        paramsMap.put("userName", mTel);
        paramsMap.put("password", password);//原始密码
        paramsMap.put("pwd", pwd);//新密码
        tokenMap.put("token", token);
        mLoadView = openLoadView("");
        HttpProxy.obtain().post(PlatformContans.User.sUpdatePasswordByUserName, tokenMap, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                if (mLoadView != null) {
                    mLoadView.dismiss();
                }
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        ToaskUtil.showToast(AmendLoginPswActivity.this, message + ",请重新登录");
                        UserInfoSharedPre.getIntance(AmendLoginPswActivity.this).clearUserInfo();
                        ActivityManager.getInstance().finishAllActivity();
                        startActivity(new Intent(AmendLoginPswActivity.this, LoginActivity.class));
                    } else {
                        ToaskUtil.showToast(AmendLoginPswActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                if (mLoadView != null) {
                    mLoadView.dismiss();
                }
            }
        });
    }


}

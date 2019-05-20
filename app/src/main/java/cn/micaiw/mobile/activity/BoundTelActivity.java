package cn.micaiw.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.micaiw.mobile.MyApplication;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.BoundResultInfo;
import cn.micaiw.mobile.entity.UserInfo;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ToaskUtil;

public class BoundTelActivity extends BaseActivity {

    @BindView(R.id.backImg)
    ImageView backImg;
//    @BindView(R.id.messageImg)
//    ImageView messageImg;
//    @BindView(R.id.saveBtn)
//    TextView saveBtn;
    @BindView(R.id.editPhoneNumber)
    EditText editPhoneNumber;
    @BindView(R.id.editPswInput)
    EditText editPswInput;
    @BindView(R.id.hintText)
    TextView hintText;
    @BindView(R.id.view2)
    View view2;

    @BindView(R.id.submit)
    Button submit;

    private String openId = "";
    private int intoType = 0;

    public static void stateBoundActivity(Context context, String openId, int intoType) {
        Intent intent = new Intent(context, BoundTelActivity.class);
        intent.putExtra("openid", openId);
        intent.putExtra("intoType", intoType);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_bound_tel;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        backImg.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        intoType = intent.getIntExtra("intoType", -1);
        openId = intent.getStringExtra("openid");

    }

    @OnClick({R.id.backImg, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.submit:
                String tel = editPhoneNumber.getEditableText().toString();
                String s = editPswInput.getEditableText().toString();
                if (checkFrom(tel, s)) {
                    if (intoType == 0) {
                        requestBound(PlatformContans.User.sRegisterWithQq, tel, s, openId, intoType);
                    } else {
                        requestBound(PlatformContans.User.sRegisterWithWechat, tel, s, openId, intoType);
                    }
                }
                break;
        }
    }

    private boolean checkFrom(String tel, String psw) {
        if (TextUtils.isEmpty(tel)) {
            ToaskUtil.showToast(this, "请输入号码");
            return false;
        }
        if (tel.length() != 11) {
            ToaskUtil.showToast(this, "号码格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(psw)) {
            ToaskUtil.showToast(this, "请输入密码");
            return false;
        }
        return true;
    }

    private void requestBound(String url, String tel, final String psw, String openId, int intoType) {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", tel);
        map.put("password", psw);
        if (intoType == 0) {
            map.put("qqId", openId);//QQ
        } else {
            map.put("openId", openId);//微信
        }
        HttpProxy.obtain().post(url, map, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("boundingdata", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    ToaskUtil.showToast(BoundTelActivity.this, message);
                    if (resultCode == 0) {
                        JSONObject data = object.optJSONObject("data");
                        UserInfo userInfo = new Gson().fromJson(data.toString(), UserInfo.class);
                        userInfo.setPassword(psw);
                        UserInfoSharedPre sharedPre = UserInfoSharedPre.getIntance(BoundTelActivity.this);
                        sharedPre.saveUserInfo(userInfo, true);
                        sharedPre.setUserIsLogin(true);
                        BoundResultInfo resultInfo = new BoundResultInfo();
                        resultInfo.isSucceed = true;
                        EventBus.getDefault().post(resultInfo);
                        finish();
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

    private void isShow(boolean b) {
        if (!b) {
            view2.setVisibility(View.GONE);
            hintText.setVisibility(View.GONE);
            editPswInput.setVisibility(View.GONE);
        } else {
            view2.setVisibility(View.VISIBLE);
            hintText.setVisibility(View.VISIBLE);
            editPswInput.setVisibility(View.VISIBLE);
        }

    }
}

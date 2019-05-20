package cn.micaiw.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.Constans;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ToaskUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddBankCardOrZFBActivity extends BaseActivity {
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.accountInput)
    EditText accountInput;
    @BindView(R.id.phoneInput)
    EditText phoneInput;

    @BindView(R.id.bankName)
    TextView mBankName;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.bankLayout)
    LinearLayout bankLayout;
    @BindView(R.id.bankLogo)
    ImageView bankLogo;
    private int mType;
    private String mBankNameString;
    private String mBankTag;
    private String mUrl;


    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, AddBankCardOrZFBActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_bank_card;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        backImg.setVisibility(View.VISIBLE);
        messageImg.setVisibility(View.GONE);
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", -1);
        if (mType == 1) {//支付宝
            bankLayout.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            title.setText("支付宝");
            accountInput.setHint("支付宝账号");
            phoneInput.setHint("支付宝对应真实姓名");

        } else {//银行
            title.setText("银行卡");
            accountInput.setHint("银行卡号");
            phoneInput.setHint("银行卡预留手机号");
            accountInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String s = editable.toString().replace(" ", "");
                    if (s.length() == 19) {
                        requestBankTag(s);
                    }
                }
            });
        }

    }

    @OnClick({R.id.backImg, R.id.submitCard})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.submitCard:
                if (mType == 0) {//提交银行卡
                    requestBankSubmit();
                } else {//Alipay
                    requestAlipaySumit();
                }
                break;
        }
    }

    //https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=6221506020009066385&cardBinCheck=true

    private void requestBankTag(String number) {
        String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=" + number + "&cardBinCheck=true";
        HttpProxy.obtain().get(url, null, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                /*{"bank": "PSBC","cardType": "DC","key": "6221506020009066385","messages": [],"stat": "ok","validated": true}*/
                Log.d("ccdcapi", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    boolean validated = object.getBoolean("validated");
                    if (validated) {
                        mBankTag = object.getString("bank");
                        if (TextUtils.isEmpty(mBankTag)) {
                            mBankName.setText("卡号出错");
                            mBankName.setTextColor(Color.RED);
                            return;
                        }
                        Map<String, String> map = Constans.getBankCardTagMap();
                        mBankNameString = map.get(mBankTag);
                        if (TextUtils.isEmpty(mBankNameString)) {
                            mBankName.setText("卡号出错");
                            mBankName.setTextColor(Color.RED);
                        } else {
                            mBankName.setText(mBankNameString);
                            mBankName.setTextColor(Color.parseColor("#333333"));
                            requestBankImg(mBankTag);
                        }
                    } else {
                        mBankName.setText("无效银行卡");
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

    private void requestBankImg(String tag) {
        mUrl = "http://120.78.77.138:8080/bank/" + tag + ".png";
        Log.d("requestBankImg", "requestBankImg: " + mUrl);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(mUrl).build();
        request.url();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bankLogo.setVisibility(View.VISIBLE);
                        bankLogo.setImageBitmap(bitmap);
                    }
                });
            }

            @Override
            public void onFailure(Call call, final IOException e) {

            }
        });
    }

    //添加银行卡
    private void requestBankSubmit() {
        Map<String, Object> bodyMap = new HashMap<>();
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        int userId = intance.getUserId();
        String tel = phoneInput.getEditableText().toString();
        if (userId == -1) {
            ToaskUtil.showToast(this, "登录过期");
            intance.clearUserInfo();
            return;
        }
        String account = accountInput.getEditableText().toString().replace(" ", "");
        if (TextUtils.isEmpty(account)) {
            ToaskUtil.showToast(this, "账号不能为空");
            return;
        }
        if (account.replace(" ", "").length() != 19) {
            ToaskUtil.showToast(this, "卡号不正确！");
            return;
        }
        if (TextUtils.isEmpty(mBankNameString)) {
            ToaskUtil.showToast(this, "未知银行卡！");
            return;
        }
        if (TextUtils.isEmpty(tel)) {
            ToaskUtil.showToast(this, "请输入预留手机号！");
            return;
        }
        if (tel.length() != 11) {
            ToaskUtil.showToast(this, "号码格式不正确！");
            return;
        }
        bodyMap.put("userId", userId);
        bodyMap.put("account", account);
        bodyMap.put("bankName", mBankNameString);
        bodyMap.put("telephone", tel);
        bodyMap.put("bankImg", mUrl);

        HashMap<String, Object> headMap = new HashMap<>();
        String token = intance.getToken();
        if (TextUtils.isEmpty(token)) {
            ToaskUtil.showToast(this, "登录过期");
            intance.clearUserInfo();
            return;
        }
        headMap.put("token", token);
        HttpProxy.obtain().post(PlatformContans.User.sAddBankCardByUserId, headMap, bodyMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                //{"resultCode":0,"message":"添加成功"}
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        ToaskUtil.showToast(AddBankCardOrZFBActivity.this, message);
                        finish();
                    } else {
                        ToaskUtil.showToast(AddBankCardOrZFBActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    ToaskUtil.showToast(AddBankCardOrZFBActivity.this, "登录过期");
//                    UserInfoSharedPre.getIntance(AddBankCardOrZFBActivity.this).clearUserInfo();
//                    finish();
                    ToaskUtil.showToast(AddBankCardOrZFBActivity.this, "登录过期");
                }
            }

            @Override
            public void onFailure(String error) {
                ToaskUtil.showToast(AddBankCardOrZFBActivity.this, "添加失败，请检查网络");
            }
        });
    }

    //添加支付宝
    private void requestAlipaySumit() {
        Map<String, Object> bodyMap = new HashMap<>();
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        int userId = intance.getUserId();
        if (userId == -1) {
            ToaskUtil.showToast(this, "登录过期");
            intance.clearUserInfo();
            return;
        }
        String account = accountInput.getEditableText().toString();
        String name = phoneInput.getEditableText().toString();
        if (TextUtils.isEmpty(account)) {
            ToaskUtil.showToast(this, "账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToaskUtil.showToast(this, "请填写真实姓名");
            return;
        }

        bodyMap.put("userId", userId);
        bodyMap.put("account", account);
        bodyMap.put("name", name);
        bodyMap.put("bankImg", "http://www.micaiqb.com:8080/bank/Alipay.png");
        HashMap<String, Object> headMap = new HashMap<>();
        String token = intance.getToken();
        if (TextUtils.isEmpty(token)) {
            ToaskUtil.showToast(this, "登录过期");
            intance.clearUserInfo();
            return;
        }
        headMap.put("token", token);
        HttpProxy.obtain().post(PlatformContans.User.sAddAlipayByUserId, headMap, bodyMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                //{"resultCode":0,"message":"添加成功"}
                Log.d("Alipay", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        ToaskUtil.showToast(AddBankCardOrZFBActivity.this, message);
                        finish();
                    } else {
                        ToaskUtil.showToast(AddBankCardOrZFBActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    ToaskUtil.showToast(AddBankCardOrZFBActivity.this, "登录过期");
//                    UserInfoSharedPre.getIntance(AddBankCardOrZFBActivity.this).clearUserInfo();
//                    finish();
                }
            }

            @Override
            public void onFailure(String error) {
                ToaskUtil.showToast(AddBankCardOrZFBActivity.this, "添加失败，请检查网络");
            }
        });

    }

}

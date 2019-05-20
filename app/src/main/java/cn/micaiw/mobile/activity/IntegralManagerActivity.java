package cn.micaiw.mobile.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.custom.KyLoadingBuilder;
import cn.micaiw.mobile.custom.MonthSignFormView;
import cn.micaiw.mobile.custom.WeekSignFormView;
import cn.micaiw.mobile.entity.ScoreRegulation;
import cn.micaiw.mobile.entity.SignDay;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

public class IntegralManagerActivity extends BaseActivity {
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.saveBtn)
    TextView saveBtn;
    @BindView(R.id.integralTextShow)
    TextView integralTextShow;
    @BindView(R.id.continueSignText)
    TextView continueSignText;

    @BindView(R.id.integralConversionBtn)
    TextView integralConversionBtn;

    MonthSignFormView signView;
    //是否可以签到
    private boolean isCanSign = false;
    private KyLoadingBuilder mOpenLoadView;
    private PopupWindow mSignPw;
    private int mIntegralBySign;
    private int mContinueSign;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_integral_manager;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        backImg.setVisibility(View.VISIBLE);
        messageImg.setVisibility(View.GONE);
        saveBtn.setVisibility(View.VISIBLE);
        saveBtn.setText("积分规则");
        saveBtn.setTextSize(14);
        saveBtn.setTextColor(Color.parseColor("#999999"));
        title.setText("积分管理");
        getSignByUserId();
        getScoreByUserId();
        signView = (MonthSignFormView) findViewById(R.id.monthSignFormView);
        mContinueSign = UserInfoSharedPre.getIntance(this).getContinueSign();
        continueSignText.setText(mContinueSign + "");
    }


    @OnClick({R.id.integralConversionBtn, R.id.backImg, R.id.signBtn, R.id.saveBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.integralConversionBtn:
                startActivity(new Intent(this, IntegraexChangelActivity.class));
                break;
            case R.id.signBtn:
//                mOpenLoadView = openLoadView("");
                requestSignData();
//                Intent intent = new Intent();
//                intent.putExtra("isFromSign", true);
//                setResult(999, intent);
//                finish();
                break;
            case R.id.saveBtn:
                startActivity(new Intent(this, IntegralRuleActivity.class));
                break;
        }
    }


    //获取用户可用积分
    private void getScoreByUserId() {
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> tokenMap = new HashMap<>();
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        int userId = intance.getUserId();
        String tokenValue = intance.getToken();
        if (userId == -1 || TextUtils.isEmpty(tokenValue)) {
            return;
        }
        paramsMap.put("userId", userId);
        tokenMap.put("token", tokenValue);
        HttpProxy.obtain().get(PlatformContans.User.sGetScoreByUserId, paramsMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                try {
                    MLog.log("ScoreByUserId66", result);
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        int number = object.getJSONObject("data").getInt("number");
                        integralTextShow.setText(number + "积分");
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

    //获取当月签到情况
    private void getSignByUserId() {
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> tokenMap = new HashMap<>();
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        int userId = intance.getUserId();
        String tokenValue = intance.getToken();
        if (userId == -1 || TextUtils.isEmpty(tokenValue)) {
            return;
        }
        paramsMap.put("userId", userId);
        tokenMap.put("token", tokenValue);
        HttpProxy.obtain().get(PlatformContans.User.sGetSignByUserId, paramsMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("sGetSignByUserId", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        int dayCount = getCurrentMonthDay();
                        Map<Integer, Boolean> signMap = new HashMap<>();
                        for (int i = 1; i <= dayCount; i++) {
                            signMap.put(i, false);
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            String signTime = item.getString("signTime");
                            String oneStrign = signTime.replace(" ", "_").split("_")[0];
                            String s = oneStrign.split("-")[2];
                            int parseInt = Integer.parseInt(s);
                            signMap.put(parseInt, true);
                        }
                        signView.setIsSign(signMap);
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


    /**
     * 获取当月的 天数
     */
    private static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    //获取连续签到次数
    private void requestSignData() {
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> tokenMap = new HashMap<>();
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        String token = intance.getToken();
        if (TextUtils.isEmpty(token)) {
            ToaskUtil.showToast(this, "未登录");
            return;
        }
        int userId = intance.getUserId();
        if (userId == -1) {
            ToaskUtil.showToast(this, "未登录");
            return;
        }
        paramsMap.put("userId", userId);
        tokenMap.put("token", token);
        mOpenLoadView = openLoadView("");
        HttpProxy.obtain().get(PlatformContans.User.sIsSignByUserId, paramsMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                if (mOpenLoadView != null) {
                    mOpenLoadView.dismiss();
                }
                try {
                    JSONObject object = new JSONObject(result);
                    Log.d("lingtaouserId", "OnSuccess: " + result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        if (message.equals("用户未签到")) {
                            isCanSign = true;
                            JSONObject data = object.getJSONObject("data");
                            int signState = data.getInt("signState");
                            JSONObject scoreRegulation = data.getJSONObject("scoreRegulation");
                            Gson gson = new Gson();
                            ScoreRegulation regulation = null;
                            regulation = gson.fromJson(scoreRegulation.toString(), ScoreRegulation.class);
                            regulation.setSignState(signState);
                            if (regulation != null) {
                                setBackgroundDrakValue(0.5f);
                                showContinuousSign(regulation);
                            }
                        } else {
                            ToaskUtil.showToast(IntegralManagerActivity.this, "当天已签到");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                if (mOpenLoadView != null) {
                    mOpenLoadView.dismiss();
                }
            }
        });
    }

    public void showContinuousSign(ScoreRegulation regulation) {
        View shareview = LayoutInflater.from(this).inflate(R.layout.pw_sign_layout, null);
        initSignView(shareview, regulation);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        mSignPw = new PopupWindow(shareview, ((int) (d.getWidth() * 0.8)), ((int) (d.getHeight() * 0.5)));
        mSignPw.setFocusable(true);
        mSignPw.setBackgroundDrawable(new BitmapDrawable());
        mSignPw.setOutsideTouchable(true);
        mSignPw.setAnimationStyle(R.style.CustomPopWindowStyle);
        mSignPw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundDrakValue(1.0f);
            }
        });
        //第一个参数可以取当前页面的任意一个View
        //第二个参数表示pw从哪一个方向显示出来
        //3、4表示pw的偏移量
        mSignPw.showAtLocation(shareview, Gravity.CENTER, 0, 0);
    }

    private void initSignView(View view, ScoreRegulation regulation) {
        final TextView signCount = (TextView) view.findViewById(R.id.signCount);
        TextView textActivityRule = (TextView) view.findViewById(R.id.textActivityRule);
        final WeekSignFormView weekSignFrom = (WeekSignFormView) view.findViewById(R.id.weekSignFrom);
        final int signState = regulation.getSignState();
        int monday = regulation.getMonday();
        int tuesday = regulation.getTuesday();
        int wednesday = regulation.getWednesday();
        int thurday = regulation.getThurday();
        int friday = regulation.getFriday();
        int saturday = regulation.getSaturday();
        int sunday = regulation.getSunday();
        int[] ints = new int[7];
        ints[0] = monday;
        ints[1] = tuesday;
        ints[2] = wednesday;
        ints[3] = thurday;
        ints[4] = friday;
        ints[5] = saturday;
        ints[6] = sunday;
        mIntegralBySign = ints[signState];
        signCount.setText("已累计签到" + signState + "天");
        weekSignFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCanSign) {//IntegralRuleActivity
                    weekSignFrom.setEnabled(false);
                    addUserSign(mIntegralBySign, weekSignFrom);
                    signCount.setText("已累计签到" + (signState + 1) + "天");
                } else {
                    ToaskUtil.showToast(IntegralManagerActivity.this, "当天已经签到");
                }
            }
        });
        textActivityRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntegralManagerActivity.this, IntegralRuleActivity.class));
            }
        });
        String[] arr = new String[7];
        Map<Integer, Boolean> signMap = new HashMap<>();
        for (int i = 1; i < 8; i++) {
            signMap.put(i, false);
        }

        int number = signState + 1;
        for (int i = 1; i < number; i++) {
            signMap.put(i, true);
        }

        arr[0] = "积分." + monday + "";
        arr[1] = "积分." + tuesday + "";
        arr[2] = "积分." + wednesday + "";
        arr[3] = "积分." + thurday + "";
        arr[4] = "积分." + friday + "";
        arr[5] = "积分." + saturday + "";
        arr[6] = "积分." + sunday + "";
        weekSignFrom.setIntegralArr(arr);
        weekSignFrom.setIsSign(signMap);

    }

    private void addUserSign(int number, final WeekSignFormView weekSignFrom) {
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        int userId = intance.getUserId();
        String token = intance.getToken();
        if (userId == -1 || TextUtils.isEmpty(token)) {
            return;
        }
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> tokenMap = new HashMap<>();
        paramsMap.put("userId", userId);
        paramsMap.put("number", number);
        tokenMap.put("token", token);
        HttpProxy.obtain().get(PlatformContans.User.sAddSignByUserId, paramsMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    ToaskUtil.showToast(IntegralManagerActivity.this, message);
                    if (resultCode == 0) {
                        isCanSign = false;
                        weekSignFrom.setEnabled(true);
                        weekSignFrom.addSign();
                        mContinueSign++;
                        continueSignText.setText(mContinueSign + "");
                        UserInfoSharedPre intance1 = UserInfoSharedPre.getIntance(IntegralManagerActivity.this);
                        intance1.saveContinueSign(mContinueSign);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                weekSignFrom.setEnabled(true);
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

}

package cn.micaiw.mobile.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.micaiw.mobile.MyApplication;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.custom.WeekSignFormView;
import cn.micaiw.mobile.entity.ScoreRegulation;
import cn.micaiw.mobile.entity.UserInfo;
import cn.micaiw.mobile.fragment.BankFragment;
import cn.micaiw.mobile.fragment.FirstOrderFragment;
import cn.micaiw.mobile.fragment.HomeFragment;
import cn.micaiw.mobile.fragment.MineFragment;
import cn.micaiw.mobile.fragment.MoneyGiveFragment;
import cn.micaiw.mobile.fragment.MoreOrderFragment;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;
import customview.ConfirmDialog;
import util.UpdateAppUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener, MineFragment.onShowSignFrom {
    private static final String TAG = "MainActivity";
    HomeFragment homeFragment;
    FirstOrderFragment mFirstOrderFragment;
    MoreOrderFragment mMoreOrderFragment;
    BankFragment mBankFragment;
    MineFragment mMineFragment;
    MoneyGiveFragment mMoneyGiveFragment;
    private FragmentManager fm;
    private List<Fragment> fragments;

    View home, first_order, more_order, bank, user,money;
    private ImageView iv_home;
    private ImageView iv_first_order;
    private ImageView iv_more_order;
    private ImageView iv_bank;
    private ImageView iv_user;
    private ImageView iv_money;
    private TextView tv_home;
    private TextView tv_first_order;
    private TextView tv_more_order;
    private TextView tv_bank;
    private TextView tv_user;
    private TextView tv_money;
    private AlertDialog mDialog;

    private PopupWindow mSoSPw;
    private PopupWindow mSignPw;

    //是否可以签到
    private boolean isCanSign = false;

    public static final int LOGIN_REQUEST_CODE = 0;
    public static final int UNLOGIN_HINT_CODE = 1;
    public static final int REQUEST_IS_SIGN_CODE = 2;//请求是否签到
    /**
     * ToaskUtil.showToast(ProxyCentreActivity.this, "登录过期");
     * UserInfoSharedPre.getIntance(ProxyCentreActivity.this).clearUserInfo();
     */

    private MyHandler mHandler = new MyHandler(this);
    private int mIntegralBySign;

    @Override
    protected int getContentViewId() {
        //getstate();
        return R.layout.activity_main;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initview();
        initListener();
        mHandler.sendEmptyMessageDelayed(LOGIN_REQUEST_CODE, 1000);
        checkUpdate();
//        View loginView = LayoutInflater.from(this).inflate(R.layout.pw_login, null);
//        showDialog(228, 329, loginView);
//        initLoginView(loginView);
//        showPopupW(loginView);
    }
    String version="2.0";
    private void checkAndUpdate() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            update(version);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                update(version);
            } else {//申请权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public void checkUpdate(){
        Map<String,Object> head=new HashMap<>();
        head.put("type",2);
        HttpProxy.obtain().get(PlatformContans.User.sGetVersion, head, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    int code=jsonObject.getInt("resultCode");
                    if(code==0){
                        JSONObject object=jsonObject.getJSONObject("data");
                        String newversion=object.getString("version");
                        String downUrl=object.getString("downUrl");
                        version=newversion;
                        newurl=downUrl;
                        Log.e("version",downUrl);
                        if(!getVersion().equals(version))
                        checkAndUpdate();

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
    String newurl="http://www.micaiqb.com:8080/download/cn.micaiw.mobile_2.apk";
    //根据versionName判断跟新
    private void update(String version) {
        Log.e("aaa",newurl);
        UpdateAppUtils.from(this)
                .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME)
                .serverVersionName(version)
                .serverVersionCode(Integer.parseInt(version.substring(0,1)))
                .apkPath(newurl)
                .downloadBy(UpdateAppUtils.DOWNLOAD_BY_APP)
                .isForce(false)
                .update();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    update(version);
                } else {
                    new ConfirmDialog(this, new feature.Callback() {
                        @Override
                        public void callback(int position) {
                            if (position==1){
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + "")); // 根据包名打开对应的设置界面
                                startActivity(intent);
                            }
                        }
                    }).setContent("暂无读写SD卡权限\\n是否前往设置？").show();
                    //.setContent("暂无读写SD卡权限\n是否前往设置？").show();
                }
                break;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "onResume: Activity的onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initview() {
        home = findViewById(R.id.main_home);
        first_order = findViewById(R.id.main_first_order);
        more_order = findViewById(R.id.main_more_order);
        money=findViewById(R.id.main_money_give);
        bank = findViewById(R.id.main_bank);
        user = findViewById(R.id.main_user);

        iv_home = (ImageView) findViewById(R.id.main_iv_home);
        iv_first_order = (ImageView) findViewById(R.id.main_iv_first_order);
        iv_more_order = (ImageView) findViewById(R.id.main_iv_more_order);
        iv_money = (ImageView) findViewById(R.id.main_iv_money_give);
        iv_bank = (ImageView) findViewById(R.id.main_iv_bank);
        iv_user = (ImageView) findViewById(R.id.main_iv_user);
        tv_home = (TextView) findViewById(R.id.main_tv_home);
        tv_first_order = (TextView) findViewById(R.id.main_tv_first_order);
        tv_more_order = (TextView) findViewById(R.id.main_tv_more_order);
        tv_money = (TextView) findViewById(R.id.main_tv_money_give);
        tv_bank = (TextView) findViewById(R.id.main_tv_bank);
        tv_user = (TextView) findViewById(R.id.main_tv_user);

        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        mFirstOrderFragment = new FirstOrderFragment();
        mMoreOrderFragment = new MoreOrderFragment();
        //mBankFragment = new BankFragment();
        mMineFragment = new MineFragment();
        mMoneyGiveFragment=new MoneyGiveFragment();
        fragments = new ArrayList<>();
        fragments.add(homeFragment);
        fragments.add(mFirstOrderFragment);
        fragments.add(mMoreOrderFragment);
        fragments.add(mMoneyGiveFragment);
        fragments.add(mMineFragment);

        for (Fragment fragment : fragments) {
            fm.beginTransaction().add(R.id.main_frame, fragment).commit();
        }
        //显示主页
        resetStateForTagbar(R.id.main_home);
        hideAllFragment();
        showFragment(0);
    }

    private void initListener() {
        home.setOnClickListener(this);
        first_order.setOnClickListener(this);
        more_order.setOnClickListener(this);
        money.setOnClickListener(this);
        bank.setOnClickListener(this);
        user.setOnClickListener(this);
    }

    private void resetStateForTagbar(int viewId) {
        clearTagbarState();
        if (viewId == R.id.main_home) {
            tv_home.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
            iv_home.setImageResource(R.mipmap.common_tab_normal);
            return;
        }
        if (viewId == R.id.main_first_order) {//首投
            tv_first_order.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
            iv_first_order.setImageResource(R.mipmap.common_tab_selected_st);
            return;
        }
        if (viewId == R.id.main_more_order) {//复投
            tv_more_order.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
            iv_more_order.setImageResource(R.mipmap.common_tab_selected_ft);
            return;
        }
        if (viewId == R.id.main_bank) {//银行
            tv_bank.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
            iv_bank.setImageResource(R.mipmap.common_tab_selected_yh);
            return;
        }
        if(viewId==R.id.main_money_give){
            tv_money.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
            iv_money.setImageResource(R.mipmap.money_select);
            return;
        }
        if (viewId == R.id.main_user) {//我的
            tv_user.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
            iv_user.setImageResource(R.mipmap.common_tab_selected_wd);
            return;
        }
    }

    private void clearTagbarState() {
        tv_home.setTextColor(ContextCompat.getColor(this, R.color.tv_333));
        iv_home.setImageResource(R.mipmap.common_tab_normal);
        tv_first_order.setTextColor(ContextCompat.getColor(this, R.color.tv_333));
        iv_first_order.setImageResource(R.mipmap.common_tab_normal_st);
        tv_more_order.setTextColor(ContextCompat.getColor(this, R.color.tv_333));
        iv_more_order.setImageResource(R.mipmap.common_tab_normal_ft);
        tv_money.setTextColor(ContextCompat.getColor(this, R.color.tv_333));
        iv_money.setImageResource(R.mipmap.money_unselect);
        tv_bank.setTextColor(ContextCompat.getColor(this, R.color.tv_333));
        iv_bank.setImageResource(R.mipmap.common_tab_normal_yh);
        tv_user.setTextColor(ContextCompat.getColor(this, R.color.tv_333));
        iv_user.setImageResource(R.mipmap.common_tab_normal_wd);
    }

    private void hideAllFragment() {
        if(fragments!=null){
            for (Fragment fragment : fragments) {
                fm.beginTransaction().hide(fragment).commit();
            }
        }else{

        }

    }

    private void showFragment(int position) {
        if(fragments.size()>0){
            fm.beginTransaction().show(fragments.get(position)).commit();
        }
        else{

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_home:
                //状态重置
                resetStateForTagbar(R.id.main_home);
                hideAllFragment();
                showFragment(0);

                break;
            case R.id.main_first_order:
                resetStateForTagbar(R.id.main_first_order);
                hideAllFragment();
                showFragment(1);
                break;
            case R.id.main_more_order:
                resetStateForTagbar(R.id.main_more_order);
                hideAllFragment();
                showFragment(2);
                break;
            case R.id.main_money_give:
                Log.e("ggg","ggg");
                if(UserInfoSharedPre.getIntance(this).getUserIsLogin()){
                    resetStateForTagbar(R.id.main_money_give);
                    hideAllFragment();
                    showFragment(3);
                }else{
                    startActivity(new Intent(this,LoginActivity.class));
                    //finish();
                }

                break;
            case R.id.main_user:
                resetStateForTagbar(R.id.main_user);
                hideAllFragment();
//                showFragment(4);
                showFragment(4);

                break;
            default:
                break;
        }

    }

    private void showDialog(int width, int height, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mDialog = builder.create();
        mDialog.setView(view);
        mDialog.show();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = mDialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
        mDialog.getWindow().setAttributes(p);     //设置生效


    }

    private void initLoginView(View view) {

        view.findViewById(R.id.pxLoginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 9);
//                startActivity(intent);
                if (mSoSPw != null) {
                    mSoSPw.dismiss();
                }
            }
        });
        view.findViewById(R.id.newUser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                if (mSoSPw != null) {
                    mSoSPw.dismiss();
                }
            }
        });
        view.findViewById(R.id.nowComeIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSoSPw != null) {
                    mSoSPw.dismiss();
                }
            }
        });

    }

    private void showPopupW(View view) {
        View shareview = LayoutInflater.from(this).inflate(R.layout.pw_login, null);
        initLoginView(shareview);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        mSoSPw = new PopupWindow(shareview, ((int) (d.getWidth() * 0.8)), ((int) (d.getHeight() * 0.5)));
        mSoSPw.setFocusable(true);
        mSoSPw.setBackgroundDrawable(new BitmapDrawable());
        mSoSPw.setOutsideTouchable(true);
        mSoSPw.setAnimationStyle(R.style.CustomPopWindowStyle);
        mSoSPw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundDrakValue(1.0f);
            }
        });
        //第一个参数可以取当前页面的任意一个View
        //第二个参数表示pw从哪一个方向显示出来
        //3、4表示pw的偏移量
        mSoSPw.showAtLocation(view, Gravity.CENTER, 0, 0);

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

    //隐藏一个后台的请求登录任务，预防用户修改了密码
    private void loginByAccount(String userName, final String password) {
        Map<String, Object> hasMap = new HashMap<>();
        hasMap.put("userName", userName);
        hasMap.put("password", password);
        HttpProxy.obtain().post(PlatformContans.User.sUserLogin, hasMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                //{"resultCode":1,"message":"登录失败，用户名或密码错误或账号已停用！"}
                Log.d("login", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    final int resultCode = object.optInt("resultCode");
                    final String message = object.optString("message");
                    final JSONObject data = object.optJSONObject("data");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resultCode == 0) {//和原来的账户密码一致
                                //保存用户信息
                                UserInfo userInfo = new Gson().fromJson(data.toString(), UserInfo.class);
                                userInfo.setPassword(password);
                                UserInfoSharedPre sharedPre = UserInfoSharedPre.getIntance(MainActivity.this);
                                sharedPre.saveUserInfo(userInfo, true);
                                sharedPre.setUserIsLogin(true);
                                Log.d("666sGetUserDetailsById", "run: 通过请求");
                                mHandler.sendEmptyMessage(REQUEST_IS_SIGN_CODE);//请求是否签到
                                mMineFragment.requeData();//只有登录验证通过，才请求数据
                            } else {//此时说明密码被修改过
                                UserInfoSharedPre sharedPre = UserInfoSharedPre.getIntance(MainActivity.this);
                                sharedPre.setUserIsLogin(false);
                                sharedPre.clearUserInfo();
                                mHandler.sendEmptyMessage(UNLOGIN_HINT_CODE);//未登录或者密码未匹配上
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
                        ToaskUtil.showToast(MainActivity.this, "请检查网络");
                    }
                });
            }
        });
    }

    @Override
    public void onShowSignView(boolean isOpen) {
        if (isOpen) {
//            requestSignData();
        }
    }


    public static class MyHandler extends Handler {
        private WeakReference<Activity> activity;

        public MyHandler(Activity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = (MainActivity) this.activity.get();
            if (activity == null) {
                return;
            }
            switch (msg.what) {
                case LOGIN_REQUEST_CODE:
                    if (MyApplication.isDebug) {
                        sendEmptyMessage(UNLOGIN_HINT_CODE);
                    } else {
                        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(activity);
                        String userName = intance.getUserName();
                        String password = intance.getPassword();
                        activity.loginByAccount(userName, password);
                    }
                    break;
                case UNLOGIN_HINT_CODE:
                    View loginView = LayoutInflater.from(activity).inflate(R.layout.pw_login, null);
                    activity.setBackgroundDrakValue(0.5f);
                    activity.showPopupW(loginView);
                    break;
                case REQUEST_IS_SIGN_CODE:
                    activity.requestSignData();
                    break;
            }

        }
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
        HttpProxy.obtain().get(PlatformContans.User.sIsSignByUserId, paramsMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("isSignToday", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    if (resultCode == 0) {
                        if (message.equals("用户未签到")) {
                            isCanSign = true;
                        }
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
                    ToaskUtil.showToast(MainActivity.this, "当天已经签到");
                }
            }
        });
        textActivityRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, IntegralRuleActivity.class));
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
                    ToaskUtil.showToast(MainActivity.this, message);
                    UserInfoSharedPre intance1 = UserInfoSharedPre.getIntance(MainActivity.this);
                    int continueSign = intance1.getContinueSign();
                    intance1.saveContinueSign(continueSign + 1);
                    if (resultCode == 0) {
                        isCanSign = false;
                        weekSignFrom.setEnabled(true);
                        weekSignFrom.addSign();
                    }
                } catch (JSONException e) {
                    weekSignFrom.setEnabled(true);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                weekSignFrom.setEnabled(true);
            }
        });
    }
}

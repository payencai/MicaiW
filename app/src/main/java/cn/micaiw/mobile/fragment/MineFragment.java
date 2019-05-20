package cn.micaiw.mobile.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.Guideline;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.DepositsHistoryActivity;
import cn.micaiw.mobile.activity.IntegralManagerActivity;
import cn.micaiw.mobile.activity.LoginActivity;
import cn.micaiw.mobile.activity.MCaiHeadlineActivity;
import cn.micaiw.mobile.activity.MainActivity;
import cn.micaiw.mobile.activity.MessageActivity;
import cn.micaiw.mobile.activity.MoneyDetailsActivity;
import cn.micaiw.mobile.activity.PayeeAccountActivity;
import cn.micaiw.mobile.activity.ProxyCentreActivity;
import cn.micaiw.mobile.activity.RegisterActivity;
import cn.micaiw.mobile.activity.UserInfoDetailsActivity;
import cn.micaiw.mobile.activity.WebViewActivity;
import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.custom.SmartScrollView;
import cn.micaiw.mobile.entity.MineOptionBean;
import cn.micaiw.mobile.entity.UserInfo;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ToaskUtil;
import de.hdodenhof.circleimageview.CircleImageView;


public class MineFragment extends BaseFragment implements SmartScrollView.ISmartScrollChangedListener {


    @BindView(R.id.mineOptionRv)
    RecyclerView mineOptionRv;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.titlebar_msg_btn)
    ImageView titlebarMsgBtn;
    @BindView(R.id.guideline_w_4_5)
    Guideline guidelineW45;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.returnMoneyDetails)
    LinearLayout returnMoneyDetails;
    private RVBaseAdapter<MineOptionBean> mMineOptionAdapter;
    private List<MineOptionBean> mOptionBeanList;
    private PopupWindow mMenuPw;

    @BindView(R.id.unLoginLayout)
    RelativeLayout unLoginLayout;
    @BindView(R.id.loginLayout)
    RelativeLayout loginLayout;
    //    @BindView(R.id.userHead)
//    ImageView userHead;
    @BindView(R.id.userHead2)
    ImageView userHead;
    @BindView(R.id.userHead)
    CircleImageView mUserHead;
    @BindView(R.id.userName)
    TextView userName;

    @BindView(R.id.totalnvestMoney)
    TextView totalnvestMoney;//总
    @BindView(R.id.predictEarnings)
    TextView predictEarnings;//预计
    @BindView(R.id.accReturnMoney)
    TextView accReturnMoney;//累计
    @BindView(R.id.waitReturnMoney)
    TextView waitReturnMoney;//等待

    @BindView(R.id.refreshLayout)
    LinearLayout refreshLayout;
    @BindView(R.id.smartScrollView)
    SmartScrollView smartScrollView;
    @BindView(R.id.rotateloading)
    RotateLoading rotateLoading;

    private UserInfo mUserInfo;//进入详情页必备
    private boolean isCanRequeData = false;
    private onShowSignFrom mOnShowSignFrom;
    //是否正在刷新
    private boolean isRefreshing = false;
    //记录刚按下的位置
    private boolean isFirstMove = true;
    private float mLastY;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    requeData();
                    refreshLayout.setVisibility(View.GONE);
                    rotateLoading.stop();
                    isRefreshing = false;
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            mOnShowSignFrom = (MainActivity) activity;
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        smartScrollView.setScanScrollChangedListener(this);
        EventBus.getDefault().register(this);
        mineOptionRv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (isFirstMove) {
                            isFirstMove = false;
                            mLastY = event.getY();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isFirstMove = true;
                        float rawY = event.getY();
                        float value = rawY - mLastY;
                        if (value > 30) {
                            onScrolledToTop();
                        }
                        break;
                }
                return false;
            }
        });
        //最初状态
        initLoginStateView();
        initOptionData();
        initMineOptionRv();
    }

    @Override
    protected void onResumeLazy() {
//        super.onResumeLazy();
//        if (isRefresh) {
//            isRefresh = false;
//        }
//        if (isCanRequeData) {
//        }
//        requeData();//请求数据
    }

    private void initLoginStateView() {
        totalnvestMoney.setText("" + 0);
        predictEarnings.setText("" + 0);
        accReturnMoney.setText("累计返现(元)" + 0);
        waitReturnMoney.setText("等待返现(元)" + 0);
        loginLayout.setVisibility(View.GONE);
        unLoginLayout.setVisibility(View.VISIBLE);
    }

    private void initOptionData() {
        mOptionBeanList = new ArrayList<>();
        mOptionBeanList.add(new MineOptionBean("投资记录", "正在持有产品记录", "#0451f9"));
        mOptionBeanList.add(new MineOptionBean("积分管理", "积分记录", "#ff51f9"));
        mOptionBeanList.add(new MineOptionBean("邀请中心", "邀请人数", "#dd51f9"));
        mOptionBeanList.add(new MineOptionBean("收款账号", "提现账号填写", "#ff0000"));
        mOptionBeanList.add(new MineOptionBean("平台介绍", "关于我们", "#ffff00"));
        mOptionBeanList.add(new MineOptionBean("米财头条", "最新资讯都在这里哦", "#00ffff"));
    }

    private void initMineOptionRv() {
        mMineOptionAdapter = new RVBaseAdapter<MineOptionBean>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }

            @Override
            protected void onClick(RVBaseViewHolder holder, final int position) {

                holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClick(position);
                    }
                });


            }
        };
        mMineOptionAdapter.setData(mOptionBeanList);
        mineOptionRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mineOptionRv.setAdapter(mMineOptionAdapter);
    }

    private void onItemClick(int position) {
        String token = UserInfoSharedPre.getIntance(getContext()).getToken();
        switch (position) {
            case 0:
                if (TextUtils.isEmpty(token)) {
                    ToaskUtil.showToast(getContext(), "未登录");
                    return;
                }
                startActivity(new Intent(getContext(), DepositsHistoryActivity.class));
                break;
            case 1:
                if (TextUtils.isEmpty(token)) {
                    ToaskUtil.showToast(getContext(), "未登录");
                    return;
                }
                Intent intent = new Intent(getContext(), IntegralManagerActivity.class);
                startActivityForResult(intent, 999);
//                startActivity(intent);
                break;
            case 2:
                if (TextUtils.isEmpty(token)) {
                    ToaskUtil.showToast(getContext(), "未登录");
                    return;
                }
                startActivity(new Intent(getContext(), ProxyCentreActivity.class));
                break;
            case 3:
                if (TextUtils.isEmpty(token)) {
                    ToaskUtil.showToast(getContext(), "未登录");
                    return;
                }
                startActivity(new Intent(getContext(), PayeeAccountActivity.class));
                break;
            case 4:
                //http://www.micaiqb.com:8080/detail/detail_MiRich.html
//                startActivity(new Intent(getContext(), PlatformIntroduceActivity.class));
                String versionName = getVersionName(getContext());
                WebViewActivity.starUi(getContext(), "http://www.micaiqb.com:8080/detail/detail_MiRich.html?versions=" + versionName, "平台介绍");
                break;
            case 5:
                startActivity(new Intent(getContext(), MCaiHeadlineActivity.class));
//                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
        }
    }

    //获取版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //获取版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    //通过PackageInfo得到的想要启动的应用的包名
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pInfo = null;

        try {
            //通过PackageManager可以得到PackageInfo
            PackageManager pManager = context.getPackageManager();
            pInfo = pManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pInfo;
    }


    @OnClick({R.id.returnMoneyDetails, R.id.titlebar_msg_btn, R.id.loginBtn, R.id.register, R.id.loginLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titlebar_msg_btn:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            case R.id.returnMoneyDetails:
                startActivity(new Intent(getContext(), MoneyDetailsActivity.class));
                break;
            case R.id.loginBtn:
                Intent intent1 = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent1, 0);
//                startActivity(intent1);
                break;
            case R.id.register:
                startActivity(new Intent(getContext(), RegisterActivity.class));
                break;
            case R.id.loginLayout:
                if (mUserInfo == null) {
                    return;
                }
                Intent intent = new Intent(getContext(), UserInfoDetailsActivity.class);
                intent.putExtra("userInfo", mUserInfo);
                startActivity(intent);
                break;
        }
    }

    public void requeData() {
        isCanRequeData = true;
        Map<String, Object> hashMap = new HashMap<>();
        Map<String, Object> tokenMap = new HashMap<>();
        UserInfoSharedPre sharedPre = UserInfoSharedPre.getIntance(getContext());
        int userId = sharedPre.getUserId();
        String token = sharedPre.getToken();
        if (TextUtils.isEmpty(token)) {
            return;
        }
        hashMap.put("id", userId);
        tokenMap.put("token", token);
        HttpProxy.obtain().get(PlatformContans.User.sGetUserDetailsById, hashMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                //{"resultCode":6666,"message":"登录信息已过期,请重新登录"}
                Log.d("666sGetUserDetailsById", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
//                    int resultCode = object.optInt("resultCode");
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == -1) {
                        return;
                    }
                    String message = object.optString("message");
                    if (resultCode == 6666) {
//                        UserInfoSharedPre.getIntance(getContext()).clearUserInfo();
//                        ToaskUtil.showToast(getContext(), message);
                        //标记用户登录状态
//                        UserInfoSharedPre.getIntance(getContext()).setUserIsLogin(false);
                        //做做一下接下来处理
                        totalnvestMoney.setText("" + 0);
                        predictEarnings.setText("" + 0);
                        accReturnMoney.setText("累计返现(元)" + 0);
                        waitReturnMoney.setText("等待返现(元)" + 0);
                        loginLayout.setVisibility(View.GONE);
                        unLoginLayout.setVisibility(View.VISIBLE);
                    } else if (resultCode == 0) {
                        //accumulativeFor累计返现
                        //waitFor等待防线
                        //totalInvestment总投资
                        //earnings预计收入
                        JSONObject data = object.optJSONObject("data");
                        double totalInvestment = data.optDouble("totalInvestment");
                        double earnings = data.optDouble("earnings");
                        double accumulativeFor = data.optDouble("accumulativeFor");
                        double waitFor = data.optDouble("waitFor");

                        JSONObject user = data.optJSONObject("user");
                        String userHeadUrl = user.optString("avatar");
                        String name = user.optString("name");
                        mUserInfo = new Gson().fromJson(user.toString(), UserInfo.class);
                        loginLayout.setVisibility(View.VISIBLE);
                        unLoginLayout.setVisibility(View.GONE);
//                        loginLayout.setGravity(View.GONE);
//                        unLoginLayout.setGravity(View.VISIBLE);
                        totalnvestMoney.setText("" + totalInvestment);
                        predictEarnings.setText("" + earnings);
                        accReturnMoney.setText("累计返现(元)+" + accumulativeFor);
                        waitReturnMoney.setText("等待返现(元)+" + waitFor);
                        String avatar = mUserInfo.getAvatar();
                        Log.d("666sGetUserDetailsById", "OnSuccess: " + avatar);
//                        Glide.with(getContext()).load(avatar)
//                                .placeholder(R.mipmap.logo)
//                                .error(R.mipmap.logo)
//                                .into(userHead);
                        Picasso.with(getContext())
                                .load(avatar)
                                .error(R.mipmap.logo)
                                .placeholder(R.mipmap.logo)
                                .into(mUserHead);
                        userName.setText(name);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("sGetUserDetailsById", "OnSuccess: " + result);
            }

            @Override
            public void onFailure(String error) {
                totalnvestMoney.setText("" + 0);
                predictEarnings.setText("" + 0);
                accReturnMoney.setText("累计返现(元)" + 0);
                waitReturnMoney.setText("等待返现(元)" + 0);
                loginLayout.setVisibility(View.GONE);
                unLoginLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * @param sNull 使用Eventbus，
     *              1、从登录界面获取数据
     *              2、修改用户信息重新获取数据
     *              3、添加投资记录获取数据
     */
    @Subscribe
    public void reLogin(String sNull) {
        requeData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 999) {
                boolean isFromSign = data.getBooleanExtra("isFromSign", false);
                if (isFromSign) {
                    mOnShowSignFrom.onShowSignView(true);
                }
            }
        }
    }


    public void setOnShowSignFrom(onShowSignFrom onShowSignFrom) {
        mOnShowSignFrom = onShowSignFrom;
    }

    @Override
    public void onScrolledToBottom() {

    }

    @Override
    public void onScrolledToTop() {
        Log.d("onScrolledToTop", "onScrolledToTop: 到顶部了");
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        refreshLayout.setVisibility(View.VISIBLE);
        rotateLoading.start();
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    public interface onShowSignFrom {
        void onShowSignView(boolean isOpen);
    }
}


package cn.micaiw.mobile.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.victor.loading.rotate.RotateLoading;

import org.greenrobot.eventbus.EventBus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

import butterknife.BindView;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.MCaiHeadlineActivity;
import cn.micaiw.mobile.activity.MainActivity;
import cn.micaiw.mobile.activity.MessageActivity;
import cn.micaiw.mobile.activity.WebUrlActivity;
import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.custom.SmartScrollView;
import cn.micaiw.mobile.entity.Company;
import cn.micaiw.mobile.entity.NewsBean;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.myEventBus.Eventbus;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;
import cn.micaiw.mobile.view.FullyLinearLayoutManager;
import customview.ConfirmDialog;
import util.UpdateAppUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, SmartScrollView.ISmartScrollChangedListener {

    @BindView(R.id.carouselVP)
    ViewPager carouselVP;

    @BindView(R.id.dot_layout)
    LinearLayout dotLayout;

    @BindView(R.id.recommendP2PRv)
    RecyclerView recommendP2PRv;
    @BindView(R.id.recommendFundRv)
    RecyclerView recommendFundRv;

    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.news_layout)
    RelativeLayout news_layout;

    @BindView(R.id.accumulative)
    TextView mAccumulative;
    @BindView(R.id.investmentUser)
    TextView mInvestmentUser;

    @BindView(R.id.refreshLayout)
    LinearLayout refreshLayout;
    @BindView(R.id.smartScrollView)
    SmartScrollView smartScrollView;
    @BindView(R.id.rotateloading)
    RotateLoading rotateLoading;
    @BindView(R.id.vf)
    ViewFlipper mViewFlipper;

    TextView signNumber;
    TextView resetSign;
    TextView activityRules;
    LinearLayout layoutMonday;
    LinearLayout layoutTuesday;
    LinearLayout layoutWednesday;
    LinearLayout layoutThursday;
    LinearLayout layoutFriday;
    LinearLayout layoutSaturday;
    LinearLayout layoutSunday;


    private AlertDialog mSignFormDialog;

    private List<String> newsList=new ArrayList<>();
    private boolean isRunning = true;
    //记录当前页面的前一个页面的position
    private int prePosition = 0;

    //是否正在刷新
    private boolean isRefreshing = false;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //获取app统计数据信息
                    getAppDataStatistical();
                    //获取p2p首页推荐数据
                    getP2pPlatformByType();
                    //获取基金首页推荐数据
                    getFundPlatformByType();
                    //获取两条最新新闻标题
                    loadData();
                    refreshLayout.setVisibility(View.GONE);
                    rotateLoading.stop();
                    isRefreshing = false;
                    break;
            }
        }
    };

    private int[] imgs = new int[]{R.mipmap.vp_banner, R.mipmap.common_bg_disabled, R.mipmap.vp_banner, R.mipmap.common_bg_disabled, R.mipmap.vp_banner, R.mipmap.common_bg_disabled};
    private List<String> pagerImgUrls = new ArrayList<>();
    private RVBaseAdapter<Company> mRecommendP2PAdapter;
    private RVBaseAdapter<Company> mRecommendFundAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initRecommendP2PRv();
        initRecommendFundRv();
        //获取后台接口，查看当天是否已经签到
        View signView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sign_form, null);
        if (false) {
            showSignForm(228, 329, signView);
        }
        initMyView(signView);
        //获取轮播图片
        getCarouselUrl();

    }

    private void initMyView(View view) {
        smartScrollView.setScanScrollChangedListener(this);
        rotateLoading.setLoadingColor(Color.BLACK);


        signNumber = (TextView) view.findViewById(R.id.signNumber);
        resetSign = (TextView) view.findViewById(R.id.resetSign);
        activityRules = (TextView) view.findViewById(R.id.activityRules);

        layoutMonday = (LinearLayout) view.findViewById(R.id.layoutMonday);
        layoutTuesday = (LinearLayout) view.findViewById(R.id.layoutTuesday);
        layoutWednesday = (LinearLayout) view.findViewById(R.id.layoutWednesday);
        layoutThursday = (LinearLayout) view.findViewById(R.id.layoutThursday);
        layoutFriday = (LinearLayout) view.findViewById(R.id.layoutFriday);
        layoutSaturday = (LinearLayout) view.findViewById(R.id.layoutSaturday);
        layoutSunday = (LinearLayout) view.findViewById(R.id.layoutSunday);

        news_layout.setOnClickListener(this);
        resetSign.setOnClickListener(this);
        activityRules.setOnClickListener(this);
        layoutMonday.setOnClickListener(this);
        layoutTuesday.setOnClickListener(this);
        layoutWednesday.setOnClickListener(this);
        layoutThursday.setOnClickListener(this);
        layoutFriday.setOnClickListener(this);
        layoutSaturday.setOnClickListener(this);
        layoutSunday.setOnClickListener(this);
        messageImg.setOnClickListener(this);
        img1.setOnClickListener(this);
    }

    private void initViewPagerData() {
        for (int i = 0; i < pagerImgUrls.size(); i++) {
            View view = new View(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
            lp.leftMargin = 10;
            //设置view的宽高左边距等参数
            view.setLayoutParams(lp);
            //默认情况下所有设置View的所有属性为false
            view.setEnabled(false);
            //给view设置背景选择器，enable属性为true时背景为红色，否则为白色
            view.setBackgroundResource(R.drawable.dot_bg);
            //将View添加到容器中
            dotLayout.addView(view);
        }

        //设置第一个View的enable为true，则该View  背景为红色
        View view=dotLayout.getChildAt(0);
        if(view!=null)
            view.setEnabled(true);
        CarouselImageAdapter carouselAdapter = new CarouselImageAdapter(getContext(), pagerImgUrls);
        carouselAdapter.setOnImgClickListener(new CarouselImageAdapter.OnImgClickListener() {
            @Override
            public void start(int position) {
                String url=weburllist.get(position);
                if(!TextUtils.isEmpty(url)){
                  //  Log.e("pos",url);
                    Intent intent=new Intent(getActivity(),WebUrlActivity.class);
                    intent.putExtra("url",url);
                    startActivity(intent);
                }

            }
        });
        carouselVP.setAdapter(carouselAdapter);
        carouselVP.setOffscreenPageLimit(pagerImgUrls.size());
        carouselVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面滑动过程中回调该方法
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面滑动结束时回调该方法
            @Override
            public void onPageSelected(int position) {
//                position = position % pagerImgUrls.size();
                dotLayout.getChildAt(prePosition).setEnabled(false);
                dotLayout.getChildAt(position).setEnabled(true);
                prePosition = position;
            }

            //页面状态发生改变时回调
            //state三种取值：
            //0 表示页面静止
            //1 表示手指在ViewPager上拖动
            //2 表示手指离开ViewPager，页面自由滑动
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    //睡眠3秒
                    SystemClock.sleep(3000);
                    FragmentActivity activity = getActivity();
                    if (activity == null) {
                        isRunning = false;
                        return;

                    }
                    //子线程更新UI
                    getActivity().runOnUiThread(new Runnable() {
                        //该方法运行在主线程
                        @Override
                        public void run() {
                            if (carouselVP != null) {
                                int item = carouselVP.getCurrentItem() + 1;//出现过空指针
                                if (item >= pagerImgUrls.size()) {
                                    item = 0;
                                }
                                carouselVP.setCurrentItem(item);
                            }
                        }
                    });
                }
            }
        }).start();

    }

    private void initRecommendP2PRv() {

        mRecommendP2PAdapter = new RVBaseAdapter<Company>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }

        };
        List<Company> companyList = new ArrayList<>();
        mRecommendP2PAdapter.setData(companyList);
        recommendP2PRv.setLayoutManager(new FullyLinearLayoutManager(getContext()));
        recommendP2PRv.setNestedScrollingEnabled(false);
        //recommendP2PRv.setHasFixedSize(true);
        recommendP2PRv.setAdapter(mRecommendP2PAdapter);
    }

    private void initRecommendFundRv() {
        mRecommendFundAdapter = new RVBaseAdapter<Company>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }
        };
        recommendP2PRv.setNestedScrollingEnabled(false);
        recommendFundRv.setLayoutManager(new FullyLinearLayoutManager(getContext()));
      //  recommendFundRv.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendFundRv.setAdapter(mRecommendFundAdapter);
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        Log.d("onResumeLazy", "onResumeLazy: 加载数据");
        //获取app统计数据信息
        getAppDataStatistical();
        //获取p2p首页推荐数据
        getP2pPlatformByType();
        //获取基金首页推荐数据
        getFundPlatformByType();
        //获取两条最新新闻标题
        loadData();

    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();

    }

    private void showSignForm(int width, int height, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.show();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5);   //高度设置为屏幕的0.3
        p.width = (int) (d.getWidth() * 0.8);    //宽度设置为屏幕的0.5
        dialog.getWindow().setAttributes(p);     //设置生效

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private void setIsSelectState(View view, boolean b) {
        LinearLayout layout = (LinearLayout) view;
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            child.setSelected(b);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.messageImg:
                startActivity(new Intent(getContext(), MessageActivity.class));
                break;
            case R.id.resetSign:
                break;

            case R.id.activityRules:

                break;
            case R.id.layoutMonday:
                setIsSelectState(view, true);
                break;
            case R.id.layoutTuesday:
                setIsSelectState(view, true);
                break;
            case R.id.layoutWednesday:
                setIsSelectState(view, true);
                break;
            case R.id.layoutThursday:
                setIsSelectState(view, true);
                break;
            case R.id.layoutFriday:
                setIsSelectState(view, true);
                break;
            case R.id.layoutSaturday:
                setIsSelectState(view, true);
                break;
            case R.id.layoutSunday:
                setIsSelectState(view, true);
                break;
            case R.id.news_layout:
                startActivity(new Intent(getContext(), MCaiHeadlineActivity.class));
                break;
            case R.id.img1:
//                List<String> labelList = mRecommendP2PAdapter.getData().get(0).getLabelList();
//                for (String s : labelList) {
//                    Log.d("labelList", "onClick: " + s + "---");
//                }
//                Log.d("labelList", "onClick:  -------------------------------");
//                List<String> labelList1 = mRecommendP2PAdapter.getData().get(1).getLabelList();
//                for (String s : labelList1) {
//                    Log.d("labelList", "onClick: " + s + "---");
//                }
                break;
        }
    }

    List<String> weburllist=new ArrayList<>();
    public void getCarouselUrl() {
        Map<String, Object> hasMap = new HashMap<>();
        hasMap.put("page", 1);
        hasMap.put("state", 1);
        hasMap.put("type", 2);
        HttpProxy.obtain().post(PlatformContans.User.sGetPropagandaMap, hasMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("imgUrl", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.optInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray imgUrls = object.optJSONObject("data").optJSONArray("beanList");
                        for (int i = 0; i < imgUrls.length(); i++) {
                            JSONObject item = imgUrls.optJSONObject(i);
                            String imgUrl = item.optString("picture");
                            String weburl=item.getString("propagandaUrl");
                            pagerImgUrls.add(imgUrl);
                            weburllist.add(weburl);
                        }
                    }
                    initViewPagerData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                ToaskUtil.showToast(getContext(), "请检查网络");
            }
        });
    }

    @Override
    public void onScrolledToBottom() {

    }

    @Override
    public void onScrolledToTop() {
        if (isRefreshing) {
            return;
        }
        isRefreshing = true;
        refreshLayout.setVisibility(View.VISIBLE);
        rotateLoading.start();
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    public static class CarouselImageAdapter extends PagerAdapter {
        private Context context;
        private List<String> urlLists;
        private LayoutInflater inflate;
        private OnImgClickListener mOnImgClickListener;
        public interface OnImgClickListener{
            void start(int position);
        }
        public  void setOnImgClickListener(OnImgClickListener mOnImgClickListener){
            this.mOnImgClickListener=mOnImgClickListener;
        }
        public CarouselImageAdapter(Context context, List<String> urlLists) {
            this.context = context;
            this.urlLists = urlLists;
            inflate = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
//            return Integer.MAX_VALUE;
            return urlLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

//            ImageView iv = new ImageView(context);
//            String url = urlLists.get(position % urlLists.size());
//            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            Glide.with(context)
//                    .load(url)
//                    .placeholder(R.mipmap.vp_banner)
//                    .error(R.mipmap.vp_banner)
//                    .into(iv);
//            container.addView(iv);
//            return iv;
            View view = inflate.inflate(R.layout.vp_banner_list, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("aaaa",position+"");
                    mOnImgClickListener.start(position);
                }
            });
            Glide.with(context)
                    .load(urlLists.get(position))
                    .placeholder(R.mipmap.vp_banner)
                    .error(R.mipmap.vp_banner)
                    .into(iv);
            container.addView(view);
            return view;
        }
    }

    //获取app统计数据信息
    private void getAppDataStatistical() {
        HttpProxy.obtain().get(PlatformContans.User.sGetAppDataStatistical, null, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("getAppDataStatistical", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.optInt("resultCode");
                    if (resultCode == 0) {
                        JSONObject data = object.optJSONObject("data");
                        int investmentUser = data.optInt("investmentUser");//人数
                        int accumulative = data.optInt("accumulative");//返现
                        float value = ((float) accumulative) / 10000;
                        DecimalFormat decimalFormat = new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                        String p = decimalFormat.format(value);//format 返回的是字符串
                        mAccumulative.setText(p + "万");
                        mInvestmentUser.setText(investmentUser + "人");
                    } else {
                        ToaskUtil.showToast(getContext(), "统计数据请求失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d("getAppDataStatistical", "onFailure: " + error);
            }
        });
    }

    private void getFundPlatformByType() {//获取基金首页推荐数据
        HttpProxy.obtain().post(PlatformContans.User.sGetFundPlatformByType, null, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("getFundPlatformByType", "OnSuccess: " + result);
//                try {
//                    JSONObject object = new JSONObject(result);
//                    int resultCode = object.optInt("resultCode");
//                    if (resultCode == 0) {
//                        JSONArray data = object.getJSONArray("data");
//                        Gson gson = new Gson();
//                        List<FundBean> list = new ArrayList<>();
//                        for (int i = 0; i < data.length(); i++) {
//                            JSONObject item = data.getJSONObject(i);
//                            FundBean bean = gson.fromJson(item.toString(), FundBean.class);
//                            list.add(bean);
//                        }
//                        mRecommendFundAdapter.resetData(list);
//                    } else {
//                        ToaskUtil.showToast(getContext(), "数据失败");
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.optInt("resultCode");
                    Gson gson = new Gson();
                    if (resultCode == 0) {
                        JSONArray data = object.optJSONArray("data");
                        List<Company> companyList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.optJSONObject(i);
                            Company company = gson.fromJson(item.toString(), Company.class);
                            company.setCompanyType(2);
                            String platformLabel = company.getPlatformLabel();
                            if (!TextUtils.isEmpty(platformLabel)) {
                                String[] split = platformLabel.split(",");
                                for (String s : split) {
                                    Log.d("123456789", "OnSuccess: " + s);
                                    company.addLabelList(s);
                                }
                            }
                            companyList.add(company);
                        }
                        mRecommendFundAdapter.resetData(companyList);
                    } else {
                        ToaskUtil.showToast(getContext(), "数据失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                ToaskUtil.showToast(getContext(), "数据请求超时，请检查网络");
            }
        });
    }

    private void getP2pPlatformByType() {//获取p2p首页推荐数据
        HttpProxy.obtain().post(PlatformContans.User.sGetPtpPlatformByType, null, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("getP2pPlatformByType", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.optInt("resultCode");
                    Gson gson = new Gson();
                    if (resultCode == 0) {
                        JSONArray data = object.optJSONArray("data");
                        List<Company> companyList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.optJSONObject(i);
                            Company company = gson.fromJson(item.toString(), Company.class);
                            company.setCompanyType(1);
                            String platformLabel = company.getPlatformLabel();
                            if (!TextUtils.isEmpty(platformLabel)) {
                                String[] split = platformLabel.split(",");
                                for (String s : split) {
                                    Log.e("label",  s);
                                    company.addLabelList(s);
                                }
                            }
                            companyList.add(company);
                        }
                        mRecommendP2PAdapter.resetData(companyList);
                    } else {
                        ToaskUtil.showToast(getContext(), "数据失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                ToaskUtil.showToast(getContext(), "数据请求超时，请检查网络");
            }
        });
    }

    private void loadData() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", 1);
        paramsMap.put("categoryId", 1);
        HttpProxy.obtain().get(PlatformContans.News.sGetNewsOnline, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("categoryId", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == -1) {
                        return;
                    }
                    if (resultCode == 0) {
                        JSONArray beanList = object.optJSONObject("data").optJSONArray("beanList");
                        Gson gson = new Gson();
                        List<NewsBean> newsBeans = new ArrayList<>();
                        for (int i = 0; i < beanList.length(); i++) {
                            JSONObject item = beanList.optJSONObject(i);
                            NewsBean newsBean = gson.fromJson(item.toString(), NewsBean.class);
                            newsList.add(i,newsBean.getTitle());
                            newsBeans.add(newsBean);
                        }

                        for (int i = 0; i < newsList.size(); i++) {
                            Log.e("news",newsList.get(i));
                            View ll_content = View.inflate(getContext(), R.layout.item_flipper, null);
                            TextView tv_news1 = (TextView) ll_content.findViewById(R.id.n1);
                            TextView tv_news2 = (TextView) ll_content.findViewById(R.id.n2);
                            tv_news1.setText(newsList.get(i));
                            int j=i+1;
                            if(j<newsList.size())
                            tv_news2.setText(newsList.get(j));
                            if (i==0){
                                tv_news1.setTextColor(getResources().getColor(R.color.orange_1a));
                            }
                            mViewFlipper.addView(ll_content);
                            mViewFlipper.startFlipping();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
            }
        });
    }

}

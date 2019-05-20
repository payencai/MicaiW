package cn.micaiw.mobile.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.cell.ImageCell;
import cn.micaiw.mobile.cell.TextCell;
import cn.micaiw.mobile.common.commonRv.base.Cell;
import cn.micaiw.mobile.common.commonRv.fragment.AbsBaseFragment;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.Company;
import cn.micaiw.mobile.entity.model.DataMocker;
import cn.micaiw.mobile.entity.model.Entry;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.ToaskUtil;

/**
 * 作者：凌涛 on 2018/5/17 17:26
 * 邮箱：771548229@qq..com
 */
public class P2PItemFundFragment extends AbsBaseFragment<Company> {

    private int mPage = 1;//请求的页数，默认为1
    private int mIsFirst = 1;//是否是首投，默认为1，（1.首投，2.复投）
    private String mPlatformSearch = "米财推荐";//过滤的条件,默认为大额区，只有"大额区", "小额区", "高评分", "高返利", "存管系", "风投系", "上市系", "民营系"
    private boolean isRefresh = false;

    public static P2PItemFundFragment getInstance(String platformSearch, int isFrist) {
        P2PItemFundFragment baseFragment = new P2PItemFundFragment();
        Bundle args = new Bundle();
        args.putInt("isFirst", isFrist);
        args.putString("platformSearch", platformSearch);
        baseFragment.setArguments(args);
        return baseFragment;
    }

    @Override
    public void onRecyclerViewInitialized() {
        //初始化View和数据加载
        //设置刷新进度条颜色
//        setColorSchemeResources(R.color.colorAccent);
        loadData();
    }

    @Override
    public void onPullRefresh() {
        //下拉刷新回调
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(false);
                isRefresh = true;
                mPage = 1;
                requeData(mPage, mIsFirst, mPlatformSearch);
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        //上拉加载回调
//        loadMore();
        mPage++;
        requeData(mPage, mIsFirst, mPlatformSearch);
    }

    public String getString(String string) {
        return string;
    }

    @Override
    protected List<Cell> getCells(List<Company> list) {
        return null;
    }


    /**
     * 模拟从服务器取数据
     */
    private void loadData() {
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.manu_loading_layout, null);
        mBaseAdapter.showLoading(loadingView);
        Bundle bundle = getArguments();
        mIsFirst = bundle.getInt("isFirst");
        mPlatformSearch = bundle.getString("platformSearch");
        requeData(1, mIsFirst, mPlatformSearch);
    }

    private void requeData(int page, int isFirst, String platformSearch) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("state", 1);
        paramsMap.put("isFirst", isFirst);//（1.首投，2.复投）
        paramsMap.put("platformSearch", platformSearch);//过滤的条件
        HttpProxy.obtain().post(PlatformContans.User.sFindFundPlatformCondition, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                mBaseAdapter.hideLoading();
                hideLoadMore();
                Log.d("FundPlatformCondition", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.optInt("resultCode");
                    Gson gson = new Gson();
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONObject("data").getJSONArray("beanList");
                        List<Company> companyList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
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
                        if (isRefresh) {
                            isRefresh = false;
                            mBaseAdapter.resetData(companyList);
                        } else {
                            mBaseAdapter.addAll(companyList);
                        }
                    } else {
                        ToaskUtil.showToast(getContext(), "数据失败");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                mBaseAdapter.hideLoading();
                isRefresh = false;
            }
        });
    }

}

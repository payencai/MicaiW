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
import cn.micaiw.mobile.common.commonRv.base.Cell;
import cn.micaiw.mobile.common.commonRv.fragment.AbsBaseFragment;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.Company;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

/**
 * 作者：凌涛 on 2018/5/22 18:09
 * 邮箱：771548229@qq..com
 */
public class P2PFinishFragment extends AbsBaseFragment<Company> {

    private int mPage = 1;
    private int isFirst = 1;//1为首投，2 为副投
    private boolean isRefresh = false;

    public static P2PFinishFragment getInstance(int isFrist) {
        P2PFinishFragment baseFragment = new P2PFinishFragment();
        Bundle args = new Bundle();
        args.putInt("isFirst", isFrist);
        baseFragment.setArguments(args);
        return baseFragment;
    }

    @Override
    public void onRecyclerViewInitialized() {
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.manu_loading_layout, null);
        mBaseAdapter.showLoading(loadingView);
        Bundle bundle = getArguments();
        isFirst = bundle.getInt("isFirst");
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
                loadData();
            }
        }, 1500);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        mBaseAdapter.showLoadMore();
        loadData();
    }

    @Override
    protected List<Cell> getCells(List<Company> list) {
        return null;
    }

    private void loadData() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("page", mPage);
        paramsMap.put("isFirst", isFirst);
        HttpProxy.obtain().post(PlatformContans.User.sFindPlatformByCondition, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                mBaseAdapter.hideLoading();
                mBaseAdapter.hideLoadMore();
                MLog.log("P2PFinishFragment", result);
                if (isFirst == 2) {
                    Log.d("isFinishP2p", "OnSuccess: " + result);
                }
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        Gson gson = new Gson();
                        List<Company> list = new ArrayList<>();
                        JSONObject data = object.getJSONObject("data");
                        JSONArray ptpPlatform = data.getJSONArray("ptpPlatform");
                        JSONArray fundPlatformList = data.getJSONArray("fundPlatformList");
                        for (int i = 0; i < ptpPlatform.length(); i++) {
                            JSONObject item = ptpPlatform.getJSONObject(i);
                            Company company = gson.fromJson(item.toString(), Company.class);
                            company.isFinish = true;
                            String platformLabel = company.getPlatformLabel();
                            if (!TextUtils.isEmpty(platformLabel)) {
                                String[] split = platformLabel.split(",");
                                for (String s : split) {
                                    company.addLabelList(s);
                                }
                            }
                            list.add(company);
                        }
                        for (int i = 0; i < fundPlatformList.length(); i++) {
                            JSONObject item = fundPlatformList.getJSONObject(i);
                            Company company = gson.fromJson(item.toString(), Company.class);
                            company.isFinish = true;
                            String platformLabel = company.getPlatformLabel();
                            if (!TextUtils.isEmpty(platformLabel)) {
                                String[] split = platformLabel.split(",");
                                for (String s : split) {
                                    company.addLabelList(s);
                                }
                            }
                            Log.d("caonima", "OnSuccess: " + company.toString());
                            list.add(company);
                        }
                        if (isRefresh) {
                            isRefresh = false;
                            mBaseAdapter.resetData(list);
                        } else {
                            mBaseAdapter.addAll(list);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                mBaseAdapter.hideLoading();
                mBaseAdapter.hideLoadMore();
                ToaskUtil.showToast(getContext(), "请检查网络");
            }
        });
    }
}

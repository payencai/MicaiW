package cn.micaiw.mobile.fragment;

import android.os.Bundle;
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
import cn.micaiw.mobile.entity.NewsBean;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;

/**
 * 作者：凌涛 on 2018/5/18 17:36
 * 邮箱：771548229@qq..com
 */
public class NewsFragment extends AbsBaseFragment<NewsBean> {

    private int mPage = 1;
    private boolean isRefresh = false;
    private int mCategoryId = 1;

    public static NewsFragment getInstance(int pageNum, int categoryId) {
        NewsFragment baseFragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putInt("pageNum", pageNum);
        args.putInt("categoryId", categoryId);
        baseFragment.setArguments(args);
        return baseFragment;
    }

    @Override
    public void onRecyclerViewInitialized() {
        Bundle bundle = getArguments();
        mPage = bundle.getInt("pageNum");
        mCategoryId = bundle.getInt("categoryId");
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.manu_loading_layout, null);
        mBaseAdapter.showLoading(loadingView);
        loadData();
    }

    @Override
    public void onPullRefresh() {
        mPage = 1;
        isRefresh = true;
        //下拉刷新回调
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(false);
            }
        }, 2000);
        loadData();
    }

    @Override
    public void onLoadMore() {
        mPage++;
        loadData();
    }

    @Override
    protected List<Cell> getCells(List<NewsBean> list) {
        return null;
    }

    private void loadData() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", mPage);
        paramsMap.put("categoryId", mCategoryId);
        HttpProxy.obtain().get(PlatformContans.News.sGetNewsOnline, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                mBaseAdapter.hideLoading();
                mBaseAdapter.hideLoadMore();
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
                            newsBeans.add(newsBean);
                        }
                        if (isRefresh) {
                            isRefresh = false;
                            mBaseAdapter.clear();
                            mBaseAdapter.addAll(newsBeans);

                        } else {
                            mBaseAdapter.addAll(newsBeans);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                isRefresh = false;
                mBaseAdapter.hideLoading();
            }
        });
    }
}

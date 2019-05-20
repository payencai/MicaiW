package cn.micaiw.mobile.fragment;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import cn.micaiw.mobile.entity.IntegralPrize;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.MLog;


/**
 * Created by zhouwei on 17/2/4.
 */

public class DetailFragment extends AbsBaseFragment<IntegralPrize> {

    private int mPage = 1;
    private boolean isRefresh = false;

    @Override
    public void onRecyclerViewInitialized() {
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.manu_loading_layout, null);
        mBaseAdapter.showLoading(loadingView);
        requestConversionGoods();
//        List<IntegralPrize> list = loadData();
//        mBaseAdapter.addAll(list);
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
        requestConversionGoods();

    }

    @Override
    public void onLoadMore() {
        mPage++;
        requestConversionGoods();
    }

    @Override
    protected RecyclerView.LayoutManager initLayoutManger() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        return layoutManager;
    }

    @Override
    protected List<Cell> getCells(List<IntegralPrize> list) {
        return null;
    }

    @Override
    protected View customLoadMoreView() {
        View loadMoreView = LayoutInflater.from(getContext()).inflate(R.layout.custeom_load_more_layout, null);
        return loadMoreView;
    }

    private void requestConversionGoods() {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pageNum", mPage);
        HttpProxy.obtain().get(PlatformContans.Commodity.sGetCommoditiesForApp, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                mBaseAdapter.hideLoading();
                MLog.log("commoditiesForApp", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray beanList = data.getJSONArray("beanList");
                        List<IntegralPrize> list = new ArrayList<>();
                        Gson gson = new Gson();
                        for (int i = 0; i < beanList.length(); i++) {
                            JSONObject item = beanList.getJSONObject(i);
                            IntegralPrize prize = gson.fromJson(item.toString(), IntegralPrize.class);
                            list.add(prize);
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
                isRefresh = false;
            }
        });
    }

}

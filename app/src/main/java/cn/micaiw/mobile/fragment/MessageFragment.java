package cn.micaiw.mobile.fragment;

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
import cn.micaiw.mobile.entity.MessageBean;
import cn.micaiw.mobile.entity.model.DataMocker;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

/**
 * 作者：凌涛 on 2018/5/19 09:56
 * 邮箱：771548229@qq..com
 */
public class MessageFragment extends AbsBaseFragment<MessageBean> {

    private int mPage = 1;
    private boolean isRefresh = false;


    @Override
    public void onRecyclerViewInitialized() {
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
    protected List<Cell> getCells(List<MessageBean> list) {
        return null;
    }

    private void loadData() {
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.manu_loading_layout, null);
        mBaseAdapter.showLoading(loadingView);
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> token = new HashMap<>();
        paramsMap.put("pageNum", mPage);
        String tokenValue = UserInfoSharedPre.getIntance(getContext()).getToken();
        if (TextUtils.isEmpty(tokenValue)) {
            mBaseAdapter.hideLoading();
            isRefresh = false;
            ToaskUtil.showToast(getContext(), "未登录");
            return;
        }
        token.put("token", tokenValue);
        HttpProxy.obtain().get(PlatformContans.Message.sGetMessageOnline, paramsMap, token, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("getMessageOnline", result);
                mBaseAdapter.hideLoading();
//                hideLoadMore();
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.optInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray beanList = object.getJSONObject("data").getJSONArray("beanList");
                        List<MessageBean> list = new ArrayList<>();
                        Gson gson = new Gson();
                        for (int i = 0; i < beanList.length(); i++) {
                            JSONObject item = beanList.getJSONObject(i);
                            MessageBean bean = gson.fromJson(item.toString(), MessageBean.class);
                            list.add(bean);
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
                    //这里处理token为空或者其他状态
                }

            }

            @Override
            public void onFailure(String error) {
                isRefresh = false;
                mBaseAdapter.hideLoading();
                hideLoadMore();
            }
        });
    }
}

package cn.micaiw.mobile.fragment;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.micaiw.mobile.common.commonRv.base.Cell;
import cn.micaiw.mobile.common.commonRv.fragment.AbsBaseFragment;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.DepositsHistoryBean;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ToaskUtil;

/**
 * 作者：凌涛 on 2018/5/19 11:11
 * 邮箱：771548229@qq..com
 */
public class DepositsHistoryFragment extends AbsBaseFragment<DepositsHistoryBean> {

    private int mPage = 1;

    @Override
    public void onRecyclerViewInitialized() {
        //加载请求头信息后在加载列表
//        mSwipeRefreshLayout.setEnabled(false);
        loadHeadData();
    }

    @Override
    public void onPullRefresh() {
//        mRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                loadHeadData();
//            }
//        }, 2000);

        setRefreshing(false);

    }

    @Override
    public void onLoadMore() {
        mPage++;
        loadP2PData();
    }

    private void loadHeadData() {
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> token = new HashMap<>();
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(getContext());
        String tokenValue = intance.getToken();
        String userName = intance.getUserName();
        if (TextUtils.isEmpty(tokenValue) || TextUtils.isEmpty(userName)) {
            return;
        }
        paramsMap.put("userName", userName);
        token.put("token", tokenValue);

        HttpProxy.obtain().get(PlatformContans.User.sGetPtpRecordDetailsByUserName, paramsMap, token, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                //{"resultCode":0,"message":null,"data":{"totalInvestment":111000,"accumulativeFor":200}}
                Log.d("DetailsByUserName", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONObject data = object.getJSONObject("data");
                        DepositsHistoryBean bean = new DepositsHistoryBean();
                        bean.setType(0);
                        DepositsHistoryBean.Head head = new DepositsHistoryBean.Head();
                        head.totalInvestment = data.getDouble("totalInvestment");
                        head.accumulativeFor = data.getDouble("accumulativeFor");
                        bean.setHead(head);
                        mBaseAdapter.add(bean);
                        loadP2PData();
                    }

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

    private void loadP2PData() {
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> token = new HashMap<>();
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(getContext());
        String tokenValue = intance.getToken();
        String userName = intance.getUserName();
        if (TextUtils.isEmpty(tokenValue) || TextUtils.isEmpty(userName)) {
            return;
        }
        paramsMap.put("userName", userName);
        paramsMap.put("page", mPage);
        token.put("token", tokenValue);
        HttpProxy.obtain().get(PlatformContans.User.sFindPtpRecordByUserName, paramsMap, token, new ICallBack() {
            @Override
            public void OnSuccess(String result) {

                hideLoadMore();
                try {
                    Log.d("PtpRecordByUserName", "OnSuccess: " + result);
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray beanList = object.getJSONObject("data").getJSONArray("beanList");
                        Gson gson = new Gson();
                        List<DepositsHistoryBean> list = new ArrayList<>();
                        for (int i = 0; i < beanList.length(); i++) {
                            JSONObject item = beanList.getJSONObject(i);
                            DepositsHistoryBean bean = new DepositsHistoryBean();
                            bean.setType(1);
                            DepositsHistoryBean.Entity entity = gson.fromJson(item.toString(), DepositsHistoryBean.Entity.class);
                            try {
                                JSONObject investmentScheme = item.getJSONObject("investmentScheme");
                                entity.setInvestmentSchemeString(investmentScheme.toString());
                                entity.setInvestmentScheme(gson.fromJson(investmentScheme.toString(), DepositsHistoryBean.Entity.InvestmentSchemeBean.class));
                            } catch (JSONException e) {

                            }
                            bean.addList(entity);
                            list.add(bean);
                        }
                        mBaseAdapter.addAll(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                hideLoadMore();
                ToaskUtil.showToast(getContext(), "请检查网络");
            }
        });
    }

    @Override
    protected List<Cell> getCells(List<DepositsHistoryBean> list) {
        return null;
    }
}

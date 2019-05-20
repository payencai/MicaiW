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

import cn.micaiw.mobile.activity.ProxyCentreActivity;
import cn.micaiw.mobile.common.commonRv.base.Cell;
import cn.micaiw.mobile.common.commonRv.fragment.AbsBaseFragment;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.InvitationBean;
import cn.micaiw.mobile.entity.model.DataMocker;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

/**
 * 作者：凌涛 on 2018/5/14 10:05
 * 邮箱：771548229@qq..com
 */
public class MyInviteFragment extends AbsBaseFragment<InvitationBean> {

    private int mListSize = 0;
    //请求次数
    private int requstCount = 0;
    private List<InvitationBean> mList = new ArrayList<>();

    @Override
    public void onRecyclerViewInitialized() {
        InvitationBean headBean = new InvitationBean();
        headBean.setName("昵称");
        headBean.setAccount("用户账号");
        headBean.setTotalAward("该用户总提成");
        mList.add(headBean);
        requestHeadData();

//        loadData();
    }

    private void loadData() {
        List<InvitationBean> list = new ArrayList<>();

        InvitationBean headBean = new InvitationBean();
        headBean.setName("昵称");
        headBean.setAccount("用户账号");
        headBean.setTotalAward("该用户总提成");
        mBaseAdapter.add(headBean);
        for (int i = 0; i < 20; i++) {
            InvitationBean bean = new InvitationBean();
            bean.setName("张三" + i);
            bean.setAccount("1232" + i);
            bean.setTotalAward("1314" + i);
            for (int j = 0; j < 5; j++) {
                InvitationBean.Entity entity = new InvitationBean.Entity();
                entity.setPlatformName("李四" + i);
                entity.setTotal(123456789);
                entity.setTerm("30天");
                entity.setAward(j);
                bean.addList(entity);
            }
            list.add(bean);
        }
        mBaseAdapter.addAll(list);
    }

    @Override
    public void onPullRefresh() {
        setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadMore();
            }
        }, 1000);
    }

    @Override
    protected List<Cell> getCells(List<InvitationBean> list) {
        return null;
    }

    private void requestHeadData() {
        UserInfoSharedPre sharedPre = UserInfoSharedPre.getIntance(getContext());
        int userId = sharedPre.getUserId();
        String token = sharedPre.getToken();
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> tokenMap = new HashMap<>();
        if (userId == -1 || TextUtils.isEmpty(token)) {
            ToaskUtil.showToast(getContext(), "未登录");
            return;
        }
        paramsMap.put("id", userId);
        tokenMap.put("token", token);
        HttpProxy.obtain().get(PlatformContans.AgencyForA.sGetMyInvitationById, paramsMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("sGetMyInvitationById", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        Gson gson = new Gson();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            InvitationBean bean = gson.fromJson(item.toString(), InvitationBean.class);
                            mList.add(bean);
                        }
                        mListSize = mList.size();
                        requestListData(mList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    ToaskUtil.showToast(getActivity(), "登录过期");
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void requestListData(List<InvitationBean> list) {
        for (InvitationBean bean : list) {
            requestItemDta(bean);
        }
    }

    private void requestItemDta(InvitationBean bean) {
        UserInfoSharedPre sharedPre = UserInfoSharedPre.getIntance(getContext());
        int userId = sharedPre.getUserId();
        String token = sharedPre.getToken();
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> tokenMap = new HashMap<>();
        if (userId == -1 || TextUtils.isEmpty(token)) {
            ToaskUtil.showToast(getContext(), "未登录");
            return;
        }
        paramsMap.put("id", userId);
        paramsMap.put("account", bean.getAccount());
        tokenMap.put("token", token);
        HttpProxy.obtain().get(PlatformContans.AgencyForA.sGetMyInvitation, paramsMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("sGetMyInvitation", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        Gson gson = new Gson();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            InvitationBean.Entity entity = gson.fromJson(item.toString(), InvitationBean.Entity.class);
                            mList.get(requstCount).addList(entity);
                        }
                        requstCount++;
                        if (requstCount == mListSize) {
                            requstCount = 0;
                            mListSize = 0;
                            for (int i = 0; i < mList.size(); i++) {
                                List<InvitationBean.Entity> list = mList.get(i).getList();
                                for (InvitationBean.Entity entity : list) {
                                    MLog.log("entity666", entity.toString());
                                }
                            }
                            mBaseAdapter.addAll(mList);
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
}

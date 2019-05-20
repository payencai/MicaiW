package cn.micaiw.mobile.fragment;

import android.support.v4.app.FragmentActivity;
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
import cn.micaiw.mobile.activity.AllPlatfromInfoActivity;
import cn.micaiw.mobile.common.commonRv.base.Cell;
import cn.micaiw.mobile.common.commonRv.fragment.AbsBaseFragment;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.Company;
import cn.micaiw.mobile.entity.PlatfromBean;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

/**
 * 作者：凌涛 on 2018/5/23 11:05
 * 邮箱：771548229@qq..com
 */
public class AllPlatfromFragment extends AbsBaseFragment<PlatfromBean> implements PlatfromBean.OnSelectPlatfrom {

    private int mPage = 1;
    private boolean isRefresh = false;
    private int mCompanyType = 1;//默认为1
    private boolean isFirst=true;
    @Override
    public void onRecyclerViewInitialized() {
        FragmentActivity activity = getActivity();
        if (activity instanceof AllPlatfromInfoActivity) {
            mCompanyType = ((AllPlatfromInfoActivity) activity).companyType;
        }
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.manu_loading_layout, null);
        mBaseAdapter.showLoading(loadingView);
        if (mCompanyType == 1) {
            ToaskUtil.showToast(getContext(), "P2P投资");
            requestP2PPlatfromAllInfo(PlatformContans.User.sFindPtpPlatformCondition);

        } else if (mCompanyType == 2) {
            ToaskUtil.showToast(getContext(), "基金投资");
            requestP2PPlatfromAllInfo(PlatformContans.User.sGetAllFundPlatform);
        }

    }

    @Override
    public void onPullRefresh() {
        mPage = 1;
        isRefresh = true;
        //下拉刷新回调
        isFirst=true;
//        mRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setRefreshing(false);
//            }
//        }, 2000);
        if (mCompanyType == 1) {
            requestP2PPlatfromAllInfo(PlatformContans.User.sFindPtpPlatformCondition);

        } else if (mCompanyType == 2) {
            requestP2PPlatfromAllInfo(PlatformContans.User.sGetAllFundPlatform);
        }
    }

    @Override
    public void onLoadMore() {
        mPage++;
        if (mCompanyType == 1) {
            requestP2PPlatfromAllInfo(PlatformContans.User.sFindPtpPlatformCondition);

        } else if (mCompanyType == 2) {
            requestP2PPlatfromAllInfo(PlatformContans.User.sGetAllFundPlatform);
        }
    }

    @Override
    protected List<Cell> getCells(List<PlatfromBean> list) {
        return null;
    }

    private void requestP2PPlatfromAllInfo(final String url) {
        final Map<String, Object> params = new HashMap<>();
        params.put("page", mPage);
        HttpProxy.obtain().post(url, params, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                setRefreshing(false);
                mBaseAdapter.hideLoading();
                mBaseAdapter.hideLoadMore();
                MLog.log("PtpPlatformCondition", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray beanList = object.getJSONObject("data").getJSONArray("beanList");
                        List<PlatfromBean> list = new ArrayList<>();
                        Gson gson = new Gson();
                        for (int i = 0; i < beanList.length(); i++) {
                            JSONObject item = beanList.getJSONObject(i);
                            PlatfromBean bean = gson.fromJson(item.toString(), PlatfromBean.class);
                            bean.setOnSelectPlatfrom(AllPlatfromFragment.this);
                            list.add(bean);
                        }
                        if(isFirst){
                            if(isRefresh){
                                isRefresh = false;
                                isFirst=false;
                                mBaseAdapter.clear();
                                mBaseAdapter.addAll(list);
                                requestP2PPlatfromAllInfo(url);
                            }
                            mPage++;
                            isFirst=false;
                            mBaseAdapter.addAll(list);
                            requestP2PPlatfromAllInfo(url);
                        }else{
                            if (isRefresh) {
                                isRefresh = false;
                                mBaseAdapter.clear();
                                mBaseAdapter.addAll(list);

                            } else {
                                mBaseAdapter.addAll(list);
                            }
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSelectPlatfromBean(int id, String platfromName) {
        onitemclicklistener.onItemClick(id, platfromName);
    }

    public interface OnItemClickListener {
        void onItemClick(int id, String platfromName);
    }

    private OnItemClickListener onitemclicklistener;

    public void setOnitemclicklistener(OnItemClickListener onitemclicklistener) {
        this.onitemclicklistener = onitemclicklistener;
    }
}

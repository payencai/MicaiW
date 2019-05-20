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
import cn.micaiw.mobile.base.component.CommonFragment;
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
public class P2PItemFragment extends AbsBaseFragment<Company> {
    private boolean isRefresh = false;
    private int mPage = 1;//请求的页数，默认为1
    private int mIsFirst = 1;//是否是首投，默认为1，（1.首投，2.复投）
    private String mPlatformSearch = "大额区";//过滤的条件,默认为大额区，只有"大额区", "小额区", "高评分", "高返利", "存管系", "风投系", "上市系", "民营系"

    public static P2PItemFragment getInstance(String platformSearch, int isFrist) {
        P2PItemFragment baseFragment = new P2PItemFragment();
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

                setRefreshing(false);
                isRefresh = true;
                mPage = 1;
                requeData(mPage, mIsFirst, mPlatformSearch);


    }

    @Override
    public void onLoadMore() {
        mPage++;
        Log.e("page",mPage+":"+mIsFirst+mPlatformSearch);
        //ToaskUtil.showToast(getContext(),"触发了加载更多");
        requeData(mPage,mIsFirst,mPlatformSearch);
        //上拉加载回调
        //loadMore();
    }

    private void loadMore() {
//        mRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideLoadMore();
//                mBaseAdapter.addAll(getCells(DataMocker.mockMoreData()));
//            }
//        }, 2000);
    }

    protected List<Cell> getCells(List<Company> entries) {
//        //根据实体生成Cell
//        List<Cell> cells = new ArrayList<>();
////        cells.add(new BannerCell(Arrays.asList(DataMocker.images)));
//        for (int i = 0; i < entries.size(); i++) {
//            Entry entry = entries.get(i);
//            if (entry.type == Entry.TYPE_IMAGE) {
//                cells.add(new ImageCell(entry));
//            } else {
//                cells.add(new TextCell(entry));
//            }
//        }
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
        Log.e("plat",mPlatformSearch);
        requeData(mPage, mIsFirst, mPlatformSearch);


    }

    private void requeData(final int page, final int isFirst, String platformSearch) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("isFirst", isFirst);//（1.首投，2.复投）
        paramsMap.put("state", 1);//（1.首投，2.复投）
        if (!platformSearch.equals("全部"))
        paramsMap.put("platformSearch", platformSearch);//过滤的条件
//        for (String s : paramsMap.keySet()) {
//            Log.d("paramsMap", "requeData: key:" + s + ":value:" + paramsMap.get(s));
//        }
        HttpProxy.obtain().post(PlatformContans.User.sFindPtpPlatformCondition, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                mBaseAdapter.hideLoading();
                if(page>1){
                   // ToaskUtil.showToast(getContext(),"隐藏加载更多图标");
                    mBaseAdapter.hideLoadMore();
                }
                Log.d("getPtpPlatformByType", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.optInt("resultCode");
                    Gson gson = new Gson();
                    if (resultCode == 0) {
                        List<Company> companyList = new ArrayList<>();
                        JSONArray data = object.optJSONObject("data").optJSONArray("beanList");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.optJSONObject(i);
                            Company company = gson.fromJson(item.toString(), Company.class);
                            company.setCompanyType(1);
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
                            setRefreshing(false);
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
            }
        });
    }

}

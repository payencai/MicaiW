package cn.micaiw.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.P2PDetailsActivity;
import cn.micaiw.mobile.adapter.P2pItemAdapter;
import cn.micaiw.mobile.common.commonRv.base.Cell;
import cn.micaiw.mobile.common.commonRv.fragment.AbsBaseFragment;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.Company;
import cn.micaiw.mobile.entity.P2pBean;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.ToaskUtil;

/**
 * 作者：凌涛 on 2018/5/17 17:26
 * 邮箱：771548229@qq..com
 */
public class P2PPageFragment extends Fragment {
    @BindView(R.id.rf_p2p)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_p2p)
    RecyclerView mRecyclerView;
    P2pItemAdapter mP2pItemAdapter;
    boolean isLoadMore = false;
    private int mPage = 1;//请求的页数，默认为1
    private   int mIsFirst = 1;//是否是首投，默认为1，（1.首投，2.复投）
    private  String mPlatformSearch = "大额区";//过滤的条件,默认为大额区，只有"大额区", "小额区", "高评分", "高返利", "存管系", "风投系", "上市系", "民营系"

    public static P2PPageFragment getInstance(String platformSearch, int isFrist) {
        P2PPageFragment baseFragment = new P2PPageFragment();
        Bundle args = new Bundle();
        args.putInt("isFirst", isFrist);
        args.putString("platformSearch", platformSearch);
        baseFragment.setArguments(args);
        return baseFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_p2p_page, container, false);
        ButterKnife.bind(this,rootView);
        initAdapter();
        requeData(mPage,mIsFirst,mPlatformSearch);
        return rootView;
    }
    private void initAdapter(){
        Bundle bundle = getArguments();
        mIsFirst = bundle.getInt("isFirst");
        mPlatformSearch = bundle.getString("platformSearch");
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                requeData(mPage,mIsFirst,mPlatformSearch);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mP2pItemAdapter=new P2pItemAdapter(R.layout.item_company_rv);
        mP2pItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), P2PDetailsActivity.class);
                P2pBean p2pBean= (P2pBean) adapter.getItem(position);
                intent.putExtra("platformLogo", p2pBean.getPlatformLogo());//logo
                intent.putExtra("platformName", p2pBean.getPlatformName());//名称
                intent.putExtra("labelList", (Serializable) Arrays.asList(p2pBean.getPlatformLabel().split(",")));
                intent.putExtra("content", p2pBean.getContent());//内容
                intent.putExtra("annualRate", p2pBean.getAnnualRate());//内容
                intent.putExtra("avgAnnualRate", p2pBean.getAvgAnnualRate());//内容
                intent.putExtra("riskScore", p2pBean.getRiskScore() + "  " + p2pBean.getRiskAssessment());//风控分
                intent.putExtra("userNumber", p2pBean.getUserNumber());//参加人数
                intent.putExtra("isFirst", p2pBean.getIsFirst());//是否首投
                intent.putExtra("platformUrl", p2pBean.getPlatformUrl());//直达连接
                intent.putExtra("ptpPlatformId", p2pBean.getId());//
                intent.putExtra("companyType", 1);
                startActivity(intent);
            }
        });
        mP2pItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPage++;
                isLoadMore = true;
                requeData(mPage,mIsFirst,mPlatformSearch);
            }
        }, mRecyclerView);
    }
    private void requeData(final int page, final int isFirst, String platformSearch) {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("isFirst", isFirst);//（1.首投，2.复投）
        paramsMap.put("state", 1);//（1.首投，2.复投）
        if (!platformSearch.equals("全部"))
        paramsMap.put("platformSearch", platformSearch);//过滤的条件
        HttpProxy.obtain().post(PlatformContans.User.sFindPtpPlatformCondition, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {

                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.optInt("resultCode");
                    Gson gson = new Gson();
                    if (resultCode == 0) {
                        Log.e("mpage", result.toString());
                        List<P2pBean> p2pBeans = new ArrayList<>();
                        JSONArray data = object.optJSONObject("data").optJSONArray("beanList");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.optJSONObject(i);
                            P2pBean p2pBean = gson.fromJson(item.toString(), P2pBean.class);
                            p2pBeans.add(p2pBean);
                        }
                        if (p2pBeans.size() == 0) {
                            if (isLoadMore) {
                                isLoadMore=false;
                                mP2pItemAdapter.loadMoreEnd(true);
                            }else{

                            }
                        } else {
                            if (isLoadMore) {
                                Log.e("mpage",page+"");
                                isLoadMore = false;
                                mP2pItemAdapter.addData(p2pBeans);
                                mP2pItemAdapter.loadMoreComplete();
                            } else {
                                mP2pItemAdapter.setNewData(p2pBeans);
                                mRecyclerView.setAdapter(mP2pItemAdapter);

                            }
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

            }
        });
    }

}

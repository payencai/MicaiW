package cn.micaiw.mobile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.adapter.FundAdapter;
import cn.micaiw.mobile.base.component.BaseListFragment;
import cn.micaiw.mobile.base.component.WrapContentLinearLayoutManager;
import cn.micaiw.mobile.entity.FundPage;

/**
 * Created by Administrator on 2018/3/23 0023.
 * 基金页
 * 废弃，不使用
 */

public class FundListFragment extends BaseListFragment {
    private View mViewNodata;
    protected List<FundPage.ListBean> mList = new ArrayList<>();
    private FundAdapter mAdapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mViewNodata = findViewById(R.id.common_order_nodata);
        for (int i = 0; i < 10; i++)
            mList.add(new FundPage.ListBean());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_fund_list;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdapter = new FundAdapter(getActivity(), mList);
        return mAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new WrapContentLinearLayoutManager(getActivity());
    }

    @Override
    protected int getMultiRecyclerViewLayoutId() {
        return R.id.common_fund_rv;
    }

    @Override
    protected void doLoreMore(int currentPage, int size) {
        getDataList(currentPage, false);
    }

    @Override
    protected void doRefresh(int currentPage, int size) {
        getDataList(currentPage, true);
    }

    public void getDataList(int currentPage, final boolean isRefresh) {
        if (isRefresh) {
//            mList.clear();
        }
        toggleNoDataView(false);
        showToast("loading...");
        doLoadMoreFinish(0);
        doRefreshFinish(0);
    }

    protected void toggleNoDataView(boolean isShow) {
        if (isShow) {
            mViewNodata.setVisibility(View.VISIBLE);
            mMultiLayout.setVisibility(View.GONE);
        } else {
            mViewNodata.setVisibility(View.GONE);
            mMultiLayout.setVisibility(View.VISIBLE);
        }
    }

}

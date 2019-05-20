package cn.micaiw.mobile.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.adapter.P2PAdapter;
import cn.micaiw.mobile.base.component.BaseListFragment;
import cn.micaiw.mobile.base.component.WrapContentLinearLayoutManager;
import cn.micaiw.mobile.entity.P2PPage;

/**
 * Created by Administrator on 2018/3/23 0023.
 * P2P列表
 */

public class P2PListFragment extends BaseListFragment {
    private View mViewNodata;
    protected List<P2PPage.ListBean> mList = new ArrayList<>();
    private P2PAdapter mAdapter;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mViewNodata = findViewById(R.id.common_order_nodata);
        for(int i=0;i<10;i++)
            mList.add(new P2PPage.ListBean());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_p2p_list;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdapter = new P2PAdapter(getActivity(), mList);
        return mAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new WrapContentLinearLayoutManager(getActivity());
    }

    @Override
    protected int getMultiRecyclerViewLayoutId() {
        return R.id.common_p2p_rv;
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
        if(isRefresh) {
//            mList.clear();
        }
        toggleNoDataView(false);
        showToast("loading...");
        doLoadMoreFinish(0);
        doRefreshFinish(0);
    }

    protected void toggleNoDataView(boolean isShow) {
        if(isShow) {
            mViewNodata.setVisibility(View.VISIBLE);
            mMultiLayout.setVisibility(View.GONE);
        } else {
            mViewNodata.setVisibility(View.GONE);
            mMultiLayout.setVisibility(View.VISIBLE);
        }
    }

}

package cn.micaiw.mobile.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.MessageActivity;
import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.entity.FundBean;
import cn.micaiw.mobile.view.ItemSwitchButton;

public class BankFragment extends BaseFragment {

    @BindView(R.id.titlebar_swicth_btn)
    ItemSwitchButton mItemSwitchButton;
    @BindView(R.id.recommendFundRv)
    RecyclerView recommendFundRv;
    private RVBaseAdapter<FundBean> mRecommendFundAdapter;
    @BindView(R.id.title)
    TextView title;


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_bank;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        title.setVisibility(View.VISIBLE);
        title.setText("银行");
        mItemSwitchButton.setVisibility(View.GONE);
        initRecommendFundRv();
    }

    @OnClick({R.id.titlebar_msg_btn})
    public void onCick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_msg_btn:
                startActivity(new Intent(getActivity(), MessageActivity.class));
                break;
            default:
                break;
        }
    }

    private void initRecommendFundRv() {
        mRecommendFundAdapter = new RVBaseAdapter<FundBean>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }
        };

        List<FundBean> list = new ArrayList<>();
        list.add(new FundBean());
        list.add(new FundBean());
        list.add(new FundBean());
        list.add(new FundBean());
        list.add(new FundBean());
        list.add(new FundBean());
        list.add(new FundBean());
        list.add(new FundBean());
        list.add(new FundBean());
        list.add(new FundBean());
        list.add(new FundBean());
        list.add(new FundBean());
        mRecommendFundAdapter.setData(list);
        recommendFundRv.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendFundRv.setAdapter(mRecommendFundAdapter);
    }

}

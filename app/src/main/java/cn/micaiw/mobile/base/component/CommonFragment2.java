package cn.micaiw.mobile.base.component;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.entity.Company;

public class CommonFragment2 extends BaseFragment {


    @BindView(R.id.recommendP2PRv)
    RecyclerView recommendP2PRv;

    private RVBaseAdapter<Company> mRecommendP2PAdapter;


    public static CommonFragment2 getInstance(String name, int index) {
        CommonFragment2 baseFragment = new CommonFragment2();
        Bundle args = new Bundle();
        args.putString("name2", name);
        args.putInt("index", index);
        baseFragment.setArguments(args);
        return baseFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.p2p_layout2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initRecommendP2PRv();


    }

    private void initRecommendP2PRv() {
        Bundle bundle = getArguments();
        String name = bundle.getString("name2");
        mRecommendP2PAdapter = new RVBaseAdapter<Company>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }
        };
        List<Company> companyList = new ArrayList<>();
        mRecommendP2PAdapter.setData(companyList);
        recommendP2PRv.setLayoutManager(new LinearLayoutManager(getContext()));
        recommendP2PRv.setAdapter(mRecommendP2PAdapter);
    }
}

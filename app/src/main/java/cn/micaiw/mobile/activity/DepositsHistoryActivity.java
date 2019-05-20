package cn.micaiw.mobile.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.entity.DepositsHistoryBean;
import cn.micaiw.mobile.fragment.DepositsHistoryFragment;

public class DepositsHistoryActivity extends BaseActivity {


    @BindView(R.id.depHistoryRv)
    RecyclerView depHistoryRv;
    @BindView(R.id.rvFrameLayout)
    FrameLayout rvFrameLayout;
    private RVBaseAdapter<DepositsHistoryBean> mAdapter;
    private List<DepositsHistoryBean> mList = new ArrayList<>();

    @Override
    protected int getContentViewId() {
        return R.layout.activity_deposits_history;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(R.id.rvFrameLayout, new DepositsHistoryFragment()).commit();
    }

    private void initData() {
//        for (int i = 0; i < 10; i++) {
//
//            DepositsHistoryBean bean = new DepositsHistoryBean();
//            bean.setType(1);
//            DepositsHistoryBean.Entity entity = new DepositsHistoryBean.Entity("投哪网" + i, 89.98d + i, i, 23.30d + i,
//                    "2018-5-12", "返现失败:投资金额与上传金额不符", "哇哈哈" + i, "18878554275");
//            bean.addList(entity);
//            mList.add(bean);
//        }
    }

    private void initDspHistoryRv() {

        mAdapter = new RVBaseAdapter<DepositsHistoryBean>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }

            @Override
            protected void onClick(RVBaseViewHolder holder, int position) {
                if (position == 0) {
                    holder.getView(R.id.backImg).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                }
            }
        };

        mAdapter.setData(mList);
        depHistoryRv.setLayoutManager(new LinearLayoutManager(this));
        depHistoryRv.setAdapter(mAdapter);
    }

}

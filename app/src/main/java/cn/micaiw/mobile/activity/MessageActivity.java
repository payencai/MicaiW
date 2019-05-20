package cn.micaiw.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.entity.MessageBean;
import cn.micaiw.mobile.fragment.MessageFragment;

/**
 * Created by Administrator on 2018/3/24 0024.
 */

public class MessageActivity extends BaseActivity {

    @BindView(R.id.titlebar_tv_title)
    TextView mTvTitle;
    @BindView(R.id.message_rv_list)
    RecyclerView messageRvList;
    @BindView(R.id.rvFrameLayout)
    FrameLayout rvFrameLayout;
    @BindView(R.id.itemHead)
    RelativeLayout itemHead;
    private RVBaseAdapter<MessageBean> mMAdapter;
    private List<MessageBean> mList = new ArrayList<>();


    @Override
    protected int getContentViewId() {
        return R.layout.activity_message;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mTvTitle.setText("消息");
        initMsgRv();
        getSupportFragmentManager().beginTransaction().add(R.id.rvFrameLayout, new MessageFragment()).commit();
    }

    private void initMsgRv() {
        mMAdapter = new RVBaseAdapter<MessageBean>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }

            @Override
            protected void onClick(RVBaseViewHolder holder, int position) {
                if (position == 0) {
                    holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MessageActivity.this, ServiceActivity.class));
                        }
                    });
                } else {
                    holder.getView(R.id.itemBody).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MessageActivity.this, MsgDetailsActivity.class));
                        }
                    });
                }
            }
        };
        mMAdapter.setData(mList);
        messageRvList.setLayoutManager(new LinearLayoutManager(this));
        messageRvList.setAdapter(mMAdapter);
    }

    @OnClick({R.id.titlebar_btn_left,R.id.itemHead})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_btn_left:
                finish();
                break;
            case R.id.itemHead:
                startActivity(new Intent(this, ServiceActivity.class));
                break;
            default:
                break;
        }
    }


}

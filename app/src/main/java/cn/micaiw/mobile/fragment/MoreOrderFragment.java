package cn.micaiw.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.activity.MessageActivity;
import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.view.ItemSwitchButton;

/**
 * Created by Administrator on 2018/3/24 0024.
 * //复投
 */

/*more_order_frame*/

public class MoreOrderFragment extends BaseFragment {

    @BindView(R.id.titlebar_swicth_btn)
    ItemSwitchButton mItemSwitchButton;
    @BindView(R.id.titlebar_msg_btn)
    ImageView msgBtn;

    private P2PFragment2 mP2PFragment2;
    private FundFragment2 mFundFragment2;
    private FragmentManager fm;
    private List<Fragment> fragments;


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_more_order;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initview();
    }


    private void initview() {
        fm = getActivity().getSupportFragmentManager();
        mP2PFragment2 = new P2PFragment2();
        mFundFragment2 = new FundFragment2();
        fragments = new ArrayList<>();
        fragments.add(mP2PFragment2);
        fragments.add(mFundFragment2);
        fragments.add(P2PFinishFragment.getInstance(2));
        for (Fragment fragment : fragments) {
            fm.beginTransaction().add(R.id.more_order_frame, fragment).commit();
        }
        mItemSwitchButton.setSwicthBtnClickListener(new ItemSwitchButton.OnSwicthClickListener() {
            @Override
            public void onSwicthClick(String target) {
                Log.d("TAG", ">>>>>>" + target);
                switch (target) {
                    case "P2P":
                        hideAllFragment();
                        showFragment(0);
                        break;
                    case "基金":
                        hideAllFragment();
                        showFragment(1);
                        break;
                    case "结束":
                        hideAllFragment();
                        showFragment(2);
                        break;

                }
            }
        });

        hideAllFragment();
        showFragment(0);
    }

    private void hideAllFragment() {
        for (Fragment fragment : fragments) {
            fm.beginTransaction().hide(fragment).commit();
        }
    }

    private void showFragment(int position) {
        fm.beginTransaction().show(fragments.get(position)).commit();
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

}

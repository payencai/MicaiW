package cn.micaiw.mobile.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.fragment.P2PRecordFragment;

public class MoneyDetailsActivity extends BaseActivity {

    @BindView(R.id.returnMoneyLayout)
    FrameLayout returnMoneyLayout;
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.alreadyText)
    TextView alreadyText;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.grandText)
    TextView grandText;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.awaitText)
    TextView awaitText;
    @BindView(R.id.view3)
    View view3;

    private List<Fragment> fragments;
    private FragmentManager fm;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_money_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setSelectTable(0);
        initData();
    }

    private void initData() {
        fm = getSupportFragmentManager();
        fragments = new ArrayList<>();
        fragments.add(P2PRecordFragment.getInstance(PlatformContans.User.sGetUserInvestmentDataByUserName, 1));
        fragments.add(P2PRecordFragment.getInstance(PlatformContans.User.sFindUserInvestmentDataByUserName, 2));
        fragments.add(P2PRecordFragment.getInstance(PlatformContans.User.sSelectUserInvestmentDataByUserName, 3));

        for (Fragment fragment : fragments) {
//            addFragment(fragment, fragment.getClass().getSimpleName());
            fm.beginTransaction().add(R.id.returnMoneyLayout, fragment).commit();
        }
        hideAllFragment();
        showFragment(0);
    }

    private void setSelectTable(int type) {
        switch (type) {
            case 0:
                resetView();
                img1.setSelected(true);
                int color1 = Color.parseColor("#4A88ED");
                alreadyText.setTextColor(color1);//选中状态
                view1.setVisibility(View.VISIBLE);
                view1.setBackgroundColor(color1);
                break;
            case 1:
                resetView();
                img2.setSelected(true);
                int color2 = Color.parseColor("#FF6A48");
                grandText.setTextColor(color2);//选中状态
                view2.setVisibility(View.VISIBLE);
                view2.setBackgroundColor(color2);
                break;
            case 2:
                resetView();
                img3.setSelected(true);
                int color3 = Color.parseColor("#7ED321");
                awaitText.setTextColor(color3);//选中状态
                view3.setVisibility(View.VISIBLE);
                view3.setBackgroundColor(color3);
                break;
        }

    }

    private void resetView() {
        img1.setSelected(false);
        img2.setSelected(false);
        img3.setSelected(false);
        alreadyText.setTextColor(Color.parseColor("#333333"));//选中状态
        grandText.setTextColor(Color.parseColor("#333333"));//选中状态
        awaitText.setTextColor(Color.parseColor("#333333"));//选中状态
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);

    }

    @OnClick({R.id.alreadyReturnMoney, R.id.grandReturnMoney, R.id.awaitReturnMoney, R.id.backImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.alreadyReturnMoney:
                setSelectTable(0);
                hideAllFragment();
                showFragment(0);
                break;
            case R.id.grandReturnMoney:
                setSelectTable(1);
                hideAllFragment();
                showFragment(1);
                break;
            case R.id.awaitReturnMoney:
                setSelectTable(2);
                hideAllFragment();
                showFragment(2);
                break;
        }
    }

    private void resetStateForTagbar(int viewId) {
//        clearTagbarState();
//        if (viewId == R.id.main_home) {
//            tv_home.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
//            iv_home.setImageResource(R.mipmap.common_tab_normal);
//            return;
//        }
//        if (viewId == R.id.main_first_order) {//首投
//            tv_first_order.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
//            iv_first_order.setImageResource(R.mipmap.common_tab_selected_st);
//            return;
//        }
//        if (viewId == R.id.main_more_order) {//复投
//            tv_more_order.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
//            iv_more_order.setImageResource(R.mipmap.common_tab_selected_ft);
//            return;
//        }
//        if (viewId == R.id.main_bank) {//银行
//            tv_bank.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
//            iv_bank.setImageResource(R.mipmap.common_tab_selected_yh);
//            return;
//        }
//        if (viewId == R.id.main_user) {//我的
//            tv_user.setTextColor(ContextCompat.getColor(this, R.color.bg_blue));
//            iv_user.setImageResource(R.mipmap.common_tab_selected_wd);
//            return;
//        }
    }

    private void hideAllFragment() {
        for (Fragment fragment : fragments) {
            fm.beginTransaction().hide(fragment).commit();
        }
    }

    private void showFragment(int position) {
        fm.beginTransaction().show(fragments.get(position)).commit();
    }

}

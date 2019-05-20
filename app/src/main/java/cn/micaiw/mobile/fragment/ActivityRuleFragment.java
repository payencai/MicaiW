package cn.micaiw.mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseFragment;

public class ActivityRuleFragment extends BaseFragment {


    @BindView(R.id.moneyText)
    TextView moneyText;
    @BindView(R.id.activityRules)
    TextView activityRules;
    Unbinder unbinder;
    private int mCash;
    private String mRegulation;


    public static ActivityRuleFragment getInstance(String regulation, int cash) {
        ActivityRuleFragment baseFragment = new ActivityRuleFragment();
        Bundle args = new Bundle();
        args.putInt("cash", cash);
        args.putString("regulation", regulation);
        baseFragment.setArguments(args);
        return baseFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_activity_rule;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = getArguments();
        mCash = bundle.getInt("cash");
        mRegulation = bundle.getString("regulation");
        moneyText.setText(mCash + "元现金");
        activityRules.setText(mRegulation);
    }


}

package cn.micaiw.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;

public class TodayNewsFragment extends BaseFragment {


    @Override
    protected int getContentViewId() {
        return R.layout.fragment_today_news;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }


}

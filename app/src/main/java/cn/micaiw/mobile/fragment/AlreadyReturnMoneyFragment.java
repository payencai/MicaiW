package cn.micaiw.mobile.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseFragment;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;

/**
 * 作者：凌涛 on 2018/5/14 18:44
 * 邮箱：771548229@qq..com
 */
public class AlreadyReturnMoneyFragment extends BaseFragment {

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_already_return_money;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        requestData();
    }


}

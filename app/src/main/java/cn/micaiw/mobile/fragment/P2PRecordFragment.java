package cn.micaiw.mobile.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.common.commonRv.base.Cell;
import cn.micaiw.mobile.common.commonRv.fragment.AbsBaseFragment;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.CreditRecord;
import cn.micaiw.mobile.entity.FinanceRecord;
import cn.micaiw.mobile.entity.InsuranceRecord;
import cn.micaiw.mobile.entity.P2PRecord;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ToaskUtil;

/**
 * 作者：凌涛 on 2018/5/18 15:51
 * 邮箱：771548229@qq..com
 */
public class P2PRecordFragment extends AbsBaseFragment<P2PRecord> {
    ImageView toobarImg;
    TextView toobarText;
    private String mUrl;
    private boolean isLoadmore=false;
    private int type;//1为今日返现，2为累计返现，3为预计返现

    public static P2PRecordFragment getInstance(String url, int type) {
        P2PRecordFragment baseFragment = new P2PRecordFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putInt("type", type);
        baseFragment.setArguments(args);
        return baseFragment;
    }

    @Override
    public void onRecyclerViewInitialized() {
        setColorSchemeResources(R.color.colorAccent);
        View loadingView = LayoutInflater.from(getContext()).inflate(R.layout.manu_loading_layout, null);
        mBaseAdapter.showLoading(loadingView);
        Bundle bundle = getArguments();
        mUrl = bundle.getString("url");
        type = bundle.getInt("type", 1);
        if (!TextUtils.isEmpty(mUrl)) {
            requestData();
        } else {
            ToaskUtil.showToast(getContext(), "传入的url为空");
        }
    }

    @Override
    public void onPullRefresh() {
        setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        isLoadmore=true;
        requestData();

        //hideLoadMore();
    }

    @Override
    protected List<Cell> getCells(List<P2PRecord> list) {
        return null;
    }

    @Override
    public View addToolbar() {
        View toolbar = LayoutInflater.from(getContext()).inflate(R.layout.rm_fragment_toobar_layout, null);
        toobarText = (TextView) toolbar.findViewById(R.id.toobarText);
        toobarImg = (ImageView) toolbar.findViewById(R.id.toobarImg);
        return toolbar;
    }

    private void requestData() {
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> tokenMap = new HashMap<>();
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(getContext());
        String userName = intance.getUserName();
        String token = intance.getToken();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(token)) {
            return;
        }
        paramsMap.put("userName", userName);
        tokenMap.put("token", token);
        //PlatformContans.User.sGetUserInvestmentDataByUserName
        HttpProxy.obtain().get(mUrl, paramsMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                mBaseAdapter.hideLoading();
                if(isLoadmore){
                    mBaseAdapter.hideLoadMore();
                    return;
                }
                Log.d("P2PRecordFragment", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONObject data = object.optJSONObject("data");
                        double cashFor = data.optDouble("cashFor");
                        switch (type) {
                            case 1:
                                toobarImg.setImageResource(R.mipmap.ic_today_already_rm);
                                toobarText.setText("今日已返现:￥" + cashFor);
                                break;
                            case 2:
                                toobarImg.setImageResource(R.mipmap.ic_today_grand_rm);
                                toobarText.setText("累计返现:￥" + cashFor);
                                break;
                            case 3:
                                toobarImg.setImageResource(R.mipmap.ic_today_wait_rm);
                                toobarText.setText("预计返现:￥" + cashFor);
                                break;
                        }
                        //accountImg累计返现图标
                        JSONArray ptpRecordList = data.optJSONArray("ptpRecordList");
                        JSONArray fundRecordList = data.optJSONArray("fundRecordList");
                        JSONArray creditRecordList = data.optJSONArray("creditRecordList");
                        JSONArray financeRecordList = data.optJSONArray("financeRecordList");
                        JSONArray insuranceRecordList = data.optJSONArray("insuranceRecordList");
                        List<P2PRecord> recordList = new ArrayList<>();

                        for (int i = 0; i < ptpRecordList.length(); i++) {
                            JSONObject item = ptpRecordList.optJSONObject(i);
                            String s = item.toString();
                            Log.d("fromJson", "OnSuccess: " + s);
                            P2PRecord p2PRecord = new Gson().fromJson(item.toString(), P2PRecord.class);
                            if (type == 1) {
                                p2PRecord.setType(1);
                            } else if (type == 2) {
                                p2PRecord.setType(2);
                            } else if (type == 3) {
                                p2PRecord.setType(3);
                            }
                            recordList.add(p2PRecord);
                        }
                        for (int i = 0; i < fundRecordList.length(); i++) {
                            JSONObject item = fundRecordList.optJSONObject(i);
                            String s = "";
                            P2PRecord p2PRecord = new Gson().fromJson(item.toString(), P2PRecord.class);
                            if (type == 1) {
                                p2PRecord.setType(1);
                            } else if (type == 2) {
                                p2PRecord.setType(2);
                            } else if (type == 3) {
                                p2PRecord.setType(3);
                            }
                            recordList.add(p2PRecord);
                        }
                        //finance
                        for (int i = 0; i <financeRecordList.length(); i++) {
                            JSONObject item = financeRecordList.optJSONObject(i);
//                            String s = item.toString();
//                            s="{\n" +
//                                    "\t\"account\": \"string\",\n" +
//                                    "\t\"accountImg\": \"string\",\n" +
//                                    "\t\"adminName\": \"string\",\n" +
//                                    "\t\"alipayName\": \"string\",\n" +
//                                    "\t\"alipayNo\": \"string\",\n" +
//                                    "\t\"createTime\": \"2018-12-15 12:32:12\",\n" +
//                                    "\t\"financeId\": 0,\n" +
//                                    "\t\"financeName\": \"lisi\",\n" +
//                                    "\t\"financeType\": \"string\",\n" +
//                                    "\t\"id\": 0,\n" +
//                                    "\t\"income\": 20,\n" +
//                                    "\t\"keyword\": \"string\",\n" +
//                                    "\t\"openTime\": \"string\",\n" +
//                                    "\t\"processingTime\": \"2018-12-15T02:58:49.015Z\",\n" +
//                                    "\t\"reason\": \"string\",\n" +
//                                    "\t\"state\": 0,\n" +
//                                    "\t\"total\": 17000,\n" +
//                                    "\t\"userId\": \"string\",\n" +
//                                    "\t\"userName\": \"string\"\n" +
//                                    "}";
//                            Log.d("fromJson", "OnSuccess: " + s);
                            P2PRecord p2PRecord = new Gson().fromJson(item.toString(), P2PRecord.class);
                            p2PRecord.setType(4);
                            recordList.add(p2PRecord);
                        }//credit
                        for (int i = 0; i < creditRecordList.length(); i++) {
                            JSONObject item = creditRecordList.optJSONObject(i);
//                            String s = "";
//                            s=" {\n" +
//                                    "        \"accountImg\": \"string\",\n" +
//                                    "        \"adminName\": \"string\",\n" +
//                                    "        \"alipayName\": \"string\",\n" +
//                                    "        \"alipayNo\": \"string\",\n" +
//                                    "        \"cardType\": \"string\",\n" +
//                                    "        \"createTime\": \"2018-12-15T02:58:49.015Z\",\n" +
//                                    "        \"creditId\": 0,\n" +
//                                    "        \"creditName\": \"hhhha\",\n" +
//                                    "        \"id\": 0,\n" +
//                                    "        \"idCard\": \"string\",\n" +
//                                    "        \"income\": 60,\n" +
//                                    "        \"keyword\": \"string\",\n" +
//                                    "        \"name\": \"string\",\n" +
//                                    "        \"openTime\": \"string\",\n" +
//                                    "        \"processingTime\": \"2018-12-15T02:58:49.015Z\",\n" +
//                                    "        \"reason\": \"string\",\n" +
//                                    "        \"state\": 0,\n" +
//                                    "        \"telephone\": \"string\",\n" +
//                                    "        \"userId\": \"string\",\n" +
//                                    "        \"userName\": \"string\"\n" +
//                                    "      }";
                            //Log.d("fromJson", "OnSuccess: " + s);
                            P2PRecord p2PRecord = new Gson().fromJson(item.toString(), P2PRecord.class);
                            p2PRecord.setType(5);
                            recordList.add(p2PRecord);
                        }//insurance
                        for (int i = 0; i < insuranceRecordList.length(); i++) {
                            JSONObject item = insuranceRecordList.optJSONObject(i);
//                            String s = "";
//                            s="{\n" +
//                                    "        \"accountImg\": \"string\",\n" +
//                                    "        \"adminName\": \"string\",\n" +
//                                    "        \"alipayName\": \"string\",\n" +
//                                    "        \"alipayNo\": \"string\",\n" +
//                                    "        \"createTime\": \"2018-12-15T02:58:49.015Z\",\n" +
//                                    "        \"id\": 0,\n" +
//                                    "        \"income\": 80,\n" +
//                                    "        \"insuranceId\": 0,\n" +
//                                    "        \"insuranceName\": \"libai\",\n" +
//                                    "        \"insuranceType\": \"string\",\n" +
//                                    "        \"keyword\": \"string\",\n" +
//                                    "        \"name\": \"string\",\n" +
//                                    "        \"openTime\": \"string\",\n" +
//                                    "        \"processingTime\": \"2018-12-15T02:58:49.015Z\",\n" +
//                                    "        \"reason\": \"string\",\n" +
//                                    "        \"state\": 0,\n" +
//                                    "        \"total\": 12000,\n" +
//                                    "        \"userId\": \"string\",\n" +
//                                    "        \"userName\": \"string\"\n" +
//                                    "      }";
                            //Log.d("fromJson", "OnSuccess: " + s);
                            P2PRecord p2PRecord = new Gson().fromJson(item.toString(), P2PRecord.class);
                            p2PRecord.setType(6);
                            recordList.add(p2PRecord);
                        }
                        mBaseAdapter.addAll(recordList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                mBaseAdapter.hideLoading();
                Log.d("userNameuserName", "onFailure: " + error);
            }
        });
    }

}

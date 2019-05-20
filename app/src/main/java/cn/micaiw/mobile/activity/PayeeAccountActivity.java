package cn.micaiw.mobile.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.common.view.MyDividerItemDecoration;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.entity.BankCardBean;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.ToaskUtil;

public class PayeeAccountActivity extends BaseActivity {


    @BindView(R.id.payeeAccountRv)
    RecyclerView payeeAccountRv;
    private RVBaseAdapter<BankCardBean> mAdapter;
    private List<BankCardBean> mList = new ArrayList<>();
    private PopupWindow mMenuPw;
    private boolean mIsSelectFinish;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_payee_account;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getIntent();
        mIsSelectFinish = intent.getBooleanExtra("isSelect", false);
        initPayeeAccountRv();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        int userId = intance.getUserId();
        if (userId == -1) {
            ToaskUtil.showToast(this, "登录过期");
            intance.clearUserInfo();
            return;
        }
        String token = intance.getToken();
        if (TextUtils.isEmpty(token)) {
            ToaskUtil.showToast(this, "登录过期");
            intance.clearUserInfo();
            return;
        }
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userId", userId);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);

        HttpProxy.obtain().get(PlatformContans.User.sGetAccountByUserId, paramsMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("sGetAccountByUserId", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray alipayList = data.getJSONArray("alipayList");
                        JSONArray bankCardList = data.getJSONArray("bankCardList");
                        Gson gson = new Gson();
                        List<BankCardBean> list = new ArrayList<>();
                        for (int i = 0; i < alipayList.length(); i++) {
                            JSONObject item = alipayList.getJSONObject(i);
                            String createTime = item.getString("createTime");
                            String account = item.getString("account");
                            String name = item.getString("name");
                            String bankImg = item.getString("bankImg");
                            int id = item.getInt("id");
                            BankCardBean bean = new BankCardBean();
                            bean.isBankCard = false;
                            bean.setAccount(account);
                            bean.setBankName(name);
                            bean.setBankImg(bankImg);
                            bean.setCreateTime(createTime);
                            bean.setId(id);
                            list.add(bean);
                        }
                        for (int i = 0; i < bankCardList.length(); i++) {
                            JSONObject item = bankCardList.getJSONObject(i);
                            BankCardBean bean = gson.fromJson(item.toString(), BankCardBean.class);
                            bean.isBankCard = true;
                            list.add(bean);
                        }
                        Collections.sort(list, new Comparator<BankCardBean>() {
                            @Override
                            public int compare(BankCardBean o1, BankCardBean o2) {
                                return getValue(o1) - getValue(o2);
                            }

                            private int getValue(BankCardBean bean) {
                                String createTime = bean.getCreateTime();
                                String replace = createTime.replace(" ", ":");
                                String s = replace.split(":")[0].replace("-", "");
                                int parseInt = Integer.parseInt(s);
                                return parseInt;
                            }
                        });
//                        mAdapter.addAll(list);
                        mAdapter.resetData(list);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });

    }

    private void initPayeeAccountRv() {

        mAdapter = new RVBaseAdapter<BankCardBean>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {
            }

            @Override
            protected void onClick(RVBaseViewHolder holder, final int position) {
                holder.getImageView(R.id.delImg).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BankCardBean bean = mData.get(position);
                        int id = bean.getId();
                        if (bean.isBankCard) {
                            showExitDialog(id, PlatformContans.User.sDeleteBankCardById);
                        } else {
                            showExitDialog(id, PlatformContans.User.sDeleteAlipayById);
                        }
                    }
                });
                holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mIsSelectFinish) {
                            Intent intent = new Intent();
                            BankCardBean bean = mData.get(position);
                            intent.putExtra("bankName", bean.getBankName());
                            intent.putExtra("account", bean.getAccount());
                            intent.putExtra("bankImg", bean.getBankImg());
                            setResult(1, intent);
                            finish();
                        }
                    }
                });
            }
        };
        mAdapter.setData(mList);
        payeeAccountRv.setLayoutManager(new LinearLayoutManager(this));
        payeeAccountRv.addItemDecoration(new MyDividerItemDecoration(30));
        payeeAccountRv.setAdapter(mAdapter);

    }

    @OnClick({R.id.backImg, R.id.addPayeeAccount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.addPayeeAccount:
                showAddMenu(view);
                break;
        }
    }

    private void showAddMenu(View view) {
        if (mMenuPw != null) {
            mMenuPw = null;
        }

        View showView = LayoutInflater.from(this).inflate(R.layout.pw_add_payway, null);
        handleView(showView);
        mMenuPw = new PopupWindow(showView,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 94, getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics()));
        mMenuPw.setFocusable(true);
        mMenuPw.setBackgroundDrawable(new BitmapDrawable());
        mMenuPw.setOutsideTouchable(true);
        mMenuPw.showAsDropDown(view, 0, 20);

    }

    private void handleView(View view) {
        view.findViewById(R.id.addBankCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(PayeeAccountActivity.this, PayeeAccountActivity.class));
                AddBankCardOrZFBActivity.startActivity(PayeeAccountActivity.this, 0);
                if (mMenuPw != null) {
                    mMenuPw.dismiss();
                }
            }
        });
        view.findViewById(R.id.addZfb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(PayeeAccountActivity.this, PayeeAccountActivity.class).putExtra("intoType", 0));
                AddBankCardOrZFBActivity.startActivity(PayeeAccountActivity.this, 1);
                if (mMenuPw != null) {
                    mMenuPw.dismiss();
                }
            }
        });
    }


    private void showExitDialog(final int id, final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //第二个参数用来设置预选中的item
        builder.setTitle("提示")
                .setMessage("是否删除收款账号？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(id, PayeeAccountActivity.this, url);
                    }
                }).setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void delete(int id, final Context context, String url) {
        Map<String, Object> paramsMap = new HashMap<>();
        HashMap<String, Object> tokenMap = new HashMap<>();
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(context);
        String token = intance.getToken();
        int userId = intance.getUserId();
        if (userId == -1) {
            ToaskUtil.showToast(context, "登录过期");
            intance.clearUserInfo();
            return;
        }
        if (TextUtils.isEmpty(token)) {
            ToaskUtil.showToast(context, "登录过期");
            intance.clearUserInfo();
            return;
        }
        paramsMap.put("id", id);
        tokenMap.put("token", token);
        HttpProxy.obtain().get(url, paramsMap, tokenMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("deltelAlipy", "OnSuccess: " + result);
                //{"resultCode":0,"message":"删除成功"}
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    ToaskUtil.showToast(PayeeAccountActivity.this, message);
                    if (resultCode == 0) {
//                        mAdapter.notifyDataSetChanged();
                        initData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    ToaskUtil.showToast(PayeeAccountActivity.this, "登录过期");
//                    UserInfoSharedPre.getIntance(PayeeAccountActivity.this).clearUserInfo();
                }

            }

            @Override
            public void onFailure(String error) {
                ToaskUtil.showToast(context, "删除失败，请检查网络");
            }
        });
    }
}

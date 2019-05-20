package cn.micaiw.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.common.customview.CustomDatePicker;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.custom.KyLoadingBuilder;
import cn.micaiw.mobile.entity.P2PDetailsBean;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

public class WriteReturnMoneyActivity extends BaseActivity {

    @BindView(R.id.titelImg)
    ImageView titelImg;
    @BindView(R.id.selectPlatformName)
    TextView selectPlatformName;
    @BindView(R.id.investTime)
    TextView investTime;
    @BindView(R.id.schemeSelectText)
    TextView schemeSelectText;
    @BindView(R.id.inputReturnMoney)
    EditText inputReturnMoney;

    private int mPlatformId;
    private CustomDatePicker customDatePicker;

    @BindView(R.id.bankImg)
    ImageView bankImage;
    @BindView(R.id.shroffAccount)
    TextView shroffAccount;

    //兑换率
    private double mScoreRate;
    //是否请求成功兑换率
    private boolean isRequestRate = false;

    @BindView(R.id.investorAccountInput)
    EditText investorAccountInput;
    @BindView(R.id.investMoneyEdit)
    EditText investMoneyEdit;
    @BindView(R.id.wxOrQqAccount)
    EditText wxOrQqAccount;


    private List<P2PDetailsBean> mP2PDetailsBeanList = new ArrayList<>();
    private ArrayList<String> mArrayList = new ArrayList<>();
    private Map<String, Integer> scheme = new HashMap<>();
    private PopupWindow mSchemeSelect;
    //根据方案选择返现的金额，可以更待
    private Map<String, Integer> totalIncomeMap = new HashMap<>();
    private KyLoadingBuilder mOpenLoadView;
    private String mAccount;//收款账号
    private String mBankName;
    private String mBankImg;
    private int companyType;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_write_return_money;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        requestScore();
        WindowManager m = getWindowManager();
        companyType = getIntent().getIntExtra("companyType", 1);
//        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
//        mSoSPw = new PopupWindow(shareview, ((int) (d.getWidth() * 0.8)), ((int) (d.getHeight() * 0.5)));
        Glide.with(this).load(R.mipmap.ic_write_return_money).into(titelImg);
        initDatePicker();
    }

    @OnClick({R.id.backImg, R.id.selectPlatfrom, R.id.selectTimeLayout, R.id.schemeSelectLayout, R.id.paySelectLayout, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.selectPlatfrom:
                Intent intent = new Intent(this, AllPlatfromInfoActivity.class);
                intent.putExtra("companyType", companyType);
                startActivityForResult(intent, 0);
                break;
            case R.id.selectTimeLayout:
                customDatePicker.show(investTime.getText().toString());
                break;
            case R.id.schemeSelectLayout:
                String s = selectPlatformName.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    ToaskUtil.showToast(this, "请选择平台");
                    return;
                }
                showSchemeSelect(view);
                break;
            case R.id.paySelectLayout:
                Intent intent1 = new Intent(this, PayeeAccountActivity.class);
                intent1.putExtra("isSelect", true);
                startActivityForResult(intent1, 1);
                break;
            case R.id.submit:
                if (check()) {
                    if (isRequestRate) {
                        if (companyType == 1) {
                            submitData(PlatformContans.User.sAddPtpRecord);
                        } else {
                            submitData(PlatformContans.User.sAddFundRecord);
                        }
                    } else {
                        ToaskUtil.showToast(this, "汇率请求失败");
                        requestScore();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 0) {
                mPlatformId = data.getIntExtra("id", -1);//平台id
                String platformName = data.getStringExtra("platformName");//平台名称
                selectPlatformName.setText(platformName);
                if (companyType == 1) {
                    requestSchemeData(PlatformContans.User.sGetInvestmentSchemeByPtpId, mPlatformId);
                } else {
                    requestSchemeData(PlatformContans.User.sGetFundSchemeByFundPlatformId, mPlatformId);
                }
            }
            if (requestCode == 1) {
                mBankName = data.getStringExtra("bankName");
                mAccount = data.getStringExtra("account");
                mBankImg = data.getStringExtra("bankImg");
                shroffAccount.setText(mAccount);
//                requestBankImg(this, mBankImg, bankImage);
                Glide.with(this).load(mBankImg).placeholder(R.mipmap.logo).error(R.mipmap.logo).into(bankImage);
            }
        }
    }

    private int getScore(int startNumber) {
        double v = mScoreRate * startNumber;
        return (int) v;
    }

    private void requestBankImg(Context context, String tag, ImageView bankLogo) {
        String url = "http://120.78.77.138:8080/bank/" + tag + ".png";
        Glide.with(context).load(url).placeholder(R.mipmap.logo).error(R.mipmap.logo).into(bankLogo);

    }

    //获取投资方案
    private void requestSchemeData(String url, int platformId) {
        Map<String, Object> paramsMap = new HashMap<>();
        if (platformId == -1) {
            ToaskUtil.showToast(this, "数据出错");
            return;
        }
        if (companyType == 1) {
            paramsMap.put("ptpPlatformId", platformId);
        } else {
            paramsMap.put("fundPlatformId", platformId);
        }
        HttpProxy.obtain().get(url, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("ptpPlatformId", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray data = object.getJSONArray("data");
                        Gson gson = new Gson();
                        mP2PDetailsBeanList.clear();
                        totalIncomeMap.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            P2PDetailsBean bean = gson.fromJson(item.toString(), P2PDetailsBean.class);
                            bean.TYPE = 1;
                            mP2PDetailsBeanList.add(bean);
                            //
                            totalIncomeMap.put(bean.getName(), bean.getReward());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToaskUtil.showToast(WriteReturnMoneyActivity.this, "登录过期");
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    //弹出选择时间
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        String s = now.replace(" ", "_").split("_")[0];
        investTime.setText(s);
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                String s = time.replace(" ", "_").split("_")[0];
                investTime.setText(s);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动


    }

    public void showSchemeSelect(View view) {
        View shareView = LayoutInflater.from(this).inflate(R.layout.pw_scheme_select_layout, null);
        handleView(shareView);
        mSchemeSelect = new PopupWindow(shareView, LinearLayout.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics()));
        mSchemeSelect.setFocusable(true);
        mSchemeSelect.setBackgroundDrawable(new BitmapDrawable());
        mSchemeSelect.setOutsideTouchable(true);

        mSchemeSelect.setAnimationStyle(R.style.pwStyle);
        //第一个参数可以取当前页面的任意一个View
        //第二个参数表示pw从哪一个方向显示出来
        //3、4表示pw的偏移量
        mSchemeSelect.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void handleView(View view) {
        ListView list = (ListView) view.findViewById(R.id.schemeList);
        if (mArrayList != null) {
            mArrayList.clear();
        }
        for (P2PDetailsBean bean : mP2PDetailsBeanList) {
            mArrayList.add(bean.getName());
            scheme.put(bean.getName(), bean.getId());
        }
        mArrayList.add("其他");
        scheme.put("其他", 0);
        totalIncomeMap.put("其他", 0);
        MyAdapter adapter = new MyAdapter(this, mArrayList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = mArrayList.get(i);
                schemeSelectText.setText(s);
                int totalIncome = totalIncomeMap.get(s);
                inputReturnMoney.setText(totalIncome + "");
                ToaskUtil.showToast(WriteReturnMoneyActivity.this, "您选择了:" + s);
                if (mSchemeSelect != null) {
                    mSchemeSelect.dismiss();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> list;
        private LayoutInflater mInflater;

        public MyAdapter(Context context, List<String> list) {
            mContext = context;
            this.list = list;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHoder hoder;
            if (view == null) {
                view = mInflater.inflate(R.layout.simple_list_item_1, viewGroup, false);
                hoder = new ViewHoder();
                hoder.mTextView = view.findViewById(R.id.textview);
                view.setTag(hoder);
            } else {
                hoder = (ViewHoder) view.getTag();
            }
            hoder.mTextView.setText(list.get(i));
            return view;
        }

        public class ViewHoder {
            TextView mTextView;
        }

    }

    private void requestScore() {
        HttpProxy.obtain().post(PlatformContans.User.sGetScoreProportion, null, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.d("sGetScoreProportion", "OnSuccess: " + result);
                try {
                    JSONObject object = new JSONObject(result);
                    mScoreRate = object.getDouble("data");
                    isRequestRate = true;
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private boolean check() {
        String investorAccountInputString = investorAccountInput.getEditableText().toString();//投资账号
        String platformNameString = selectPlatformName.getText().toString();//平台名字
        String schemeSelectString = schemeSelectText.getText().toString();//选择方案
        String investMoneyEditString = investMoneyEdit.getEditableText().toString();//投资金额
        String wxOrQqAccountString = wxOrQqAccount.getEditableText().toString();//微信或者qq账号
        String inputReturnMoneyString = inputReturnMoney.getEditableText().toString();//返现金额
        String shroffAccountString = shroffAccount.getText().toString();//支付账号
        if (TextUtils.isEmpty(investorAccountInputString)) {
            ToaskUtil.showToast(this, "投资账号不能为空");
            return false;
        }
        if (TextUtils.isEmpty(platformNameString)) {
            ToaskUtil.showToast(this, "请选择平台");
            return false;
        }
        if (TextUtils.isEmpty(schemeSelectString)) {
            ToaskUtil.showToast(this, "请选择投资方案");
            return false;
        }
        if (TextUtils.isEmpty(investMoneyEditString)) {
            ToaskUtil.showToast(this, "请输入投资金额");
            return false;
        }
        if (TextUtils.isEmpty(wxOrQqAccountString)) {
            ToaskUtil.showToast(this, "请输入微信或者QQ");
            return false;
        }
        if (TextUtils.isEmpty(inputReturnMoneyString)) {
            ToaskUtil.showToast(this, "请输入返现金额");
            return false;
        }
        if (TextUtils.isEmpty(shroffAccountString)) {
            ToaskUtil.showToast(this, "请选择收款账号");
            return false;
        }

        return true;
    }

    private void submitData(String url) {
        UserInfoSharedPre intance = UserInfoSharedPre.getIntance(this);
        String tokenValue = intance.getToken();
        String userName = intance.getUserName();
        if (TextUtils.isEmpty(tokenValue)) {
            ToaskUtil.showToast(this, "未登录");
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            ToaskUtil.showToast(this, "未登录");
            return;
        }
        Map<String, Object> paramsMap = new HashMap<>();
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", tokenValue);
        /*参数体*/
        String investMoneyEditString = investMoneyEdit.getEditableText().toString();//投资金额
        String platformNameString = selectPlatformName.getText().toString();//平台名字
        String investorAccountInputString = investorAccountInput.getEditableText().toString();//投资账号
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String now = sdf.format(new Date());
        String investTimeString = investTime.getText().toString() + " " + now;//投资时间
        Log.d("investTimeString", "submitData: " + investTimeString);
        String inputReturnMoneyString = inputReturnMoney.getEditableText().toString();//返现金额
        String wxOrQqAccountString = wxOrQqAccount.getEditableText().toString();//微信或者qq账号
        String schemeSelectString = schemeSelectText.getText().toString();//选择方案
        paramsMap.put("userName", userName);
        int investMoneyValu = Integer.parseInt(investMoneyEditString);
        paramsMap.put("total", investMoneyValu);//投资金额
        paramsMap.put("platformId", mPlatformId);//投资平台id
        paramsMap.put("platformName", platformNameString);//投资平台名字
        paramsMap.put("alipayNo", mAccount);//支付宝账号或者银行卡号
        paramsMap.put("investmentAccount", investorAccountInputString);//投资账号，用户输入的
        paramsMap.put("alipayName", mBankName);//支付宝名字或者银行卡的电话号码
        paramsMap.put("accountImg", mBankImg);//支付宝或者银行卡的图片
        paramsMap.put("createTime", investTimeString);//投资时间
        paramsMap.put("investmentId", scheme.get(schemeSelectString));//投资方案id
        paramsMap.put("income", Integer.parseInt(inputReturnMoneyString));//返利金额
        paramsMap.put("wxqq", wxOrQqAccountString);//微信或者QQ
        paramsMap.put("score", getScore(investMoneyValu));//投资得分
        if (companyType == 2) {
            paramsMap.put("investmentName", schemeSelectString);//投资得分
        }
        mOpenLoadView = openLoadView("");
        HttpProxy.obtain().post(url, tokenMap, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                if (mOpenLoadView != null) {
                    mOpenLoadView.dismiss();
                }
                MLog.log("sAddPtpRecord", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    String message = object.getString("message");
                    ToaskUtil.showToast(WriteReturnMoneyActivity.this, message);
                    if (resultCode == 0) {
                        EventBus.getDefault().post("更新用户信息");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                if (mOpenLoadView != null) {
                    mOpenLoadView.dismiss();
                }
            }
        });
    }

    //totalIncome
}

package cn.micaiw.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.MLog;

public class PlatformDetailsActivity extends BaseActivity {


    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.companyName)
    TextView companyName;
    @BindView(R.id.registeredAssets)
    TextView registeredAssets;
    @BindView(R.id.publishTime)
    TextView publishTime;
    @BindView(R.id.companyBackground)
    TextView companyBackground;
    @BindView(R.id.onlineLendingHouse)
    TextView onlineLendingHouse;
    @BindView(R.id.netCreditEye)
    TextView netCreditEye;
    @BindView(R.id.operationSituation)
    TextView operationSituation;
    @BindView(R.id.complianceInfo)
    TextView complianceInfo;
    @BindView(R.id.miCaiGrade)
    TextView miCaiGrade;
    private int mPlatformId;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_platform_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        title.setText("平台详情");
        backImg.setVisibility(View.VISIBLE);
        backImg.setImageResource(R.mipmap.back);
        messageImg.setVisibility(View.GONE);
        Intent intent = getIntent();
        mPlatformId = intent.getIntExtra("platformId", -1);
        requestData();
    }

    @OnClick({R.id.backImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
        }
    }

    private void requestData() {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("platformId", mPlatformId);
        HttpProxy.obtain().get(PlatformContans.User.sGetPlatformDetailsByPlatformId, paramsMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("platformId", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        JSONObject data = object.getJSONObject("data");
                        int id = data.getInt("id");
                        String platformName = data.getString("platformName");//深圳投哪金融服务有限公司"
                        String capital = data.getString("capital");//10000万
                        String background = data.getString("background");//上市控股
                        String homeGrade = data.getString("homeGrade");//第11名
                        String eyeGrade = data.getString("eyeGrade");//第7名
                        String operate = data.getString("operate");//
                        String information = data.getString("information");//广发银行银行资金存管
                        String micScore = data.getString("micScore");//95分
                        String createTime = data.getString("createTime");//2018-05-16 16:53:31
                        String onlineDate = data.getString("onlineDate");//2012-05-04 00:00:00
                        int type = data.getInt("type");//1

                        String s = onlineDate.replace(" ", ":").split(":")[0];
                        companyName.setText(platformName);
                        registeredAssets.setText(capital);
                        publishTime.setText(s);
                        companyBackground.setText(background);
                        onlineLendingHouse.setText(homeGrade);
                        netCreditEye.setText(eyeGrade);
                        operationSituation.setText(operate);
                        complianceInfo.setText(information);
                        miCaiGrade.setText(micScore);
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

}

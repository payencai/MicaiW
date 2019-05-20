package cn.micaiw.mobile.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
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

public class IntegralRuleActivity extends BaseActivity {
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.releText)
    TextView releText;


    @Override
    protected int getContentViewId() {
        return R.layout.activity_integral_rule;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        backImg.setVisibility(View.VISIBLE);
        messageImg.setVisibility(View.GONE);
        title.setText("积分规则");
        requestIntegralRule();
    }

    private void requestIntegralRule() {
        HttpProxy.obtain().post(PlatformContans.User.sGetScoreRegulation, null, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                MLog.log("ScoreRegulation", result);
                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.getInt("resultCode");
                    if (resultCode == 0) {
                        String string = object.getJSONObject("data").getString("regulation");
                        releText.setText(string);
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

    @OnClick({R.id.backImg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
        }
    }
}

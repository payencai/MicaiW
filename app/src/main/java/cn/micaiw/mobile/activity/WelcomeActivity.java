package cn.micaiw.mobile.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.constant.PlatformContans;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.util.MLog;
import cn.micaiw.mobile.util.ToaskUtil;

public class WelcomeActivity extends AppCompatActivity {
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }
    };
    private ImageView mImageView;
    private ImageView big;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getstate();
        setContentView(R.layout.activity_welcome);

        mImageView = (ImageView) findViewById(R.id.welcomeImg);
        big= (ImageView) findViewById(R.id.welImg);

    }
    private void getstate(){
        HttpProxy.obtain().get(PlatformContans.weburl, null, new ICallBack() {
            @Override
            public void OnSuccess(String result) {
                Log.e("result",result);
               // result="{\"resultCode\":0,\"message\":null,\"data\":{\"state\":\"1\",\"skipUrl\":\"http://shop.wanmei.com/\"}}";
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    JSONObject data=jsonObject.getJSONObject("data");
                    String stat=data.getString("state");
                    if("1".equals(stat)){
                        String url=data.getString("skipUrl");
                        Intent intent=new Intent(WelcomeActivity.this,WebShopActivity.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                        finish();
                    }else{
                        requestGuideImg();
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
    private void requestGuideImg() {
        Map<String, Object> hasMap = new HashMap<>();
        hasMap.put("page", 1);
        hasMap.put("state", 1);
        hasMap.put("type", 1);
        HttpProxy.obtain().post(PlatformContans.User.sGetPropagandaMap, hasMap, new ICallBack() {
            @Override
            public void OnSuccess(String result) {

                try {
                    JSONObject object = new JSONObject(result);
                    int resultCode = object.optInt("resultCode");
                    if (resultCode == 0) {
                        JSONArray imgUrls = object.optJSONObject("data").optJSONArray("beanList");
                        if(imgUrls.length()==0){
                            MLog.log("guideimage", "OnSuccess: " + result);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mImageView.setVisibility(View.VISIBLE);
                                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                                    finish();
                                }
                            });
                        }
                        for (int i = 0; i < imgUrls.length(); i++) {
                            JSONObject item = imgUrls.optJSONObject(i);
                            String imgUrl = item.optString("picture");
                            Glide.with(WelcomeActivity.this).load(imgUrl)
                                    //.placeholder(R.mipmap.launch)
                                    .error(R.mipmap.launch)
                                    .into(big);
                            mHandler.sendEmptyMessageDelayed(0, 2000);
                            big.setVisibility(View.VISIBLE);
                        }

                    }
                } catch (JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mImageView.setVisibility(View.VISIBLE);
                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                ToaskUtil.showToast(WelcomeActivity.this, "请检查网络");
                //mImageView.setImageResource(R.mipmap.launch);
                mImageView.setImageResource(R.mipmap.logo);
                mImageView.setVisibility(View.VISIBLE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    }
                });
               // mHandler.sendEmptyMessageDelayed(0, 2000);
            }
        });
    }


}

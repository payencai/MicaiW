package cn.micaiw.mobile.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.component.BaseActivity;
import cn.micaiw.mobile.common.commonRv.base.RVBaseAdapter;
import cn.micaiw.mobile.common.commonRv.base.RVBaseViewHolder;
import cn.micaiw.mobile.entity.SettingItemBean;
import cn.micaiw.mobile.sharedpre.UserInfoSharedPre;
import cn.micaiw.mobile.util.APPInfoUtil;
import cn.micaiw.mobile.util.ActivityManager;
import cn.micaiw.mobile.util.DataCleanManager;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.backImg)
    ImageView backImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.messageImg)
    ImageView messageImg;
    @BindView(R.id.settingRv)
    RecyclerView settingRv;

    private PopupWindow mclearCachePw;

    private RVBaseAdapter<SettingItemBean> mAdapter;
    private List<SettingItemBean> mList = new ArrayList<>();


    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        backImg.setVisibility(View.VISIBLE);
        title.setText("设置");
        messageImg.setVisibility(View.GONE);
        initData();
        initSettingRv();

    }

    private void initData() {
        String content = "";
        try {
            content = DataCleanManager.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mList.add(new SettingItemBean("清除缓存", null, content));
        mList.add(new SettingItemBean("用户使用协议", null, ""));
        mList.add(new SettingItemBean("当前版本", null, "V " + APPInfoUtil.getVersionName(this)));
    }

    private void initSettingRv() {
        mAdapter = new RVBaseAdapter<SettingItemBean>() {
            @Override
            protected void onViewHolderBound(RVBaseViewHolder holder, int position) {

            }

            @Override
            protected void onClick(final RVBaseViewHolder holder, final int position) {
                holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position == 0) {
                            //清楚缓存窗口
                            setBackgroundDrakValue(0.5f);
                            showPopupW(holder.getItemView());
                        }
                        if (position == 1) {
                            startActivity(new Intent(SettingActivity.this, UseProtocolActivity.class));
                        }
                    }
                });

            }
        };
        mAdapter.setData(mList);
        settingRv.setLayoutManager(new LinearLayoutManager(this));
        settingRv.setAdapter(mAdapter);
    }

    @OnClick({R.id.backImg, R.id.exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImg:
                finish();
                break;
            case R.id.exit:
                showExitDialog();
                break;
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //第二个参数用来设置预选中的item
        builder.setTitle("提示")
                .setMessage("是否退出登录？")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoSharedPre.getIntance(SettingActivity.this).clearUserInfo();
                        ActivityManager instance = ActivityManager.getInstance();
                        instance.finishAllActivity();
                        startActivity(new Intent(SettingActivity.this, MainActivity.class));
                    }
                }).setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPopupW(View view) {
        View shareview = LayoutInflater.from(this).inflate(R.layout.pw_clear_cache_layout, null);
        initLoginView(shareview);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        mclearCachePw = new PopupWindow(shareview, ((int) (d.getWidth() * 0.6)), ((int) (d.getHeight() * 0.15)));
        mclearCachePw.setFocusable(true);
        mclearCachePw.setBackgroundDrawable(new BitmapDrawable());
        mclearCachePw.setOutsideTouchable(true);
        mclearCachePw.setAnimationStyle(R.style.CustomPopWindowStyle);
        mclearCachePw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundDrakValue(1.0f);
            }
        });
        //第一个参数可以取当前页面的任意一个View
        //第二个参数表示pw从哪一个方向显示出来
        //3、4表示pw的偏移量
        mclearCachePw.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    private void initLoginView(View view) {
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mclearCachePw != null) {
                    mclearCachePw.dismiss();
                }
            }
        });
        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataCleanManager.clearAllCache(SettingActivity.this);
                // 我也不想这样
                mList.clear();
                initData();
                mAdapter.resetData(mList);
                if (mclearCachePw != null) {
                    mclearCachePw.dismiss();
                }
            }
        });
    }

    protected void setBackgroundDrakValue(float value) {
        if (value > 1) {
            value = 1;
        }
        if (value < 0) {
            value = 0;
        }
        Window mWindow = getWindow();
        WindowManager.LayoutParams params = mWindow.getAttributes();
        params.alpha = value;
        mWindow.setAttributes(params);

    }

}

package cn.micaiw.mobile.base.component;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;

import com.changelcai.mothership.component.activity.MSBaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.micaiw.mobile.R;
import cn.micaiw.mobile.base.util.DialogHelper;
import cn.micaiw.mobile.common.util.PermissionUtil;
import cn.micaiw.mobile.custom.KyLoadingBuilder;
import cn.micaiw.mobile.util.ActivityManager;

/**
 * Created by ckerv on 2018/1/6.
 */

public abstract class BaseActivity extends MSBaseActivity {

    private Unbinder mUnbinder;
    private DialogFragment mLoadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this);//使用butterknife

        ActivityManager.getInstance().pushActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        ActivityManager.getInstance().finishActivity(this);
    }

    protected void showLoading(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = DialogHelper.showProgress(getSupportFragmentManager(), msg, true);
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    /**
     * 打开loading
     */
    protected KyLoadingBuilder openLoadView(String showText) {
        KyLoadingBuilder mLoadingBuilder = new KyLoadingBuilder(this);
        mLoadingBuilder.setIcon(R.mipmap.loading_32dp);
        if (TextUtils.isEmpty(showText)) {
            mLoadingBuilder.setText("正在加载中...");
        } else {
            mLoadingBuilder.setText(showText);
        }
        //builder.setOutsideTouchable(false);
        //builder.setBackTouchable(true);
        mLoadingBuilder.show();
        return mLoadingBuilder;
    }

    protected void closeLoadView( KyLoadingBuilder mLoadingBuilder) {
        if (mLoadingBuilder != null) {
            mLoadingBuilder.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(BaseActivity.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

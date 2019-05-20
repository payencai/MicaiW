package cn.micaiw.mobile;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.nohttp.Logger;
import com.nohttp.NoHttp;

import cn.micaiw.mobile.constant.Constans;
import cn.micaiw.mobile.entity.PhoneUserEntity;
import cn.micaiw.mobile.http.HttpProxy;
import cn.micaiw.mobile.http.processor.OkHttpProcessor;
import cn.tool.ExceptionHandler;


/**
 * Created by pengying on 2017/3/9.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private static Context context;
    private PhoneUserEntity user;
    private static final String TAG = "MyApplication--->>>";
    public static final boolean isDebug = false;
    public static  int userId;
    public static  String userName;
    public static  String token;
    public static  String isLogin;
    //9882292d2fbc0111520159a3331a439a
    //最原始的
    //fa510b01e2fd5c6089b6b4aeac5d51e2

    @Override
    public void onCreate() {
        super.onCreate();
        //android  N 版本问题<处理7.0相册问题>
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        //方式一
        StrictMode.setVmPolicy(builder.build());
        //方式二
        //启用文件曝光检查，如果我们没有设置VmPolicy，这也是默认行为。
//        builder.detectFileUriExposure()

        context = getApplicationContext();
        instance = this;
        user = new PhoneUserEntity();
        Constans.setBankCardTagMap();//初始化银行卡的键值对
        //使用okhttp请求网络
        HttpProxy.init(new OkHttpProcessor());
        //初始化Nohttp
        initNohttp();
        //初始化Fresco
//        initFresco();
        //初始化第三方分享、登录
        initShareUtils();

        // 系统崩溃处理
//        initCrashHandler();

    }

    private void initShareUtils() {


    }

    //    private void initShareUtils() {
//        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
//        Config.DEBUG = true;
//        UMShareAPI.get(this);
//    }
//
//    {
//        PlatformConfig.setWeixin("wxa5fefb7214b6814d", "75a2411032debefca3451449a988304c");
//    }


    private void initCrashHandler() { // 系统崩溃处理
        ExceptionHandler crashHandler = ExceptionHandler.getInstance();
        crashHandler.init(this);
    }

    //初始化Fresco
    private void initFresco() {
//        Fresco.initialize(this);
    }

    //初始化Nohttp
    private void initNohttp() {
        Logger.setDebug(true); // 开启NoHttp调试模式。
        Logger.setTag("NoHttpSample"); // 设置NoHttp打印Log的TAG。
        NoHttp.initialize(this);

    }


    public static void restoreAppCxt(Context context) {
        if (MyApplication.context == null) {
            MyApplication.context = context.getApplicationContext();
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public PhoneUserEntity getUser() {
        return user;
    }

    public void setUser(PhoneUserEntity user) {
        this.user = user;
    }
}

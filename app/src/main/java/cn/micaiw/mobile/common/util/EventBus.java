package cn.micaiw.mobile.common.util;

/**
 * Created by ckerv on 2018/2/5.
 */

public class EventBus {

    public static org.greenrobot.eventbus.EventBus getInstance() {
        return org.greenrobot.eventbus.EventBus.getDefault();//对EventBus进行包装 方便以后修改配置
    }

    public static void register(Object object){
        if (!getInstance().isRegistered(object)){
            getInstance().register(object);
        }
    }
    public static void unregister(Object object){
        if (getInstance().isRegistered(object)){
            getInstance().unregister(object);
        }
    }
}

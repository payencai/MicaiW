package cn.micaiw.mobile.common.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cn.micaiw.mobile.common.entity.Result;
//import ikidou.reflect.TypeBuilder;


public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    /**
     * 转成json
     *
     * @param object
     * @return
     */
    public static String GsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    public static Object gsonToBean(String jsonStr, Class clazz) {
        Object object = null;
        if (gson != null) {
            object = gson.fromJson(jsonStr, clazz);
        }
        return object;
    }

    /**
     * 转成bean
     *
     * @param gsonString
     * @return
     */
    public static <T> Result<T> gsonToBean(String gsonString, Type type) {
        if (gson != null) {
            return gson.fromJson(gsonString, type);
        }
        return null;
    }

//    public static <T> Result<List<T>> fromJsonArray(String gsonStr, Class<T> clazz) {
//        if (gson != null){
//            Type type = TypeBuilder
//                    .newInstance(Result.class)
//                    .beginSubType(List.class)
//                    .addTypeParam(clazz)
//                    .endSubType()
//                    .build();
//            return gson.fromJson(gsonStr, type);
//        }
//        return null;
//    }


    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}
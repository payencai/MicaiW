package cn.micaiw.mobile.http;

import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/29.
 */

public interface IHttpProcessor {
    void post(String url, Map<String, Object> bodyParams, ICallBack callBack);

    void post(String url, Map<String, Object> headParams, Map<String, Object> bodyParams, ICallBack callBack);

    void get(String url, Map<String, Object> headParams, ICallBack callBack);

    void get(String url, Map<String, Object> headParams, Map<String, Object> tokenMap, ICallBack callBack);

    void post(String url, String token, File file, ICallBack callBack);

}

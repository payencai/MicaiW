package cn.micaiw.mobile.http;

import android.util.Log;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/29.
 */

public class HttpProxy implements IHttpProcessor {
    private static HttpProxy _instance;
    private static IHttpProcessor mIHttpProcessor = null;
    private Map<String, String> mParams = null;

    public static HttpProxy obtain() {
        synchronized (HttpProxy.class) {
            if (_instance == null) {
                _instance = new HttpProxy();
            }
        }
        return _instance;
    }

    private HttpProxy() {
        mParams = new HashMap<>();
    }

    public static void init(IHttpProcessor httpProcessor) {
        mIHttpProcessor = httpProcessor;
    }

    @Override
    public void post(String url, Map<String, Object> bodyParams, ICallBack callBack) {
        mIHttpProcessor.post(url, bodyParams, callBack);
    }

    @Override
    public void post(String url, Map<String, Object> headParams, Map<String, Object> bodyParams, ICallBack callBack) {
        mIHttpProcessor.post(url, headParams, bodyParams, callBack);
    }

    @Override
    public void get(String url, Map<String, Object> headParams, ICallBack callBack) {
//        mIHttpProcessor.get(url, headParams, callBack);
        String newUrl = appendParams(url, headParams);
        mIHttpProcessor.get(newUrl, null, callBack);
    }

    @Override
    public void get(String url, Map<String, Object> headParams, Map<String, Object> tokenMap, ICallBack callBack) {
        String newUrl = appendParams(url, headParams);
        mIHttpProcessor.get(newUrl, null, tokenMap, callBack);
    }

    @Override
    public void post(String url, String token, File imgFile, ICallBack callBack) {
        mIHttpProcessor.post(url, token, imgFile, callBack);
    }

    private static String appendParams(String url, Map<String, Object> params) {
        if (params == null || params.size() == 0) {
            return url;
        }
        List<String> keys = new ArrayList<String>(params.keySet());
        StringBuffer result = new StringBuffer();
        result.append(url + "?");
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = params.get(key) + "";
            result.append(buildKeyValue(key, value, false));
            result.append("&");
        }
        String tailKey = keys.get(keys.size() - 1);
        String tailValue = params.get(tailKey) + "";
        result.append(buildKeyValue(tailKey, tailValue, true));
        return result.toString();
    }

    private String appendParamss(String url, Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        StringBuffer result = new StringBuffer();
        result.append(url + "?");
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = params.get(key);
            result.append(buildKeyValue(key, value, false));
            result.append("&");
        }
        String tailKey = keys.get(keys.size() - 1);
        String tailValue = params.get(tailKey);
        result.append(buildKeyValue(tailKey, tailValue, true));
        return result.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }
}

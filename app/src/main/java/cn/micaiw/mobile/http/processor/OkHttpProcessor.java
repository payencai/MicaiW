package cn.micaiw.mobile.http.processor;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.micaiw.mobile.http.ICallBack;
import cn.micaiw.mobile.http.IHttpProcessor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by LT on 2017/12/12.
 */

public class OkHttpProcessor implements IHttpProcessor {


    public Handler mHandler = new Handler();
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final OkHttpClient sCclient = new OkHttpClient();

    @Override
    public void post(String url, Map<String, Object> bodyParams, final ICallBack callBack) {

        OkHttpClient client = new OkHttpClient();
        FormBody body = buildBody(bodyParams);
        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.OnSuccess(result);
                    }
                });
            }
        });

    }

    @Override
    public void post(String url, Map<String, Object> headParams, Map<String, Object> bodyParams, final ICallBack callBack) {
        OkHttpClient client = new OkHttpClient();
        FormBody body = buildBody(bodyParams);
        Request request = addHeaders(headParams).post(body).url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.OnSuccess(result);
                    }
                });
            }
        });
    }

    @Override
    public void get(String url, Map<String, Object> headParams, final ICallBack callBack) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = addHeaders(headParams).url(url).build();
        request.url();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String request = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.OnSuccess(request);
                    }
                });
            }
        });

    }

    @Override
    public void get(String url, Map<String, Object> headParams, Map<String, Object> tokenMap, final ICallBack callBack) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = addHeaders(tokenMap).url(url).build();
        request.url();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String request = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.OnSuccess(request);
                    }
                });
            }
        });
    }

    @Override
    public void post(String url, String token, File imgFile, final ICallBack callBack) {
        MultipartBody.Builder builder  = new MultipartBody.Builder()
                .addFormDataPart("img", imgFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, imgFile))
//                .addFormDataPart("portrait", imgFile.getName(), RequestBody.create(MediaType.parse("image/jpg"), imgFile))
                .setType(MultipartBody.FORM);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        MultipartBody requestBody = builder.build();
        Request request = addHeaders(tokenMap).url(url).post(requestBody).build();
//        Request request = new Request.Builder().url(url).post(requestBody).build();
        Call call = sCclient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.OnSuccess(result);
                    }
                });
            }
        });

    }

    private FormBody buildBody(Map<String, Object> bodyMap) {
        FormBody.Builder builder = new FormBody.Builder();
        if (bodyMap == null) {
            return builder.build();
        }

        for (String key : bodyMap.keySet()) {
            builder.add(key, bodyMap.get(key) + "");
        }
        return builder.build();
    }

    private Map<String, String> buildMap(Map<String, Object> sourceMap) {
        Map<String, String> hasMap = new HashMap<>();
        for (String key : sourceMap.keySet()) {
            hasMap.put(key, sourceMap.get(key) + "");
        }
        return hasMap;
    }

    private String appendParamss(String url, Map<String, Object> headMap) {
        StringBuffer requst = new StringBuffer();
        requst.append(url);
        if (headMap.size() == 0 || headMap == null) {
            return requst.toString();
        }
        requst.append("?");
        int size = headMap.size();
        int count = 0;
        for (String key : headMap.keySet()) {
            count++;
            requst.append(key);
            requst.append("=" + headMap.get(key));
            if (count != size) {
                requst.append("&");
            }
        }
        String newUrl = requst.toString();
        return newUrl;
    }

    private Request.Builder addHeaders(Map<String, Object> headMap) {
        Request.Builder builder = new Request.Builder();
        if (headMap == null) {
            return builder;
        }
        for (String key : headMap.keySet()) {
            builder.addHeader(key, headMap.get(key) + "");
        }
        return builder;
    }
}

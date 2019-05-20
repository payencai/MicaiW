package cn.micaiw.mobile.common.util;

import java.util.Map;

/**
 * Created by ckerv on 2018/2/9.
 */

public class UrlUtil {

    public static String formTotalUrl(String orginUrl, Map<String,String> params) {
        orginUrl = orginUrl + "?";
        for(Map.Entry<String, String> entry : params.entrySet()) {
            orginUrl += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return orginUrl.substring(0, orginUrl.length() - 1);
    }
}

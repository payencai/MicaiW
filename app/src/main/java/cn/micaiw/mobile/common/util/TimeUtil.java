package cn.micaiw.mobile.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ckerv on 2018/2/7.
 */

public class TimeUtil {


    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /***
     * 获得当前时间
     * @param format
     * @return
     */
    public static String getCurrentDate(String format)
    {
        String today = "";
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date now = new Date();
            today = sdf.format(now);
            sdf = null;
            now = null;
        } catch (Exception e) {
        }
        return today;
    }

}

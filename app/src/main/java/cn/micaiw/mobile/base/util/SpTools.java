package cn.micaiw.mobile.base.util;

import android.content.Context;
import android.content.SharedPreferences;


public class SpTools {

	//缓存文件名
	public static final String CONFIGFILE = "SP_CONFIGFILE";

	public static void setBoolean(Context context, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(
				CONFIGFILE, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();

	}

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(
				CONFIGFILE, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}

	public static void setString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(
				CONFIGFILE, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();

	}

	public static String getString(Context context, String key, String defValue) {
		SharedPreferences sp = context.getSharedPreferences(
				CONFIGFILE, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
}

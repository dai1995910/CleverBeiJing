package com.dwb.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ����SharePrefence�Ĺ��� 
 * @author admin
 *
 */
public class SharePrefenceUtils {
	public static String PREFERENCENAME = "config";
	
	/**
	 * ����boolean������ֵ
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setBooleanValue(Context context , String key , boolean value) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	/**
	 * ��ȡboolean������ֵ
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBooleanValue(Context context , String key , boolean defValue) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
		boolean result = sp.getBoolean(key, defValue);
		return result;
	}
	
	/**
	 * �����ַ���
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setString(Context context , String key , String value) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	/**
	 * ��ȡ�ַ���
	 * @param context
	 * @param keyString
	 * @param value
	 */
	public static String getString(Context context , String key , String defValue) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCENAME, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
}

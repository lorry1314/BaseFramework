package com.musejianglan.baseframework.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.musejianglan.baseframework.app.BaseApp;

/**
 * sharedPreference工具类V1.0(后期改进)
 * 
 * @author renxiaomin
 * @date 2014年8月6日 update 08/13 +getString(String key,String defaultStr)
 */
public class PreferenceUtil {

	private static SharedPreferences mSharedPreferences;

	private static synchronized SharedPreferences getPreferneces() {
		if (mSharedPreferences == null) {
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApp
					.mContext);
		}
		return mSharedPreferences;
	}

	/**
	 * 打印所有
	 */
	public static void print() {
		System.out.println(getPreferneces().getAll());
	}

	/**
	 * 清空保存在默认SharePreference下的所有数据
	 */
	public static void clear() {
		getPreferneces().edit().clear().commit();
	}

	/**
	 * 保存字符串
	 * 
	 * @return
	 */
	public static void putString(String key, String value) {
		getPreferneces().edit().putString(key, value).commit();
	}

	/**
	 * 读取字符串
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return getPreferneces().getString(key, null);

	}

	/**
	 * 读取字符串由默认值
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key, String defaultStr) {
		return getPreferneces().getString(key, defaultStr);

	}

	/**
	 * 保存整型值
	 * 
	 * @return
	 */
	public static void putInt(String key, int value) {
		getPreferneces().edit().putInt(key, value).commit();
	}

	/**
	 * 读取整型值
	 * 
	 * @param key
	 * @return
	 */
	public static int getInt(String key) {
		return getPreferneces().getInt(key, 0);
	}

	/**
	 * 保存布尔值
	 * 
	 * @return
	 */
	public static void putBoolean(String key, Boolean value) {
		getPreferneces().edit().putBoolean(key, value).commit();
	}

	public static void putLong(String key, long value) {
		getPreferneces().edit().putLong(key, value).commit();
	}

	public static long getLong(String key) {
		return getPreferneces().getLong(key, 0);
	}

	/**
	 * t 读取布尔值
	 * 
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key, boolean defValue) {
		return getPreferneces().getBoolean(key, defValue);

	}

	/**
	 * 移除字段
	 * 
	 * @return
	 */
	public static void removeString(String key) {
		getPreferneces().edit().remove(key).commit();
	}
}

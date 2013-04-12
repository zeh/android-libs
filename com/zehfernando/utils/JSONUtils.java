package com.zehfernando.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtils {

	public static String getString(JSONObject __object, String __key) {
		return getString(__object, __key, "");
	}

	public static String getString(JSONObject __object, String __key, String __default) {
		return (__object.isNull(__key)) ? __default : __object.optString(__key, __default);
	}

	public static long getLong(JSONObject __object, String __key) {
		return getLong(__object, __key, 0);
	}

	public static long getLong(JSONObject __object, String __key, long __default) {
		return (__object.isNull(__key)) ? __default : __object.optLong(__key, __default);
	}

	public static float getFloat(JSONObject __object, String __key) {
		return getFloat(__object, __key, 0);
	}

	public static float getFloat(JSONObject __object, String __key, float __default) {
		return (__object.isNull(__key)) ? __default : (float)__object.optDouble(__key, __default);
	}

	public static int getInt(JSONObject __object, String __key) {
		return getInt(__object, __key, 0);
	}

	public static int getInt(JSONObject __object, String __key, int __default) {
		return (__object.isNull(__key)) ? __default : __object.optInt(__key, __default);
	}

	public static boolean getBoolean(JSONObject __object, String __key) {
		return getBoolean(__object, __key, false);
	}

	public static boolean getBoolean(JSONObject __object, String __key, boolean __default) {
		return (__object.isNull(__key)) ? __default : __object.optBoolean(__key, __default);
	}

	public static long[] getLongArray(JSONObject __object, String __key) {
		return getLongArray(__object, __key, new long[] {});
	}

	public static long[] getLongArray(JSONObject __object, String __key, long[] __default) {
		if (__object.isNull(__key)) {
			return __default;
		} else {
			JSONArray array = __object.optJSONArray(__key);
			long[] list = new long[array.length()];
			for (int i = 0; i < array.length(); i++) {
				list[i] = array.optLong(i, 0);
			}
			return list;
		}
	}

	public static int[] getIntArray(JSONObject __object, String __key) {
		return getIntArray(__object, __key, new int[] {});
	}

	public static int[] getIntArray(JSONObject __object, String __key, int[] __default) {
		if (__object.isNull(__key)) {
			return __default;
		} else {
			JSONArray array = __object.optJSONArray(__key);
			int[] list = new int[array.length()];
			for (int i = 0; i < array.length(); i++) {
				list[i] = array.optInt(i, 0);
			}
			return list;
		}
	}

	public static JSONArray getArray(JSONObject __object, String __key) {
		return getArray(__object, __key, new JSONArray());
	}

	public static JSONArray getArray(JSONObject __object, String __key, JSONArray __default) {
		return (__object.isNull(__key)) ? __default : __object.optJSONArray(__key);
	}

}

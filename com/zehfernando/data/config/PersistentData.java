package com.zehfernando.data.config;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;

import com.zehfernando.utils.F;

public class PersistentData {

	// A proxy class for SharedPreferences with a simpler API

	// Constant properties
	protected static ArrayList<PersistentData> datas = new ArrayList<PersistentData>();

	// Properties
	protected String name;
	protected SharedPreferences preferences;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public PersistentData(Context __context, String __name) {
		name = __name;
		PersistentData.addInstance(this);
		preferences = __context.getSharedPreferences(__name, 0);
	}

	// ================================================================================================================
	// STATIC INTERFACE -----------------------------------------------------------------------------------------------

	protected static void addInstance(PersistentData __data) {
		datas.add(__data);
	}

	public static PersistentData getInstance(Context __context) {
		return getInstance(__context, "");
	}

	public static PersistentData getInstance(Context __context, String __name) {
		// TODO: use hashmap for speed?

		// Looks for one on the list first
		for (int i = 0; i < datas.size(); i++) {
			if (datas.get(i).getName().equals(__name)) return datas.get(i);
		}

		// Doesn't exist, create a new one and return it

		return new PersistentData(__context, __name);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void clear() {
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	public Boolean getBoolean(String __key) {
		return getBoolean(__key, false);
	}

	public Boolean getBoolean(String __key, Boolean __default) {
		return preferences.getBoolean(__key, __default);
	}

	public void putBoolean(String __key, Boolean __value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(__key, __value);
		editor.commit();
	}

	public String getString(String __key) {
		return getString(__key, "");
	}

	public String getString(String __key, String __default) {
		return preferences.getString(__key, __default);
	}

	public void putString(String __key, String __value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(__key, __value);
		editor.commit();
	}

	public void putJSONArray(String __key, JSONArray __array) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(__key, __array.toString());
		//F.log("====> SAVING ARRAY! " + __array.toString());
		editor.commit();
	}

	public JSONArray getJSONArray(String __key) {
		try {
			//F.log("===> READING ARRAY! " + preferences.getString(__key, ""));
			return new JSONArray(preferences.getString(__key, ""));
		} catch (JSONException __e) {
			F.warn("CANNOT READ JSON ARRAY!");
			return new JSONArray();
		}
	}

	public long getLong(String __key) {
		return getLong(__key, 0L);
	}

	public long getLong(String __key, long __default) {
		return preferences.getLong(__key, __default);
	}

	public void putLong(String __key, long __value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(__key, __value);
		editor.commit();
	}

	public float getFloat(String __key) {
		return getFloat(__key, 0);
	}

	public float getFloat(String __key, float __default) {
		return preferences.getFloat(__key, __default);
	}

	public void putFloat(String __key, float __value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putFloat(__key, __value);
		editor.commit();
	}

	public int getInt(String __key) {
		return getInt(__key, 0);
	}

	public int getInt(String __key, int __default) {
		return preferences.getInt(__key, __default);
	}

	public void putInt(String __key, int __value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(__key, __value);
		editor.commit();
	}

	public void remove(String __key) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(__key);
		editor.commit();
	}


	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getName() {
		return name;
	}
}

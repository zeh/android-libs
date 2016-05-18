package com.zehfernando.data.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.zehfernando.utils.F;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class PersistentData {

	// A proxy class for SharedPreferences with a simpler API

	// Constant properties
	protected static HashMap<String, PersistentData> _instances = new HashMap<>();

	// Properties
	protected String _name;
	protected SharedPreferences _preferences;


	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public PersistentData(Context context, String name) {
		_name = name;
		_preferences = context.getSharedPreferences(_name, 0);
		PersistentData.addInstance(this);
	}


	// ================================================================================================================
	// STATIC INTERFACE -----------------------------------------------------------------------------------------------

	protected static void addInstance(PersistentData data) {
		_instances.put(data.getName(), data);
	}

	public static PersistentData getInstance(Context context) {
		return getInstance(context, "");
	}

	/**
	 * Returns a PersistentData instance from the list, otherwise create a new one and return it
	 *
	 * @param context Context to be used
	 * @param name    Name (key) of the PersistentData instance to be retrieved
	 * @return A PersistentData instance
	 */
	public static PersistentData getInstance(Context context, String name) {
		//
		if (_instances.containsKey(name)) {
			return _instances.get(name);
		} else {
			return new PersistentData(context, name);
		}
	}


	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void clear() {
		SharedPreferences.Editor editor = _preferences.edit();
		editor.clear();
		editor.apply();
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return _preferences.getBoolean(key, defaultValue);
	}

	public void putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = _preferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}

	public String getString(String key) {
		return getString(key, "");
	}

	public String getString(String key, String defaultValue) {
		return _preferences.getString(key, defaultValue);
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor editor = _preferences.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public void putJSONArray(String key, JSONArray array) {
		SharedPreferences.Editor editor = _preferences.edit();
		editor.putString(key, array.toString());
		//F.log("====> SAVING ARRAY! " + array.toString());
		editor.apply();
	}

	public JSONArray getJSONArray(String key) {
		try {
			//F.log("===> READING ARRAY! " + _preferences.getString(key, ""));
			return new JSONArray(_preferences.getString(key, ""));
		} catch (JSONException e) {
			F.warn("CANNOT READ JSON ARRAY!");
			return new JSONArray();
		}
	}

	public long getLong(String key) {
		return getLong(key, 0L);
	}

	public long getLong(String key, long defaultValue) {
		return _preferences.getLong(key, defaultValue);
	}

	public void putLong(String key, long value) {
		SharedPreferences.Editor editor = _preferences.edit();
		editor.putLong(key, value);
		editor.apply();
	}

	public float getFloat(String key) {
		return getFloat(key, 0);
	}

	public float getFloat(String key, float defaultValue) {
		return _preferences.getFloat(key, defaultValue);
	}

	public void putFloat(String key, float value) {
		SharedPreferences.Editor editor = _preferences.edit();
		editor.putFloat(key, value);
		editor.apply();
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int defaultValue) {
		return _preferences.getInt(key, defaultValue);
	}

	public void putInt(String key, int value) {
		SharedPreferences.Editor editor = _preferences.edit();
		editor.putInt(key, value);
		editor.apply();
	}

	public void remove(String key) {
		SharedPreferences.Editor editor = _preferences.edit();
		editor.remove(key);
		editor.apply();
	}


	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getName() {
		return _name;
	}
}

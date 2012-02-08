package com.zehfernando.data.config;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.util.Log;

import com.redken.stylestation.ApplicationConstants;

public class PersistentData {

	// A proxy class for SharedPreferences with a simpler API

	// Constant properties
	protected static ArrayList<PersistentData> datas = new ArrayList<PersistentData>();

	// Properties
	protected String name;
	protected SharedPreferences preferences;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public PersistentData(String __name) {
		name = __name;
		PersistentData.addInstance(this);
		if (ApplicationConstants.context == null) Log.e("PersistentData", "Error! Context has not been initialized for SharedPreferences instantiation!");
		preferences = ApplicationConstants.context.getSharedPreferences(__name, 0);
	}

	// ================================================================================================================
	// STATIC INTERFACE -----------------------------------------------------------------------------------------------

	protected static void addInstance(PersistentData __data) {
		datas.add(__data);
	}

	public static PersistentData getInstance() {
		return getInstance("");
	}

	public static PersistentData getInstance(String __name) {
		// TODO: use hashmap for speed

		// Looks for one on the list first
		for (int i = 0; i < datas.size(); i++) {
			if (datas.get(i).getName().equals(__name)) return datas.get(i);
		}

		// Doesn't exist, create a new one and return it

		return new PersistentData(__name);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void putBoolean(String __key, Boolean __value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(__key, __value);
		editor.commit();
	}

	public Boolean getBoolean(String __key) {
		return getBoolean(__key, false);
	}

	public Boolean getBoolean(String __key, Boolean __default) {
		return preferences.getBoolean(__key, __default);
	}

	public void putString(String __key, String __value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(__key, __value);
		editor.commit();
	}

	public String getString(String __key) {
		return getString(__key, "");
	}

	public String getString(String __key, String __default) {
		return preferences.getString(__key, __default);
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getName() {
		return name;
	}
}

package com.zehfernando.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtils {

	public static boolean hasNetworkConnection(Context __context) {
		// Checks whether there's a valid network connection available
		// Requires permission: android.permission.ACCESS_NETWORK_STATE
		ConnectivityManager connectivityManager = (ConnectivityManager) __context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getActiveNetworkInfo() != null;
	}

}

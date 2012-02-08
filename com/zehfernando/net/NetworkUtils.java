package com.zehfernando.net;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkUtils {

	public static boolean hasNetworkConnection(Context __context) {
		// Whether a network connection exists or not.
		// Requires permission: android.permission.ACCESS_NETWORK_STATE
		ConnectivityManager connectivityManager = (ConnectivityManager) __context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getActiveNetworkInfo() != null;
	}
}

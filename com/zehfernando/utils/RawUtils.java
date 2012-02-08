package com.zehfernando.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class RawUtils {

	public static String getContentOfRawFile(Context __context, int __resourceId) {
		InputStream inputStream = __context.getResources().openRawResource(__resourceId);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		int i;

		try {
			i = inputStream.read();
			while (i != -1) {
				outputStream.write(i);
				i = inputStream.read();
			}
			inputStream.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return outputStream.toString();

	}

	public static int getResourceFromString(Context __context, String __resourceName) {
		// Converts "R.raw.something" to R.raw.something
		return __context.getResources().getIdentifier(__resourceName, "raw", __context.getPackageName());
	}
}

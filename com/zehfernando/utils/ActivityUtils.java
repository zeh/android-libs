package com.zehfernando.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsSpinner;

public class ActivityUtils {

	// http://stackoverflow.com/questions/1147172/what-android-tools-and-methods-work-best-to-find-memory-resource-leaks

	public static void unbindDrawables(View __view) {
		unbindDrawables(__view, false);
	}

	public static void unbindDrawables(View __view, boolean __skipGarbageCollection) {
		if (__view.getBackground() != null) {
			__view.getBackground().setCallback(null);
		}
		if (__view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) __view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) __view).getChildAt(i), true);
			}
			if (!(__view instanceof AbsSpinner) && !(__view instanceof AbsListView)) {
				((ViewGroup) __view).removeAllViews();
			}
		}

		if (!__skipGarbageCollection) System.gc();
	}
}

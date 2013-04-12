package com.zehfernando.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {
	public static void hideSoftKeyboard(View __anyView) {
		InputMethodManager mgr = (InputMethodManager) __anyView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(__anyView.getWindowToken(), 0);
	}
}

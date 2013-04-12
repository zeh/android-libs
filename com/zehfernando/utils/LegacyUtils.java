package com.zehfernando.utils;

import android.annotation.SuppressLint;
import android.content.Context;

public class LegacyUtils {

	// More shitty functions for supporting old versions

	@SuppressLint("NewApi")
	public static void copyTextToClibboard(Context __context, String __text) {
		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) __context.getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(__text);
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) __context.getSystemService(Context.CLIPBOARD_SERVICE);
			android.content.ClipData clip = android.content.ClipData.newPlainText("Clipboard", __text);
			clipboard.setPrimaryClip(clip);
		}
	}
}

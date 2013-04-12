package com.zehfernando.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;

public class LegacyViewUtils {

	private static final int VERSION_CODES_HONEYCOMB = 11; // Build.VERSION_CODES.HONEYCOMB

	// Shitty functions to provide methods to manipulate Views on old Android versions
	// When possible, apply transforms via native calls; when not, create shitty animations to simulate it
	//F.debug("Build SDK version is " + Build.VERSION.SDK_INT + ", higher than " + Build.VERSION_CODES.HONEYCOMB + ", using native call");

	public static void setAlpha(View __view, double __alpha) {
		setAlpha(__view, (float)__alpha);
	}

	public static void setAlpha(View __view, float __alpha) {
		if (Build.VERSION.SDK_INT >= VERSION_CODES_HONEYCOMB) {
			setAlphaNatively(__view, __alpha);
		} else {
			__view.clearAnimation();
			AlphaAnimation alpha = new AlphaAnimation(__alpha, __alpha);
			alpha.setDuration(0); // Make animation instant
			alpha.setFillAfter(true); // Tell it to persist after the animation ends
			__view.startAnimation(alpha);
		}
	}

	@TargetApi(VERSION_CODES_HONEYCOMB)
	private static void setAlphaNatively(View __view, float __alpha) {
		__view.setAlpha(__alpha);
	}

	public static void setTranslation(View __view, float __translationX, float __translationY) {
		if (Build.VERSION.SDK_INT >= VERSION_CODES_HONEYCOMB) {
			setTranslationNatively(__view, __translationX, __translationY);
		} else {
			TranslateAnimation translate = new TranslateAnimation(0, __translationX, 0, __translationY);
			translate.setDuration(0);
			translate.setFillAfter(true);
			__view.startAnimation(translate);
		}
	}

	@TargetApi(VERSION_CODES_HONEYCOMB)
	private static void setTranslationNatively(View __view, float __translationX, float __translationY) {
		__view.setTranslationX(__translationX);
		__view.setTranslationX(__translationY);
	}

}

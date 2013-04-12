package com.zehfernando.utils;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class AnimationUtils {

	public static void fadeIn(final View __view, int __time) {
		AlphaAnimation fader = new AlphaAnimation(0, 1);
		fader.setFillBefore(true);
		fader.setFillAfter(false);
		fader.setInterpolator(new AccelerateDecelerateInterpolator());
		fader.setDuration(__time);
		fader.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				__view.setVisibility(View.VISIBLE);
			}
			@Override
			public void onAnimationRepeat(Animation animation) { }
			@Override
			public void onAnimationEnd(Animation animation) { }
		});

		__view.clearAnimation();
		__view.startAnimation(fader);
		if (__view.getParent() != null) ((View)__view.getParent()).invalidate();
	}

	public static void fadeOut(final View __view, int __time) {
		AlphaAnimation fader = new AlphaAnimation(1, 0);
		fader.setFillBefore(true);
		fader.setFillAfter(false);
		fader.setInterpolator(new AccelerateDecelerateInterpolator());
		fader.setDuration(__time);
		fader.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) { }
			@Override
			public void onAnimationRepeat(Animation animation) { }
			@Override
			public void onAnimationEnd(Animation animation) {
				__view.setVisibility(View.GONE);
			}
		});

		__view.clearAnimation();
		__view.startAnimation(fader);
		if (__view.getParent() != null) ((View)__view.getParent()).invalidate();
	}
}

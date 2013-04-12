package com.zehfernando.display.animations;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class LayoutParamHeightAnimation extends Animation {

	// Properties
	private final ViewGroup target;
	private int startHeight;
	private final int targetHeight;
	private final ViewGroup.LayoutParams layoutParams;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LayoutParamHeightAnimation(ViewGroup __target, int __targetHeight) {
		target = __target;
		layoutParams = target.getLayoutParams();
		targetHeight = __targetHeight;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void initialize(int __width, int __height, int __parentWidth, int __parentHeight) {
		super.initialize(__width, __height, __parentWidth, __parentHeight);
		startHeight = target.getHeight();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	@Override
	protected void applyTransformation(float __interpolatedTime, Transformation __t) {
		layoutParams.height = Math.round(startHeight + (targetHeight - startHeight) * __interpolatedTime);
		target.setLayoutParams(layoutParams);
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}

	@Override
	public boolean willChangeTransformationMatrix() {
		return false;
	}
}

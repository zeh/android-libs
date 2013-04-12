package com.zehfernando.display.animations;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class LayoutParamMarginsAnimation extends Animation {

	// Constants
	public static final int UNDEFINED = -1876297629;

	// Properties
	private float lastInterpolatedValue;
	private final View target;
	private int startMarginLeft;
	private int startMarginTop;
	private int startMarginRight;
	private int startMarginBottom;
	private final int targetMarginLeft;
	private final int targetMarginTop;
	private final int targetMarginRight;
	private final int targetMarginBottom;
	private ViewGroup.MarginLayoutParams layoutParams;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LayoutParamMarginsAnimation(View __target, int __targetMarginLeft, int __targetMarginTop, int __targetMarginRight, int __targetMarginBottom) {
		lastInterpolatedValue = -1;
		target = __target;
		targetMarginLeft = __targetMarginLeft;
		targetMarginTop = __targetMarginTop;
		targetMarginRight = __targetMarginRight;
		targetMarginBottom = __targetMarginBottom;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void initialize(int __width, int __height, int __parentWidth, int __parentHeight) {
		super.initialize(__width, __height, __parentWidth, __parentHeight);
		layoutParams = (ViewGroup.MarginLayoutParams) target.getLayoutParams();
		startMarginLeft   = layoutParams.leftMargin;
		startMarginTop    = layoutParams.topMargin;
		startMarginRight  = layoutParams.rightMargin;
		startMarginBottom = layoutParams.bottomMargin;
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	@Override
	protected void applyTransformation(float __interpolatedTime, Transformation __t) {
		// Applies the transformation to the view's LayoutParams
		// This weird code is to minimize visual changes

		if (__interpolatedTime != lastInterpolatedValue) {
			boolean updatedAny = false;
			int val;

			if (targetMarginLeft != UNDEFINED) {
				val = Math.round(startMarginLeft + (targetMarginLeft - startMarginLeft) * __interpolatedTime);
				if (layoutParams.leftMargin != val) {
					layoutParams.leftMargin = val;
					updatedAny = true;
				}
			}
			if (targetMarginTop != UNDEFINED) {
				val = Math.round(startMarginTop + (targetMarginTop - startMarginTop) * __interpolatedTime);
				if (layoutParams.topMargin != val) {
					layoutParams.topMargin = val;
					updatedAny = true;
				}
			}
			if (targetMarginRight != UNDEFINED) {
				val = Math.round(startMarginRight + (targetMarginRight - startMarginRight) * __interpolatedTime);
				if (layoutParams.rightMargin != val) {
					layoutParams.rightMargin = val;
					updatedAny = true;
				}
			}
			if (targetMarginBottom != UNDEFINED) {
				val = Math.round(startMarginBottom + (targetMarginBottom - startMarginBottom) * __interpolatedTime);
				if (layoutParams.bottomMargin != val) {
					layoutParams.bottomMargin = val;
					updatedAny = true;
				}
			}
			//if (targetMarginRight != UNDEFINED)  F.log("===> SET "+ target+" @ "  + __interpolatedTime + " AS "  + layoutParams.rightMargin);
			// Why does it keep applying the param to the target after the animation is already finished?!

			if (updatedAny) target.setLayoutParams(layoutParams);

			lastInterpolatedValue = __interpolatedTime;
		}
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

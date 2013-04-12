package com.zehfernando.display.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;


public class ZFrameLayout extends FrameLayout {

	// Like a FrameLayout, but allows children to be disabled (so the whole layout is clickable)

	// Properties
	private boolean childrenEnabled = true;


	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public ZFrameLayout(Context __context) {
		super(__context);
	}

	public ZFrameLayout(Context __context, AttributeSet __attrs) {
		super(__context, __attrs);
	}

	public ZFrameLayout(Context __context, AttributeSet __attrs, int __defStyle) {
		super(__context, __attrs, __defStyle);
	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

	@Override
	public boolean onInterceptTouchEvent(MotionEvent __e) {
		if (!childrenEnabled) return true;
		return super.onInterceptTouchEvent(__e);
	}

	@Override
	public boolean onTouchEvent(MotionEvent __e) {
		// Called when a touch screen motion event occurs.
		return super.onTouchEvent(__e);
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public boolean getChildrenEnabled() {
		return childrenEnabled;
	}

	public void setChildrenEnabled(boolean __value) {
		childrenEnabled = __value;
	}

}

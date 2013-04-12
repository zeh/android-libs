package com.zehfernando.display.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.HorizontalScrollView;

public class ZHorizontalScrollView extends HorizontalScrollView {

	/*
	 * A modified version of HorizontalScrollView.
	 * . Adds a setScrollViewListener() method for capturing scrolling events.
	 * . Adds getMaxScrollX()
	 * . Adds getPosition()
	 */

	private ZHorizontalScrollViewListener scrollViewListener = null;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public ZHorizontalScrollView(Context __context) {
		super(__context);
	}

	public ZHorizontalScrollView(Context __context, AttributeSet __attrs) {
		super(__context, __attrs);
	}

	public ZHorizontalScrollView(Context __context, AttributeSet __attrs, int __defStyle) {
		super(__context, __attrs, __defStyle);
	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

	@Override
	protected void onScrollChanged(int __x, int __y, int __oldx, int __oldy) {
		super.onScrollChanged(__x, __y, __oldx, __oldy);
		Log.v("ZHorizontalScrollViewListener", "Scrolling from " + __oldx + " to " + __x);
		// Log.v("ZHorizontalScrollViewListener", "mIsBeingDragged = " + );
		if (scrollViewListener != null)
			scrollViewListener.onScrollChanged(this, __x, __y, __oldx, __oldy);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void setOnScrollViewListener(ZHorizontalScrollViewListener __scrollViewListener) {
		scrollViewListener = __scrollViewListener;
	}

	public interface ZHorizontalScrollViewListener {
		public void onScrollChanged(ZHorizontalScrollView scrollView, int x, int y, int oldx, int oldy);
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public int getMaxScrollX() {
		return getChildAt(0).getWidth() - getWidth();
	}

	public float getPositionX() {
		if (getMaxScrollX() == 0)
			return 0;
		return (float) getScrollX() / (float) getMaxScrollX();
	}

	// @Override
	// protected void onAnimationStart() {
	// Log.v("ZHorizontalScrollView", "Animation start");
	// super.onAnimationStart();
	// }
	//
	// @Override
	// protected void onAnimationEnd() {
	// Log.v("ZHorizontalScrollView", "Animation end");
	// super.onAnimationEnd();
	// }

	// @Override
	// public void scrollBy(int __x, int __y) {
	// Log.v("ZHorizontalScrollView", "scrollBy "+__x+", "+__y);
	// super.scrollBy(__x, __y);
	// }
	//
	// @Override
	// public void scrollTo(int __x, int __y) {
	// Log.v("ZHorizontalScrollView", "scrollTo "+__x+", "+__y);
	// super.scrollTo(__x, __y);
	// }
}
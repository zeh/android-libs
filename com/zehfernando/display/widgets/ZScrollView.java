package com.zehfernando.display.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ScrollView;

public class ZScrollView extends ScrollView {

	/*
	 * A better ScrollView, based on my previous ZHorizontalScrollView
	 * . Adds a setScrollViewListener() method for capturing scrolling events
	 * . Adds getMaxScrollY()
	 * . Adds getPosition() (0-1)
	 * . Adds a scrollToWithGuarantees() that scrolls even if you're trying to scroll to a position that is not available anymore (because a child object was just added)
	 */

	// Properties
	private int desiredScrollX = -1;
	private int desiredScrollY = -1;

	// Instances
	private OnGlobalLayoutListener gol;

	private ZScrollViewListener scrollViewListener = null;


	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public ZScrollView(Context __context) {
		super(__context);
	}

	public ZScrollView(Context __context, AttributeSet __attrs) {
		super(__context, __attrs);
	}

	public ZScrollView(Context __context, AttributeSet __attrs, int __defStyle) {
		super(__context, __attrs, __defStyle);
	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

	@Override
	protected void onScrollChanged(int __x, int __y, int __oldx, int __oldy) {
		super.onScrollChanged(__x, __y, __oldx, __oldy);
		//F.log("Scrolling from " + __oldy + " to " + __y);
		if (scrollViewListener != null) scrollViewListener.onScrollChanged(this, __x, __y, __oldx, __oldy);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void setOnScrollViewListener(ZScrollViewListener __scrollViewListener) {
		scrollViewListener = __scrollViewListener;
	}

	public interface ZScrollViewListener {
		public void onScrollChanged(ZScrollView scrollView, int x, int y, int oldx, int oldy);
	}

	public void scrollToWithGuarantees(int __x, int __y) {
		// REALLY Scrolls to a position
		// When adding items to a scrollView, you can't immediately scroll to it - it takes a while
		// for the new addition to cycle back and update the scrollView's max scroll... so we have
		// to wait and re-set as necessary

		scrollTo(__x, __y);

		desiredScrollX = -1;
		desiredScrollY = -1;

		if (getScrollX() != __x || getScrollY() != __y) {
			// Didn't scroll properly: will create an event to try scrolling again later

			if (getScrollX() != __x) desiredScrollX = __x;
			if (getScrollY() != __y) desiredScrollY = __y;

			if (gol == null) {
				gol = new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {

						int nx = desiredScrollX == -1 ? getScrollX() : desiredScrollX;
						int ny = desiredScrollY == -1 ? getScrollY() : desiredScrollY;

						desiredScrollX = -1;
						desiredScrollY = -1;

						scrollTo(nx, ny);
					}
				};

				getViewTreeObserver().addOnGlobalLayoutListener(gol);
			}
		}
	}


	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public int getMaxScrollY() {
		return getChildAt(0).getHeight() - getHeight();
	}

	public float getPositionY() {
		if (getMaxScrollY() == 0) return 0;
		return (float) getScrollY() / getMaxScrollY();
	}

}

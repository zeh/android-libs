package com.zehfernando.display.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ZViewPager extends ViewPager {

	// Like ViewPager, but with paging enabled/disabled
	// With help from http://blog.svpino.com/2011/08/disabling-pagingswiping-on-android.html

	// Properties
	private boolean pagingEnabled;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public ZViewPager(Context __context, AttributeSet __attrs) {
		super(__context, __attrs);

		init();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void init() {
		pagingEnabled = true;
	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

	@Override
	public boolean onTouchEvent(MotionEvent __event) {
		if (pagingEnabled) {
			return super.onTouchEvent(__event);
		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent __event) {
		if (pagingEnabled) {
			return super.onInterceptTouchEvent(__event);
			// Intercepts touch events when inside a scrollview
			// http://stackoverflow.com/questions/2646028/android-horizontalscrollview-within-scrollview-touch-handling
			// Uh, none of this is needed?
			//boolean intercepted = super.onInterceptTouchEvent(__event);
			//if (intercepted) getParent().requestDisallowInterceptTouchEvent(true); // Doesn't always work -- probably better to implement on the parent; see CurlFinderResultsActivity for example
			//return intercepted;

		}

		return false;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------


	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public void setPagingEnabled(boolean __enabled) {
		pagingEnabled = __enabled;
	}

}

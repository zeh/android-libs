package com.zehfernando.display.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ZViewPager extends ViewPager {
	
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
		}
 
		return false;
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public void setPagingEnabled(boolean __enabled) {
		pagingEnabled = __enabled;
	}
}

package com.zehfernando.display.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ScrollableLinearLayout extends LinearLayout {

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public ScrollableLinearLayout(Context context) {
		super(context);
	}

	public ScrollableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

//	public boolean onTouchEvent(MotionEvent event) {
//		//return false; // passes correctly; allows drag if clicked outside, doesn't work if clicked inside button
//		//return true; // doesn't work; button click works, but doesn't drag
//		return super.onTouchEvent(event);
//	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void setPressed(boolean pressed) {
		// Overrides setPressed to prevent dispatching
		// http://stackoverflow.com/questions/3858220/gallery-adapterview-child-drawable-state
		//super.setPressed(pressed);
	}
}

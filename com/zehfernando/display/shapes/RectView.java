package com.zehfernando.display.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class RectView extends View {

	// Properties
//	private int backgroundColor;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public RectView(Context __context) {
		super(__context);

		init();
	}

	public RectView(Context __context, AttributeSet __attrs) {
		super(__context, __attrs, 0);

		init();
	}

	public RectView(Context __context, AttributeSet __attrs, int __defStyle) {
		super(__context);
	
		init();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void onDraw(Canvas canvas) {
//		shape.getPaint().setColor(color | (Math.round(alpha * (float)255.0) << 24));
//		shape.setBounds(0, 0, getWidth(), getHeight());
//		shape.draw(canvas);
	}

	protected void init() {
//		shape = new ShapeDrawable(new RectShape());
//		shape.setBounds(0, 0, 100, 100);

		setDrawingCacheQuality(DRAWING_CACHE_QUALITY_LOW);
		setDrawingCacheEnabled(true);
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

//	public float getAlpha() {
//		Log.v("RectView", "getAlpha backgroundColor1 = " + backgroundColor);
//		Log.v("RectView", "getAlpha backgroundColor2 = " + (backgroundColor & 0xff000000));
//		Log.v("RectView", "getAlpha backgroundColor3 = " + ((backgroundColor & 0xff000000) >> 24));
//		return ((backgroundColor & 0xff000000) >> 24) / 255F;
//	}
//
//	public void setAlpha(float __alpha) {
//		Log.v("RectView", "setAlpha alpha = " + (Math.round(__alpha * 255F) & 0xff));
//		setBackgroundColor(((Math.round(__alpha * 255F) & 0xff) << 24) | (backgroundColor & 0xffffff));
//		//invalidate();
//	}
//
//	public void setAlpha(double __alpha) {
//		setAlpha(Math.round(__alpha));
//	}
//
//	@Override
//	public void setBackgroundColor(int __color) {
//		Log.v("RectView", "getAlpha setBackgroundColor = " + String.valueOf((long) __color));
//		backgroundColor = (int)__color;
//		super.setBackgroundColor((int)__color);
//
//		//Log.v("RectView", "Color set to " + __color);
//	}

//	public void getBackgroundColor(int __color) {
//		return ((ColorDrawable)getBackground()).getConstantState().mBaseC
//	}
}

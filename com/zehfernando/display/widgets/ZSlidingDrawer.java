package com.zehfernando.display.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.SlidingDrawer;

import com.zehfernando.utils.MathUtils;

public class ZSlidingDrawer extends SlidingDrawer {

	/*
	 * A better SlidingDrawer
	 * . Adds a setSlideListener() method for a listener that gets dispatched every time the sliding drawer changes the slide phase
	 */

	// Properties
	protected float lastSlidePhase = -1;					// 1 is opened, 0 is closed

	// Instances
	private OnSlideListener onSlideListener = null;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public ZSlidingDrawer(Context __context, AttributeSet __attrs) {
		super(__context, __attrs);
		evaluateSlidePhase();
	}

	public ZSlidingDrawer(Context __context, AttributeSet __attrs, int __defStyle) {
		super(__context, __attrs, __defStyle);
		evaluateSlidePhase();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	private void evaluateSlidePhase() {
		float slidePhase;

		if (isMoving()) {
			// Is moving, so need to find fractional value
			if (getHandle() != null) {
				int handleY = getHandle().getTop();
				int minY = 0;
				int maxY = getHeight() - getHandle().getHeight();
				slidePhase = 1-MathUtils.map(handleY, minY, maxY, 0, 1, true);
			} else {
				slidePhase = 0;
			}
		} else {
			// Not moving, value is integer
			slidePhase = isOpened() ? 1 : 0;
		}

		if (slidePhase != lastSlidePhase) {
			lastSlidePhase = slidePhase;
			dispatchOnSlide();
		}
	}

	private void dispatchOnSlide() {
		if (onSlideListener != null) onSlideListener.onSlide(lastSlidePhase);
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		evaluateSlidePhase();
		super.dispatchDraw(canvas);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void setOnSlideListener(OnSlideListener __onSlideListener) {
		onSlideListener = __onSlideListener;
	}

	public interface OnSlideListener {
		public void onSlide(float __slidePhase);
	}
}

package com.zehfernando.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class ZView extends View {

	// http://developer.android.com/guide/topics/ui/custom-components.html

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public ZView(Context context) {
		super(context);
	}

	public ZView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ZView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

	// Creation -------------------------------------------------------------------------------------------------------

	@Override
	protected void onFinishInflate() {
		// Called after a view and all of its children has been inflated from XML.
		super.onFinishInflate();
	}

	// Layout ---------------------------------------------------------------------------------------------------------

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Called to determine the size requirements for this view and all of its children.
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// Called when this view should assign a size and position to all of its children.
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// Called when the size of this view has changed.
		super.onSizeChanged(w, h, oldw, oldh);
	}

	// Drawing --------------------------------------------------------------------------------------------------------

	@Override
	protected void onDraw(Canvas canvas) {
		// Called when the view should render its content.
		super.onDraw(canvas);
	}

	// Event processing -----------------------------------------------------------------------------------------------

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Called when a new key event occurs.
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// Called when a key up event occurs.
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		// Called when a trackball motion event occurs.
		return super.onTrackballEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Called when a touch screen motion event occurs.
		return super.onTouchEvent(event);
	}

	// Focus ----------------------------------------------------------------------------------------------------------

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
		// Called when the view gains or loses focus.
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// Called when the window containing the view gains or loses focus.
		super.onWindowFocusChanged(hasWindowFocus);
	}

	// Attaching ------------------------------------------------------------------------------------------------------

	@Override
	protected void onAttachedToWindow() {
		// Called when the view is attached to a window.
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		// Called when the view is detached from its window.
		super.onDetachedFromWindow();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		// Called when the visibility of the window containing the view has changed.
		super.onWindowVisibilityChanged(visibility);
	}
}

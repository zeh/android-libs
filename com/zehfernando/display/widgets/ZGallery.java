package com.zehfernando.display.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Transformation;
import android.widget.Gallery;

import com.zehfernando.utils.MathUtils;

public class ZGallery extends Gallery {

	// Properties
	protected float _unselectedAlpha;
	protected float _positionX; // Current position as a float from 0 [first item focused] to (number of items-1) [last item focused]

	// Properties
	protected boolean isPressed;
	protected float startPressX;
	protected float startPressY;
	protected boolean isDragging;

	protected int oldL;						// Properties to save the old scroll pos
	protected int oldT;

	protected OnScrollZGalleryListener onScrollZGalleryListener;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public ZGallery(Context context) {
		super(context);

		init();
	}

	public ZGallery(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public ZGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void init() {
		setFadingEdgeLength(0);
		setSpacing(0);
		setGravity(Gravity.CENTER);
	}

	protected float getPositionXInternal() {
		// Returns the current position in item units, as a float from 0 to (number of items-1)
		if (getMaxPositionX() <= 0) return 0;

		return MathUtils.map(getActualScrollX(), 0, getMaxScrollX(), 0, getMaxPositionX());
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		t.clear();
		// t.setAlpha(child == mSelectedChild ? 1.0f : mUnselectedAlpha);
		// Set the alpha based on how much out the screen it is
		t.setAlpha(MathUtils.map(Math.abs(child.getId() - getPositionX()), 0, 1, 1, getUnselectedAlpha()));
		return true;
	}

	protected void dispatchOnScrollZGallery(int __l, int __t, int __oldl, int __oldt) {
		if (onScrollZGalleryListener != null) onScrollZGalleryListener.onScrollZGallery(this, __l, __t, __oldl, __oldt);
	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

	 @Override
	protected void onScrollChanged(int __l, int __t, int __oldl, int __oldt) {
		 // Error: never called normally, so fired from computeScroll()
//		 Log.v("ZGallery", "---------------------------------------------------------------");
//		 Log.v("ZGallery", "Scrolling from " + __oldl + " to " + __l);
//		 Log.v("ZGallery", "getWidth = " + getWidth());
//		 Log.v("ZGallery", "getPositionX = " + getPositionX());
//		 Log.v("ZGallery", "getSelectedItemId = " + getSelectedItemId());
//		 Log.v("ZGallery", "getActualScrollX = " + getActualScrollX());
//		 Log.v("ZGallery", "getMaxScrollX = " + getMaxScrollX());
//		 Log.v("ZGallery", "getPositionX = " + getPositionX());
//		 Log.v("ZGallery", "getMaxPositionX = " + getMaxPositionX());
		 super.onScrollChanged(__l, __t, __oldl, __oldt);

		 dispatchOnScrollZGallery(__l, __t, __oldl, __oldt);
	 }

	// @Override
	// public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	// // Only called during a drag
	// return super.onScroll(e1, e2, distanceX, distanceY);
	// }

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// Called at the very start
		super.onLayout(changed, l, t, r, b);
//		Log.v("ZGallery", "Layout: " + l + ", " + t + ", " + r + ", " + b);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//		Log.v("ZGallery", "onFling velocityX = " + velocityX);
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent __e) {
		// Intercepts all touch screen motion events.  This allows you to watch events as they are dispatched to your children, and take ownership of the current gesture at any point.
		// Return true to steal motion events from the children and have them dispatched to this ViewGroup through onTouchEvent().
		// The current target will receive an ACTION_CANCEL event, and no further messages will be delivered here.

		//return super.onInterceptTouchEvent(__e); // super always returns false

		// If this function returns TRUE, NO children get dragging events. This only happens
		// the first interception (mouse down); if true is returned, nothing is intercepted anymore, and
		// events are passed to onTouchEvent directly.
		// If FALSE is returned, this may be called again, but only if there's a children receiving the
		// events instead of this.
		// In sum, once onTouchEvent is called here, onInterceptTouchEvent is not called anymore.

//		Log.v("ZGallery", "INTERCEPT event --> " + __e.getAction() + ", isPressed = " + isPressed + ", isDragging = " + isDragging);

		//super.onTouchEvent(__e);
		return evaluateTouchEvent(__e);
	}

	@Override
	public boolean onTouchEvent(MotionEvent __e) {
//		Log.v("ZGallery", "MAIN event --> " + __e.getAction() + ", isPressed = " + isPressed + ", isDragging = " + isDragging);
		evaluateTouchEvent(__e);
		return super.onTouchEvent(__e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.v("ZGallery", "SINGLE TAP UP");
		return super.onSingleTapUp(e);
	}

	protected boolean evaluateTouchEvent(MotionEvent __e) {
		float dragDeltaX;
		float dragDeltaY;

		// If dragging for more than this amount of pixels, means it's a scroll
		float dragThreshold = ViewConfiguration.get(getContext()).getScaledTouchSlop();

		switch (__e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// Pressing...
				isPressed = true;
				startPressX = __e.getX();
				startPressY = __e.getY();
//				Log.v("ZGallery", "    pressing");
				break;
			case MotionEvent.ACTION_MOVE:
				// Moving...
				if (isPressed && !isDragging) {
					dragDeltaX = __e.getX() - startPressX;
					dragDeltaY = __e.getY() - startPressY;
//					Log.v("ZGallery", "    moving: " + dragDeltaX + ", " + dragDeltaY);

					if (Math.abs(dragDeltaX) > dragThreshold || Math.abs(dragDeltaY) > dragThreshold) {
						// Moved for too long, means it's dragging!
//						Log.v("ZGallery", "    DRAGGING!");

						// Inject click from correct position so superclass knows how to drag
						MotionEvent me = MotionEvent.obtain(__e);
						me.setAction(MotionEvent.ACTION_DOWN);
						me.setLocation(__e.getX() - dragDeltaX, __e.getY() - dragDeltaY);
						super.onTouchEvent(me);

						isDragging = true;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				// Releasing
//				Log.v("ZGallery", "    up");
				if (isPressed) {
					isPressed = false;
					// Let go while pressed
					if (isDragging) {
						// Was dragging, so just go back
						isDragging = false;
//						Log.v("ZGallery", "    NOT DRAGGING anymore");
					} else {
						// Was not dragging, so trigger a click!
//						Log.v("ZGallery", "    CLICK!");
					}
				}
				break;
		}

		if (!isDragging) {
			// Still passes it on
			return false;
		}

		return true;
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// //return super.onTouchEvent(event);
	// return true;
	// }

	// @Override
	// public void invalidate() {
	// // Called every time a new redraw is necessary (several times on every frame)
	// Log.v("ZGallery", "invalidate");
	// super.invalidate();
	// }

	// @Override
	// public void invalidate(int l, int t, int r, int b) {
	// // Never called?
	// Log.v("ZGallery", "invalidate L = " + l + ", R = " + r);
	// super.invalidate(l, t, r, b);
	// }

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

//	public void useScrollingHack() {
//		// Scrolling hack so scrollToPrevious/scrollToNext will work
//		setSpacing(-1);
//	}

	@Override
	public void computeScroll() {
		// Called every time scroll must be computed (every frame)
		// Log.v("ZGallery", "computeScroll ---------------------------------------------");

		// Log.v("ZGallery", "onScroll distanceX = " + distanceX); // Actual screen delta, not scroll
		// Log.v("ZGallery", "onScroll distanceY = " + distanceY); // Actual screen delta, not scroll
		// Log.v("ZGallery", "scrollX = " + getScrollX()); // always 0
		// Log.v("ZGallery", "getLeftPaddingOffset = " + getLeftPaddingOffset()); // always 0
		// Log.v("ZGallery", "left = " + getLeft()); // always 0
		// Log.v("ZGallery", "c0 left = " + getChildAt(0).getLeft()); // useless?
		// Log.v("ZGallery", "c0 width = " + getChildAt(0).getWidth()); // 480
		// Log.v("ZGallery", "width = " + getWidth()); // actual view size width (480)
		// Log.v("ZGallery", "children = " + getChildCount()); // 2
		// Log.v("ZGallery", "measured width = " + getMeasuredWidth());
		// Rect newRect = new Rect();
		// getDrawingRect(newRect);
		// Log.v("ZGallery", "drawing rectangle = " + newRect);
		// Log.v("ZGallery", "selected item pos = " + getSelectedItemPosition()); // Get current item
		// Log.v("ZGallery", "computeHorizontalScrollExtent = " + computeHorizontalScrollExtent()); // always 1 (View:
		// getWidth())
		// Log.v("ZGallery", "computeHorizontalScrollOffset = " + computeHorizontalScrollOffset()); // always same as
		// getSelectedItemPosition() (View: mScrollX)
		// Log.v("ZGallery", "computeHorizontalScrollRange = " + computeHorizontalScrollRange()); // always 3 (View:
		// getWidth())
		// Log.v("ZGallery", "delta = " + String.valueOf(e2.getX() - e1.getX())); // Total scroll delta for the current
		// drag

		// Log.v("ZGallery", "c0 left = " + getChildAt(0).getLeft()); // X of the item that is at the left side of the
		// screen
		// Log.v("ZGallery", "selected item pos = " + getSelectedItemPosition()); // Get current item
		// Log.v("ZGallery", "selected item = " + getSelectedItem());
		// Log.v("ZGallery", "getPositionX = " + getPositionX());
		// Log.v("ZGallery", "id = " + getSelectedItemId());
		// Log.v("ZGallery", "left children id = " + getChildAt(0).getId());
		// Log.v("ZGallery", "current animation = " + String.valueOf(mCurrentAnimation));

		// Recalculate position X, in item units
		_positionX = getPositionXInternal();

		int l = getActualScrollX();
		int t = 0;
		onScrollChanged(l, t, oldL, oldT);

		oldL = l;
		oldT = t;
	}

	public void setOnScrollZGalleryListener(OnScrollZGalleryListener __onScrollListener) {
		onScrollZGalleryListener = __onScrollListener;
	}

	// ================================================================================================================
	// INTERFACES -----------------------------------------------------------------------------------------------------

	public interface OnScrollZGalleryListener {
		public void onScrollZGallery(ZGallery __gallery, int __l, int __t, int __oldl, int __oldt);
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public int getActualScrollX() {
		// Return the actual scrolled x position (in pixels)

		// No position if there are no elements
		if (getMaxPositionX() <= 0) return 0;

		// Get total width
		int w = getWidth();

		// Get the position of the left element
		int leftPos = getChildAt(0).getLeft();

//		Log.v("ZGallery - gs", "getLeft = " + getChildAt(0).getLeft());

		// Get the position on screen of the selected item
		// TODO: this is a crappy solution because it depends on id, which must be set on the adapter. Find a different/simpler solution?
		int currPos = getChildAt(0).getId() == getSelectedItemId() ? leftPos : leftPos + w;

//		Log.v("ZGallery - gs", "currPos = " + currPos);
//		Log.v("ZGallery - gs", "getId() = " + getChildAt(0).getId());
//		Log.v("ZGallery - gs", "getSelectedItemId() = " + getSelectedItemId());

		currPos -= getSelectedItemPosition() * w;

		return -currPos;
	}

	public int getMaxScrollX() {
		// Return the maximum scroll x (in pixels)
		// Transform the current position in relative position
		//float screenPos = MathUtils.map(-getActualScrollX(), 0, getWidth(), 0, 1);

		//return getSelectedItemPosition() - screenPos;

		return getWidth() * getMaxPositionX();
	}

	@Override
	public void setUnselectedAlpha(float unselectedAlpha) {
		_unselectedAlpha = unselectedAlpha;
		super.setUnselectedAlpha(unselectedAlpha);
	}

	public float getUnselectedAlpha() {
		return _unselectedAlpha;
	}

	public float getPositionX() {
		// Get the current item position in item units (0-n)
		return _positionX;
	}

	public int getMaxPositionX() {
		// Get the maximum position x in item units (0-n)
		if (getChildCount() <= 1) return 0;
		return computeHorizontalScrollRange() - 1;
	}

//	protected void fixFocusToScroll() {
//		Log.v("ZGallery", "----------");
//
//		View currItem = getChildAt(0).getId() == getSelectedItemId() ? getChildAt(0) : getChildAt(1);
//		//currItem.requestFocus();
//		//Log.v("ZGallery", " hasFocus === " + hasFocus()); // Always false
//		//Log.v("ZGallery", " parent = " + currItem.getParent()); // Always ZGallery
//		//Log.v("ZGallery", " current focus ==== " + ((Activity)getContext()).getCurrentFocus()); // Always null
//
//		Log.v("ZGallery", " mItemCount ==== " + computeHorizontalScrollRange());
//		Log.v("ZGallery", " mSelectedPosition ==== " + computeHorizontalScrollOffset());
//
//	}

	public void scrollToPrevious() {
		onFling(null, null, getWidth() * 4, 0);

		// This is stupid, but works well, but only works when the gallery has been focused before
		//photoGallery.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, new KeyEvent(0, 0));

		// Ugh. This works, but it's slower than the onKeyDown solution
		//photoGallery.onFling(null, null, photoGallery.getWidth() * 3, 0);

		// This should work, but doesn't
		//showPhoto(currentPosition - 1);

	}

	public void scrollToNext() {
		onFling(null, null, -getWidth() * 4, 0);
	}

//	protected void showPhoto(int __position) {
//		int newPos = MathUtils.clamp(__position, 0, photosCursor.getCount()-1);
//		if (newPos != currentPosition) {
//			//Log.v("LookbookPhotoActivity", "Showing photo " + newPos);
//			// This is not working (not animating)
//			photoGallery.setSelection(newPos, true);
//		}
//	}

	/*
    boolean movePrevious() {
        if (mItemCount > 0 && mSelectedPosition > 0) {
            scrollToChild(mSelectedPosition - mFirstPosition - 1);
            return true;
        } else {
            return false;
        }
    }

    boolean moveNext() {
        if (mItemCount > 0 && mSelectedPosition < mItemCount - 1) {
            scrollToChild(mSelectedPosition - mFirstPosition + 1);
            return true;
        } else {
            return false;
        }
    }
	 */
}

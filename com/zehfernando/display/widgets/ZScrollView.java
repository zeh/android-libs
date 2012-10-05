package com.zehfernando.display.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.zehfernando.utils.F;

// Only scrolls vertically, allowing horizontal scroll events to be passed to children
// http://stackoverflow.com/questions/2646028/android-horizontalscrollview-within-scrollview-touch-handling

// TODO: don't use this. Not working.

public class ZScrollView extends ScrollView {

	private float xDistance, yDistance, lastX, lastY;

	public ZScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		F.log("intercepting...");
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				xDistance = yDistance = 0f;
				lastX = ev.getX();
				lastY = ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				final float curX = ev.getX();
				final float curY = ev.getY();
				xDistance += Math.abs(curX - lastX);
				yDistance += Math.abs(curY - lastY);
				lastX = curX;
				lastY = curY;

				F.log("xDistance = " + xDistance + ", yDistance = " + yDistance);
				if (xDistance > yDistance) return false;
		}

		return super.onInterceptTouchEvent(ev);
	}
}

/*ScrollView {
private final GestureDetector mGestureDetector;
View.OnTouchListener mGestureListener;

public ZScrollView(Context context, AttributeSet attrs) {
	super(context, attrs);
	mGestureDetector = new GestureDetector(new YScrollDetector());
	setFadingEdgeLength(0);
}

@Override
public boolean onInterceptTouchEvent(MotionEvent ev) {
	F.log("intercepting...");
	return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
}

// Return false if we're scrolling in the x direction
class YScrollDetector extends SimpleOnGestureListener {
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		F.log("distance X = " + distanceX + ", distanceY = " + distanceY);
		if(Math.abs(distanceY) > Math.abs(distanceX)) {
			F.log("  X is higher");
			return true;
		}
		F.log("  X is lower, doesn't intercept");
		return false;
	}
}
}*/
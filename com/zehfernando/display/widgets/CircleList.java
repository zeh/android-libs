package com.zehfernando.display.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

public class CircleList extends View {

	protected ShapeDrawable bgCircle;
	protected int numCircles;
	protected float circleRadius;
	protected float circleDistance;
	protected float position;
	protected boolean roundPosition;
	protected boolean hideWhenSingleCircle;
	protected int color;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public CircleList(Context context) {
		super(context);

		init();
	}

	public CircleList(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		init();
	}

	public CircleList(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);

		init();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void init() {
		setDefaultProperties();
		createAssets();
	}

	protected void setDefaultProperties() {
		position = 0;
		numCircles = 1;
		roundPosition = true;
		circleRadius = 6 * getResources().getDisplayMetrics().density;
		circleDistance = Math.round(17 * getResources().getDisplayMetrics().density);
		hideWhenSingleCircle = true;
		color = 0xffffff;

//		Log.v("CircleList", "init");
	}

	protected void createAssets() {
		bgCircle = new ShapeDrawable(new OvalShape());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float w = getWidth();
		float h = getHeight();
		float cx;
		float cy = (float) (h * 0.5);
		int i;

		float usePos = roundPosition ? Math.round(position) : position;

		//Log.v("CircleList", "Drawing list with " + numCircles + " circles, at position " + position);

		// DisplayMetrics metrics = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// Draw base circles
		bgCircle.getPaint().setColor(color);
		bgCircle.getPaint().setAlpha(51);

		for (i = 0; i < numCircles; i++) {
			cx = (float) (w * 0.5 + i * circleDistance - ((numCircles - 1) * circleDistance) / 2.0);
			bgCircle.setBounds(Math.round(cx - circleRadius), Math.round(cy - circleRadius), Math.round(cx + circleRadius), Math.round(cy + circleRadius));
			bgCircle.draw(canvas);
		}

		// Draw selector
		bgCircle.getPaint().setColor(color);
		bgCircle.getPaint().setAlpha(255);

		cx = (float) (w * 0.5 + usePos * circleDistance - ((numCircles - 1) * circleDistance) / 2.0);
		bgCircle.setBounds(Math.round(cx - circleRadius), Math.round(cy - circleRadius), Math.round(cx + circleRadius), Math.round(cy + circleRadius));
		bgCircle.draw(canvas);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public float getPosition() {
		return position;
	}

	public void setPosition(float __position) {
		position = __position;
		invalidate();
	}

	public void setColor(int __color) {
		color = __color;
		invalidate();
	}

	public int getNumCircles() {
		return numCircles;
	}

	public void setNumCircles(int __numCircles) {
		numCircles = __numCircles;
		invalidate();

		if (hideWhenSingleCircle) setVisibility(numCircles > 1 ? VISIBLE : INVISIBLE);
	}

	public float getCircleRadius() {
		return circleRadius;
	}

	public void setCircleRadius(float __circleRadius) {
		circleRadius = __circleRadius;
		invalidate();
	}

	public float getCircleDistance() {
		return circleDistance;
	}

	public void setCircleDistance(float __circleDistance) {
		circleDistance = __circleDistance;
		invalidate();
	}

	public int getBestWidth() {
		return (Math.round((numCircles + 1) * circleDistance));
	}

	public int getBestHeight() {
		return Math.round(circleDistance * 3);
	}
}
package com.zehfernando.display.shapes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {

	// Properties
	protected int color;				// 0xffffff
	protected float alpha;				// 0-1

	// Instances
	protected ShapeDrawable circle;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public CircleView(Context context) {
		super(context);

		init();
	}

	public CircleView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		init();
	}

	public CircleView(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);

		init();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void init() {
		color = 0xffffff;
		alpha = 1;

		circle = new ShapeDrawable(new OvalShape());

		setDrawingCacheEnabled(true);
	}

	protected void onDraw(Canvas canvas) {
		float w = getWidth();
		float h = getHeight();
		float cx = w * 0.5F;
		float cy = h * 0.5F;
		float r = Math.min(cx,  cy);

		circle.getPaint().setColor(color);
		circle.getPaint().setAlpha(Math.round(alpha * 255));

		circle.setBounds(Math.round(cx - r), Math.round(cy - r), Math.round(cx + r), Math.round(cy + r));
		circle.draw(canvas);
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		setMeasuredDimension(100, 100);
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//	}

	@Override
	protected int getSuggestedMinimumHeight() {
		return 100;
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		return 100;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void setColor(int __color) {
		color = __color;
		invalidate();
	}

	public int getColor() {
		return color;
	}

	public void setAlpha(float __alpha) {
		alpha = __alpha;
		invalidate();
	}

	public float getAlpha() {
		return alpha;
	}
}
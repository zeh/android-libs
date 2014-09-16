package com.zehfernando.display.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.zehfernando.data.geom.Point;
import com.zehfernando.utils.MathUtils;

public class SwitchView extends View {

	// Properties
	private float _currentState = 0;							// 0-1; 0 = off, 1 = on
	private boolean _leftIsOn = true;						// If true, "on" (_currentState = 1) is when the button is on the left
	private boolean _isChecked = false;							// Actual value

	private int finalWidth;
	private int finalHeight;

	private float buttonScale;
	private float buttonWidth;

	private boolean isPointerDown;
	private boolean isDragging;
	private Point pointerDownPoint;

	// Instances
	private Bitmap bitmapBackground;							// Background
	private Bitmap bitmapButton;								// Button that moves

	private Paint paintBitmap;
	private Matrix matrixBackground;
	private Matrix matrixButton;

	private Paint paintText;

	private Typeface fontTypeface;
	private int fontColor;
	private float fontSize;
	private float fontMargin;
	private String textOn;
	private String textOff;

	private OnChangeListener onChangeListener;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public SwitchView(Context context) {
		super(context);
		init();
	}

	public SwitchView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		init();
	}

	public SwitchView(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);
		init();
	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

	@Override
	protected void onMeasure(int __widthMeasureSpec, int __heightMeasureSpec) {
		if (bitmapBackground != null && bitmapButton != null) {
			// Set the measured size to be the same as the background size
			setMeasuredDimension(finalWidth, finalHeight);
		} else {
			super.onMeasure(__widthMeasureSpec, __heightMeasureSpec);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent __event) {
		// Testing... moves needle

		if (!isEnabled()) return false;

		switch (__event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!isPointerDown) onPointerDown(__event.getX(), __event.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				if (isPointerDown) onPointerMove(__event.getX(), __event.getY());
				break;
			case MotionEvent.ACTION_UP:
				if (isPointerDown) onPointerUp(__event.getX(), __event.getY());
				break;
			case MotionEvent.ACTION_CANCEL:
				if (isPointerDown) onPointerCancel(__event.getX(), __event.getY());
				break;
		}

		return true;
	}

	@Override
	protected void onDraw(Canvas __canvas) {
		drawSwitch(__canvas);
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	private void init() {
	}

	private void initImageResources() {
		// Prepares necessary rendering data from bitmaps

		float assetScale = getResources().getDisplayMetrics().density * 0.5f; // Assume the asset has a scale of 320dpi

		finalWidth = Math.round(bitmapBackground.getWidth() * assetScale);
		finalHeight = Math.round(bitmapBackground.getHeight() * assetScale);

		//F.log("WxH read as " + finalWidth + "x" + finalHeight);

		// Other cached instances for speed
		paintBitmap = new Paint();
		paintBitmap.setFilterBitmap(true);

		matrixBackground = new Matrix();
		matrixBackground.postScale((float)finalWidth / (float)bitmapBackground.getWidth(), (float)finalHeight / (float)bitmapBackground.getHeight());

		// Button is assumed to scale up to the max height and always align left and right when animating the switch
		buttonScale = (float)finalHeight / (float)bitmapButton.getHeight();
		buttonWidth = buttonScale * bitmapButton.getWidth();

		matrixButton = new Matrix(); // Scaled later, when drawing
	}

	private void initFontResources() {
		// Prepares necessary rendering data from fonts

		paintText = new Paint();
		paintText.setColor(fontColor);
		paintText.setStyle(Paint.Style.FILL);
		paintText.setTypeface(fontTypeface);
		paintText.setTextSize(fontSize);
		paintText.setTextAlign(Align.CENTER);
		paintText.setAntiAlias(true);
	}

	private void drawSwitch(Canvas __canvas) {
		// Draws the entire view

		if (bitmapBackground != null) {
			// Draws background
			__canvas.drawBitmap(bitmapBackground, matrixBackground, paintBitmap);
		}

		if (fontTypeface != null && textOn != null) {
			// Draws text
			float onPhase = MathUtils.map(_currentState, 0.5f, 1, 0, 1, true);
			float offPhase = MathUtils.map(_currentState, 0, 0.5f, 1, 0, true);

			String text = "";
			float textAlpha = 0;
			float textX = 0;
			float centerLeft = MathUtils.map(0.5f, 0, 1, fontMargin, finalWidth - buttonWidth); // Center of the text when the button is on the right
			float centerRight = MathUtils.map(0.5f, 0, 1, buttonWidth, finalWidth - fontMargin); // Center of the text when the button is on the right

			if (onPhase > 0) {
				text = textOn;
				textAlpha = onPhase;
				textX = _leftIsOn ? centerRight : centerLeft;
			}
			if (offPhase > 0) {
				text = textOff;
				textAlpha = offPhase;
				textX = _leftIsOn ? centerLeft: centerRight;
			}

			if (textAlpha > 0) {
				Rect textBounds = new Rect();
				paintText.getTextBounds(text, 0, text.length(), textBounds);
				paintText.setAlpha(Math.round(textAlpha * 255f));

				__canvas.drawText(text, textX, (finalHeight + textBounds.height() * 0.9f)/2f, paintText);

				//F.log("drawing text " + text + " as " + textAlpha);
			}
		}

		if (bitmapButton != null) {
			// Draws button
			float buttonX = MathUtils.map(_leftIsOn ? 1 - _currentState : _currentState, 0, 1, 0, finalWidth - buttonWidth);

			matrixButton.reset();
			matrixButton.postScale(buttonScale, buttonScale);
			matrixButton.postTranslate(buttonX, 0);
			__canvas.drawBitmap(bitmapButton, matrixButton, paintBitmap);
		}

	}

	private void onPointerDown(float __x, float __y) {
		// Start dragging one point
		isPointerDown = true;
		isDragging = false;
		pointerDownPoint = Point.fromXY(__x, __y);
	}

	private void onPointerMove(float __x, float __y) {
		// Continues moving
		Point currentPoint = Point.fromXY(__x, __y);

		if (!isDragging) {
			// Started a click; check if the user is actually dragging
			if (Point.distance(pointerDownPoint, currentPoint) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
				isDragging = true;
			}
		}

		// Animate if it has dragged for a while
		if (isDragging || Point.distance(pointerDownPoint, currentPoint) > 2f * getResources().getDisplayMetrics().density) {
			float minButtonX = 0;
			float maxButtonX = finalWidth - buttonWidth;
			float posButton = MathUtils.clamp(__x - buttonWidth/2f, minButtonX, maxButtonX);
			_currentState = MathUtils.map(posButton, minButtonX, maxButtonX);
			if (_leftIsOn) _currentState = 1 - _currentState;
			invalidate();
		}
	}

	private void onPointerUp(float __x, float __y) {
		boolean wasDragging = isDragging;
		isPointerDown = false;
		isDragging = false;
		if (wasDragging) {
			// Animate to the closest position
			setChecked(_currentState > 0.5, true);
		} else {
			// Click
			setChecked(!isChecked(), true);
		}
	}

	private void onPointerCancel(float __x, float __y) {
		// Cancels the movement
		onPointerUp(__x, __y);
	}

	private void updateStateFromChecked(boolean __animated) {
		// Updates the button position from the current checked state, animating if needed

		clearAnimation();

		float newState = _isChecked ? 1 : 0;

		if (!__animated) {
			_currentState = newState;
			invalidate();
		} else {
			StateAnimation anim = new StateAnimation(this, newState);
			anim.setFillAfter(false);
			anim.setDuration(100);
			anim.setInterpolator(new AccelerateDecelerateInterpolator());
			startAnimation(anim);
		}
	}

	private void dispatchOnChange() {
		if (onChangeListener != null) onChangeListener.onChange(this, isChecked());
	}


	// ================================================================================================================
	// INTERFACE INTERFACE --------------------------------------------------------------------------------------------

	public interface OnChangeListener {
		public void onChange(SwitchView __switch, boolean __isChecked);
	}


	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void setImageResources(int __buttonResId, int __backgroundResId) {
		// Sets the resource ids for the bitmaps that need to be used

		// Loads assets
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;

		bitmapBackground = BitmapFactory.decodeResource(getResources(), __backgroundResId, opts);
		bitmapButton = BitmapFactory.decodeResource(getResources(), __buttonResId, opts);

		// Initialize all values for rendering
		initImageResources();

		invalidate();
	}

	public void setFontOptions(Typeface __typeface, int __color, float __size, float __fontMargin) {
		fontTypeface = __typeface;
		fontColor = __color;
		fontSize = __size;
		fontMargin = __fontMargin;

		// Initialize all values for rendering
		initFontResources();

		invalidate();
	}

	public void setTextOptions(String __textOn, String __textOff) {
		textOn = __textOn;
		textOff = __textOff;

		invalidate();
	}

	public boolean getLeftIsOn() {
		return _leftIsOn;
	}

	public void setLeftIsOn(boolean __value) {
		if (_leftIsOn != __value) {
			_leftIsOn = __value;
			invalidate();
		}
	}

	public float getCurrentState() {
		return _currentState;
	}

	public void setCurrentState(float __value) {
		if (_currentState != __value) {
			_currentState = __value;
			invalidate();
		}
	}

	public boolean isChecked() {
		return _isChecked;
	}

	public void setChecked(boolean __checked) {
		setChecked(__checked, false);
	}

	public void setChecked(boolean __checked, boolean __animated) {
		//if (_isChecked != __checked) {
		_isChecked = __checked;
		dispatchOnChange();
		updateStateFromChecked(__animated);
		//}
	}

	public void setOnChangeListener(OnChangeListener __listener) {
		onChangeListener = __listener;
	}


	// ================================================================================================================
	// HELPER CLASSES -------------------------------------------------------------------------------------------------

	class StateAnimation extends Animation {

		// Properties
		private final SwitchView target;
		private final float startState;
		private final float targetState;

		// ================================================================================================================
		// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

		public StateAnimation(SwitchView __target, float __targetState) {
			target = __target;
			startState = __target.getCurrentState();
			targetState = __targetState;
		}

		// ================================================================================================================
		// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

//		@Override
//		public void initialize(int __width, int __height, int __parentWidth, int __parentHeight) {
//			super.initialize(__width, __height, __parentWidth, __parentHeight);
//			startHeight = target.getHeight();
//		}

		// ================================================================================================================
		// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

		@Override
		protected void applyTransformation(float __interpolatedTime, Transformation __t) {
			target.setCurrentState(startState + (targetState - startState) * __interpolatedTime);
		}

		@Override
		public boolean willChangeBounds() {
			return false;
		}

		@Override
		public boolean willChangeTransformationMatrix() {
			return false;
		}
	}
}
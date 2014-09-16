package com.zehfernando.display;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class CustomTypefaceSpan extends TypefaceSpan {

	private final Typeface typeface;
	private final int color;

	public CustomTypefaceSpan(Typeface __typeface, int __color) {
		super("sans-serif");
		typeface = __typeface;
		color = __color;
	}

	@Override
	public void updateDrawState(TextPaint __paint) {
		applyCustomTypeFace(__paint, typeface, color);
	}

	@Override
	public void updateMeasureState(TextPaint __paint) {
		applyCustomTypeFace(__paint, typeface, color);
	}

	private static void applyCustomTypeFace(Paint __paint, Typeface __typeface, int __color) {
		int oldStyle;
		Typeface old = __paint.getTypeface();
		if (old == null) {
			oldStyle = 0;
		} else {
			oldStyle = old.getStyle();
		}

		int fake = oldStyle & ~__typeface.getStyle();
		if ((fake & Typeface.BOLD) != 0) {
			__paint.setFakeBoldText(true);
		}

		if ((fake & Typeface.ITALIC) != 0) {
			__paint.setTextSkewX(-0.25f);
		}

		__paint.setColor(__color);
		__paint.setTypeface(__typeface);
	}
}
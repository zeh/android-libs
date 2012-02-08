package com.zehfernando.display.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class SVGImageView extends ImageView {

	// Instances
	protected SVG svg;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public SVGImageView(Context context, int __resource) {
		super(context);

		init(__resource);
	}

	public SVGImageView(Context context, int __resource, AttributeSet attrs) {
		super(context, attrs);

		init(__resource);
	}

	public SVGImageView(Context context, int __resource, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(__resource);
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void init(int __resource) {
		//setBackgroundColor(Color.WHITE);
		svg = SVGParser.getSVGFromResource(getResources(), __resource); // R.raw.filename

		//Drawable drawable = svg.createPictureDrawable();
		//setImageDrawable(drawable);

		//drawable.setColorFilter(0xff0000, PorterDuff.Mode.SRC_ATOP);
		//drawable.setColorFilter(new ColorMatrixColorFilter(new float[] { 1, 0, 0, 0, 0,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  0, 0, 0, 1, 0}));

		//Picture picture = svg.getPicture();

		//setColorFilter(new ColorMatrixColorFilter(new float[] { 1, 0, 0, 0, 0,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  0, 0, 0, 1, 0}));
		//getDrawable().setColorFilter(new ColorMatrixColorFilter(new float[] { 1, 0, 0, 0, 0,  0, 0, 0, 0, 0,  0, 0, 0, 0, 0,  0, 0, 0, 1, 0}));
		//getDrawable().setColorFilter(0xff00ff00, PorterDuff.Mode.SRC_ATOP);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// Creates a new bitmap with the vector, with full quality

		// Dimensions of the container
		float targetW = right - left;
		float targetH = bottom - top;
		float targetR = targetW / targetH;

		// Source picture and its dimensions
		Picture picture = svg.getPicture();
		float sourceW = picture.getWidth();
		float sourceH = picture.getHeight();
		float sourceR = sourceW / sourceH;

		// Find the best size for the final image
		float finalS;
		if (targetR > sourceR) {
			// Target ratio is wider than source, use source height
			finalS = targetH / sourceH;
		} else {
			// Target ratio is taller than source, use source width
			finalS = targetW / sourceW;
		}
		int finalW = Math.round(sourceW * finalS);
		int finalH = Math.round(sourceH * finalS);

		// Create bitmap holder
		Bitmap bitmap = Bitmap.createBitmap(finalW, finalH, Bitmap.Config.ARGB_8888);

		// Create canvas manipulator
		Canvas canvas = new Canvas(bitmap);
		//canvas.drawARGB(127, 255, 0, 0);

		// Resizes source accordingly
		Matrix mtx = new Matrix();
		mtx.setScale(finalW / sourceW, finalH / sourceH);
		canvas.setMatrix(mtx);

		// Draws the image
		canvas.drawPicture(picture);

		// Set it as the imagedrawable
		setImageBitmap(bitmap);

		//Log.v("SVGImageView", "creating images! w = " + targetW + ", " + targetH + " to " + sourceW + ", " + sourceH + " =====> " + finalW + ", " + finalH);
	}

//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//
//		//Picture picture = svg.getPicture();
//		//canvas.drawPicture(picture);
//
//		Log.v("SVGImageView", "onDraw");
//	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

//	public void setFillColor(int __color) {
//		setColorFilter(0xff000000 | __color, PorterDuff.Mode.SRC_ATOP);
//		int r = (__color & 0xff0000) >> 16;
//		int g = (__color & 0xff00) >> 8;
//		int b = __color & 0xff;
//		setColorFilter(new ColorMatrixColorFilter(new float[] { 0, 0, 0, 0, r,  0, 0, 0, 0, g,  0, 0, 0, 0, b,  0, 0, 0, 1, 0}));
//	}

}

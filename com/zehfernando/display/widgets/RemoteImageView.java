package com.zehfernando.display.widgets;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.zehfernando.net.cache.FileCache;
import com.zehfernando.net.loaders.Loader;
import com.zehfernando.net.loaders.Loader.OnLoaderLoadingCompleteListener;
import com.zehfernando.net.loaders.Loader.OnLoaderLoadingErrorListener;

public class RemoteImageView extends ViewGroup {

	// Like an ImageView, but allowing an external URL to be loaded (and cached)

	// TODO: make it use ImageLoader!

	// Properties
	protected String imageURL;
	protected boolean useCache;

	protected String fileCacheName;

	// Children
	protected ImageView image;
	protected ProgressBar progressBar;
	protected Loader loader;


	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public RemoteImageView(Context __context) {
		super(__context);

		init();
	}

	public RemoteImageView(Context __context, AttributeSet __attrs) {
		super(__context, __attrs);

		init();
	}

	public RemoteImageView(Context __context, AttributeSet __attrs, int __defStyle) {
		super(__context, __attrs, __defStyle);

		init();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void init() {
		//setBackgroundColor(0x55ff0000);
		image = new ImageView(getContext());
		addView(image);

		fileCacheName = "";

		progressBar = null;

		setScaleType(ScaleType.FIT_XY);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int w = getWidth();
		int h = getHeight();

		image.layout(0, 0, w, h);

		if (progressBar != null) {
			int pbw = Math.round(30F * getResources().getDisplayMetrics().density);
			int pbh = pbw;

			int pl = Math.round(w * 0.5F - pbw * 0.5F);
			int pt = Math.round(h * 0.5F - pbh * 0.5F);

			progressBar.layout(pl, pt, pl + pbw, pt + pbh);
		}
	}

	public FileCache getFileCache() {
		// Returns the file cache used
		// Change this to use a private FileCache?
		return FileCache.getFileCache(fileCacheName);
	}

	protected void removeLoader() {
		if (loader != null) {
			loader.setOnLoaderLoadingErrorListener(null);
			loader.setOnLoaderLoadingProgressListener(null);
			loader.setOnLoaderLoadingStartListener(null);
			loader.setOnLoaderLoadingCompleteListener(null);
			loader.cancel();
			loader = null;
		}
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void setScaleType(ScaleType __scaleType) {
		image.setScaleType(__scaleType);
	}

	public void loadRemoteContent(String __imageURL, boolean __useCache) {
		// Starts trying to download the remote image in a separate thread

		removeLoader();

		imageURL = __imageURL;
		useCache = __useCache;

		if (__useCache) {
			// Check if the image exists in the cache first
			if (getFileCache().getFileExists(imageURL)) {
				// Already exists! Use cached image
				image.setImageBitmap(BitmapFactory.decodeStream(getFileCache().getFile(imageURL)));
				return;
			}
		}

		// Create progress bar
		if (progressBar == null) {
			progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyle);
			//progressBar.setIndeterminate(false);
			//progressBar.setMax(100);
			//progressBar.setProgress(50);
			addView(progressBar);
		}

		loader = new Loader();
		loader.setOnLoaderLoadingCompleteListener(new OnLoaderLoadingCompleteListener() {
			@Override
			public void onLoaderLoadingComplete(Loader __loader) {
				// Loading complete
				// Save the image to the cache if allowed
				if (useCache) getFileCache().putFile(imageURL, __loader.getData());

				// Create a new bitmap from it
				image.setImageBitmap(BitmapFactory.decodeByteArray(__loader.getData(), 0, __loader.getData().length));

				// Remove the progress bar
				removeView(progressBar);
				progressBar = null;

				removeLoader();
			}
		});
//		loader.setOnLoaderLoadingStartListener(new OnLoaderLoadingStartListener() {
//			@Override
//			public void onLoaderLoadingStart(Loader __loader) {
//				Log.v("RemoteImageView", "--> Image has started loading, filesize is " + __loader.getTotalBytes());
//			}
//		});
		loader.setOnLoaderLoadingErrorListener(new OnLoaderLoadingErrorListener() {
			@Override
			public void onLoaderLoadingError(Loader __loader) {
				Log.v("RemoteImageView", "--> ERROR loading image");
				removeLoader();
			}
		});
//		loader.setOnLoaderLoadingProgressListener(new OnLoaderLoadingProgressListener() {
//			@Override
//			public void onLoaderLoadingProgress(Loader __loader, float __phase) {
//				Log.v("RemoteImageView", "--> Image loading, progress at " + __phase);
//			}
//		});
		loader.load(__imageURL);

		// Starts loading
//		String imageURL = cursor.getString(cursor.getColumnIndex(StyleStationDatabaseHelper.Consultations.COLUMN_IMAGE));
//		Log.v("ConsultationDetailsActivity", "Image to load: " + imageURL);
//		FileCache fc = FileCache.getFileCache();
//		if (fc.getFileExists(imageURL)) {
//			image.setImageBitmap(BitmapFactory.decodeStream(fc.getFile(imageURL)));
//		} else {
//			downloadImage(imageURL);
//			image.setImageBitmap(BitmapFactory.decodeStream(fc.getFile(imageURL)));
//		}
	}

	public void setImageResource(int __imageResource) {
		// Set the current image
		image.setImageResource(__imageResource);
	}

	public void clearImage() {
		setImageResource(android.R.color.transparent);
	}

	public void destroy() {
		clearImage();

		removeLoader();
		image = null;

		removeAllViews();
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getFileCacheName() {
		return fileCacheName;
	}

	public void setFileCacheName(String __value) {
		fileCacheName = __value;
	}
}

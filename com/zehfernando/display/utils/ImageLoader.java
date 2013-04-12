package com.zehfernando.display.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.zehfernando.net.cache.FileCache;
import com.zehfernando.net.loaders.Loader;
import com.zehfernando.net.loaders.Loader.OnLoaderLoadingCompleteListener;
import com.zehfernando.net.loaders.Loader.OnLoaderLoadingErrorListener;
import com.zehfernando.net.loaders.Loader.OnLoaderLoadingProgressListener;
import com.zehfernando.net.loaders.Loader.OnLoaderLoadingStartListener;
import com.zehfernando.utils.F;

public class ImageLoader {

	// Loads an image into an ImageView asynchronously

	// Constants
	protected static final int MAX_TRIES = 2;
	protected static final String LOCAL_PREFFIX = "file://";

	// Static properties
	protected static ArrayList<ImageLoader> loaders = new ArrayList<ImageLoader>();

	// Properties
	protected ImageView imageView;
	protected String uri;							// URL or file location
	protected Boolean skipCache;
	protected int triesLeft;
	protected Boolean isLocal;
	protected Boolean highPriority;					// If true, AND it's local, it loads it immediately instead of in a separate thread
	protected String cacheName;
	protected long expirationTime;					// Time to expire, in ms; if 0, never expires

	// Instances
	protected Loader loader;

	private final HashMap<String, String> headers;

	// Properties
	protected OnRemoteImageLoaderLoadingStartListener onLoadingStartListener;
	protected OnRemoteImageLoaderLoadingErrorListener onLoadingErrorListener;
	protected OnRemoteImageLoaderLoadingProgressListener onLoadingProgressListener;
	protected OnRemoteImageLoaderLoadingCompleteListener onLoadingCompleteListener;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public ImageLoader(ImageView __imageView, String __uri, Boolean __skipCache, Boolean __highPriority, long __expirationTime) {
		this(__imageView, __uri, __skipCache, __highPriority, __expirationTime, "", null);
	}

	public ImageLoader(ImageView __imageView, String __uri, Boolean __skipCache, Boolean __highPriority, long __expirationTime, String __cacheName, HashMap<String,String> __headers) {
		imageView = __imageView;
		uri = __uri;
		skipCache = __skipCache;
		highPriority = __highPriority;
		triesLeft = MAX_TRIES;
		isLocal = __uri.indexOf("http://") != 0 && __uri.indexOf("https://") != 0;
		cacheName = __cacheName;
		expirationTime = __expirationTime;
		loaders.add(this);
		headers = new HashMap<String, String>();

		if (__headers != null) {
			Iterator it = __headers.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				headers.put(pairs.getKey().toString(), pairs.getValue().toString());
				//it.remove();
			}
		}
	}

	// ================================================================================================================
	// PUBLIC STATIC INTERFACE ----------------------------------------------------------------------------------------

	public static boolean load(ImageView __imageView, String __uri, Boolean __skipCache, Boolean __highPriority, long __expirationTime, String __cacheName) {
		return load(__imageView, __uri, __skipCache, __highPriority, __expirationTime, __cacheName, null);
	}

	public static boolean load(ImageView __imageView, String __uri, Boolean __skipCache, Boolean __highPriority, long __expirationTime, String __cacheName, HashMap<String,String> __headers) {
		// Loads the image from __url into __imageView
		// Returns true if already loaded

		//F.info("Loading " + __uri);

		ImageLoader loader = new ImageLoader(__imageView, __uri, __skipCache, __highPriority, __expirationTime, __cacheName, __headers);
		return loader.start();
	}

	public static void stop(ImageView __imageView) {
		// Stops loading anything into __imageView

		ImageLoader loader = getRemoteImageLoader(__imageView);
		//if (loader != null) F.log("Loading will be destroyed ==================================> " + loader.getURL());
		if (loader != null) destroyRemoteImageLoader(loader);
	}

	public static void stop(String __url) {
		// Stops loading __url into anything

		ImageLoader loader = getRemoteImageLoader(__url);
		if (loader != null) destroyRemoteImageLoader(loader);
	}

	public FileCache getCache() {
		// Returns the file cache used
		return FileCache.getFileCache(imageView.getContext(), cacheName);
	}

	public static ImageLoader getRemoteImageLoader(ImageView __imageView) {
		for (int i = 0; i < loaders.size(); i++) {
			if (loaders.get(i).getImageView() == __imageView) return loaders.get(i);
		}
		return null;
	}

	public static ImageLoader getRemoteImageLoader(String __url) {
		for (int i = 0; i < loaders.size(); i++) {
			if (loaders.get(i).getURL() == __url) return loaders.get(i);
		}
		return null;
	}


	// ================================================================================================================
	// INTERNAL STATIC INTERFACE --------------------------------------------------------------------------------------

	protected static void destroyRemoteImageLoader(ImageLoader __loader) {
		__loader.stop();
		loaders.remove(__loader);
		F.debug("Cleaned ImageLoader; Remaining image loaders: " + loaders.size());
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void dispatchOnLoadingStart() {
		if (onLoadingStartListener != null) onLoadingStartListener.onRemoteImageLoaderLoadingStart(this);
	}

	protected void dispatchOnLoadingError() {
		if (onLoadingErrorListener != null) onLoadingErrorListener.onRemoteImageLoaderLoadingError(this);
	}

	protected void dispatchOnLoadingProgress() {
		if (onLoadingProgressListener != null) {
			if (loader != null) {
				onLoadingProgressListener.onRemoteImageLoaderLoadingProgress(this, (float)loader.getLoadedBytes() / (float)loader.getTotalBytes());
			} else {
				onLoadingProgressListener.onRemoteImageLoaderLoadingProgress(this, 1);
			}
		}
	}

	protected void dispatchOnLoadingComplete() {
		if (onLoadingCompleteListener != null) onLoadingCompleteListener.onRemoteImageLoaderLoadingComplete(this);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public boolean start() {
		// Starts loading

		// Returns true if already loaded

		triesLeft--;

		if (!skipCache && !isLocal) {
			// Check if the image exists in the cache first
			if (getCache().getFileExists(uri)) {
				// Already exists! Use cached image
				//F.info("Image is already cached; used cached version");
				uri = getCache().getFilePath(uri);
				isLocal = true;
				start();
				return true;
			}
		}

		if (isLocal && highPriority) {
			doHighPriorityLoad();
			return true;
		}

		if (isLocal && uri.indexOf(LOCAL_PREFFIX) != 0) uri = LOCAL_PREFFIX + uri;

		loader = new Loader();

		// Set headers
		Iterator it = headers.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			loader.addHeader(pairs.getKey().toString(), pairs.getValue().toString());
			//it.remove();
		}

		loader.setOnLoaderLoadingStartListener(new OnLoaderLoadingStartListener() {
			@Override
			public void onLoaderLoadingStart(Loader __loader) {
				dispatchOnLoadingStart();
			}
		});
		loader.setOnLoaderLoadingErrorListener(new OnLoaderLoadingErrorListener() {
			@Override
			public void onLoaderLoadingError(Loader __loader) {
				if (triesLeft > 0) {
					F.warn("Error loading image [" + uri + "] trying again (" + triesLeft + " tries left)");
					stop();
					start();
				} else {
					F.error("--> FINAL ERROR loading image");
					dispatchOnLoadingError();
					destroyRemoteImageLoader(ImageLoader.this);
				}
			}
		});
		loader.setOnLoaderLoadingProgressListener(new OnLoaderLoadingProgressListener() {
			@Override
			public void onLoaderLoadingProgress(Loader __loader, int __bytesLoaded, int __bytesTotal) {
				dispatchOnLoadingProgress();
			}
		});
		loader.setOnLoaderLoadingCompleteListener(new OnLoaderLoadingCompleteListener() {
			@Override
			public void onLoaderLoadingComplete(Loader __loader) {
				// Loading complete
				// Save the image to the cache if allowed
				if (!skipCache && !isLocal) {
					getCache().putFile(uri, __loader.getData());
					if (expirationTime > 0) getCache().setFileExpirationTimeRelativeToNow(uri, expirationTime);
				}

				// Create a new bitmap from it
				imageView.setImageBitmap(BitmapFactory.decodeByteArray(__loader.getData(), 0, __loader.getData().length));

				dispatchOnLoadingComplete();

				destroyRemoteImageLoader(ImageLoader.this);

			}
		});
		loader.load(uri);

		return false;
	}

	public void doHighPriorityLoad() {
		// Immediate load when it's local (and is loaded), without using a separate thread
		try {
			imageView.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(new File(uri))));
		} catch (FileNotFoundException e) {
			F.error("File not found when trying to load local file with high priority: " + uri);
			dispatchOnLoadingError();
			destroyRemoteImageLoader(this);
			return;
		} catch (OutOfMemoryError e) {
			F.error("Out of memory trying to load image: " + uri);
			dispatchOnLoadingError();
			destroyRemoteImageLoader(this);
			return;
		}
		//imageView.setImageBitmap(BitmapFactory.decodeStream(getCache().getFile(uri)));
		dispatchOnLoadingStart();
		dispatchOnLoadingProgress();
		dispatchOnLoadingComplete();

		destroyRemoteImageLoader(this);
	}

	public void stop() {
		// Stops loading
		if (loader != null) {
			loader.cancel();
			loader = null;
		}
	}

	public void setOnRemoteImageLoaderLoadingStartListener(OnRemoteImageLoaderLoadingStartListener __listener) {
		onLoadingStartListener = __listener;
	}

	public void setOnRemoteImageLoaderErrorListener(OnRemoteImageLoaderLoadingErrorListener __listener) {
		onLoadingErrorListener = __listener;
	}

	public void setOnRemoteImageLoaderLoadingProgressListener(OnRemoteImageLoaderLoadingProgressListener __listener) {
		onLoadingProgressListener = __listener;
	}

	public void setOnRemoteImageLoaderLoadingCompleteListener(OnRemoteImageLoaderLoadingCompleteListener __listener) {
		onLoadingCompleteListener = __listener;
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public ImageView getImageView() {
		return imageView;
	}

	public String getURL() {
		return uri;
	}

	// ================================================================================================================
	// INTERFACE CLASSES ----------------------------------------------------------------------------------------------

	public interface OnRemoteImageLoaderLoadingStartListener {
		public void onRemoteImageLoaderLoadingStart(ImageLoader __loader);
	}

	public interface OnRemoteImageLoaderLoadingErrorListener {
		public void onRemoteImageLoaderLoadingError(ImageLoader __loader);
	}

	public interface OnRemoteImageLoaderLoadingProgressListener {
		public void onRemoteImageLoaderLoadingProgress(ImageLoader __loader, float __phase);
	}

	public interface OnRemoteImageLoaderLoadingCompleteListener {
		public void onRemoteImageLoaderLoadingComplete(ImageLoader __loader);
	}
}

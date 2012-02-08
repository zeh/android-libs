package com.zehfernando.net.loaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.util.Log;

public class Loader {

	/* Asynchronous loader for binary files (like images) */

	// Properties
	private String url;
	private byte[] data;

	private boolean isLoading;
	private boolean isLoaded;
	private int loadedBytes;
	private int totalBytes;

	private DownloadFilesTask downloadTask;

	private OnLoaderLoadingStartListener onLoaderLoadingStartListener;
	private OnLoaderLoadingErrorListener onLoaderLoadingErrorListener;
	private OnLoaderLoadingProgressListener onLoaderLoadingProgressListener;
	private OnLoaderLoadingCompleteListener onLoaderLoadingCompleteListener;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public Loader() {
		//Log.v("Loader", "Started");

		isLoading = false;
		isLoaded = false;
		loadedBytes = 0;
		totalBytes = 0;

		downloadTask = null;

	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void dispatchOnLoaderLoadingStart() {
		if (onLoaderLoadingStartListener != null) onLoaderLoadingStartListener.onLoaderLoadingStart(this);
	}

	protected void dispatchOnLoaderLoadingError() {
		if (onLoaderLoadingErrorListener != null) onLoaderLoadingErrorListener.onLoaderLoadingError(this);
	}

	protected void dispatchOnLoaderLoadingProgress() {
		if (onLoaderLoadingProgressListener != null) onLoaderLoadingProgressListener.onLoaderLoadingProgress(this, loadedBytes, totalBytes);
	}

	protected void dispatchOnLoaderLoadingComplete() {
		if (onLoaderLoadingCompleteListener != null) onLoaderLoadingCompleteListener.onLoaderLoadingComplete(this);
	}

	protected void setStart(int __totalBytes) {
		// Signal from background thread saying it has started
		totalBytes = __totalBytes;
		dispatchOnLoaderLoadingStart();
	}

	protected void setError() {
		// Signal from background thread saying it had an error :(
		downloadTask = null;
		dispatchOnLoaderLoadingError();
	}

	protected void setProgress(int __loadedBytes) {
		// Signal from background thread saying it's progressing
		loadedBytes = __loadedBytes;
		dispatchOnLoaderLoadingProgress();
	}

	protected void setComplete(byte[] __data) {
		// Signal from background thread saying it has finished
		isLoaded = true;
		isLoading = false;
		loadedBytes = totalBytes;
		downloadTask = null;

		data = __data;
		dispatchOnLoaderLoadingComplete();
	}

	protected void clear() {
		downloadTask = null;
		data = null;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void load(String __url) {
		url = __url;

		Log.i("Loader", "Loading image: " + __url);

		// TODO: unload if it already exists

		URL fileURL = null;

		try {
			fileURL = new URL(url);
		} catch (MalformedURLException e) {
			Log.v("Loader", "Malformed URL " + url);
			e.printStackTrace();
		}

		isLoaded = false;
		isLoading = true;
		loadedBytes = 0;
		totalBytes = 0;

		if (fileURL != null) {
			downloadTask = new DownloadFilesTask();
			downloadTask.execute(fileURL);
		}

	}

	public void cancel() {
		if (downloadTask != null) downloadTask.cancel(true);

		clear();
	}

	public void setOnLoaderLoadingStartListener(OnLoaderLoadingStartListener __listener) {
		onLoaderLoadingStartListener = __listener;
	}

	public void setOnLoaderLoadingErrorListener(OnLoaderLoadingErrorListener __listener) {
		onLoaderLoadingErrorListener = __listener;
	}

	public void setOnLoaderLoadingProgressListener(OnLoaderLoadingProgressListener __listener) {
		onLoaderLoadingProgressListener = __listener;
	}

	public void setOnLoaderLoadingCompleteListener(OnLoaderLoadingCompleteListener __listener) {
		onLoaderLoadingCompleteListener = __listener;
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public byte[] getData() {
		return data;
	}

	public int getTotalBytes() {
		return totalBytes;
	}

	public int getLoadedBytes() {
		return loadedBytes;
	}

	public boolean getIsLoading() {
		return isLoading;
	}

	public boolean getIsLoaded() {
		return isLoaded;
	}

	// ================================================================================================================
	// INTERFACE CLASSES ----------------------------------------------------------------------------------------------

	public interface OnLoaderLoadingStartListener {
		public void onLoaderLoadingStart(Loader __loader);
	}

	public interface OnLoaderLoadingErrorListener {
		public void onLoaderLoadingError(Loader __loader);
	}

	public interface OnLoaderLoadingProgressListener {
		public void onLoaderLoadingProgress(Loader __loader, int __loadedBytes, int __totalBytes);
	}

	public interface OnLoaderLoadingCompleteListener {
		public void onLoaderLoadingComplete(Loader __loader);
	}

	// ================================================================================================================
	// AUXILIARY CLASSES ----------------------------------------------------------------------------------------------

	private class DownloadFilesTask extends AsyncTask<URL, Integer, byte[]> {

		protected boolean calledOnStart = false;	// Whether it was already called once or not

		protected byte[] doInBackground(URL... urls) {
			// Invoked on background thread

			//Log.v("Loader", " ===> Running! " + urls[0]);

			// Only the first URL is used!
			URL fileURL = urls[0];

			InputStream inputStream = null;

			int totalBytes;

			try {
				URLConnection connection = fileURL.openConnection();
				connection.setDoInput(true); // Probably not necessary?
				connection.setConnectTimeout(3000);
				connection.connect();
				inputStream = connection.getInputStream();

				// Same:
				//InputStream is = (InputStream)fileURL.getContent();

				//Log.d("Loader", "File last modified is " + new Date(connection.getLastModified())); // Correct last modified date

				totalBytes = connection.getContentLength();
			} catch (IOException __e) {
				Log.e("Loader", " ===> IOException while trying to open remote file [" + urls[0] + "]! " + __e);
				return null;
			}

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] fdata = new byte[16384];
			int nRead;
			int loadedBytes = 0;

			publishProgress(totalBytes);

			try {
				while ((nRead = inputStream.read(fdata, 0, fdata.length)) != -1) {
					loadedBytes += nRead;
					publishProgress(loadedBytes);
					buffer.write(fdata, 0, nRead);
				}
				buffer.flush();
			} catch (IOException __e) {
				Log.e("Loader", " ===> IOException while trying to read remote file! " + __e);
				return null;
			}

			publishProgress(totalBytes);

			fdata = null;

			return buffer.toByteArray();
		}

		protected void onProgressUpdate(Integer... __loadedBytes) {
			// Invoked on UI thread
			if (!calledOnStart) {
				// First call
				calledOnStart = true;
				// This is actually total bytes
				setStart(__loadedBytes[0]);
			} else {
				// Normal progress
				setProgress(__loadedBytes[0]);
			}
		}

		protected void onPostExecute(byte[] __output) {
			// Invoked on UI thread
			if (__output == null) {
				setError();
			} else {
				setComplete(__output);
			}
			//Log.v("Loader", " ===> Completed!");
		}

		protected void onCancelled() {
			// Invoked on UI thread
			//Log.v("Loader", " ===> Cancelled!");
		}
	}
}

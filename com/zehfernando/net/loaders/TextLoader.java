package com.zehfernando.net.loaders;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class TextLoader {

	/* Asynchronous loader for text files (like XMLs) */

	// Constants
	protected static final int MESSAGE_TYPE_START = 0;				// Param is: total bytes
	protected static final int MESSAGE_TYPE_PROGRESS = 1;			// Param is: loaded bytes
	protected static final int MESSAGE_TYPE_COMPLETE = 2;			// No param
	protected static final int MESSAGE_TYPE_ERROR = 3;				// No param

	protected static final int MESSAGE_TYPE_HEADER = 4;
	protected static final int MESSAGE_TYPE_HEADER_LAST_MODIFIED = 0;

	public static final String METHOD_POST = "post";
	public static final String METHOD_GET = "get";

	// Properties
	private String url;
	private String data;

	private String method;
	private String requestContent;
	private String contentType;

	private boolean isLoading;
	private boolean isLoaded;
	private int loadedBytes;
	private int totalBytes;

	private long headerLastModified;

	private LoadingThread loadingThread;
	private Handler loadingHandler;

	private OnTextLoaderLoadingStartListener onLoadingStartListener;
	private OnTextLoaderLoadingErrorListener onLoadingErrorListener;
	private OnTextLoaderLoadingProgressListener onLoadingProgressListener;
	private OnTextLoaderLoadingCompleteListener onLoadingCompleteListener;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public TextLoader() {
		isLoading = false;
		isLoaded = false;
		loadedBytes = 0;
		totalBytes = 0;

		method = METHOD_GET;
		requestContent = "";
		contentType = "";

		headerLastModified = 0;

		// Starts loading the actual file
		loadingHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch(msg.arg1) {
					case MESSAGE_TYPE_START:
						totalBytes = msg.arg2;
						dispatchOnLoadingStart();
						break;
					case MESSAGE_TYPE_PROGRESS:
						loadedBytes = msg.arg2;
						dispatchOnLoadingProgress();
						break;
					case MESSAGE_TYPE_ERROR:
						dispatchOnLoadingError();
						break;
					case MESSAGE_TYPE_COMPLETE:
						dispatchOnLoadingComplete();
						break;
					case MESSAGE_TYPE_HEADER:
						switch(msg.arg2) {
							case MESSAGE_TYPE_HEADER_LAST_MODIFIED:
								headerLastModified = (Long)msg.obj;
								break;
						}
						break;
				}
			}
		};

		loadingThread = new LoadingThread(loadingHandler);
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void dispatchOnLoadingStart() {
		// Loading has started, header is known (total bytes, etc)
		if (onLoadingStartListener != null) onLoadingStartListener.onLoadingStart(this);
	}

	protected void dispatchOnLoadingError() {
		// Loading stopped
		if (onLoadingErrorListener != null) onLoadingErrorListener.onLoadingError(this);
	}

	protected void dispatchOnLoadingProgress() {
		// Loading progress, loadedBytes has been updated
		if (onLoadingProgressListener != null) onLoadingProgressListener.onLoadingProgress(this, loadedBytes, totalBytes);
	}

	protected void dispatchOnLoadingComplete() {
		// Loading is complete
		if (onLoadingCompleteListener != null) onLoadingCompleteListener.onLoadingComplete(this);
	}

	protected void clear() {
		loadingHandler = null;
		data = null;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void load(String __url) {
		url = __url;

		//Log.i("TextLoader", "Loading text: " + __url);

		// TODO: unload if it already exists

		isLoaded = false;
		isLoading = true;

		loadingThread.start();
	}

	public void cancel() {
		if (loadingThread != null) loadingThread.stop();

		clear();
	}

	public void setOnTextLoaderLoadingStartListener(OnTextLoaderLoadingStartListener __listener) {
		onLoadingStartListener = __listener;
	}

	public void setOnTextLoaderLoadingErrorListener(OnTextLoaderLoadingErrorListener __listener) {
		onLoadingErrorListener = __listener;
	}

	public void setOnTextLoaderLoadingProgressListener(OnTextLoaderLoadingProgressListener __listener) {
		onLoadingProgressListener = __listener;
	}

	public void setOnTextLoaderLoadingCompleteListener(OnTextLoaderLoadingCompleteListener __listener) {
		onLoadingCompleteListener = __listener;
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String __value) {
		contentType = __value;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String __value) {
		method = __value;
	}

	public String getRequestContent() {
		return requestContent;
	}

	public void setRequestContent(String __value) {
		requestContent = __value;
	}

	public String getData() {
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

	public long getLastModified() {
		return headerLastModified;
	}

	// ================================================================================================================
	// INTERFACE CLASSES ----------------------------------------------------------------------------------------------

	public interface OnTextLoaderLoadingStartListener {
		public void onLoadingStart(TextLoader __loader);
	}

	public interface OnTextLoaderLoadingErrorListener {
		public void onLoadingError(TextLoader __loader);
	}

	public interface OnTextLoaderLoadingProgressListener {
		public void onLoadingProgress(TextLoader __loader, int __loadedBytes, int __totalBytes);
	}

	public interface OnTextLoaderLoadingCompleteListener {
		public void onLoadingComplete(TextLoader __loader);
	}

	// ================================================================================================================
	// HELPER CLASSES -------------------------------------------------------------------------------------------------

	private class LoadingThread extends Thread {

		// Properties
		private Handler handler;
		private boolean isLoading;
		private boolean isLoaded;

		// ================================================================================================================
		// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

		LoadingThread(Handler h) {
			handler = h;

			data = "";

			isLoading = false;
			isLoaded = false;
		}

		@Override
		public void run() {
			if (!isLoading && !isLoaded) {
				isLoading = true;
				try {
					Log.i("TextLoader", "Loading " + url + "...");

					URL urlRequest = new URL(url);

					// Create connection
					HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();

					// Set headers
					if (contentType.length() > 0) connection.setRequestProperty("Content-Type", contentType);

					// Set other properties
					connection.setConnectTimeout(3000);

					if (method == TextLoader.METHOD_POST) {
						// POST
						connection.setDoOutput(true);
						if (requestContent.length() > 0) {
							//Log.v("TextLoader", ">>> ADDING QUERY == " + requestContent);
							OutputStream output = new BufferedOutputStream(connection.getOutputStream());
							output.write(requestContent.getBytes("UTF-8"));
							output.flush();
							output.close();
						}
					} else {
						// GET
						connection.setDoInput(true);
					}

					// Connect
					connection.connect();

					//Log.d("TextLoader", "File date is " + new Date(connection.getDate())); // Always the current date
					//Log.d("TextLoader", "File last modified is " + new Date(connection.getLastModified())); // Correct last modified date

					int l = 0;
					int t = connection.getContentLength();

					sendMessageForHeader(MESSAGE_TYPE_HEADER_LAST_MODIFIED, connection.getLastModified());

					sendMessage(MESSAGE_TYPE_START, t);

					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					//BufferedReader in = new BufferedReader(new InputStreamReader(urlRequest.openStream()));
					//String str;
					String finalStr = "";

					char buff[] = new char[2048];
					int read = 0;

					while ((read = in.read(buff)) != -1) {
						l += read;
						finalStr += new String(buff, 0, read);
						sendMessage(MESSAGE_TYPE_PROGRESS, Math.min(l, t));
					}

					// TODO: use a quicker, straight data read instead? http://stackoverflow.com/questions/3562585/cache-online-file-contents-in-string-rather-than-local-file
//					while ((str = in.readLine()) != null) {
//						l += str.length() + 2; // This is not correct because it ignores the type of linefeed! Always assumes 2 bytes, which is not correct in unix filesystems
//						finalStr += str + "\r\n";
//						sendMessage(MESSAGE_TYPE_PROGRESS, Math.min(l, t));
//					}

					in.close();

					data = finalStr.intern();
					//data = sb.toString();

					Log.i("TextLoader", "Loading has finished!");

					finalStr = "";
					//str = "";

					isLoading = false;
					isLoaded = true;

					sendMessage(MESSAGE_TYPE_COMPLETE);

				} catch (Exception e) {
					Log.e("TextLoader", "Error loading file! :(");

					isLoading = false;
					isLoaded = false;

					sendMessage(MESSAGE_TYPE_ERROR);
				}
			}
		}

		protected void sendMessageForHeader(int __type, long __long) {
			Message msg = handler.obtainMessage();
			msg.arg1 = MESSAGE_TYPE_HEADER;
			msg.arg2 = __type;
			msg.obj = __long;
			handler.sendMessage(msg);
		}

		protected void sendMessage(int __type) {
			sendMessage(__type, 0);
		}

		protected void sendMessage(int __type, int __params) {
			Message msg = handler.obtainMessage();
			msg.arg1 = __type;
			msg.arg2 = __params;
			handler.sendMessage(msg);
		}
	}
}

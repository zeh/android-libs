package com.zehfernando.net.loaders;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.zehfernando.utils.F;

public class TextLoader {

	/* Asynchronous loader for text files (like XMLs) */

	// Constants
	protected static final int MESSAGE_TYPE_START = 0;				// Param is: total bytes
	protected static final int MESSAGE_TYPE_PROGRESS = 1;			// Param is: loaded bytes
	protected static final int MESSAGE_TYPE_COMPLETE = 2;			// No param
	protected static final int MESSAGE_TYPE_ERROR = 3;				// No param
	protected static final int MESSAGE_TYPE_CANCELED = 4;			// No param

	protected static final int MESSAGE_TYPE_HEADER = 5;
	protected static final int MESSAGE_TYPE_HEADER_LAST_MODIFIED = 0;

	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";

	// Properties
	private String url;
	private String data;

	private boolean needsDataFromOutputStream;
	private ByteArrayOutputStream dataStream;

	private String method;
	private String requestContent;
	private String contentType;

	private boolean isLoading;
	private boolean isLoaded;
	private int loadedBytes;
	private int totalBytes;

	private long headerLastModified;

	private LoadingThread loadingThread;
	private final Handler loadingHandler;

	private OnTextLoaderStartListener onStartListener;
	private OnTextLoaderErrorListener onErrorListener;
	private OnTextLoaderProgressListener onProgressListener;
	private OnTextLoaderCompleteListener onCompleteListener;
	private OnTextLoaderCancelListener onCancelListener;

	private RequestContentStreamWriter requestContentStreamWriter;

	private final HashMap<String, String> headers;

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

		needsDataFromOutputStream = true;
		dataStream = null;

		headers = new HashMap<String, String>();

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
					case MESSAGE_TYPE_CANCELED:
						dispatchOnCancel();
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
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void dispatchOnLoadingStart() {
		// Loading has started, header is known (total bytes, etc)
		if (onStartListener != null) onStartListener.onStart(this);
	}

	protected void dispatchOnLoadingError() {
		// Loading stopped
		if (onErrorListener != null) onErrorListener.onError(this);
	}

	protected void dispatchOnLoadingProgress() {
		// Loading progress, loadedBytes has been updated
		if (onProgressListener != null) onProgressListener.onProgress(this, loadedBytes, totalBytes);
	}

	protected void dispatchOnLoadingComplete() {
		// Loading is complete
		if (onCompleteListener != null) onCompleteListener.onComplete(this);
	}

	protected void dispatchOnCancel() {
		// Loading has been properly stopped after a cancel was called
		if (onCancelListener != null) onCancelListener.onCancel(this);
	}

	protected void clear() {
		//loadingHandler = null;
		requestContentStreamWriter = null;
		needsDataFromOutputStream = false;
		data = null;
		dataStream = null;
		isLoaded = false;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void load(String __url) {
		cancel();

		loadingThread = new LoadingThread(loadingHandler);

		url = __url;

		isLoaded = false;
		isLoading = true;

		loadingThread.start();
	}

	public void cancel() {
		if (loadingThread != null) {
			Thread moribund = loadingThread;
			loadingThread = null;
			moribund.interrupt();
			moribund = null;
		}

		data = null;
		dataStream = null;
		isLoading = false;
		isLoaded = false;
	}

	public void setOnLoadingStartListener(OnTextLoaderStartListener __listener) {
		onStartListener = __listener;
	}

	public void setOnLoadingErrorListener(OnTextLoaderErrorListener __listener) {
		onErrorListener = __listener;
	}

	public void setOnLoadingProgressListener(OnTextLoaderProgressListener __listener) {
		onProgressListener = __listener;
	}

	public void setLoadingCompleteListener(OnTextLoaderCompleteListener __listener) {
		onCompleteListener = __listener;
	}

	public void setOnCancelListener(OnTextLoaderCancelListener __listener) {
		onCancelListener = __listener;
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

	public void setRequestContentStreamWriter(RequestContentStreamWriter __requestContentStreamWriter) {
		requestContentStreamWriter = __requestContentStreamWriter;
	}

	public String getData() {
		if (needsDataFromOutputStream && dataStream != null) {
			// Delays getting data until when it's needed, so avoids early memory consumption
			try {
				data = dataStream.toString("UTF-8");
			} catch (UnsupportedEncodingException e) {
				F.error("Invalid charset used! Duh!");
				data = "";
			}
			needsDataFromOutputStream = false;
		}
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

	public void addHeader(String __key, String __value) {
		// Adds a value to the request header
		headers.put(__key, __value);
	}


	// ================================================================================================================
	// INTERFACE CLASSES ----------------------------------------------------------------------------------------------

	public interface RequestContentStreamWriter {
		public void writeToStream(OutputStream __stream);
	}

	public interface OnTextLoaderStartListener {
		public void onStart(TextLoader __loader);
	}

	public interface OnTextLoaderErrorListener {
		public void onError(TextLoader __loader);
	}

	public interface OnTextLoaderProgressListener {
		public void onProgress(TextLoader __loader, int __loadedBytes, int __totalBytes);
	}

	public interface OnTextLoaderCompleteListener {
		public void onComplete(TextLoader __loader);
	}

	public interface OnTextLoaderCancelListener {
		public void onCancel(TextLoader __loader);
	}

	public interface OnTextLoaderHeaderListener {
		public void onHeader(TextLoader __loader);
	}

	// ================================================================================================================
	// HELPER CLASSES -------------------------------------------------------------------------------------------------

	private class LoadingThread extends Thread {

		// Properties
		private final Handler handler;
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

				F.debug("[" + method + "] to " + url + "...");

				URL urlRequest;
				try {
					urlRequest = new URL(url);
				} catch (MalformedURLException e1) {
					F.warn("Malformed URL!");
					terminateInFail();
					return;
				}

				// Create connection
				HttpURLConnection connection;
				try {
					connection = (HttpURLConnection) urlRequest.openConnection();
				} catch (IOException e1) {
					F.warn("Could not get connection from URL!");
					terminateInFail();
					return;
				}

				// Set headers
				if (contentType.length() > 0) connection.setRequestProperty("Content-Type", contentType);
				Iterator it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry)it.next();
					//F.log("HEADER => " + pairs.getKey().toString() + " as " + pairs.getValue().toString());
					connection.setRequestProperty(pairs.getKey().toString(), pairs.getValue().toString());
					//it.remove();
				}

				// Set other properties
				connection.setConnectTimeout(3000);
				connection.setRequestProperty("Accept","*/*");
				try {
					connection.setRequestMethod(method);
				} catch (ProtocolException e1) {
					F.warn("Protocol [" + method + "] is not allowed!");
				}

				if (method.equals(TextLoader.METHOD_POST)) {
					// POST
					connection.setDoOutput(true);
					if (requestContent != null && requestContent.length() > 0) {
						//F.log("ADDING CONTENT AS QUERY == " + requestContent);
						OutputStream output;
						try {
							output = new BufferedOutputStream(connection.getOutputStream());
							output.write(requestContent.getBytes("UTF-8"));
							output.flush();
							output.close();
						} catch (IOException e) {
							F.warn("Could not open output connection for writing request data!");
							terminateInFail();
							return;
						}
					} else if (requestContentStreamWriter != null) {
						//F.log("ADDING CONTENT AS OUTPUTSTREAM == " + requestContentStreamWriter);
						connection.setRequestProperty("Connection", "Keep-Alive");
						try {
							requestContentStreamWriter.writeToStream(connection.getOutputStream());
						} catch (IOException e) {
							F.warn("Could not use requestContentStreamWriter to write to stream!");
							terminateInFail();
							return;
						}
					} else {
						F.warn("NO CONTENT TO SEND!");
					}
				} else {
					// GET
					connection.setDoInput(true);
				}

				// Connect
				try {
					connection.connect();
				} catch (IOException e) {
					F.warn("Could not open connection!");
					terminateInFail();
					return;
				}

				//Log.d("TextLoader", "File date is " + new Date(connection.getDate())); // Always the current date
				//Log.d("TextLoader", "File last modified is " + new Date(connection.getLastModified())); // Correct last modified date

				int l = 0;
				int t = connection.getContentLength();

				sendMessageForHeader(MESSAGE_TYPE_HEADER_LAST_MODIFIED, connection.getLastModified());

				sendMessage(MESSAGE_TYPE_START, t);

				//connection.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");

				int status = -1;
				try {
					status = connection.getResponseCode();
				} catch (IOException e) {
					F.warn("Could not get connection response code!");
					terminateInFail();
					return;
				}

				if (status != 200) F.warn("Status not 200! Status: " + status);

				InputStream in;
				try {
					in = new BufferedInputStream(connection.getInputStream());
				} catch (IOException e) {
					F.warn("Error getting normal input stream! Will respond with error stream instead.");
					in = connection.getErrorStream();
				}

				if (in == null) {
					F.warn("Could not get ANY connection response!");
					terminateInFail();
				}

				data = "";

				byte buff[] = new byte[1024 * 10];
				int read = 0;

				//F.log("Attempting to load: " + t + " bytes");

				ByteArrayOutputStream output = new ByteArrayOutputStream(t > 0 ? t : 32); // If not specified, the buffer size of the byte array is doubled as needed, leading to some massive memory consumption

				// http://stackoverflow.com/questions/3562585/cache-online-file-contents-in-string-rather-than-local-file
				try {
					while ((read = in.read(buff)) != -1 && Thread.currentThread() == loadingThread) {
						l += read;
						output.write(buff, 0, read);
						sendMessage(MESSAGE_TYPE_PROGRESS, Math.min(l, t));
					}

					//F.log("Bytes written to stream: " + output.size());

					output.flush();
					output.close();
					in.close();

//					needsDataFromOutputStream = true;
//					dataStream = output;
					try {
						data = output.toString("UTF-8");
					} catch (UnsupportedEncodingException e) {
						F.error("Invalid charset used! Duh!");
						data = "";
					}

					output = null;
					in = null;

				} catch (IOException e) {
					F.warn("Error reading input stream!");
					terminateInFail();
					return;
				}

				if (Thread.currentThread() == loadingThread) {
					F.debug("Loading has finished.");

					isLoading = false;
					isLoaded = true;

					sendMessage(MESSAGE_TYPE_COMPLETE);
				} else {
					F.debug("Loading was canceled.");

					isLoading = false;
					isLoaded = false;

					sendMessage(MESSAGE_TYPE_CANCELED);
				}
			}
		}

		protected void terminateInFail() {
			F.warn("Failed. Returning error result.");
			isLoading = false;
			isLoaded = false;

			sendMessage(MESSAGE_TYPE_ERROR);
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

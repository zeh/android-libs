package com.zehfernando.net.apis;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import android.util.Log;

import com.zehfernando.net.loaders.TextLoader;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderCompleteListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderErrorListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderProgressListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderStartListener;
import com.zehfernando.net.loaders.TextLoader.RequestContentStreamWriter;

public class BasicServiceRequest implements RequestContentStreamWriter {

	protected static final String MULTIPART_BOUNDARY = "---------------------------7da843b2a1b04";

	// Enums
	protected static final String CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	protected static final String CONTENT_TYPE_MULTIPART_FORM_DATA = "multipart/form-data; boundary=" + MULTIPART_BOUNDARY;

	// Properties
	protected TextLoader loader;

	protected String requestURL;
	protected String rawResponse;

	protected boolean isLoading;
	protected boolean isLoaded;

	protected HashMap<String, String> requestParameters;
	protected HashMap<String, InputStream> requestAttachments;
	protected HashMap<String, String> requestAttachmentsNames;

//			protected var urlRequest:URLRequest;
	protected String requestMethod;
	protected String requestContentType;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public BasicServiceRequest() {

		// Basic service configuration
		requestURL = "";
		requestMethod = TextLoader.METHOD_GET;
		requestContentType = CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED;
		requestParameters = new HashMap<String, String>();
		requestAttachments = new HashMap<String, InputStream>();
		requestAttachmentsNames = new HashMap<String, String>();

//		requestMethod = URLRequestMethod.GET;
//		requestContentType = "application/x-www-form-urlencoded"; // Default

		rawResponse = null;

		isLoading = false;
		isLoaded = false;
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

//	protected function getURLVariables(): URLVariables {
//		// Returns the URLVariables needed by this request
//		var vars:URLVariables = new URLVariables();
//		return vars;
//	}

//	protected function getRequestHeaders(): Array {
//		// Returns the request headers needed by this request
//		var headers:Array = [];
//		return headers;
//	}

	protected void clearData() {
		// Clear all the loaded data
		if (isLoaded) {
			isLoaded = false;
			rawResponse = null;
			requestParameters = null;
			requestAttachments = null;
			requestAttachmentsNames = null;
		}
	}

	protected void stopLoading() {
		// Stop loading everything
		if (isLoading) {
			loader.cancel();
			isLoading = false;
			removeLoader();
		}
	}

	protected void removeLoader() {
		loader.setOnLoadingErrorListener(null);
		loader.setOnLoadingStartListener(null);
		loader.setOnLoadingProgressListener(null);
		loader.setLoadingCompleteListener(null);
		loader = null;
	}

	// ================================================================================================================
	// EXTENDABLE INTERFACE -------------------------------------------------------------------------------------------

	protected String getRequestContent() {
		// Iterate through the parameters hashmap and generate the content

		String content = "";

		// TODO: move this to the textLoader class?

		if (requestContentType == CONTENT_TYPE_APPLICATION_X_WWW_FORM_URLENCODED) {
			// Send as querystrings
			boolean hasStarted = false;
			for (String key:requestParameters.keySet()) {
				if (hasStarted) content += "&";

				try {
					content += key + "=" + URLEncoder.encode(requestParameters.get(key), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					Log.e("BasicServiceRequest", "Could not encode value from key " + key + " to UTF-8!");
				}

				hasStarted = true;
			}
		} else if (requestContentType == CONTENT_TYPE_MULTIPART_FORM_DATA) {
			// Send as encoded multipart data; this will be handled by writeToStream!

			content = null;

			Log.v("BasicServiceRequest", "Content is NULL");
		}

		return content;
	}

	@Override
	public void writeToStream(OutputStream __stream) {

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = MULTIPART_BOUNDARY;

		Log.v("BasicServiceRequest", "Writing content to stream!");

		try {
			DataOutputStream writer = new DataOutputStream(__stream);

			// http://reecon.wordpress.com/2010/04/25/uploading-files-to-http-server-using-post-android-sdk/

			// Normal fields
			for (String key:requestParameters.keySet()) {
				Log.v("BasicServiceRequest", "--> Writing field: " + key + " as " + requestParameters.get(key));

				writer.writeBytes(twoHyphens + boundary + lineEnd);
				writer.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
				writer.writeBytes(lineEnd);
				writer.writeBytes(requestParameters.get(key) + lineEnd);
			}

			// Files
			InputStream inputStream;
			int bytesAvailable, bufferSize, bytesRead;
			byte[] buffer;
			int maxBufferSize = 1*1024*1024;

			for (String key:requestAttachments.keySet()) {

				inputStream = requestAttachments.get(key);

				Log.v("BasicServiceRequest", "--> Writing file: " + key + " as " + requestAttachmentsNames.get(key) + " (" + inputStream.available() + " bytes)");

				writer.writeBytes(twoHyphens + boundary + lineEnd);
				writer.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + requestAttachmentsNames.get(key) + "\"" + lineEnd);
				writer.writeBytes("Content-Type: image/pjpeg" + lineEnd);
				writer.writeBytes(lineEnd);

				bytesAvailable = inputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// Read file
				bytesRead = inputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {
					writer.write(buffer, 0, bufferSize);
					bytesAvailable = inputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = inputStream.read(buffer, 0, bufferSize);
				}

				writer.writeBytes(lineEnd);

				inputStream.close();
				inputStream = null;

				// Responses from the server (code and message)
				//serverResponseCode = connection.getResponseCode();
				//serverResponseMessage = connection.getResponseMessage();
			}

			writer.writeBytes(twoHyphens + boundary + lineEnd);

			writer.flush();
			writer.close();
			writer = null;

		} catch (Exception __e) {
			Log.e("BasicServiceRequest", "Exception when trying to upload file! " + __e);
		}


	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

	protected void onServiceLoadingError() {
		rawResponse = loader.getData();

		isLoading = false;
		isLoaded = false;

		removeLoader();
	}


	protected void onServiceLoadingStart() {
	}

	protected void onServiceLoadingProgress() {
	}

	protected void onServiceLoadingComplete() {
		rawResponse = loader.getData();

		isLoading = false;
		isLoaded = true;

		removeLoader();
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void execute() {

		if (isLoading) stopLoading();
		if (isLoaded) clearData();

//		var vars:URLVariables = getURLVariables();
//
//		urlRequest = new URLRequest();
//
//		urlRequest.url = requestURL;
//		urlRequest.method = requestMethod;
//		urlRequest.data = vars;
//		urlRequest.requestHeaders = getRequestHeaders();
//		urlRequest.contentType = requestContentType;

		String requestContent = getRequestContent();

		loader = new TextLoader();
		loader.setMethod(requestMethod);
		if (requestContent != null) {
			Log.v("BasicServiceRequest", "Setting request content from STRING");
			loader.setRequestContent(requestContent);
		} else {
			Log.v("BasicServiceRequest", "Setting request content as a STREAM WRITER");
			loader.setRequestContentStreamWriter(this);
		}
		loader.setContentType(requestContentType);

		//Log.v("BasicServiceRequest", "Request FROM: " + requestURL);
		//Log.v("BasicServiceRequest", "Request BY: " + requestMethod);

		loader.setOnLoadingErrorListener(new OnTextLoaderErrorListener() {
			@Override
			public void onError(TextLoader __loader) {
				onServiceLoadingError();
			}
		});
		loader.setOnLoadingStartListener(new OnTextLoaderStartListener() {
			@Override
			public void onStart(TextLoader __loader) {
				onServiceLoadingStart();
			}
		});
		loader.setOnLoadingProgressListener(new OnTextLoaderProgressListener() {
			@Override
			public void onProgress(TextLoader __loader, int __loadedBytes, int __totalBytes) {
				onServiceLoadingProgress();
			}
		});
		loader.setLoadingCompleteListener(new OnTextLoaderCompleteListener() {
			@Override
			public void onComplete(TextLoader __loader) {
				onServiceLoadingComplete();
			}
		});

		loader.load(requestURL);
	}

	public void dispose() {
		if (isLoading) stopLoading();
		if (isLoaded) clearData();
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getRawResponse() {
		return rawResponse;
	}

	public void setParameter(String __key, String __value) {
		requestParameters.put(__key, __value);
	}

	public void setParameter(String __key, int __value) {
		setParameter(__key, Integer.toString(__value, 10));
	}

	public String getParameter(String __key) {
		return requestParameters.get(__key);
	}

	public void setAttachment(String __key, String __filename, InputStream __inputStream) {
		requestAttachments.put(__key, __inputStream);
		requestAttachmentsNames.put(__key, __filename);

		// getContentResolver().openInputStream(Uri.parse(parameterImageUri)));
	}

//	public function get rawRequest():Object {
//		return urlRequest.data;
//	}

	public boolean getIsLoading() {
		return isLoading;
	}

	public boolean getIsLoaded() {
		return isLoaded;
	}

}

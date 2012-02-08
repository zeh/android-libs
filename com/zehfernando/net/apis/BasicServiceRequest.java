package com.zehfernando.net.apis;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import android.util.Log;

import com.zehfernando.net.loaders.TextLoader;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderLoadingCompleteListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderLoadingErrorListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderLoadingProgressListener;
import com.zehfernando.net.loaders.TextLoader.OnTextLoaderLoadingStartListener;

public class BasicServiceRequest {

	// Enums

	// Properties
	protected TextLoader loader;

	protected String requestURL;
	protected String rawResponse;

	protected boolean isLoading;
	protected boolean isLoaded;

	protected HashMap<String, String> requestParameters;

//			protected var urlRequest:URLRequest;
	protected String requestMethod;
	protected String requestContentType;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public BasicServiceRequest() {

		// Basic service configuration
		requestURL = "";
		requestMethod = TextLoader.METHOD_GET;
		requestContentType = "application/x-www-form-urlencoded";
		requestParameters = new HashMap<String, String>();

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
		loader.setOnTextLoaderLoadingErrorListener(null);
		loader.setOnTextLoaderLoadingStartListener(null);
		loader.setOnTextLoaderLoadingProgressListener(null);
		loader.setOnTextLoaderLoadingCompleteListener(null);
		loader = null;
	}

	// ================================================================================================================
	// EXTENDABLE INTERFACE -------------------------------------------------------------------------------------------

	protected String getRequestContent() {
		// Iterate through the parameters hashmap and generate the content

		boolean hasStarted = false;
		String content = "";

		for (String key:requestParameters.keySet()) {
			if (hasStarted) content += "&";

			try {
				content += key + "=" + URLEncoder.encode(requestParameters.get(key), "UTF-8");
			} catch (UnsupportedEncodingException e) {
	            Log.e("BasicServiceRequest", "Could not encode value from key " + key + " to UTF-8!");
			}

			hasStarted = true;
		}

		return content;
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

		loader = new TextLoader();
		loader.setMethod(requestMethod);
		loader.setRequestContent(getRequestContent());
		loader.setContentType(requestContentType);

		//Log.v("BasicServiceRequest", "Request FROM: " + requestURL);
		//Log.v("BasicServiceRequest", "Request BY: " + requestMethod);
		Log.i("BasicServiceRequest", "Request WITH: " + getRequestContent());

		loader.setOnTextLoaderLoadingErrorListener(new OnTextLoaderLoadingErrorListener() {
			@Override
			public void onLoadingError(TextLoader __loader) {
				onServiceLoadingError();
			}
		});
		loader.setOnTextLoaderLoadingStartListener(new OnTextLoaderLoadingStartListener() {
			@Override
			public void onLoadingStart(TextLoader __loader) {
				onServiceLoadingStart();
			}
		});
		loader.setOnTextLoaderLoadingProgressListener(new OnTextLoaderLoadingProgressListener() {
			@Override
			public void onLoadingProgress(TextLoader __loader, int __loadedBytes, int __totalBytes) {
				onServiceLoadingProgress();
			}
		});
		loader.setOnTextLoaderLoadingCompleteListener(new OnTextLoaderLoadingCompleteListener() {
			@Override
			public void onLoadingComplete(TextLoader __loader) {
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

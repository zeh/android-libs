package com.zehfernando.net.apis.leverage.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import android.util.Log;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.apis.BasicServiceRequest;
import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.LeverageDataUtils;
import com.zehfernando.net.apis.leverage.data.LeverageObject;
import com.zehfernando.net.loaders.TextLoader;

public class BasicLeverageRequest extends BasicServiceRequest {

	// Parameters
	public static final String PARAMETER_METHOD = "method";
	public static final String PARAMETER_CLIENT_ID = "client";
	public static final String PARAMETER_API_KEY = "apikey";
	public static final String PARAMETER_FORMAT = "format";

	public static final String PARAMETER_SORT = "sort";

	// Results
	protected String errorMessage;

	protected String title;
	protected String link;
	protected String description;
	protected Date pubDate;

	protected ArrayList<LeverageObject> items;

	// Listeners
	protected OnServiceLoadingError onServiceLoadingErrorListener;
	protected OnServiceLoadingStart onServiceLoadingStartListener;
	protected OnServiceLoadingProgress onServiceLoadingProgressListener;
	protected OnServiceLoadingComplete onServiceLoadingCompleteListener;

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected boolean parseResponseData() {
		Log.i("BasicLeverageRequest", "Service load successful");
		XML response = new XML(getRawResponse());

		if (response.getNodeName().equals("error")) {
			// A response was received, but it's actually an error response
			errorMessage = response.getText();
			return false;
		} else {
			// Parse success data
			parseSuccessResponseData(new XML(rawResponse));
			return true;
		}
	}

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public BasicLeverageRequest(String __clientId, String __apiKey) {
		super();

		// Basic service configuration
		requestURL = LeverageConstants.SERVICE_URL;
		requestMethod = TextLoader.METHOD_POST;

		// Parameters
		setParameter(PARAMETER_METHOD, "");
		setParameter(PARAMETER_CLIENT_ID, __clientId);
		setParameter(PARAMETER_API_KEY, __apiKey);
		setParameter(PARAMETER_FORMAT, "rss2");

		// Results
		errorMessage = null;
		title = "";
		link = "";
		description = "";
		pubDate = new Date();
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void parseSuccessResponseData(XML __response) {
		// Parses the raw response data into the result fields -- must be extended!
		ArrayList<XML> xmlItems = __response.getChild("channel", "").getChildren("item");
		items = LeverageObject.fromXMLArrayList(xmlItems);

		title = __response.getChild("channel", "").getChild("title", "").getText();
		link = __response.getChild("channel", "").getChild("link", "").getText();
		description = __response.getChild("channel", "").getChild("description", "").getText();
		pubDate = LeverageDataUtils.getDateFromString(__response.getChild("channel", "").getChild("pubDate", "").getText());

	}

	// ================================================================================================================
	// PUBLIC INTERFACE ------------------------------------------------------------------------------------------------

	@Override
	public void dispose() {
		setOnServiceLoadingErrorListener(null);
		setOnServiceLoadingStartListener(null);
		setOnServiceLoadingProgressListener(null);
		setOnServiceLoadingCompleteListener(null);
		super.dispose();
	}

	// ================================================================================================================
	// EVENT DISPATCHING INTERFACE ------------------------------------------------------------------------------------

	public void setOnServiceLoadingErrorListener(OnServiceLoadingError __listener) {
		onServiceLoadingErrorListener = __listener;
	}

	public void setOnServiceLoadingStartListener(OnServiceLoadingStart __listener) {
		onServiceLoadingStartListener = __listener;
	}

	public void setOnServiceLoadingProgressListener(OnServiceLoadingProgress __listener) {
		onServiceLoadingProgressListener = __listener;
	}

	public void setOnServiceLoadingCompleteListener(OnServiceLoadingComplete __listener) {
		onServiceLoadingCompleteListener = __listener;
	}

	public interface OnServiceLoadingError {
		public void onServiceLoadingError(BasicLeverageRequest __request);
	}

	public interface OnServiceLoadingStart {
		public void onServiceLoadingStart(BasicLeverageRequest __request);
	}

	public interface OnServiceLoadingProgress {
		public void onServiceLoadingProgress(BasicLeverageRequest __request);
	}

	public interface OnServiceLoadingComplete {
		public void onServiceLoadingComplete(BasicLeverageRequest __request);
	}

	public void dispatchOnServiceLoadingError() {
		if (onServiceLoadingErrorListener != null) onServiceLoadingErrorListener.onServiceLoadingError(this);
	}

	public void dispatchOnServiceLoadingStart() {
		if (onServiceLoadingStartListener != null) onServiceLoadingStartListener.onServiceLoadingStart(this);
	}

	public void dispatchOnServiceLoadingProgress() {
		if (onServiceLoadingProgressListener != null) onServiceLoadingProgressListener.onServiceLoadingProgress(this);
	}

	public void dispatchOnServiceLoadingComplete() {
		if (onServiceLoadingCompleteListener != null) onServiceLoadingCompleteListener.onServiceLoadingComplete(this);
	}

	// ================================================================================================================
	// EXTENDABLE INTERFACE -------------------------------------------------------------------------------------------

//	@Override
//	protected String getRequestContent() {
//		// Iterate through the parameters hashmap and generate the content
//
//		boolean hasStarted = false;
//		String content = "";
//
//		for (String key:requestParameters.keySet()) {
//			if (hasStarted) content += "&";
//
//			try {
//				content += key + "=" + URLEncoder.encode(requestParameters.get(key), "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//	            Log.e("BasicServiceRequest", "Could not encode value from key " + key + " to UTF-8!");
//			}
//
//			hasStarted = true;
//		}
//
//		return content;
//	}

	// ================================================================================================================
	// EVENT INTERFACE ------------------------------------------------------------------------------------------------

	@Override
	protected void onServiceLoadingError() {
		super.onServiceLoadingError();
		dispatchOnServiceLoadingError();
	};

	@Override
	protected void onServiceLoadingStart() {
		super.onServiceLoadingStart();
		dispatchOnServiceLoadingStart();
	};

	@Override
	protected void onServiceLoadingProgress() {
		super.onServiceLoadingProgress();
		dispatchOnServiceLoadingProgress();
	};

	@Override
	protected void onServiceLoadingComplete() {
		super.onServiceLoadingComplete();
		boolean isSuccess = parseResponseData();
		if (isSuccess) {
			dispatchOnServiceLoadingComplete();
		} else {
			dispatchOnServiceLoadingError();
		}
	};

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<LeverageObject> getItems() {
		return items;
	}

	public ArrayList<LeverageObject> getItemsFiltered(String... __objectTypes) {
		ArrayList<String> objs = new ArrayList<String>(Arrays.asList(__objectTypes));

		// Create a list of items filtered by specific types
		ArrayList<LeverageObject> newItems = new ArrayList<LeverageObject>();

		for (int i = 0; i < items.size(); i++) {
			if (objs.indexOf(items.get(i).getType()) != -1) {
				// This item is allowed
				newItems.add(items.get(i));
			}
		}
		return newItems;
	}
}

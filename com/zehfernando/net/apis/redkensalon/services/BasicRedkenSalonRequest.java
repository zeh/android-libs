package com.zehfernando.net.apis.redkensalon.services;

import android.util.Log;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.apis.BasicServiceRequest;
import com.zehfernando.net.loaders.TextLoader;

public class BasicRedkenSalonRequest extends BasicServiceRequest {

	// Results
	protected String errorMessage;

	// Listeners
	protected OnServiceLoadingError onServiceLoadingErrorListener;
	protected OnServiceLoadingStart onServiceLoadingStartListener;
	protected OnServiceLoadingProgress onServiceLoadingProgressListener;
	protected OnServiceLoadingComplete onServiceLoadingCompleteListener;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public BasicRedkenSalonRequest() {
		super();

		// Basic service configuration
		requestURL = "";
		requestMethod = TextLoader.METHOD_POST;

		// Parameters

		// Results
		errorMessage = null;
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected Boolean parseResponseData() {
		// Rreturns TRUE if success

		Log.i("BasicRedkenSalonRequest", "Service load successful");
		XML response = new XML(getRawResponse());

		int numRecords = response.getChild("proUser").getChild("recordCount").getTextAsInt(10);

		if (numRecords == 0) {
			errorMessage = response.getChild("proUser").getChild("errorMessage").getText();
			return false;
		} else {
			parseResponseSuccessData(response);
			return true;
		}
	}

	protected void parseResponseSuccessData(XML response) {
		// Must be extended!
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
		public void onServiceLoadingError(BasicRedkenSalonRequest __request);
	}

	public interface OnServiceLoadingStart {
		public void onServiceLoadingStart(BasicRedkenSalonRequest __request);
	}

	public interface OnServiceLoadingProgress {
		public void onServiceLoadingProgress(BasicRedkenSalonRequest __request);
	}

	public interface OnServiceLoadingComplete {
		public void onServiceLoadingComplete(BasicRedkenSalonRequest __request);
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
}


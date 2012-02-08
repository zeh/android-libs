package com.zehfernando.net.apis.leverage.services.news;

import java.util.ArrayList;

import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.data.LeverageObject;
import com.zehfernando.net.apis.leverage.services.BasicLeverageRequest;

public class LeverageNewsGetRequest extends BasicLeverageRequest {

	// Parameters
	public static final String PARAMETER_AUTHTOKEN = LeverageConstants.PARAMETER_AUTHTOKEN;

	// Response
	protected String title;
	protected String link;
	protected String description;
	protected String pubDate;
	protected ArrayList<LeverageObject> items;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageNewsGetRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "News.Get");

		// Parameters
		setParameter(PARAMETER_AUTHTOKEN, "");

	}
}

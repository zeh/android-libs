package com.zehfernando.net.apis.leverage.services.news;

import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.services.BasicLeverageAuthorizedRequest;

public class LeverageNewsGetRequest extends BasicLeverageAuthorizedRequest {

	// Parameters
	public static final String PARAMETER_COUNT = LeverageConstants.PARAMETER_COUNT;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageNewsGetRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "News.Get");

		// Parameters
		setParameter(PARAMETER_COUNT, "10");

	}
}

package com.zehfernando.net.apis.leverage.services;

import com.zehfernando.net.apis.leverage.LeverageConstants;

public class BasicLeverageAuthorizedRequest extends BasicLeverageRequest {

	// Parameters
	public static final String PARAMETER_AUTHTOKEN = LeverageConstants.PARAMETER_AUTHTOKEN;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public BasicLeverageAuthorizedRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Parameters
		setParameter(PARAMETER_AUTHTOKEN, "");
	}
}

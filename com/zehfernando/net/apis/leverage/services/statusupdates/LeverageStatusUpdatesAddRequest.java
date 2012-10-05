package com.zehfernando.net.apis.leverage.services.statusupdates;

import com.zehfernando.net.apis.leverage.services.BasicLeverageAuthorizedRequest;

public class LeverageStatusUpdatesAddRequest extends BasicLeverageAuthorizedRequest {

	// Parameters
	public static final String PARAMETER_MESSAGE = "message";

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageStatusUpdatesAddRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "StatusUpdates.Add");

		// Parameters
		setParameter(PARAMETER_MESSAGE, "");
	}
}

package com.zehfernando.net.apis.leverage.services.customers;

import com.zehfernando.net.apis.leverage.services.BasicLeverageAuthorizedRequest;

public class LeverageCustomersUpdateRequest extends BasicLeverageAuthorizedRequest {

	// Parameters
	public static final String PARAMETER_MEMBERNAME = "membername";
	public static final String PARAMETER_EMAIL = "email";
	public static final String PARAMETER_PASSWORD = "password";

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageCustomersUpdateRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "Customers.Update");

		// Parameters
		setParameter(PARAMETER_MEMBERNAME, "");
		setParameter(PARAMETER_EMAIL, "");
		setParameter(PARAMETER_PASSWORD, "");
	}
}

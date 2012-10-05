package com.zehfernando.net.apis.leverage.services.customers;

import com.zehfernando.net.apis.leverage.services.BasicLeverageAuthorizedRequest;

public class LeverageCustomersAddRequest extends BasicLeverageAuthorizedRequest {

	// Parameters
	public static final String PARAMETER_MEMBERNAME = "membername";
	public static final String PARAMETER_EMAIL = "email";
	public static final String PARAMETER_PASSWORD = "password";
	public static final String PARAMETER_PLAN = "plan";
	public static final String PARAMETER_SIGNUPTOKEN = "signuptoken";

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageCustomersAddRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "Customers.Add");

		// Parameters
		setParameter(PARAMETER_MEMBERNAME, "");
		setParameter(PARAMETER_EMAIL, "");
		setParameter(PARAMETER_PASSWORD, "");
		setParameter(PARAMETER_PLAN, "");
		setParameter(PARAMETER_SIGNUPTOKEN, "");
	}
}

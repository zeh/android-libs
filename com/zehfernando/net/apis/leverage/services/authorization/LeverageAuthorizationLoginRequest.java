package com.zehfernando.net.apis.leverage.services.authorization;

import android.util.Log;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.apis.leverage.services.BasicLeverageRequest;

public class LeverageAuthorizationLoginRequest extends BasicLeverageRequest {

	// Parameters
	public static final String PARAMETER_USERNAME = "username";
	public static final String PARAMETER_PASSWORD = "password";

	// Results
	protected String authToken;
	protected String userId;
	protected String userName;
	protected String signUpToken;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageAuthorizationLoginRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "Authentication.Login");

		// Parameters
		setParameter(PARAMETER_USERNAME, "");
		setParameter(PARAMETER_PASSWORD, "");

		// Results
		authToken = "";
		userId = "";
		userName = "";
		signUpToken = "";
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	@Override
	protected void parseSuccessResponseData(XML __response) {
		authToken = __response.getChild("channel", "").getChild("item", "").getChild("AuthToken", "").getText().trim();
		userId = __response.getChild("channel", "").getChild("item", "").getChild("CustomerID", "").getText().trim();
		userName = __response.getChild("channel", "").getChild("item", "").getChild("MemberName", "").getText().trim();
		signUpToken = __response.getChild("channel", "").getChild("item", "").getChild("SignupToken", "").getText().trim();

		Log.i("LeverageAuthorizationLoginRequest", "User token is " + authToken);

		// title
		// link
		// pubDate
		// description
		// ls:ExpirationDate
		// ls:Email
		// ls:SignupToken -> empty?
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getSignUpToken() {
		return signUpToken;
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}
}
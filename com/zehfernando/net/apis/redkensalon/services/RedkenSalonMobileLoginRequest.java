package com.zehfernando.net.apis.redkensalon.services;

import com.zehfernando.data.xml.XML;
import com.zehfernando.utils.StringUtils;


public class RedkenSalonMobileLoginRequest extends BasicRedkenSalonRequest {

	// http://www.redkensalon.com/mobileLogin?email=myredken@gmail.com&password=redken&key=b6f082d4d0d9f142098a395b125125b6

	// Parameters
	public static final String PARAMETER_EMAIL = "email";
	public static final String PARAMETER_PASSWORD  = "password";

	// Response
	protected static String userId;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public RedkenSalonMobileLoginRequest() {
		super();

		requestURL = "http://www.redkensalon.com/mobileLogin";

		// Parameters
		setParameter(PARAMETER_EMAIL, "");
		setParameter(PARAMETER_PASSWORD, "");

	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void parseResponseSuccessData(XML response) {
		userId = response.getChild("proUser").getChild("userId").getText();
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void execute() {
		// Set the verification key
		setParameter("key", StringUtils.calculateMD5(getParameter(PARAMETER_EMAIL).toLowerCase()+"redkenmobileuser"));

		super.execute();
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getUserId() {
		return userId;
	}
}


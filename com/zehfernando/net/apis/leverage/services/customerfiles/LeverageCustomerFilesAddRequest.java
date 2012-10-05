package com.zehfernando.net.apis.leverage.services.customerfiles;

import com.zehfernando.net.apis.leverage.services.BasicLeverageAuthorizedRequest;

public class LeverageCustomerFilesAddRequest extends BasicLeverageAuthorizedRequest {

	// Parameters
	public static final String PARAMETER_NAME = "name";
	public static final String PARAMETER_DESCRIPTION = "description";
	public static final String ATTACHMENT_FILE = "file";

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageCustomerFilesAddRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "CustomerFiles.Add");
		requestContentType = CONTENT_TYPE_MULTIPART_FORM_DATA;

		// Parameters
		setParameter(PARAMETER_NAME, "");
		setParameter(PARAMETER_DESCRIPTION, "");

		setAttachment(ATTACHMENT_FILE, "", null);
	}
}

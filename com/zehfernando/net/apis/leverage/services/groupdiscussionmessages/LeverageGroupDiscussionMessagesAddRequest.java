package com.zehfernando.net.apis.leverage.services.groupdiscussionmessages;

import com.zehfernando.net.apis.leverage.services.BasicLeverageAuthorizedRequest;

public class LeverageGroupDiscussionMessagesAddRequest extends BasicLeverageAuthorizedRequest {

	// Parameters
	public static final String PARAMETER_DISCUSSIONID = "discussionid";
	public static final String PARAMETER_MESSAGE = "message";

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageGroupDiscussionMessagesAddRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "GroupDiscussionMessages.Add");

		// Parameters
		setParameter(PARAMETER_DISCUSSIONID, "");
		setParameter(PARAMETER_MESSAGE, "");
	}
}

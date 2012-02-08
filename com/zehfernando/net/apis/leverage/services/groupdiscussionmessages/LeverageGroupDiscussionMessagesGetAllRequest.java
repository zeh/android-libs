package com.zehfernando.net.apis.leverage.services.groupdiscussionmessages;

import java.util.ArrayList;

import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.data.LeverageObject;
import com.zehfernando.net.apis.leverage.services.BasicLeverageRequest;

public class LeverageGroupDiscussionMessagesGetAllRequest extends BasicLeverageRequest {

	// Parameters
	public static final String PARAMETER_AUTHTOKEN = LeverageConstants.PARAMETER_AUTHTOKEN;
	public static final String PARAMETER_GROUPID = "groupid"; // If omitted, lists everything
	public static final String PARAMETER_DISCUSSIONID = "discussionid";
	public static final String PARAMETER_KEYWORD = LeverageConstants.PARAMETER_KEYWORD;
	public static final String PARAMETER_COUNT = LeverageConstants.PARAMETER_COUNT;

	public static final String SORT_TYPE_ACCESSDATE = LeverageConstants.SORT_TYPE_ACCESSDATE;
	public static final String SORT_TYPE_CREATIONDATE = LeverageConstants.SORT_TYPE_CREATIONDATE;
	public static final String SORT_TYPE_POSTCOUNT = LeverageConstants.SORT_TYPE_POSTCOUNT;
	public static final String SORT_TYPE_RATING = LeverageConstants.SORT_TYPE_RATING; // Default

	// Results
	protected String title;
	protected String link;
	protected String description;
	protected String pubDate;
	protected ArrayList<LeverageObject> items;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageGroupDiscussionMessagesGetAllRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "GroupDiscussionMessages.GetAll");

		// Parameters
		setParameter(PARAMETER_AUTHTOKEN, "");
		setParameter(PARAMETER_GROUPID, "");
		setParameter(PARAMETER_KEYWORD, "");
		setParameter(PARAMETER_COUNT, "10");

	}
}

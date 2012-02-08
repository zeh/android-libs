package com.zehfernando.net.apis.leverage.services.groupdiscussions;

import java.util.ArrayList;

import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.data.LeverageObject;
import com.zehfernando.net.apis.leverage.services.BasicLeverageRequest;

public class LeverageGroupDiscussionsGetAllRequest extends BasicLeverageRequest {

	// Parameters
	public static final String PARAMETER_AUTHTOKEN = LeverageConstants.PARAMETER_AUTHTOKEN;
	public static final String PARAMETER_GROUPID = "groupid"; // If omitted, lists everything
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

	public LeverageGroupDiscussionsGetAllRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "GroupDiscussions.GetAll");

		// Parameters
		setParameter(PARAMETER_AUTHTOKEN, "");
		setParameter(PARAMETER_GROUPID, "");
		setParameter(PARAMETER_KEYWORD, "");
		setParameter(PARAMETER_COUNT, "10");

	}
}

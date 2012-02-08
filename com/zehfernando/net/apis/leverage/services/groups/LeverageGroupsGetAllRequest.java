package com.zehfernando.net.apis.leverage.services.groups;

import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.services.BasicLeverageRequest;

public class LeverageGroupsGetAllRequest extends BasicLeverageRequest {

	// Parameters
	public static final String PARAMETER_AUTHTOKEN = LeverageConstants.PARAMETER_AUTHTOKEN;
	public static final String PARAMETER_IMAGE = "image";
	public static final String PARAMETER_KEYWORD = LeverageConstants.PARAMETER_KEYWORD;
	public static final String PARAMETER_COUNT = LeverageConstants.PARAMETER_COUNT;

	public static final String SORT_TYPE_ACCESSDATE = LeverageConstants.SORT_TYPE_ACCESSDATE;
	public static final String SORT_TYPE_POSTCOUNT = LeverageConstants.SORT_TYPE_POSTCOUNT;
	public static final String SORT_TYPE_RATING = LeverageConstants.SORT_TYPE_RATING; // Default

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageGroupsGetAllRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "Groups.GetAll");

		// Parameters
		setParameter(PARAMETER_AUTHTOKEN, "");
		setParameter(PARAMETER_IMAGE, "");
		setParameter(PARAMETER_KEYWORD, "");
		setParameter(PARAMETER_COUNT, "10");

	}
}

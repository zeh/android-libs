package com.zehfernando.net.apis.leverage;

public class LeverageConstants {

	// Configurations
	public static final String SERVICE_URL = "http://web.services.leveragesoftware.com/rest/service.aspx";

	// Re-used parameters
	public static final String PARAMETER_AUTHTOKEN = "authtoken";
	public static final String PARAMETER_KEYWORD = "keyword";
	public static final String PARAMETER_COUNT = "count";

	// Sort enums
	public static final String SORT_TYPE_ACCESSDATE = "accessdate";
	public static final String SORT_TYPE_CREATIONDATE = "creationdate";
	public static final String SORT_TYPE_POSTCOUNT = "postcount";
	public static final String SORT_TYPE_RATING = "rating";

	// Other stuff
	public static final String THUMBNAIL_URL_PHOTO = "http://redkencommunity.leveragesoftware.com/thumbnail.aspx?dt=photo&fid=[[id]]&w=100&h=100";
	public static final String THUMBNAIL_URL_GROUPPHOTO = "http://redkencommunity.leveragesoftware.com/thumbnail.aspx?dt=groupphoto&fid=&fn=&oid=[[id]]&w=50&h=50";
}

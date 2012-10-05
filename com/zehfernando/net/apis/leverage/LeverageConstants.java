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

	// Used to make sure thumbs have the correct ratio
	public static final String THUMBNAIL_PREFFIX = "&scaletype=preserve";

	// Used by status update image, group discussion image
	public static final String THUMBNAIL_URL_PHOTO = "http://redkencommunity.leveragesoftware.com/thumbnail.aspx?dt=photo&fid=[[id]]&w=100&h=100" + THUMBNAIL_PREFFIX;

	// Not used anymore? Need correct FID and FN
	public static final String THUMBNAIL_URL_GROUPPHOTO = "http://redkencommunity.leveragesoftware.com/thumbnail.aspx?dt=groupphoto&fid=&fn=&oid=[[id]]&w=100&h=100" + THUMBNAIL_PREFFIX;

	// Used by CustomerFile
	public static final String FILE_URL_PHOTO = "http://redkencommunity.leveragesoftware.com/thumbnail.aspx?dt=portfolio&fid=[[id]]&fn=[[filename]]&oid=[[author]]&w=500&h=500&scaletype=preserve";

}

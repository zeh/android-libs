package com.zehfernando.net.apis.leverage.data;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.enums.LeverageObjectTypes;

public class LeverageStatusUpdate extends LeverageObject {

	// Properties
	protected String authorId;
	protected String authorName;
	protected int ratingTotal;
	protected String message;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageStatusUpdate() {
		super();

		type = LeverageObjectTypes.STATUS_UPDATE;

		authorId = "";
		authorName = "";
		ratingTotal = 0;
		message = "";

	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void setDataFromXML(XML __item) {
		super.setDataFromXML(__item);

		authorId = __item.getChild("CustomerID", "").getText();
		authorName = __item.getChild("MemberName", "").getText();
		message = __item.getChild("Message", "").getText();
		ratingTotal = __item.getChild("RatingTotal", "0").getTextAsInt(10);
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getAuthorId() {
		return authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public int getRatingTotal() {
		return ratingTotal;
	}

	public String getMessage() {
		return message;
	}

	public String getGeneratedImageUrl() {
		// Generate an image URL for this status update based on the author ID
		return LeverageConstants.THUMBNAIL_URL_PHOTO.replace("[[id]]", authorId);
	}
}

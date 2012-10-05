package com.zehfernando.net.apis.leverage.data;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.enums.LeverageObjectTypes;

public class LeverageGroupDiscussion extends LeverageObject {

	// Properties
	protected String authorId;
	protected String authorName;
	protected int ratingTotal;
	protected String messageDate;
	protected String messageBody;
	protected String groupId;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageGroupDiscussion() {
		super();

		type = LeverageObjectTypes.GROUP_DISCUSSION;

		authorId = "";
		authorName = "";
		ratingTotal = 0;
		messageDate = "";
		messageBody = "";
		groupId = "";
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void setDataFromXML(XML __item) {
		super.setDataFromXML(__item);

		authorId = __item.getChild("OwnerID", "").getText();
		authorName = __item.getChild("MemberName", "").getText();
		ratingTotal = __item.getChild("RatingTotal", "0").getTextAsInt(10);
		messageDate = __item.getChild("MessageDate", "").getText();
		messageBody = __item.getChild("MessageBody", "").getText();
		groupId = __item.getChild("GroupID", "").getText();

		// ls:MessageOwnerID -> same as ownerID
		// ls:MessageOwnerMemberName -> same as memberName
		// ls:CustomerID -> same as ownerID
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

	public String getMessageDate() {
		return messageDate;
	}

	public String getMessageBody() {
		return messageBody;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getGeneratedImageUrl() {
		// Generate an image URL for this group discussion based on the author ID
		return LeverageConstants.THUMBNAIL_URL_PHOTO.replace("[[id]]", authorId);
		//return LeverageConstants.THUMBNAIL_URL_GROUPPHOTO.replace("[[id]]", authorId);
	}
}

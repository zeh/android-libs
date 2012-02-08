package com.zehfernando.net.apis.leverage.data;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.apis.leverage.enums.LeverageObjectTypes;

public class LeverageGroupDiscussionMessage extends LeverageGroupDiscussion {

	// Properties
	protected String groupName;
	protected String groupDiscussionId;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageGroupDiscussionMessage() {
		super();

		type = LeverageObjectTypes.GROUP_DISCUSSION_MESSAGE;

		groupName = "";
		groupDiscussionId = "";
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void setDataFromXML(XML __item) {
		super.setDataFromXML(__item);

		groupName = __item.getChild("GroupName", "").getText();
		groupDiscussionId = __item.getChild("GroupDiscussionID", "").getText();
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

	public String getGroupName() {
		return groupName;
	}

	public String getGroupDiscussionId() {
		return groupDiscussionId;
	}
}

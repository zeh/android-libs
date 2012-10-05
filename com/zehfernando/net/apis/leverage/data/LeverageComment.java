package com.zehfernando.net.apis.leverage.data;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.apis.leverage.enums.LeverageObjectTypes;

public class LeverageComment extends LeverageObject {

	// Properties
	protected String message;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageComment() {
		super();

		type = LeverageObjectTypes.COMMENT;

		message = "";
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void setDataFromXML(XML __item) {
		super.setDataFromXML(__item);

		message = __item.getChild("Message", "").getText();

		// ls:CommentObjectType
		// ls:CommentObjectID
		// ls:CustomerID
		// ls:MemberName
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getMessage() {
		return message;
	}
}

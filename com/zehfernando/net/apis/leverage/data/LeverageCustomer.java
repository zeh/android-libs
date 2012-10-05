package com.zehfernando.net.apis.leverage.data;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.enums.LeverageObjectTypes;

public class LeverageCustomer extends LeverageObject {

	// Properties
	protected String imageURL;
	protected String name;
	protected int relevance;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageCustomer() {
		super();

		type = LeverageObjectTypes.CUSTOMER;

		imageURL = "";
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void setDataFromXML(XML __item) {
		super.setDataFromXML(__item);

		imageURL = __item.getChild("enclosure", "").getAttribute("url").getText() + LeverageConstants.THUMBNAIL_PREFFIX;
		name = __item.getChild("MemberName", "").getText();
		relevance = __item.getChild("Relevance", "0").getTextAsInt(10);

		// ls:ProfileItem attribute = Title
		// ls:ProfileItem attribute = CompanyName
		// ls:ProfileItem attribute = FocusAreas
		// ls:ProfileItem attribute = FocusAreas
		// ls:ProfileItem attribute = FocusAreas
		// ls:ProfileItem attribute = FocusAreas
		// ls:ProfileItem attribute = ReasonForJoining
		// ls:ProfileItem attribute = ReasonForJoining
		// ls:ProfileItem attribute = ReasonForJoining
		// ls:ProfileItem attribute = Description
		// ls:ProfileItem attribute = Location
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getImageUrl() {
		return imageURL;
	}

	public String getName() {
		return name;
	}

	public int getRelevance() {
		return relevance;
	}
}

package com.zehfernando.net.apis.leverage.data;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.enums.LeverageObjectTypes;

public class LeverageCustomerFile extends LeverageObject {

	// Properties
	protected String imageURL;
	protected String filename;
	protected String authorId;
	protected String authorName;
	protected int ratingTotal;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageCustomerFile() {
		super();

		type = LeverageObjectTypes.CUSTOMER_FILE;

		imageURL = "";
		filename = "";
		authorId = "";
		authorName = "";
		ratingTotal = 0;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void setDataFromXML(XML __item) {
		super.setDataFromXML(__item);

		imageURL = __item.getChild("enclosure", "").getAttribute("url").getText() + "&w=200&h=200" + LeverageConstants.THUMBNAIL_PREFFIX;
		filename = __item.getChild("FileName", "").getText();

		authorId = __item.getChild("OwnerID", "").getText();
		authorName = __item.getChild("MemberName", "").getText();

		ratingTotal = __item.getChild("RatingTotal", "0").getTextAsInt(10);

		// ls:GroupID
		// ls:FileURL
		// ls:CustomerID - same as OwnerID

		/*
		<item>
			<title>Fishtail</title>
			<link>
				http://redkencommunity.leveragesoftware.com/profile_view.aspx?CustomerID=e866d974955a4aa6b3b3be5e0e2f0e2d
			</link>
			<pubDate>Wed, 22 Feb 2012 03:48:26 GMT</pubDate>
			<description>Love this braid style!</description>
			<enclosure url="http://redkencommunity.leveragesoftware.com/thumbnail.aspx?dt=portfolio&fid=9f692ef48e5b42f9b66b52ebf8f3f106&fn=1202210948261031.jpg&oid=e866d974955a4aa6b3b3be5e0e2f0e2d" length="0" type="image/jpeg"/>
			<ls:ID xmlns:ls="http://leveragesoftware.com">9f692ef48e5b42f9b66b52ebf8f3f106</ls:ID>
			<ls:GroupID xmlns:ls="http://leveragesoftware.com">redkencommunity</ls:GroupID>
			<ls:OwnerID xmlns:ls="http://leveragesoftware.com">e866d974955a4aa6b3b3be5e0e2f0e2d</ls:OwnerID>
			<ls:MemberName xmlns:ls="http://leveragesoftware.com">LeslieBurke2012</ls:MemberName>
			<ls:FileName xmlns:ls="http://leveragesoftware.com">1202210948261031.jpg</ls:FileName>
			<ls:FileUrl xmlns:ls="http://leveragesoftware.com">
				http://redkencommunity.leveragesoftware.com/portfolio_detail.aspx?fileid=9f692ef48e5b42f9b66b52ebf8f3f106
			</ls:FileUrl>
			<ls:ObjectType xmlns:ls="http://leveragesoftware.com">CustomerFile</ls:ObjectType>
			<ls:CustomerID xmlns:ls="http://leveragesoftware.com">e866d974955a4aa6b3b3be5e0e2f0e2d</ls:CustomerID>
			<ls:RatingTotal xmlns:ls="http://leveragesoftware.com">0</ls:RatingTotal>
		</item>
		//http://redkencommunity.leveragesoftware.com/thumbnail.aspx?dt=portfolio&fid=9f692ef48e5b42f9b66b52ebf8f3f106&fn=1202210948261031.jpg&oid=e866d974955a4aa6b3b3be5e0e2f0e2d&w=500&h=500&scaletype=preserve
		*/
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getImageURL() {
		return imageURL;
	}

	public String getOwnerImageURL() {
		return LeverageConstants.THUMBNAIL_URL_PHOTO.replace("[[id]]", authorId);
	}

	public String getFilename() {
		return filename;
	}

	public String authorId() {
		return authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public int getRatingTotal() {
		return ratingTotal;
	}

	public String getFullImageURL() {
		// Generate an image URL for this group discussion based on the author ID
		return LeverageConstants.FILE_URL_PHOTO.replace("[[id]]", id).replace("[[filename]]", filename).replace("[[author]]", authorId);
	}

}

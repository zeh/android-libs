package com.zehfernando.net.apis.leverage.services.groupdiscussions;

import com.zehfernando.net.apis.leverage.LeverageConstants;
import com.zehfernando.net.apis.leverage.services.BasicLeverageRequest;

public class LeverageGroupDiscussionsAddRequest extends BasicLeverageRequest {

	// Parameters
	public static final String PARAMETER_AUTHTOKEN = LeverageConstants.PARAMETER_AUTHTOKEN;
	public static final String PARAMETER_GROUPID = "groupid"; // If omitted, lists everything
	public static final String PARAMETER_NAME = "name";
	public static final String PARAMETER_MESSAGE = "message";

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageGroupDiscussionsAddRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "GroupDiscussions.Add");

		// Parameters
		setParameter(PARAMETER_AUTHTOKEN, "");
		setParameter(PARAMETER_GROUPID, "");
		setParameter(PARAMETER_NAME, "");
		setParameter(PARAMETER_MESSAGE, "");

		// After posted, getItems() will contain a single GroupDiscussion object with a reference to it
	}

	/*
	OK response:
		<rss version="2.0">
		  <channel>
		    <title>Group Discussion</title>
		    <link>http://web.services.leveragesoftware.com/rest/service.aspx</link>
		    <description>Group Discussion</description>
		    <item>
		      <title>djdh</title>
		      <link>http://redkencommunity.leveragesoftware.com/group_discussion.aspx?DiscussionID=7d9f107d1ff24a91b20dfcdc91837a88</link>
		      <pubDate>Wed, 01 Feb 2012 21:32:04 GMT</pubDate>
		      <description>Cjtihf</description>
		      <ls:ID xmlns:ls="http://leveragesoftware.com">7d9f107d1ff24a91b20dfcdc91837a88</ls:ID>
		      <ls:GroupID xmlns:ls="http://leveragesoftware.com">fffc26a484c34f32848bb9cbe4249e53</ls:GroupID>
		      <ls:OwnerID xmlns:ls="http://leveragesoftware.com">15d03982a26446f29b669bd28aba5f7f</ls:OwnerID>
		      <ls:MemberName xmlns:ls="http://leveragesoftware.com">ZehTest</ls:MemberName>
		      <ls:MessageOwnerID xmlns:ls="http://leveragesoftware.com">15d03982a26446f29b669bd28aba5f7f</ls:MessageOwnerID>
		      <ls:MessageOwnerMemberName xmlns:ls="http://leveragesoftware.com">ZehTest</ls:MessageOwnerMemberName>
		      <ls:MessageDate xmlns:ls="http://leveragesoftware.com">2/1/2012 3:32:04 PM</ls:MessageDate>
		      <ls:MessageBody xmlns:ls="http://leveragesoftware.com">Cjtihf</ls:MessageBody>
		      <ls:ObjectType xmlns:ls="http://leveragesoftware.com">GroupDiscussion</ls:ObjectType>
		      <ls:CustomerID xmlns:ls="http://leveragesoftware.com">15d03982a26446f29b669bd28aba5f7f</ls:CustomerID>
		      <ls:RatingTotal xmlns:ls="http://leveragesoftware.com">0</ls:RatingTotal>
		    </item>
		  </channel>
		</rss>

	Error response (the usual):
		<error>A group id is required.</error>

	 */
}

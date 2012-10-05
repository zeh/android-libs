package com.zehfernando.net.apis.leverage.services.ratings;

import com.zehfernando.net.apis.leverage.data.LeverageRating;
import com.zehfernando.net.apis.leverage.services.BasicLeverageAuthorizedRequest;

public class LeverageRatingsAddRequest extends BasicLeverageAuthorizedRequest {

	// Constants
	protected static final String ERROR_MESSAGE_ALREADY_RATED = "Object has already been rated.";

	// Parameters
	public static final String PARAMETER_OBJECT_TYPE = "objecttype";
	public static final String PARAMETER_OBJECT_ID = "objectid";

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageRatingsAddRequest(String __clientId, String __apiKey) {
		super(__clientId, __apiKey);

		// Basic service configuration
		setParameter(PARAMETER_METHOD, "Ratings.Add");

		// Parameters
		setParameter(PARAMETER_OBJECT_TYPE, "");
		setParameter(PARAMETER_OBJECT_ID, "");
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	@Override
	protected boolean parseResponseData() {
		boolean isSuccess = super.parseResponseData();

		if (isSuccess) {

//			Log.v("LeverageRatingsAddRequest", "====> SIZE = " + getItems().size());
//			Log.v("LeverageRatingsAddRequest", "====> VALUE = " + getItems().size());

			// Not an error, but it may be an invalid rating
			if (getItems().size() > 0 && ((LeverageRating)getItems().get(0)).getValue() == 0) {
				// Rating was already added before, so re-classifies that as an error
				isSuccess = false;
				errorMessage = ERROR_MESSAGE_ALREADY_RATED;
			}
		}

		return isSuccess;
	}

	/*
	//Success response:
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):   <channel>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):     <title>Rating</title>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):     <link>http://web.services.leveragesoftware.com/rest/service.aspx</link>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):     <description>Rating</description>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):     <item>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       <title>Rating</title>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       <link>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       </link>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       <pubDate>Tue, 21 Feb 2012 08:58:00 GMT</pubDate>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       <description>Rating</description>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       <ls:ID xmlns:ls="http://leveragesoftware.com">600dd056aecb4532aad04d3c6b5b2edd</ls:ID>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       <ls:ObjectType xmlns:ls="http://leveragesoftware.com">Rating</ls:ObjectType>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       <ls:RatingObjectType xmlns:ls="http://leveragesoftware.com">StatusUpdate</ls:RatingObjectType>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       <ls:RatingObjectID xmlns:ls="http://leveragesoftware.com">d16e195858a245eaaab893413ec7f5a8</ls:RatingObjectID>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       <ls:CustomerID xmlns:ls="http://leveragesoftware.com">15d03982a26446f29b669bd28aba5f7f</ls:CustomerID>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):       <ls:Value xmlns:ls="http://leveragesoftware.com">1</ls:Value>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):     </item>
02-21 09:55:26.248: V/BreakroomBrowserObject(6297):   </channel>

	// In case it's already added:
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):   <channel>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):     <title>Rating</title>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):     <link>http://web.services.leveragesoftware.com/rest/service.aspx</link>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):     <description>Rating</description>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):     <item>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       <title>Rating</title>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       <link>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       </link>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       <pubDate>Tue, 21 Feb 2012 08:59:17 GMT</pubDate>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       <description>Rating</description>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       <ls:ID xmlns:ls="http://leveragesoftware.com">e91632a17b714f37ab3ee42dcfe4880d</ls:ID>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       <ls:ObjectType xmlns:ls="http://leveragesoftware.com">Rating</ls:ObjectType>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       <ls:RatingObjectType xmlns:ls="http://leveragesoftware.com">StatusUpdate</ls:RatingObjectType>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       <ls:RatingObjectID xmlns:ls="http://leveragesoftware.com">d16e195858a245eaaab893413ec7f5a8</ls:RatingObjectID>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       <ls:CustomerID xmlns:ls="http://leveragesoftware.com">15d03982a26446f29b669bd28aba5f7f</ls:CustomerID>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):       <ls:Value xmlns:ls="http://leveragesoftware.com">0</ls:Value>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):     </item>
02-21 09:56:42.848: V/BreakroomBrowserObject(6297):   </channel>

	*/
}

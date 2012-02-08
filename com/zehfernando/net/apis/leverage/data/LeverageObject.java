package com.zehfernando.net.apis.leverage.data;

import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

import com.zehfernando.data.xml.XML;
import com.zehfernando.net.apis.leverage.LeverageDataUtils;
import com.zehfernando.net.apis.leverage.enums.LeverageObjectTypes;

public class LeverageObject {

	// Properties
	protected String id;
	protected String title;
	protected String link;
	protected String description;
	protected Date pubDate;
	protected String type;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public LeverageObject() {
		id = "";
		title = "";
		link = "";
		description = "";
		pubDate = new Date();
		type = "";
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public static ArrayList<LeverageObject> fromXMLArrayList(ArrayList<XML> __items) {
		// Parses a list of items from the original (RDF-based) XML response

		ArrayList<LeverageObject> objects = new ArrayList<LeverageObject>();
		LeverageObject object;

		for (int i = 0; i < __items.size(); i++) {
			object = LeverageObject.fromXML(__items.get(i));
			if (object != null) objects.add(object);
		}

		object = null;

		return objects;
	}

	public static LeverageObject fromXML(XML __item) {
		// Parses one item from the original (RDF-based) XML response

		LeverageObject object = createObject(__item.getChild("ObjectType", "").getText());

		if (object != null) object.setDataFromXML(__item);

		return object;
	}

	public void setDataFromXML(XML __item) {

		title = __item.getChild("title", "").getText();
		link = __item.getChild("link", "").getText();
		description = __item.getChild("description", "").getText();
		pubDate = LeverageDataUtils.getDateFromString(__item.getChild("pubDate", "").getText());

		id = __item.getChild("ID", "").getText();
	}

	public static LeverageObject createObject(String __objectType) {
		// Create a new type based on the required ObjectType

		if (__objectType == LeverageObjectTypes.GROUP)						return new LeverageGroup();
		if (__objectType == LeverageObjectTypes.GROUP_DISCUSSION)			return new LeverageGroupDiscussion();
		if (__objectType == LeverageObjectTypes.GROUP_DISCUSSION_MESSAGE)	return new LeverageGroupDiscussionMessage();
		if (__objectType == LeverageObjectTypes.STATUS_UPDATE)				return new LeverageStatusUpdate();
		if (__objectType == LeverageObjectTypes.CUSTOMER)					return new LeverageCustomer();
//		if (__objectType == LeverageObjectTypes.CUSTOMER)					return new LeverageCustomerFile();
//		if (__objectType == LeverageObjectTypes.COMMENT)					return new LeverageComment();

		Log.e("LeverageObject", "Error! Tried creating an object of type " + __objectType + "!!!");

		return null;
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public String getType() {
		return type;
	}
}

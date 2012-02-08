package com.zehfernando.data.xml;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import android.util.Log;

public class XmlResourceParserHandler {

	// Content properties
	protected XML xml;

	// Stage properties
	protected ArrayList<XML> currentNodeTree;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public XmlResourceParserHandler(XML __xml) {
		xml = __xml;
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void startElement(XmlResourceParser __inputResource) {
//		Log.v("XMLParser", "startElement :: " + localName);

		// Start node
		XML node;

		if (currentNodeTree.size() > 0) {
			// Root XML already exists, so add it
			node = new XML();
			currentNodeTree.get(currentNodeTree.size()-1).addChild(node);
		} else {
			// Root XML doesn't exist, this is the parent
			node = xml;
		}

		// Create basic node
		node.setNodeName(__inputResource.getName());

		// Parse attributes
		int i;
		for (i = 0; i < __inputResource.getAttributeCount(); i++) {
			//Log.v("XMLParser", "  adding attribute to " + node.getNodeName() + " :: " + atts.getLocalName(i) + " = " + atts.getValue(i));
			node.addAttribute(new XMLAttribute(__inputResource.getAttributeName(i), __inputResource.getAttributeValue(i)));
		}

		// Add to current node tree
		currentNodeTree.add(node);

	}

	protected void endElement() {
//		Log.v("XMLParser", "endElement :: " + localName);
		currentNodeTree.remove(currentNodeTree.size()-1);
	}

	protected void characters(String __text) {
		//Log.v("XMLParseHandler", "[" + currentNodeTree.get(currentNodeTree.size()-1).getNodeName() + "] characters :: [" + new String(ch, start, length) + "]");
		currentNodeTree.get(currentNodeTree.size()-1).appendText(__text.intern());
	}

	protected void startDocument() {
		currentNodeTree = new ArrayList<XML>();
	}

	protected void endDocument() {
		// Do some finishing work if needed
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public void parse(XmlResourceParser __inputResource) {
		try {
			__inputResource.next();
			int eventType = __inputResource.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					startDocument();
				} else if (eventType == XmlPullParser.START_TAG) {
					startElement(__inputResource);
				} else if (eventType == XmlPullParser.END_TAG) {
					endElement();
				} else if (eventType == XmlPullParser.TEXT) {
					characters(__inputResource.getText());
				}

				eventType = __inputResource.next(); //Get next event from xml parser
			}

			endDocument();
		} catch (XmlPullParserException __e) {
			Log.e("XmlResourceParserHandler", "Error parsing the XML! " + __e);
			__e.printStackTrace();
		} catch (IOException __e) {
			Log.e("XmlResourceParserHandler", "Error parsing the XML! " + __e);
			__e.printStackTrace();
		}
	}
}

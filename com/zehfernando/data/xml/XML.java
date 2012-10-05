package com.zehfernando.data.xml;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

public class XML {

	// Constants
	public static final String VALUE_BOOLEAN_TRUE = "true";
	public static final String VALUE_BOOLEAN_FALSE = "false";

	// Properties
	private String nodeName;
	private String text;
	private String namespace;

	private boolean isTextNode;

	private ArrayList<XML> children;
	private ArrayList<XMLAttribute> attributes;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public XML() {
		this("", "", "");
	}

	public XML(String __source) {
		this(new InputSource(new StringReader(__source)));
	}

	public XML(String __nodeName, String __text) {
		this(__nodeName, __text, "");
	}

	public XML(String __nodeName, String __text, String __namespace) {
		children = new ArrayList<XML>();
		attributes = new ArrayList<XMLAttribute>();

		isTextNode = true;

		nodeName = __nodeName;
		text = __text;
		namespace = __namespace;
	}

	// XML xml = new XML(getResources().openRawResource(R.raw.test_file));
	public XML(InputStream __inputStream) {
		this(new InputSource(__inputStream));
	}

	// XML xml = new XML(new InputSource(getResources().openRawResource(R.xml.data_main_menu)));  (doesn't work?)
	public XML(InputSource __inputSource) {
		this();

		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			xr.setContentHandler(new XMLParseHandler(this));
			xr.parse(__inputSource);
		} catch (Exception __e) {
			Log.e("XML", "Error parsing the XML! " + __e + ": " + __e.getCause());
			__e.printStackTrace();
		}
	}

	// XML xml = new XML(getResources().getXml(R.xml.data_main_menu));
	public XML(XmlResourceParser __inputResource) {
		this();

		XmlResourceParserHandler parseHandler = new XmlResourceParserHandler(this);
		parseHandler.parse(__inputResource);
	}

	// XML xml = new XML(R.xml.data_main_menu, getBaseContext());
	public XML(int __xmlResourceId, Context __context) {
		this(__context.getResources().getXml(__xmlResourceId));
	}

	// XML xml = new XML("data_main_menu", getBaseContext());
//	public XML(String __xmlResourceIdName, Context __context) {
//		this(__context.getResources().getXml(__context.getResources().getIdentifier(__xmlResourceIdName, "xml", __context.getPackageName())));
//	}

	// New factories

	public static XML fromXMLResource(String __xmlResourceIdName, Context __context) {
		// This works, but is dangerous on low-end devices (low heap size) + Android 2.2 + big XMLs
		int resId = __context.getResources().getIdentifier(__xmlResourceIdName, "xml", __context.getPackageName());
		return new XML(__context.getResources().getXml(resId));
	}

	public static XML fromRawResource(String __rawResourceIdName, Context __context) {
		int resId = __context.getResources().getIdentifier(__rawResourceIdName, "raw", __context.getPackageName());
		return new XML(__context.getResources().openRawResource(resId));
	}

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void addTextChild(String __text) {
		// Add a new text node
		XML xml = new XML();
		xml.setText(__text);
		children.add(xml);
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public String getNodeName() {
		return nodeName.intern();
	}

	public void setNodeName(String __nodeName) {
		nodeName = __nodeName.intern();
	}

	public String getNamespace() {
		return namespace.intern();
	}

	public void setNamespace(String __namespace) {
		namespace = __namespace.intern();
	}

	public XML getChild(String __name) {
		// Return the first children of a given name
		ArrayList<XML> filteredChildren = getChildren(__name, 1);

		if (filteredChildren.size() == 0) {
			Log.e("XML", "Error: trying to read a children named [" + __name + "] that doesn't exist");
			throw new Error("Error: trying to read a children named [" + __name + "] that doesn't exist");
		}

		return filteredChildren.get(0);
	}

	public XML getChild(String __name, String __defaultText) {
		// Return the first children of a given name
		return getChild(__name, new XML(__name, __defaultText));
	}

	public XML getChild(String __name, XML __defaultXML) {
		// Return the first children of a given name
		ArrayList<XML> filteredChildren = getChildren(__name, 1);

		if (filteredChildren.size() == 0) return __defaultXML;

		return filteredChildren.get(0);
	}

	public ArrayList<XML> getChildren() {
		// Returns all children that are not txt children
		ArrayList<XML> filteredChildren = new ArrayList<XML>();
		int i;
		for (i = 0; i < children.size(); i++) {
			if (children.get(i).getNodeName().length() > 0) filteredChildren.add(children.get(i));
		}

		return filteredChildren;
	}

	public ArrayList<XML> getChildren(String __name) {
		return getChildren(__name, -1);
	}

	public ArrayList<XML> getChildren(String __name, int __maximumResults) {
		// Returns all children of a given name
		ArrayList<XML> filteredChildren = new ArrayList<XML>();

		if (__maximumResults == 0) return filteredChildren;

		int i;
		for (i = 0; i < children.size(); i++) {
			if (children.get(i).getNodeName().equals(__name)) {
				filteredChildren.add(children.get(i));
				if (filteredChildren.size() == __maximumResults) break;
			}
		}
		return filteredChildren;
	}

	public ArrayList<XMLAttribute> getAttributes() {
		// Returns all attributes
		return attributes;
	}

	public XMLAttribute getAttribute(String __name) {
		// Returns one specific attribute
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i).getName().equals(__name)) return attributes.get(i);
		}

		Log.e("XML", "Error: trying to read an attribute named [" + __name + "] that doesn't exist");
		throw new Error("Error: trying to read an attribute named [" + __name + "] that doesn't exist");
	}

	public XMLAttribute getAttribute(String __name, String __defaultText) {
		// Returns one specific attribute
		return getAttribute(__name, new XMLAttribute(__name, __defaultText));
	}

	public XMLAttribute getAttribute(String __name, XMLAttribute __defaultAttribute) {
		// Returns one specific attribute
		for (int i = 0; i < attributes.size(); i++) {
			if (attributes.get(i).getName().equals(__name)) return attributes.get(i);
		}

		return __defaultAttribute;
	}

	public void addChild(XML __xml) {
		if (isTextNode) {
			// It's a simple node; need to switch to a complex node
			if (text.length() > 0) {
				// Already has text

				// Create a simple node to hold it
				addTextChild(text);

				// Reset the text
				text = "";
			}
			isTextNode = false;
		}
		children.add(__xml);
	}

	public void addAttribute(XMLAttribute __attribute) {
		attributes.add(__attribute);
		//Log.v("XML", "  attribute :: " + __attribute.getName() + " = " + __attribute.getValue() + " =-----> " + attributes.size());
	}

	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getText() {
		if (isTextNode) {
			// It's a normal text node, just respond with the text
			return text.intern();
		} else {
			// It's a complex node, concatenates everything!
			String txt = "";
			for (int i = 0; i < children.size(); i++) {
				txt += children.get(i).getTextSource();
			}
			return txt.intern();
		}
	}

	public boolean getTextAsBoolean() {
		return getText().equalsIgnoreCase(VALUE_BOOLEAN_TRUE);
	}

	public float getTextAsFloat() {
		return Float.parseFloat(getText());
	}

	public long getTextAsLong() {
		return getTextAsLong(10);
	}

	public long getTextAsLong(int __base) {
		return Long.parseLong(getText(), __base);
	}

	public int getTextAsInt() {
		return getTextAsInt(10);
	}

	public int getTextAsInt(int __base) {
		return Integer.parseInt(getText(), __base);
	}

	public void setText(String __text) {
		if (!isTextNode) {
			// It's a complex node, reset everything first!
			children = new ArrayList<XML>();
			isTextNode = true;
		}

		// It's a text node, just set the new text
		text = __text.intern();
	}

	public void appendText(String __text) {
		// Appends text accordingly.
		if (isTextNode) {
			// It's a text node, just concatenates
			text += __text.intern();
		} else {
			// It's a complex node, need to create a new text node
			addTextChild(__text.intern());
		}
	}

	public String getTextSource() {
		// Returns the whole XML source
		String txt = "";

		// Opening tag
		if (nodeName.length() > 0) {
			txt += "<" + nodeName;
			//Log.v("XML", "  concatenating :: " + attributes.size());
			//txt += " " + attributes.size();
			for (int i = 0; i < attributes.size(); i++) {
				//Log.v("XML", "	  --> " + attributes.get(i).getName());
				txt += " " + attributes.get(i).getName() + "=\"" + attributes.get(i).getText() + "\"";
			}
			txt += ">";
		}

		// Content
		txt += getText();

		// Closing tag
		if (nodeName.length() > 0) {
			txt += "</" + nodeName + ">";
		}

		return txt;


	}

	public boolean getIsTextNode() {
		// Returns whether this is a simple, text node, or a complex node with children
		return isTextNode;
	}

}

package com.zehfernando.data.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParseHandler extends DefaultHandler {

	// Content properties
	protected XML xml;

	// Stage properties
	protected ArrayList<XML> currentNodeTree;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public XMLParseHandler(XML __xml) {
		xml = __xml;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
//		Log.v("XMLParser", "startElement :: " + localName);

		// Start node
		XML node;

		//Log.v("XMLParser", "namespaceURI " + namespaceURI + ", localName " + localName + ", qName " + qName);

		if (currentNodeTree.size() > 0) {
			// Root XML already exists, so add it
			node = new XML();
			currentNodeTree.get(currentNodeTree.size()-1).addChild(node);
		} else {
			// Root XML doesn't exist, this is the parent
			node = xml;
		}

		// Create basic node
		node.setNodeName(localName);

		String[] ns = qName.split(":");
		if (ns.length > 1) node.setNamespace(ns[0]);
		//Log.v("XMLParser", " ===> " + node.getNamespace());

		// Parse attributes
		int i;
		for (i = 0; i < atts.getLength(); i++) {
			//Log.v("XMLParser", "  adding attribute to " + node.getNodeName() + " :: " + atts.getLocalName(i) + " = " + atts.getValue(i));
			node.addAttribute(new XMLAttribute(atts.getLocalName(i), atts.getValue(i)));
		}

		// Add to current node tree
		currentNodeTree.add(node);

	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
//		Log.v("XMLParser", "endElement :: " + localName);
		currentNodeTree.remove(currentNodeTree.size()-1);
	}

	// ch is "<tag>characters</tag>"
	@Override
	public void characters(char ch[], int start, int length) {
		//Log.v("XMLParseHandler", "[" + currentNodeTree.get(currentNodeTree.size()-1).getNodeName() + "] characters :: [" + new String(ch, start, length) + "]");
		String __text = new String(ch, start, length).intern();
		currentNodeTree.get(currentNodeTree.size()-1).appendText(__text);
	}

	@Override
	public void startDocument() throws SAXException {
		currentNodeTree = new ArrayList<XML>();
	}

	@Override
	public void endDocument() throws SAXException {
		// Do some finishing work if needed
	}
}

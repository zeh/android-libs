package com.zehfernando.data.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParseHandler extends DefaultHandler {

	// Content properties
	private XML xml;

	// Stage properties
	private ArrayList<XML> currentNodeTree;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public XMLParseHandler(XML __xml) {
		xml = __xml;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		// Start a node
		XML node;

		if (currentNodeTree.size() > 0) {
			// Root XML already exists, so add to it
			node = new XML();
			node.setNodeName(localName);
			currentNodeTree.get(currentNodeTree.size()-1).addChild(node);
		} else {
			// Root XML doesn't exist, this is the parent
			node = xml;
			node.setNodeName(localName);
		}

		if (qName.indexOf(":", 1) > -1) {
			String[] ns = qName.split(":");
			node.setNamespace(ns[0]);
		}

		// Parse attributes
		int i;
		int l = atts.getLength();
		for (i = 0; i < l; i++) {
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
		currentNodeTree.get(currentNodeTree.size()-1).appendText(new String(ch, start, length));
	}

	@Override
	public void startDocument() throws SAXException {
		currentNodeTree = new ArrayList<XML>();
	}

	@Override
	public void endDocument() throws SAXException {
		// Do some finishing work if needed
		currentNodeTree = null;
		xml = null;
	}
}

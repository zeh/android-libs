package com.zehfernando.data.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.zehfernando.utils.F;

public class XMLErrorHandler implements ErrorHandler {

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public XMLErrorHandler() {
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public void error(SAXParseException __arg0) throws SAXException {
		F.warn("Error parsing the XML!!!!! " + __arg0);
	}

	@Override
	public void fatalError(SAXParseException __exception) throws SAXException {
		F.warn("Fatal error parsing the XML!!!!! " + __exception);
	}

	@Override
	public void warning(SAXParseException __exception) throws SAXException {
		F.warn("Warning parsing the XML!!!!! " + __exception);
	}

}

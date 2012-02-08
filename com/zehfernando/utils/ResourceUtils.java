package com.zehfernando.utils;

import android.content.Context;
import android.content.res.XmlResourceParser;

public class ResourceUtils {

	public static XmlResourceParser getXML(Context __context, String __name) {
		// Based on a given name (e.g. "myxml") returns the XML resource (R.xml.myxml)
		return __context.getResources().getXml(getXMLResourceIdForName(__context, __name));
	}

	public static int getXMLResourceIdForName(Context __context, String __name) {
		return __context.getResources().getIdentifier(__name, "xml", __context.getPackageName());
	}
}

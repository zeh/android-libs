package com.zehfernando.data.xml;

public class XMLAttribute {

	// Properties
	private String name;
	private String text;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public XMLAttribute() {
		this ("", "");
	}

	public XMLAttribute(String __name, String __value) {
		name = __name;
		text = __value;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public String getName() {
		return name;
	}

	public void setName(String __name) {
		name = __name;
	}

	public String getText() {
		return text;
	}

	public boolean getTextAsBoolean() {
		return getText().equalsIgnoreCase(XML.VALUE_BOOLEAN_TRUE);
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

	public void setText(String __value) {
		text = __value;
	}
}

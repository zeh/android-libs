package com.zehfernando.display.templates;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class RelativeLayoutListItem extends RelativeLayout {

	// Constants

	// Properties
	private String  valueString;
	private long    valueLong;
	private boolean valueBoolean;

	protected int  resourceId;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public RelativeLayoutListItem(Context __context) {
		super(__context);

		setDefaultProperties();
		inflate();
	}


	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	protected void setDefaultProperties() {
		// Extend this
		resourceId = android.R.layout.activity_list_item;
	}

	private void inflate() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(resourceId, this);

	}


	// ================================================================================================================
	// ACCESSOR INTERFACE ---------------------------------------------------------------------------------------------

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String __value) {
		valueString = __value;
	}

	public long getValueLong() {
		return valueLong;
	}

	public void setValueLong(long __value) {
		valueLong = __value;
	}

	public boolean getValueBoolean() {
		return valueBoolean;
	}

	public void setValueBoolean(boolean __value) {
		valueBoolean = __value;
	}
}

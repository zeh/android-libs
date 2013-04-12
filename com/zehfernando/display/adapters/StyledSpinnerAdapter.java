package com.zehfernando.display.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StyledSpinnerAdapter extends ArrayAdapter<String> {

	// Properties
	protected int textViewResourceId = 0;
	protected int dropDownResourceId = 0;

	// ================================================================================================================
	// CONSTRUCTOR ----------------------------------------------------------------------------------------------------

	public StyledSpinnerAdapter(Context __context, int __textViewResourceId, int __dropDownResourceId, String[] __objects) {
		super(__context, __textViewResourceId, __objects);
		textViewResourceId = __textViewResourceId;
		dropDownResourceId = __dropDownResourceId;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	@Override
	public View getDropDownView(int __position, View __convertView, ViewGroup __parent) {
		return getCustomView(__position, __convertView, __parent, true);
	}

	@Override
	public View getView(int __position, View __convertView, ViewGroup __parent) {
		return getCustomView(__position, __convertView, __parent, false);
	}

	public View getCustomView(int __position, View __convertView, ViewGroup __parent, boolean __isDropDown) {
		int resourceId = __isDropDown ? dropDownResourceId : textViewResourceId;

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(resourceId, __parent, false);

		Object item = getItem(__position);
		if (item instanceof CharSequence) {
			getTextView(row).setText((CharSequence)item);
		} else {
			getTextView(row).setText(item.toString());
		}

		return row;
	}

	public TextView getTextView(View __view) {
		// TODO: actually look for the textview, instead of using an arbitrary text id
		return null;
		//return (TextView) __view.findViewById(R.id.textItem);
	}
}

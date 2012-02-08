package com.zehfernando.net.apis.leverage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class LeverageDataUtils {

	// Properties
	protected static DateFormat inputFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH); // Thu, 25 Aug 2011 01:59:37 GMT
	//protected static DateFormat outputFormatter = new SimpleDateFormat("MM.yyyy");

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public static Date getDateFromString(String __date) {
		try {
			return inputFormatter.parse(__date);
		} catch (ParseException e) {
			Log.e("LeverageDataUtils", "Impossible to parse date [" + __date + "]!");
			return null;
		}
//		date = outputFormatter.format(inputFormatter.parse(pubDate));
	}
}
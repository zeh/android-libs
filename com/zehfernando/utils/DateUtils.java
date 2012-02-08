package com.zehfernando.utils;

public class DateUtils {

	public static String getDescriptiveDifference(long __time) {
		// TODO: rename this, add parameters
		// Returns a friendly description of a time difference ("2 hours", "1 day", "10 seconds", "1 year" etc)

		// Full data
		float fseconds = __time / 1000F;
		float fminutes = fseconds / 60F;
		float fhours = fminutes / 60F;
		float fdays = fhours / 24F;
		//var weeks:Number = days / 7;
		float fmonths = fdays / (365.25F / 12F);
		float fyears = fdays / 365.25F;

		int seconds = (int)fseconds;
		int minutes = (int)fminutes;
		int hours = (int)fhours;
		int days = (int)fdays;
		int months = (int)fmonths;
		int years = (int)fyears;

		if (years > 1)		return years + " years";
		if (years == 1)		return years + " year";
		if (months > 1)		return months + " months";
		if (months == 1)	return months + " month";
		//if (weeks > 1)		return weeks + " weeks";
		//if (weeks == 1)		return weeks + " week";
		if (days > 1)		return days + " days";
		if (days == 1)		return days + " day";
		if (hours > 1)		return hours + " hours";
		if (hours == 1)		return hours + " hour";
		if (minutes > 1)	return minutes + " minutes";
		if (minutes == 1)	return minutes + " minute";
		if (seconds > 1)	return seconds + " seconds";
		if (seconds == 1)	return seconds + " second";

		return "";
	}
}

package com.zehfernando.utils;

import java.util.HashMap;
import java.util.regex.Pattern;

public class FractionUtils {

	private static boolean initialized = false;

	private static HashMap<String,String> fractions;

	private static void init() {
		if (!initialized) {
			initialized = true;
			fractions = new HashMap<String,String>();
			fractions.put("1/8", "⅛");
			fractions.put("1/4", "¼");
			fractions.put("1/3", "⅓");
			fractions.put("3/8", "⅜");
			fractions.put("1/2", "½");
			fractions.put("5/8", "⅝");
			fractions.put("2/3", "⅔");
			fractions.put("3/4", "¾");
			fractions.put("7/8", "⅞");
		}
	}

	public static String fromString(String __text) {
		// Converts 1/2 to ½
		init();
		if (fractions.containsKey(__text)) return fractions.get(__text);
		return "";
	}

	public static String toString(String __fraction) {
		// Converts ½ to 1/2
		init();
		for (String key:fractions.keySet()) {
			if (fractions.get(key).equals(__fraction)) return key;
		}
		return "";
	}

	public static String clearFractions(String __text) {
		// Removes all fraction characters from a string
		for (String key:fractions.keySet()) {
			__text = __text.replace(fractions.get(key), "");
		}
		return __text;
	}

	public static boolean containsShortFractions(String __text) {
		for (String key:fractions.keySet()) {
			if (__text.indexOf(fractions.get(key)) > -1) return true;
		}
		return false;
	}

	public static float toFloat(String __numberWithShortFractions) {
		// Convert strings to correct floats
		// Example: "⅓" to 0.5, "1¾" to 1.75, "2.2" to 2.2

		if (__numberWithShortFractions.indexOf(".") > -1) {
			// Normal decimal
			try {
				return Float.parseFloat(__numberWithShortFractions);
			} catch(NumberFormatException __e) {
				return 0;
			}
		} else if (containsShortFractions(__numberWithShortFractions)) {
			// Contains fractions, so add all numbers
			String allowedChars = "0123456789";
			String finalString = "";
			String tChar;
			float valueToAdd = 0;
			String[] ops;
			for (int i = 0; i < __numberWithShortFractions.length(); i++) {
				tChar = __numberWithShortFractions.substring(i, i+1);
				if (allowedChars.indexOf(tChar) > -1) {
					// A number char
					finalString += tChar;
				} else {
					for (String key:fractions.keySet()) {
						if (__numberWithShortFractions.substring(i, i+1).equals(fractions.get(key))) {
							// A fraction
							ops = key.split(Pattern.quote("/"));
							valueToAdd = Float.parseFloat(ops[0]) / Float.parseFloat(ops[1]);
							break;
						}
					}
				}
			}
			if (finalString.length() == 0) finalString = "0";
			return Integer.parseInt(finalString, 10) + valueToAdd;
		} else {
			// Standard number
			if (__numberWithShortFractions.length() == 0) __numberWithShortFractions = "0";
			return Integer.parseInt(__numberWithShortFractions, 10);
		}
	}
}

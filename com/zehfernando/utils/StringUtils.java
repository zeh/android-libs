package com.zehfernando.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import android.util.Log;
import android.util.Patterns;

public class StringUtils {

	// Public

	public static String calculateMD5(String __string) {
		// Calculate an unique key based on a string

		// Uses MD5
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			Log.e("FileCache", "Error trying to get MDR5 hash for file: " + e);
			return null;
		}

		md.update(__string.getBytes());
		byte[] hash = md.digest();
		String key = "";
		for (int i = 0; i < hash.length; i++) {
			key += Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1);
		}
		return key;
	}

	public static boolean isValidEmail(String __email) {
		return Patterns.EMAIL_ADDRESS.matcher(__email).matches();
	}

	public static String capitalizeWords(String __text) {
		char[] chars = __text.toLowerCase().toCharArray();
		boolean capitalize = true;
		for (int i = 0; i < chars.length; i++) {
			if (capitalize && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				capitalize = false;
			} else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i]=='\'') {
				capitalize = true;
			}
		}
		return String.valueOf(chars);
	}

	public static String decimalsToFractions(float __value) {
		// Returns n.25 as ¼

		// Split string
		String valueFraction = "";

		String[] splitValue = Float.toString(__value).split("\\.");
		//valueFull = splitValue[0];

		if (splitValue.length > 1) {
			// Has a fraction
			String valueDecimals = splitValue[1];

			if (valueDecimals.equals("25")) {
				valueFraction = "¼";
			} else if (valueDecimals.equals("5")) {
				valueFraction = "½";
			} else if (valueDecimals.equals("75")) {
				valueFraction = "¾";
			} else if (valueDecimals.equals("0")) {
				valueFraction = "";
			} else {
				valueFraction = "." + valueDecimals;
			}
		}

		return valueFraction;
	}

	public static String fromStub(String __text) {
		// Converts a stub text ("some-text") to readable text("Some Text")
		// Also removes all other path segments

		String[] parts;

		// Get last part of uri
		parts = __text.split(Pattern.quote("/"));
		if (parts.length > 1) __text = parts[parts.length - 1];

		// Remove extensions
		parts = __text.split(Pattern.quote("."));
		if (parts.length > 0) __text = parts[0];

		__text = __text.replace("-", " ");
		__text = capitalizeWords(__text);

		return __text;
	}
}

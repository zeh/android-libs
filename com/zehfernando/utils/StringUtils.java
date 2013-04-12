package com.zehfernando.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import android.util.Base64;
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

	public static String toBase64(String __text) {
		return Base64.encodeToString(__text.getBytes(), Base64.NO_WRAP);
//		try {
//			return Base64.encodeToString(__text.getBytes("UTF-8"), Base64.DEFAULT);
//		} catch (UnsupportedEncodingException e) {
//			F.error("Cannot convert; UnsupportedEncodingException " + e);
//			return __text;
//		}
	}

	public static String capitalizeWords(String __text) {
		return capitalizeWords(__text, true);
	}

	public static String capitalizeWords(String __text, boolean __convertToLowercaseFirst) {
		char[] chars = __convertToLowercaseFirst ? __text.toLowerCase().toCharArray() : __text.toCharArray();
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

	public static String toCapitalCase(String __text) {
		boolean afterWhiteSpace = true;
		char[] chars = __text.toLowerCase().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])) {
				if (afterWhiteSpace) {
					chars[i] = Character.toUpperCase(chars[i]);
					afterWhiteSpace = false;
				}
			} else {
				afterWhiteSpace = Character.isWhitespace(chars[i]);
			}
		}
		return new String(chars);
	}

	public static String toStub(String __text) {
		// Converts text ("Hello World!") to stub text ("hello-world")

		__text = __text.toLowerCase();
		__text = __text.replace(" ", "-");
		__text = __text.replace(",", "-");
		__text = __text.replace(".", "-");
		__text = __text.replace(":", "-");
		__text = __text.replace("/", "-");
		__text = __text.replace("!", "-");
		__text = __text.replace("--", "-");
		__text = __text.replace("--", "-");

		return __text;
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

	public static String decodeHTMLEntities(String __source) {
		// Replaces the content with proper HTML entities
		// http://www.degraeve.com/reference/specialcharacters.php

		// Missing: &#nnn;

		// Just a quick copy&paste&regex
		__source = __source.replace("&nbsp;", " ");
		__source = __source.replace("&lsquo;", "‘");
		__source = __source.replace("&rsquo;", "’");
		__source = __source.replace("&sbquo;", "‚");
		__source = __source.replace("&ldquo;", "“");
		__source = __source.replace("&rdquo;", "”");
		__source = __source.replace("&bdquo;", "„");
		__source = __source.replace("&dagger;", "†");
		__source = __source.replace("&Dagger;", "‡");
		__source = __source.replace("&permil;", "‰");
		__source = __source.replace("&lsaquo;", "‹");
		__source = __source.replace("&rsaquo;", "›");
		__source = __source.replace("&spades;", "♠");
		__source = __source.replace("&clubs;", "♣");
		__source = __source.replace("&hearts;", "♥");
		__source = __source.replace("&diams;", "♦");
		__source = __source.replace("&oline;", "‾");
		__source = __source.replace("&larr;", "←");
		__source = __source.replace("&uarr;", "↑");
		__source = __source.replace("&rarr;", "→");
		__source = __source.replace("&darr;", "↓");
		__source = __source.replace("&trade;", "™");
		__source = __source.replace("&quot;", "\"");
		__source = __source.replace("&amp;", "&");
		__source = __source.replace("&frasl;", "/");
		__source = __source.replace("&lt;", "<");
		__source = __source.replace("&gt;", ">");
		__source = __source.replace("&hellip;", "…");
		__source = __source.replace("&ndash;", "–");
		__source = __source.replace("&mdash;", "—");
		__source = __source.replace("&iexcl;", "¡");
		__source = __source.replace("&cent;", "¢");
		__source = __source.replace("&pound;", "£");
		__source = __source.replace("&curren;", "¤");
		__source = __source.replace("&yen;", "¥");
		__source = __source.replace("&brvbar;", "¦");
		__source = __source.replace("&brkbar;", "¦");
		__source = __source.replace("&sect;", "§");
		__source = __source.replace("&uml;", "¨");
		__source = __source.replace("&die;", "¨");
		__source = __source.replace("&copy;", "©");
		__source = __source.replace("&ordf;", "ª");
		__source = __source.replace("&laquo;", "«");
		__source = __source.replace("&not;", "¬");
		__source = __source.replace("&shy;", "");
		__source = __source.replace("&reg;", "®");
		__source = __source.replace("&macr;", "¯");
		__source = __source.replace("&hibar;", "¯");
		__source = __source.replace("&deg;", "°");
		__source = __source.replace("&plusmn;", "±");
		__source = __source.replace("&sup2;", "²");
		__source = __source.replace("&sup3;", "³");
		__source = __source.replace("&acute;", "´");
		__source = __source.replace("&micro;", "µ");
		__source = __source.replace("&para;", "¶");
		__source = __source.replace("&middot;", "·");
		__source = __source.replace("&cedil;", "¸");
		__source = __source.replace("&sup1;", "¹");
		__source = __source.replace("&ordm;", "º");
		__source = __source.replace("&raquo;", "»");
		__source = __source.replace("&frac14;", "¼");
		__source = __source.replace("&frac12;", "½");
		__source = __source.replace("&frac34;", "¾");
		__source = __source.replace("&iquest;", "¿");
		__source = __source.replace("&Agrave;", "À");
		__source = __source.replace("&Aacute;", "Á");
		__source = __source.replace("&Acirc;", "Â");
		__source = __source.replace("&Atilde;", "Ã");
		__source = __source.replace("&Auml;", "Ä");
		__source = __source.replace("&Aring;", "Å");
		__source = __source.replace("&AElig;", "Æ");
		__source = __source.replace("&Ccedil;", "Ç");
		__source = __source.replace("&Egrave;", "È");
		__source = __source.replace("&Eacute;", "É");
		__source = __source.replace("&Ecirc;", "Ê");
		__source = __source.replace("&Euml;", "Ë");
		__source = __source.replace("&Igrave;", "Ì");
		__source = __source.replace("&Iacute;", "Í");
		__source = __source.replace("&Icirc;", "Î");
		__source = __source.replace("&Iuml;", "Ï");
		__source = __source.replace("&ETH;", "Ð");
		__source = __source.replace("&Ntilde;", "Ñ");
		__source = __source.replace("&Ograve;", "Ò");
		__source = __source.replace("&Oacute;", "Ó");
		__source = __source.replace("&Ocirc;", "Ô");
		__source = __source.replace("&Otilde;", "Õ");
		__source = __source.replace("&Ouml;", "Ö");
		__source = __source.replace("&times;", "×");
		__source = __source.replace("&Oslash;", "Ø");
		__source = __source.replace("&Ugrave;", "Ù");
		__source = __source.replace("&Uacute;", "Ú");
		__source = __source.replace("&Ucirc;", "Û");
		__source = __source.replace("&Uuml;", "Ü");
		__source = __source.replace("&Yacute;", "Ý");
		__source = __source.replace("&THORN;", "Þ");
		__source = __source.replace("&szlig;", "ß");
		__source = __source.replace("&agrave;", "à");
		__source = __source.replace("&aacute;", "á");
		__source = __source.replace("&acirc;", "â");
		__source = __source.replace("&atilde;", "ã");
		__source = __source.replace("&auml;", "ä");
		__source = __source.replace("&aring;", "å");
		__source = __source.replace("&aelig;", "æ");
		__source = __source.replace("&ccedil;", "ç");
		__source = __source.replace("&egrave;", "è");
		__source = __source.replace("&eacute;", "é");
		__source = __source.replace("&ecirc;", "ê");
		__source = __source.replace("&euml;", "ë");
		__source = __source.replace("&igrave;", "ì");
		__source = __source.replace("&iacute;", "í");
		__source = __source.replace("&icirc;", "î");
		__source = __source.replace("&iuml;", "ï");
		__source = __source.replace("&eth;", "ð");
		__source = __source.replace("&ntilde;", "ñ");
		__source = __source.replace("&ograve;", "ò");
		__source = __source.replace("&oacute;", "ó");
		__source = __source.replace("&ocirc;", "ô");
		__source = __source.replace("&otilde;", "õ");
		__source = __source.replace("&ouml;", "ö");
		__source = __source.replace("&divide;", "÷");
		__source = __source.replace("&oslash;", "ø");
		__source = __source.replace("&ugrave;", "ù");
		__source = __source.replace("&uacute;", "ú");
		__source = __source.replace("&ucirc;", "û");
		__source = __source.replace("&uuml;", "ü");
		__source = __source.replace("&yacute;", "ý");
		__source = __source.replace("&thorn;", "þ");
		__source = __source.replace("&yuml;", "ÿ");
		__source = __source.replace("&Alpha;", "Α");
		__source = __source.replace("&alpha;", "α");
		__source = __source.replace("&Beta;", "Β");
		__source = __source.replace("&beta;", "β");
		__source = __source.replace("&Gamma;", "Γ");
		__source = __source.replace("&gamma;", "γ");
		__source = __source.replace("&Delta;", "Δ");
		__source = __source.replace("&delta;", "δ");
		__source = __source.replace("&Epsilon;", "Ε");
		__source = __source.replace("&epsilon;", "ε");
		__source = __source.replace("&Zeta;", "Ζ");
		__source = __source.replace("&zeta;", "ζ");
		__source = __source.replace("&Eta;", "Η");
		__source = __source.replace("&eta;", "η");
		__source = __source.replace("&Theta;", "Θ");
		__source = __source.replace("&theta;", "θ");
		__source = __source.replace("&Iota;", "Ι");
		__source = __source.replace("&iota;", "ι");
		__source = __source.replace("&Kappa;", "Κ");
		__source = __source.replace("&kappa;", "κ");
		__source = __source.replace("&Lambda;", "Λ");
		__source = __source.replace("&lambda;", "λ");
		__source = __source.replace("&Mu;", "Μ");
		__source = __source.replace("&mu;", "μ");
		__source = __source.replace("&Nu;", "Ν");
		__source = __source.replace("&nu;", "ν");
		__source = __source.replace("&Xi;", "Ξ");
		__source = __source.replace("&xi;", "ξ");
		__source = __source.replace("&Omicron;", "Ο");
		__source = __source.replace("&omicron;", "ο");
		__source = __source.replace("&Pi;", "Π");
		__source = __source.replace("&pi;", "π");
		__source = __source.replace("&Rho;", "Ρ");
		__source = __source.replace("&rho;", "ρ");
		__source = __source.replace("&Sigma;", "Σ");
		__source = __source.replace("&sigma;", "σ");
		__source = __source.replace("&Tau;", "Τ");
		__source = __source.replace("&tau;", "τ");
		__source = __source.replace("&Upsilon;", "Υ");
		__source = __source.replace("&upsilon;", "υ");
		__source = __source.replace("&Phi;", "Φ");
		__source = __source.replace("&phi;", "φ");
		__source = __source.replace("&Chi;", "Χ");
		__source = __source.replace("&chi;", "χ");
		__source = __source.replace("&Psi;", "Ψ");
		__source = __source.replace("&psi;", "ψ");
		__source = __source.replace("&Omega;", "Ω");
		__source = __source.replace("&omega;", "ω");
		__source = __source.replace("&#9679;", "●");
		__source = __source.replace("&#8226;", "•");

		return __source;
	}
}

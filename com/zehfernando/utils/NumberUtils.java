package com.zehfernando.utils;

public class NumberUtils {

	// Constants
	protected final static RomanNumber[] romanNumbers = {
		new RomanNumber(1000, "M"),
		new RomanNumber(900, "CM"),
		new RomanNumber(500, "D"),
		new RomanNumber(400, "CD"),
		new RomanNumber(100, "C"),
		new RomanNumber(90, "XC"),
		new RomanNumber(50, "L"),
		new RomanNumber(40, "XL"),
		new RomanNumber(10, "X"),
		new RomanNumber(9, "IX"),
		new RomanNumber(5, "V"),
		new RomanNumber(4, "IV"),
		new RomanNumber(1, "I")
	};

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public static String numberToRomanNumerals(String __number) {
		return numberToRomanNumerals(Integer.parseInt(__number));
	}

	public static String numberToRomanNumerals(int __number) {
		// Does the conversion
		if (__number == 0) return "0";

		boolean isNegative = false;
		if (__number < 0) {
			__number = Math.abs(__number);
			isNegative = true;
		}

		String romanNumber = "";
		for (int i = 0; i < romanNumbers.length; i++) {
			while (__number >= romanNumbers[i].value) {
				romanNumber += romanNumbers[i].character;
				__number -= romanNumbers[i].value;
			}
		}

		if (isNegative) romanNumber = "-" + romanNumber;

		return romanNumber;
	}
}

class RomanNumber {
	public int value;
	public String character;

	public RomanNumber (int __value, String __character) {
		value = __value;
		character = __character;
	}
}

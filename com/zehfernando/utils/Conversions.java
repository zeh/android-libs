package com.zehfernando.utils;

public class Conversions {

	// Constants
	private final static float KG_TO_LB = 2.20462f;
	private final static float FT_TO_CM = 30.48f;
	private final static float FT_TO_IN = 12f;
	private final static float IN_TO_CM = 2.54f;

//	F.log("1 cm = " + Conversions.CM2FT(1) + "ft");
//	F.log("1 cm = " + Conversions.CM2IN(1) + "in");
//	F.log("1 in = " + Conversions.IN2CM(1) + "cm");
//	F.log("1 in = " + Conversions.IN2FT(1) + "ft");
//	F.log("1 ft = " + Conversions.FT2CM(1) + "cm");
//	F.log("1 ft = " + Conversions.FT2IN(1) + "in");
//	F.log("1 kg = " + Conversions.KG2LB(1) + "lb");
//	F.log("1 lb = " + Conversions.LB2KG(1) + "kg");

	// ================================================================================================================
	// STATIC INTERFACE -----------------------------------------------------------------------------------------------

	public static final float KG2LB(float __weightInKG) {
		return __weightInKG * KG_TO_LB;
	}

	public static final float LB2KG(float __weightInLB) {
		return __weightInLB / KG_TO_LB;
	}

	public static final float FT2CM(float __distanceInFT) {
		return __distanceInFT * FT_TO_CM;
	}

	public static final float CM2FT(float __distanceInCM) {
		return __distanceInCM / FT_TO_CM;
	}

	public static final float FT2IN(float __distanceInFT) {
		return __distanceInFT * FT_TO_IN;
	}

	public static final float IN2FT(float __distanceInIN) {
		return __distanceInIN / FT_TO_IN;
	}

	public static final float CM2IN(float __distanceInCM) {
		return __distanceInCM / IN_TO_CM;
	}

	public static final float IN2CM(float __distanceInIN) {
		return __distanceInIN * IN_TO_CM;
	}
}

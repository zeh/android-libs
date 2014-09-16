package com.zehfernando.utils;

import android.util.Log;

// Ugh... global functions. static imports are not automatic
// This is ugly, need to refactor

public class F {

	// Can be used as an extra way of filtering logs
	public static String extraDecoration = null;

	public static void log() {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();

		String[] classNames = stack[3].getClassName().split("\\.");
		String className = classNames[classNames.length - 1];
		String methodName = stack[3].getMethodName();

		String tag = className+"."+methodName+"()";

		String s = "-";
		if (extraDecoration != null) s = extraDecoration + s;
		Log.v(tag, s);
	}

	public static void logStackTrace() {
		String txt = "";
		if (extraDecoration != null) txt = extraDecoration + txt;
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();

		for (int i = 3; i < stack.length; i++) {
			txt += "   ";
			txt += stack[i].getClassName()+"." + stack[i].getMethodName() + " @ " + stack[i].getLineNumber();
			txt += "\n";
		}

		Log.v("", txt);
	}

	public static void log(String __message) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();

		String[] classNames = stack[3].getClassName().split("\\.");
		String className = classNames[classNames.length - 1];
		String methodName = stack[3].getMethodName();

		String tag = className+"."+methodName+"()";
		if (extraDecoration != null) __message = extraDecoration + __message;
		Log.v(tag, __message);
	}

	public static void debug(String __message) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();

		String[] classNames = stack[3].getClassName().split("\\.");
		String className = classNames[classNames.length - 1];
		String methodName = stack[3].getMethodName();

		String tag = className+"."+methodName+"()";
		if (extraDecoration != null) __message = extraDecoration + __message;
		Log.d(tag, __message);
	}

	public static void info(String __message) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();

		String[] classNames = stack[3].getClassName().split("\\.");
		String className = classNames[classNames.length - 1];
		String methodName = stack[3].getMethodName();

		String tag = className+"."+methodName+"()";
		if (extraDecoration != null) __message = extraDecoration + __message;
		Log.i(tag, __message);
	}

	public static void warn(String __message) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();

		String[] classNames = stack[3].getClassName().split("\\.");
		String className = classNames[classNames.length - 1];
		String methodName = stack[3].getMethodName();

		String tag = className+"."+methodName+"()";
		if (extraDecoration != null) __message = extraDecoration + __message;
		Log.w(tag, __message);
	}

	public static void error(String __message) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();

		String[] classNames = stack[3].getClassName().split("\\.");
		String className = classNames[classNames.length - 1];
		String methodName = stack[3].getMethodName();

		String tag = className+"."+methodName+"()";
		if (extraDecoration != null) __message = extraDecoration + __message;
		Log.e(tag, __message);
	}
}

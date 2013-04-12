package com.zehfernando.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;

public class ActivityKiller {

	// Creates groups of activities that can be killed later.
	// Provides a better (custom) control over activity life cycle when you want to kill groups of activities so the user cannot go back.

	// Constants
	public static final String GROUP_WELCOME_LOGIN = "welcome_login";
	public static final String GROUP_WELCOME_PROFILE_CREATION = "welcome_profile_creation";
	public static final String GROUP_WELCOME_GOAL_SETUP = "welcome_goal_setup";
	public static final String GROUP_WELCOME_GOAL_ADDITIONAL = "welcome_goal_additional";
	public static final String GROUP_LOG_TRACKING = "log_tracking";
	public static final String GROUP_LOG_TRACKING_OVERVIEW = "log_tracking_overview";
	public static final String GROUP_DASHBOARD_DAY = "dashboard_day";
	public static final String GROUP_OTHER = "other";

	// A shitty way to control state
	public static final int MODE_FROM_SETTINGS = 100;			// User came from the settings screen

	// Properties
	private static final Map<String,ArrayList<Activity>> groups = new HashMap<String,ArrayList<Activity>>();
	private static int mode = 0;

	// ================================================================================================================
	// INTERNAL INTERFACE ---------------------------------------------------------------------------------------------

	private static ArrayList<Activity> getGroup(String __groupName) {
		ArrayList<Activity> group = groups.get(__groupName);
		if (group == null) {
			// Group doesn't exist, needs to be created
			group = new ArrayList<Activity>();
			groups.put(__groupName, group);
		}
		return group;
	}

	// ================================================================================================================
	// PUBLIC INTERFACE -----------------------------------------------------------------------------------------------

	public static void addActivityToGroup(String __groupName, Activity __activity) {
		// Add one activity to a group of activities that will be killed later
		getGroup(__groupName).add(__activity);
	}

	public static void killGroup(String __groupName) {
		// Kills all activities in a given group
		ArrayList<Activity> group = getGroup(__groupName);
		while (group.size() > 0) {
			if (group.get(0) != null) group.get(0).finish();
			group.remove(0);
		}
	}

	public static void killAllActivities() {
		F.log("==== KILLING ALL ACTIVITIES");
		Iterator<Entry<String, ArrayList<Activity>>> it = groups.entrySet().iterator();
		int i;
		while (it.hasNext()) {
			Map.Entry<String, ArrayList<Activity>> pairs = it.next();
			for (i = 0; i < pairs.getValue().size(); i++) {
				if (pairs.getValue().get(i) != null) {
					F.log("====== REMOVING => " + pairs.getValue().get(i));
					pairs.getValue().get(i).finish();
					pairs.getValue().remove(i);
					i--;
				} else {
					pairs.getValue().remove(i);
					i--;
				}
			}
		}
	}

	public static void killAllActivitiesExcept(Class<?> __activityToKeep) {
		// Kills all existing activities except for the class passed
		F.log("==== KILLING ALL ACTIVITIES EXCEPT " + __activityToKeep);
		Iterator<Entry<String, ArrayList<Activity>>> it = groups.entrySet().iterator();
		int i;
		while (it.hasNext()) {
			Map.Entry<String, ArrayList<Activity>> pairs = it.next();
			for (i = 0; i < pairs.getValue().size(); i++) {
				if (pairs.getValue().get(i) != null) {
					if (pairs.getValue().get(i).getClass().equals(__activityToKeep)) {
						F.log("====== NOT REMOVING => " + pairs.getValue().get(i));
					} else {
						F.log("====== REMOVING => " + pairs.getValue().get(i));
						pairs.getValue().get(i).finish();
						pairs.getValue().remove(i);
						i--;
					}
				} else {
					pairs.getValue().remove(i);
					i--;
				}
			}
			//it.remove();
		}
	}

	public static int getMode() {
		return mode;
	}

	public static void setMode() {
		mode = 0;
	}

	public static void setMode(int __mode) {
		mode = __mode;
	}
}

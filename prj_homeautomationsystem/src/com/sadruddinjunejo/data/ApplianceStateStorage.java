package com.sadruddinjunejo.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.sadruddinjunejo.homeautomationapp.constants.Constants;

/**
 * Shared Preferences Wrapper
 * for current state of appliances
 * @author Sadruddin Junejo
 *
 */
public class ApplianceStateStorage {
	
	private static SharedPreferences prefs;
	
	public static void saveApplianceState(int appliance, boolean state, Context context){
		prefs = context.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
		prefs.edit().putBoolean("" + appliance, state).commit();
	}
	
	public static boolean getApplianceState(int appliance, Context context){
		prefs = context.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
		return prefs.getBoolean("" + appliance, false);
	}

	public static boolean[] getAllApplianceStates(Context context){
		prefs = context.getSharedPreferences(Constants.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
		boolean states[] = new boolean[Constants.NUMBER_OF_CLASSES];
		for (int i = 0; i < Constants.NUMBER_OF_CLASSES; i++){
			states[i] = prefs.getBoolean("" + i, false);
		}
		return states;
	}
}

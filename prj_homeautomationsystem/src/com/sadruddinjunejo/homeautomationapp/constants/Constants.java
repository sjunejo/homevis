package com.sadruddinjunejo.homeautomationapp.constants;

import java.util.UUID;

import android.os.Environment;

/**
 * List of commonly-used constants
 * @author Sadruddin Junejo
 *
 */
public class Constants {
	
	public static final String FOLDER_PATH_MAIN = Environment.getExternalStorageDirectory().toString()
			+ "/home_automation/";
	
	public static final int NUMBER_OF_CLASSES = 5;
	public static final int NUMBER_OF_FEATURES_PER_SAMPLE = 26;
	
	public static final int CLASS_COFFEE = 0;
	public static final int CLASS_HEATING = 1;
	public static final int CLASS_LIGHTS = 2;
	public static final int CLASS_RADIO = 3;
	public static final int CLASS_TELEVISION = 4;

	public static final String KEY_SHARED_PREFERENCES = "KEY_APPLIANCE_PREFS";

    // Identifies the app
    public static final UUID PEBBLE_UUID = UUID.fromString("79cff51c-92b4-42bf-b74d-7261b745a8df");

    public static final byte DATA_FROM_PEBBLE = 1;
    
    public static final String PEBBLE_CONNECTED  = "com.getpebble.action.PEBBLE_CONNECTED";
}

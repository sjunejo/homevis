package com.sadruddinjunejo.homeautomationapp.utils;

import android.content.Context;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.sadruddinjunejo.homeautomationapp.constants.Constants;

/**
 * Class responsible for sending appliance statae
 * data to the Pebble Companion
 * @author Sadruddin Junejo
 *
 */
public class PebbleDataSender {
	   
		/**
		 * Sends state of appliance that was just toggled
		 * to Pebble 
		 * @param appliance ID of the appliance changed
		 * @param state the new state (true for ON, false for OFF)
		 * @param context the application context
		 */
	    public static void sendPebbleData(int appliance, boolean state, Context context) {
	        PebbleDictionary data = new PebbleDictionary();
	        // Send state that was just toggled
	        byte applianceStateAsByte = 0;
	        if (state) applianceStateAsByte = 1;
	        data.addInt8(appliance, applianceStateAsByte);
	        PebbleKit.sendDataToPebble(context, Constants.PEBBLE_UUID, data);
	    }
	    
	    /**
	     * Used when the view on the Pebble has to be refreshed.
	     * @param states ALL current ON/OFF states of appliances
	     * @param context the appliance state
	     */
	    public static void sendAllApplianceStatesToPebble(boolean[] states, Context context){
	    	PebbleDictionary data = new PebbleDictionary();
	    	for (int i = 0; i < states.length; i++){
	 		    byte applianceStateAsByte = 0;
	 		    if (states[i]) applianceStateAsByte = 1;
	 		    data.addInt8(i, applianceStateAsByte);
	 	     }
	    	 
	    	PebbleKit.sendDataToPebble(context, Constants.PEBBLE_UUID, data);
	    }
	    
}


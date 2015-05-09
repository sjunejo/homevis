package com.sadruddinjunejo.homeautomationapp.utils;

/**
 * Class for single-array methods
 * @author Sadruddin Junejo
 *
 */
public class SingleArrayOperations {
	
	/**
	 * Finds the index within the array with the 
	 * highest-value double 
	 */
	public static int indexWithMaxValue(double[] arr){
		double max = 0;
		int maxIndex = 0;
		for (int i = 0; i < arr.length; i++){
			if (arr[i] > max){
				max = arr[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

}

package com.sadruddinjunejo.homeautomationapp.classifiers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import android.util.Log;

import com.sadruddinjunejo.homeautomationapp.constants.Constants;


/**
 * k-Nearest Neighbours classification algorithm:
 * A template-matching machine learning technique
 * @author Sadruddin Junejo	
 *
 */
public class KNN extends TrainingSetClassifier {
	
	// The number of nearest neighbours
	private static int k = 3; 
	
	private static final int[] VALUES_OF_K = {3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 30, 40, 50};
	
	/**
	 * Testing method to find optimal value of 
	 * 'k' for this project
	 * @param features reduced feature vector
	 */
	public void classifyWithAllPossibleValuesOfK(double[][] features){
		for (int i = 0; i < VALUES_OF_K.length; i++){
			k = VALUES_OF_K[i];
			int result = classify(features);
			Log.d("K = " + k, "" + result);
		}
	}
	
	/**
	 * Making the assumption that there are only
	 * five classes to be separated
	 */
	@Override
	public int classify(double[][] features) {
		// For each point in the training set:
		//	 calculate Euclidean Distance between current point and training set
		//	 store this distance in a hashmap <K,V> where:
		//		 K = distance, V = label (from 1 to 5)
		Map<Double, Integer> distanceMap = new TreeMap<Double, Integer>();
		for (int i = 0; i < trainingSet.length; i++){
				distanceMap.put(euclideanDistance(trainingSet[i], features[0]),
						i/(trainingSet.length/Constants.NUMBER_OF_CLASSES));
		}
		
		/* Map created. Now need to iterate through it */
		Iterator<Double> it = distanceMap.keySet().iterator();
		Map<Integer, Integer> nearestLabelMap = new HashMap<Integer, Integer>();
		
		int count = 0;
		/*
		 * Finding the majority of values < k 
		 * closest to the feature vector
		 */
		while (count < k){
			Double euclideanDistance = (Double) it.next();
			Integer key = distanceMap.get(euclideanDistance);
			Log.d("Value", "" + euclideanDistance);
			Log.d("Label", "" + key);
			Integer currentNumberOfOccurences = nearestLabelMap.get(key);
			
			if (currentNumberOfOccurences== null)
				currentNumberOfOccurences = 0;
		
			nearestLabelMap.put(key, 
					currentNumberOfOccurences+1);
			count++;
		}
		
		Map.Entry<Integer, Integer> maxEntry = null;

		// maxEntry has the most occurences within the 'k' samples
		for (Map.Entry<Integer, Integer> entry : nearestLabelMap.entrySet()){
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
		        maxEntry = entry;
		    }
		}
		
		Log.d("Predicted Label", "" + maxEntry.getKey());
		
		return maxEntry.getKey();
	}
	
	/**
	 * Calculates using 
	 * @param trainingSet One feature vector in the training set
	 * @param features the reduced feature veector
	 * @return the euclidean distance between the two vectors
	 */
	public double euclideanDistance(double[] trainingSet, double[] features){
		double distance_squared = 0;
		for (int i = 0; i < trainingSet.length; i++){
			distance_squared += square(features[i]-
					trainingSet[i]);
		}
		return Math.sqrt(distance_squared);
	}

}

package com.sadruddinjunejo.homeautomationapp.classifiers;

import android.util.Log;

import com.sadruddinjunejo.homeautomationapp.constants.Constants;
import com.sadruddinjunejo.homeautomationapp.utils.SingleArrayOperations;

/**
 * Naive Bayes Classification algorithm
 * @author Sadruddin Junejo
 *
 */
public class NaiveBayesianClassifier extends TrainingSetClassifier {

	private double[][] means;
	private double[][] variances;
	private double[][] featureProbabilities;
	
	private double normalisingConstant;
	
	private int featureLength;
	
	public NaiveBayesianClassifier(){
	}
	
	@Override
	public int classify(double[][] features) {

		featureLength = features[0].length;
		
		calculateMeans();
		calculateVariances();
		
		//MatrixOperations.printMatrix(means, "Means");
		//MatrixOperations.printMatrix(variances, "variances");
		calculateFeatureProbabilities(features[0]);
		calculateNormalisingConstant();
		
		double[] prob_estimates = new double[Constants.NUMBER_OF_CLASSES];
		for (int i = 0; i < Constants.NUMBER_OF_CLASSES; i++){
			prob_estimates[i] = probabilityOfBelongingToClass(i);
			Log.d("Probs", "" + i + ": " + prob_estimates[i]);
		}
		
		return SingleArrayOperations.indexWithMaxValue(prob_estimates);
	}
	
	/**
	 * For each class:
	 * Calculate mean of each of the features
	 */
	private void calculateMeans(){
		means = new double[Constants.NUMBER_OF_CLASSES][featureLength];
		Log.d("Bla", "" + trainingSet[0].length + " | " + trainingSet.length);
		double total;
			
		int numberOfTraversedSamples = 0;
		int currentClass = 0;
		int numberOfClassesPerSample = trainingSet.length/Constants.NUMBER_OF_CLASSES;
		while (currentClass < 5){
			for (int j = 0; j < trainingSet[0].length; j++){
				total = 0;
				while (numberOfTraversedSamples < numberOfClassesPerSample){
					total += trainingSet[numberOfTraversedSamples+numberOfClassesPerSample*currentClass][j];
					numberOfTraversedSamples++;
				}
				means[currentClass][j] = total/numberOfClassesPerSample;
				numberOfTraversedSamples=0;
			}
			currentClass++;
		}
	}
	
	/**
	 * For each class:
	 * Calculate mean of each of the variances
	 */
	private void calculateVariances(){
		variances = new double[Constants.NUMBER_OF_CLASSES][featureLength];
		double sum = 0;
		int numberOfTraversedSamples = 0;
		int currentClass = 0;
		int numberOfClassesPerSample = trainingSet.length/Constants.NUMBER_OF_CLASSES;
		while (currentClass < 5){
			for (int j = 0; j < trainingSet[0].length; j++){
				sum = 0;
				while (numberOfTraversedSamples < numberOfClassesPerSample){
					//Log.d("Data", "j = " + j);
					//Log.d("Data", "currentClass = " + currentClass + ", numberOfTraversedSamples=" + numberOfTraversedSamples);
					sum += square(trainingSet[numberOfTraversedSamples+numberOfClassesPerSample*currentClass][j]
							- means[currentClass][j]);
					numberOfTraversedSamples++;
				}
				variances[currentClass][j] = sum/(numberOfClassesPerSample-1);
				numberOfTraversedSamples=0;
			}
			currentClass++;
		}
	}
	
	private double probabilityOfBelongingToClass(int probableClass){
		double[] featureProb = featureProbabilities[probableClass];

		double product = 1/(double)Constants.NUMBER_OF_CLASSES;
		Log.d("product", "" + product);
		for (int i = 0; i < featureProb.length; i++){
			//Log.d("Feature_Prob", "" + featureProb[i]);
			product *= featureProb[i];
		}
		
		return product/normalisingConstant;
		
	}
	
	private void calculateFeatureProbabilities(double[] features){
		featureProbabilities = new double[Constants.NUMBER_OF_CLASSES][featureLength];
		for (int i = 0; i < featureProbabilities.length; i++){
			for (int j = 0; j < featureProbabilities[0].length; j++){
				featureProbabilities[i][j] = estimateParameter(features[j], means[i][j], variances[i][j]);
			}
		}
	}
	
	private double estimateParameter(double v, double mean, double variance){
		double root2PiVariance = 1/Math.sqrt(2 * Math.PI * variance);
		double exponent = -1 * ((square(v - mean))/(2*variance));
		//Log.d("Data", "v = " + v + " ~ mean = " + mean + " variance = " + variance);
		//Log.d("Parameter Estm", "" + root2PiVariance * Math.exp(exponent));
		return root2PiVariance * Math.exp(exponent);
	}

	private void calculateNormalisingConstant(){
		normalisingConstant = 0.0;
		for (int i = 0; i < featureProbabilities.length; i++){
			double product = 0.2;
			for (int j = 0; j < featureProbabilities[0].length; j++){
				product *= featureProbabilities[i][j];
			}
			normalisingConstant += product;
		}
		Log.d("NormalisingConstant", "" + normalisingConstant);
	}
}

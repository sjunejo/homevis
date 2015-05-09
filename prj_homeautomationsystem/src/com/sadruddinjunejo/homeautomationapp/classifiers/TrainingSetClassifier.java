package com.sadruddinjunejo.homeautomationapp.classifiers;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.util.Log;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import com.sadruddinjunejo.homeautomationapp.constants.Constants;

/**
 * Loads training set data
 * for use in classification.
 * Currently extended by kNN algorithm
 * and Naive Bayesian Classifier
 * @author Sadruddin Junejo
 *
 */
public abstract class TrainingSetClassifier implements IClassifier {

	protected double[][] trainingSet;
	
	private static final String TRAINING_SET_FILE = "training_set.mat";
	private static final String TRAINING_SET_DATA = "training_data";
	
	public TrainingSetClassifier(){
		loadTrainingSet();
	}
	
	private void loadTrainingSet(){
		String fileString = Constants.FOLDER_PATH_MAIN + TRAINING_SET_FILE;
		try {
			MatFileReader mFileReader = new MatFileReader(fileString);
			MLDouble trainingData = (MLDouble) mFileReader.getMLArray(TRAINING_SET_DATA);
			trainingSet = trainingData.getArray();
			Log.d("Loaded", "Training data");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double square(double num){
		return num * num;
	}
}

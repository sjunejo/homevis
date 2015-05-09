package com.sadruddinjunejo.homeautomationapp.classifiers;

/**
 * Interface for all supervised
 * learning models in this program.
 * 
 * Used so that the app does not have
 * to differentiate between classifiers.
 * 
 * @author Sadruddin Junejo
 *
 */
public interface IClassifier {

	public abstract int classify(double[][] features);
	
}

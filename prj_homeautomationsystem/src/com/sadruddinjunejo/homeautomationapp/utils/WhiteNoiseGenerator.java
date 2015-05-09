package com.sadruddinjunejo.homeautomationapp.utils;

import java.util.Random;

/**
 * Testing method: Adds Gaussian White Noise
 * to speech data
 * @author Sadruddin Junejo
 *
 */
public class WhiteNoiseGenerator {

	// Gaussian noise parameters
	private static final float MEAN = 100f;
	private static final float VARIANCE = 10f;
	
	public static float[] addGaussianWhiteNoise(float[] originalSignal){
		Random random = new Random();
		float[] noisySignal = new float[originalSignal.length];
		
		for (int i = 0; i < noisySignal.length; i++){
			noisySignal[i] = (float) (originalSignal[i] + random.nextGaussian() * Math.sqrt(VARIANCE) + MEAN);
		}
		return noisySignal;
	}
}

package com.sadruddinjunejo.homeautomationapp.featureextraction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.sadruddinjunejo.homeautomationapp.utils.AudioFileReader;


/**
 * Wrapper class for Ganish Tiwari's
 * open-source MFCC code
 * Used for extracting audio features from
 * .wav files in a specified directory
 * @author Sadruddin Junejo
 *
 */
public class SJFeatureExtractor {
	
	private String directory = "";
	
	private static final int SAMPLES_PER_FRAME = 1024;
	private static final int SAMPLE_RATE = 44100;
	
	private AudioFileReader audioFileReader;
	
	
	public SJFeatureExtractor(String dir){
		this();
		directory = dir;
	}
	
	public SJFeatureExtractor(){
		audioFileReader = new AudioFileReader();
	}

	/**
	 * MFCC Extraction process
	 * @param wavFile the file to obtain MFCCs from
	 * @return MFCCs
	 */
	public double[][] extractFeaturesFromFile(File wavFile){
		byte[] audioBytes = audioFileReader.read(wavFile.getAbsolutePath());
    	double[][] mfccs = extractMFCCs(this.getFloatDataFromAudioByteBuffer(audioBytes));
    
    	return mfccs;
	}
		
	/**
	 * Used for obtaining training data
	 */
	public void writeFeaturesFromFolder(){
		File fileList = new File(directory);
		if (fileList != null){
		  int count = 0;
		  File[] filenames = fileList.listFiles();
		  Arrays.sort(filenames);
		  // These files are listed in alphabetical order
		    for (File wavFile : filenames){
		    	Log.d("FILE_NAME", wavFile.getName());
		    	double[][] mfccs = this.extractFeaturesFromFile(wavFile);
		    	this.writeToCSVFile(mfccs);
		    	Log.d("Counting", "COUNT:" + count);
		    	count++;
		    	Log.d("MFCC_matrix_lengths", "" + mfccs.length + " x " + mfccs[0].length);
		   }
		}
	}
	
	public byte[][] extractBytesFromFolder(){
		File fileList = new File(directory);
		byte[][] allBytes = null;
		if (fileList != null){
		  int count = 0;
		  File[] filenames = fileList.listFiles();
		  Arrays.sort(filenames);
		  allBytes = new byte[filenames.length][(int) filenames[0].length()];
		  
		  // These files are listed in alphabetical order
		    for (File wavFile : filenames){
		    	allBytes[count] = audioFileReader.read(wavFile.getAbsolutePath());
		    	Log.d("FILE_NAME", wavFile.getName());
		    	count++;
		   }
		}
		return allBytes;
	}
	
	File file;
	
	public double[][] extractMFCCs(float[] framedSignal){
		file = new File(Environment.getExternalStorageDirectory(), "TRAINING_DATA.csv");
		
		Log.v("FRAME_SIGNAL", "" + framedSignal.length);
		
		PreProcess preProcess = new PreProcess(framedSignal, SAMPLES_PER_FRAME, SAMPLE_RATE);
		FeatureExtract fExtract = new FeatureExtract(
				preProcess.get2DFrameSignal(),  SAMPLE_RATE, SAMPLES_PER_FRAME);
		fExtract.makeMfccFeatureVector();
		double[][] fVector = fExtract.getFeatureVector().getMfccFeature();
		Log.d("Feature vector lengths", "A: " + fVector.length + " | " + fVector[0].length);
		return fVector;
		
	}
	
	/**
	 * Takes a 2d array (typically a feature vector)
	 * and writes it to a csv file
	 * Used to port data to Matlab
	 * @param fVector
	 */
	private void writeToCSVFile(double[][] fVector){
		Log.e("Writing", "Writing...");
		try {
		    FileWriter writer = new FileWriter(file, true);
			for (int i = 0; i < fVector.length; i++){
				for (int j = 0; j < fVector[0].length; j++){
					writer.append("" + fVector[i][j]);
					writer.append(",");
				}
			}
		   
		    writer.append('\n');
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e){
		     e.printStackTrace();
		} 
	}
	
	@SuppressLint("NewApi")
	public float[] getFloatDataFromAudioByteBuffer(byte[] audioWithHeader){
		byte[] audioBytes = Arrays.copyOfRange(audioWithHeader, 44, audioWithHeader.length);
		int nlengthInSamples = audioBytes.length / 2;
		float[] audioData = new float[nlengthInSamples];
			
		for (int i = 0; i < nlengthInSamples; i++) {
			/* First byte is LSB (low order) */
			int LSB = audioBytes[2 * i];
			/* Second byte is MSB (high order) */
			int MSB = audioBytes[2 * i + 1];
			audioData[i] = MSB << 8 | (255 & LSB);
		}
				
		return audioData;
	}
	
}

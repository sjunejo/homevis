package com.sadruddinjunejo.homeautomationapp.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import com.sadruddinjunejo.homeautomationapp.constants.Constants;

import android.util.Log;

/**
 * Various methods for arithmetic
 * between 2D matrices
 * @author Sadruddin Junejo
 *
 */
public class MatrixOperations {
	
	private static final String NORMALISATION_FILE = "normalisation.mat";
	private static final String MEAN = "ABC_mu";
	private static final String STD = "ABC_sigma";
	
	static int mRows = 0;
	static int mCol = 0;
	static int nRows = 0;
	static int nCol = 0;
	
	public static double[][] addMatrices(double[][] m, double[][] n){
		   mRows = m.length;
		   mCol = m[0].length;
		   nRows = n.length;
		   nCol = n[0].length;
		   if(!canBeAdded()){
		        throw new IllegalArgumentException("Cannot multiply arrays");
		    }
		   double[][] result = new double[mRows][mCol];
		   for (int i = 0; i < mRows; i++){
			   for (int j = 0; j < mCol; j++){
				   result[i][j] = m[i][j] + n[i][j];
			   }
		   }
		   return result;
	}
	
	public static double[][] multiplyMatrices(double[][] m, double[][] n){
	    mRows = m.length;
	    mCol = m[0].length;
	    nRows = n.length;
	    nCol = n[0].length;
	    if(canBeMultiplied(m,n) == false){
	        throw new IllegalArgumentException("Cannot multiply arrays");
	    }
	    double[][] answer = new double[mRows][nCol];
	    for(int i = 0; i < mRows; i++){
	        for(int j = 0; j < nCol; j++){
	            for(int k = 0; k < mCol; k++){
	                answer[i][j] += m[i][k] * n[k][j];
	            }
	        }
	    }
	    return answer;
	}

	public static boolean canBeMultiplied(double[][] m, double[][]n){
	    mRows = m.length;
	    mCol = m[0].length;
	    nRows = n.length;
	    nCol = n[0].length;
	    if(nRows == mCol){
	        return true;
	    }
	    return false;
	}
	
	public static boolean canBeAdded(){
		if (nRows == mRows && nCol == mCol)
			return true;
		return false;
	}

	public static double[][] transposeMatrix(double[][] array){
		 if (array == null || array.length == 0) 
			 return array;

		 int width = array.length;
		 int height = array[0].length;

		double[][] array_new = new double[height][width];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
			 array_new[y][x] = array[x][y];
			}
		}
		 return array_new;
	}
	
	public static void printMatrix(double[][] matrix, String prefixString){
		System.out.println(prefixString);
		for (double[] arr : matrix) {
            System.out.println(Arrays.toString(arr));
        }
	}
	
	/**
	 * Write 2D array (typically feature vector) to csv file
	 * @param fVector the 2d array
	 * @param filepath the name of the .csv file to be written to
	 */
	public static void writeToCSVFile(double[][] fVector, String filepath){
		Log.e("Writing", "Writing...");
		try {
		    FileWriter writer = new FileWriter(new File(filepath), false);
			for (int i = 0; i < fVector.length; i++){
				for (int j = 0; j < fVector[0].length; j++){
					writer.append("" + fVector[i][j]);
					writer.append(",");
				}
				writer.append('\n');
			}
	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e){
		     e.printStackTrace();
		} 
	}
	
	/**
	 * Uses the mean and standard deviation
	 * vectors acquired from the training data
	 * to normalise the input data
	 * @param x the input vector
	 * @return the normalised data
	 */
	public static double[][] normaliseData(double[][] x){
		
		// Load mean and standard deviation arrays
		String locString = Constants.FOLDER_PATH_MAIN 
				+ NORMALISATION_FILE;
		
		double[][] meanArray = null;
		double[][] stdArray = null;
		
		try {
			MatFileReader mFileReader = new MatFileReader(locString);
			MLDouble j = (MLDouble) mFileReader.getMLArray(MEAN);
			meanArray = j.getArray();
			MLDouble s = (MLDouble) mFileReader.getMLArray(STD);
			stdArray = s.getArray();
		} catch (FileNotFoundException e) {
			Log.e("File", "FileNotFound!!!");
			e.printStackTrace();
		} catch (IOException e){
			Log.e("File", "I/O Error");
			e.printStackTrace();
		}
		
		// Now perform main zscore calculation
		for (int j = 0; j < x[0].length; j++){
			x[0][j] = (x[0][j] - meanArray[0][j])/stdArray[0][j];
		}

		return x;
	}

	public static double[][] create1DVectorFrom2DMatrix(double[][] arr){
		double[][] vector = new double[1][arr.length * arr[0].length];
		int index = 0;
		for (int i = 0; i < arr.length; i++){
			for (int j = 0; j < arr[0].length; j++){
				// Log.e("Hello", "Test vector" + arr[i][j]);
				vector[0][index++] = arr[i][j];
			}
		}	
		Log.d("Final index", "" + index);
		return vector;
	}
}

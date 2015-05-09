package com.sadruddinjunejo.homeautomationapp.classifiers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import android.util.Log;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLSparse;
import com.jmatio.types.MLStructure;
import com.sadruddinjunejo.homeautomationapp.constants.Constants;
import com.sadruddinjunejo.homeautomationapp.utils.MatrixOperations;

/**
 * Wrapper for LibSVM Java implementation
 * @author Sadruddin Junejo
 *
 */
public class SupportVectorMachine implements IClassifier {
	
	private svm_model svmModel;
	
	private static final String FILE_SVM_MODEL = "svm_model_linear.mat";
	
	private static final String ATTR_MODEL = "model";
	private static final String ATTR_PARAMETERS = "Parameters";
	private static final String ATTR_NUMBER_OF_CLASSES = "nr_class";
	private static final String ATTR_TOTAL_SV = "totalSV";
	private static final String ATTR_RHO = "rho";
	private static final String ATTR_LABEL = "Label";
	private static final String ATTR_SV_INDICES = "sv_indices";
	private static final String ATTR_PROBA = "ProbA";
	private static final String ATTR_PROBB = "ProbB";
	private static final String ATTR_NSV = "nSV";
	private static final String ATTR_SV_COEF = "sv_coef";
	private static final String ATTR_SVS = "SVs";
	
	public SupportVectorMachine(){
		initialiseSVMModel();
	}
	
	/**
	 * Loads SVM model from LibSVM implementation
	 * as well as parameter data from Matlab file,
	 * initialising the SVM with the parameter
	 */
	private void initialiseSVMModel(){
		// Load SVM from .mat file
		String fileString = Constants.FOLDER_PATH_MAIN
					+ FILE_SVM_MODEL;
		
		svmModel = new svm_model();
		
		Log.d("SVM", "Loading SVM...");
		try {
			MatFileReader mFileReader = new MatFileReader(fileString);
			
			MLStructure model = (MLStructure) mFileReader.getMLArray(ATTR_MODEL);
			
			// Pick out each part we need and initialise 
			// our model with it.
			
			// Number classes
			MLDouble doubleValue = (MLDouble) model.getField(ATTR_NUMBER_OF_CLASSES);
			svmModel.setNr_class(doubleValue.get(0).intValue());
			
			// total SVs
			doubleValue = (MLDouble) model.getField(ATTR_TOTAL_SV);
			svmModel.setL(doubleValue.get(0).intValue());
			
			// rho
			doubleValue = (MLDouble) model.getField(ATTR_RHO);
			svmModel.setRho(MatrixOperations.transposeMatrix(
					doubleValue.getArray())[0]);
			Log.d("RHO", Arrays.toString(svmModel.getRho()));
			
			// label
			svmModel.setLabel(MLDoubleToIntegerArray(
					(MLDouble) model.getField(ATTR_LABEL)));
			
			// SV indices
			svmModel.setSv_indices(MLDoubleToIntegerArray(
					(MLDouble) model.getField(ATTR_SV_INDICES)));
			
			// nSV
			svmModel.setnSV(MLDoubleToIntegerArray(
					(MLDouble) model.getField(ATTR_NSV)));
			
			svmModel.setSv_coef(MatrixOperations.transposeMatrix(
					((MLDouble) model.getField(ATTR_SV_COEF)).getArray()));

			// Parameters
			svm_parameter parameters = new svm_parameter();
			doubleValue = (MLDouble) model.getField(ATTR_PARAMETERS);
			double[] params = MatrixOperations.transposeMatrix(
					doubleValue.getArray())[0];
			
			parameters.svm_type = Double.valueOf(params[0]).intValue();
			parameters.kernel_type = Double.valueOf(params[1]).intValue();
			parameters.degree = Double.valueOf(params[2]).intValue();
			
			Log.d("Degree", "" + params[2]);
			parameters.gamma = params[3];
			
			svmModel.setParam(parameters);
			MLSparse sparseMatrix = (MLSparse) model.getField(ATTR_SVS);
			
			System.out.println("" + sparseMatrix.getM() +" | "+ sparseMatrix.getN());
			
			int l = svmModel.getL();
			Log.d("L IS ", "" + l);
	
			svm_node[][] SV = new svm_node[l][Constants.NUMBER_OF_FEATURES_PER_SAMPLE];
			for(int i=0; i<l; i++){
				for(int j=0;j<26;j++){
					svm_node node = new svm_node();
					node.index = j+1;
					node.value = sparseMatrix.get(i, j);
					SV[i][j] = node;
				}
			}
			svmModel.setSV(SV);
		} catch (FileNotFoundException e) {
			Log.e("File", "FileNotFound!!!");
			e.printStackTrace();
		} catch (IOException e){
			Log.e("File", "I/O Error");
			e.printStackTrace();
		}
	}
	
	private int[] MLDoubleToIntegerArray(MLDouble doubleValue){
		double[] arrayDouble = MatrixOperations.transposeMatrix(
				doubleValue.getArray())[0];
		int[] arrayInt = new int[arrayDouble.length];
		for (int i = 0; i < arrayDouble.length; i++){
			arrayInt[i] = Double.valueOf(arrayDouble[i]).intValue();
		}
		return arrayInt;
	}
	
	@Override
	public int classify(double[][] features) {
		svm_node[] nodes = new svm_node[features[0].length];
	    for (int i = 0; i < features[0].length; i++){
	        svm_node node = new svm_node();
	        node.index = i+1;
	        node.value = features[0][i];
	        nodes[i] = node;
	    }
	    
	    int totalClasses = Constants.NUMBER_OF_CLASSES;       
	    int[] labels = new int[totalClasses];
	    svm.svm_get_labels(svmModel,labels);
	    
	    double[] prob_estimates = new double[totalClasses];
	   
	    // Library call
	    int predictedLabel = Double.valueOf(svm.svm_predict_probability(svmModel, nodes, prob_estimates)).intValue();
	
	    Log.d("Predicted_Label", "" + predictedLabel);
	    // The SVM parameters in the MATLAB file for each class
	    // are 1, -2, 2, -2, 0
	    // Thus this casting is required
	    if (predictedLabel==1)	return 0;
	    if (predictedLabel==-1) return 1;
	    if (predictedLabel==2) 	return 2;
	    if (predictedLabel==-2) return 3;
	    if (predictedLabel==0) 	return 4;
	    return 0;
	}

}

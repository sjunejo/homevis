package com.sadruddinjunejo.homeautomationapp.views;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import com.sadruddinjunejo.data.ApplianceStateStorage;
import com.sadruddinjunejo.homeautomationapp.R;
import com.sadruddinjunejo.homeautomationapp.classifiers.ArtificialNeuralNetwork;
import com.sadruddinjunejo.homeautomationapp.classifiers.IClassifier;
import com.sadruddinjunejo.homeautomationapp.classifiers.KNN;
import com.sadruddinjunejo.homeautomationapp.classifiers.NaiveBayesianClassifier;
import com.sadruddinjunejo.homeautomationapp.classifiers.SupportVectorMachine;
import com.sadruddinjunejo.homeautomationapp.constants.Constants;
import com.sadruddinjunejo.homeautomationapp.featureextraction.SJFeatureExtractor;
import com.sadruddinjunejo.homeautomationapp.utils.ExtAudioRecorder;
import com.sadruddinjunejo.homeautomationapp.utils.FontManager;
import com.sadruddinjunejo.homeautomationapp.utils.MatrixOperations;
import com.sadruddinjunejo.homeautomationapp.utils.PebbleDataSender;
import com.sadruddinjunejo.homeautomationapp.utils.WhiteNoiseGenerator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;


/**
 * The landing page of the app.
 * Here the user can issue speech commands
 * as well as change the speech classifier
 * and view instructions for using the app.
 * @author Sadruddin Junejo
 *
 */
public class LandingActivity extends Activity implements OnNavigationListener, OnClickListener, OnItemClickListener {

	private static final boolean TEST_NOISE = false;
	
	private static final String TAG = "ACTIVITY";
	
	// Allows selection of specific classifier
	private SpinnerAdapter classifierAdapter;
	
	private Button btnVoiceInput;
	private Button btnTrain;
	
	
    private String[] classifiers;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    
    private ActionBarDrawerToggle drawerToggle;

    // Current classifier
    private IClassifier classifier;
    
    // In MILLISECONDS
    private static final int VOICE_RECORDING_DURATION = 1000;
    
    private static final String TRAINING_FOLDER_PATH = "/training/";
    private static final String COVARIANCE_FILE = "covariance_matrix.mat";
    private static final String COVARIANCE_MATRIX = "covariance_matrix";
    
    private static final String FILE_NAME = "voice_input.wav";
    
    private ExtAudioRecorder audioRecorder;
    
    private double[][] covarianceMatrix;
    
    // SVM has to be pre-initiated because of its processsor-intensive
    // nature
    private SupportVectorMachine svm;
    
    // Landing (Main UI)
    private ImageView ivBackground;
    private ImageView ivCoffee;
    private ImageView ivHeating;
    private ImageView ivLights;
    private ImageView ivRadio;
    private ImageView ivTelevision;
    
    // Bottom-up SlidePanel
    private TextView tvSlidePanelHeader;
    private TextView tvSlidePanelBody;
    
    private BroadcastReceiver pebbleConnectionReceiver;
    
    private SJFeatureExtractor featureExtractor;
    
    /**
     * Called when the app is initialised.
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialiseUI();
		initialiseOtherComponents();
		listenForDataSending();
	}
	
	/**
	 * Called when the app is brought
	 * to the foreground.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		refreshAllStates();
        sendAllApplianceStatesToPebble();
	}

	/** 
	 * Called when the screen rotates.
	 * onCreate is no longer called when screen rotates due to android:configChanges 
	 * in the manifest 
	 **/
	@Override
	public void onConfigurationChanged(Configuration newConfig){
	    super.onConfigurationChanged(newConfig);
	    setContentView(R.layout.activity_main);
	    initialiseUI();
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 if (drawerToggle.onOptionsItemSelected(item)) {
		      return true;
		    }
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (pebbleConnectionReceiver!= null){
			this.unregisterReceiver(pebbleConnectionReceiver);
		}
	}

	private PebbleKit.PebbleDataReceiver pushHandler = null;
	
	/**
	 * Required to listen for the prompt
	 * from the Pebble smartwatch to send data.
	 * 
	 * This will usually occur when the user switches into
	 * the HomeVis Pebble Watchface 
	 */
	private void listenForDataSending(){
		   final Handler handler = new Handler();
		   
	        pushHandler = new PebbleKit.PebbleDataReceiver(Constants.PEBBLE_UUID) {
	            @Override
	            public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
	            	// We are expecting <1, 1> tuple from Pebble.
	            	Log.d("Data", "Received");
	                byte newState = (byte) data.getUnsignedInteger(Constants.DATA_FROM_PEBBLE).intValue();
	         
	                PebbleKit.sendAckToPebble(context, transactionId);
	                if (newState == Constants.DATA_FROM_PEBBLE){ // Prompted by pebble to send all states
	                	 handler.post(new Runnable() {
	 	                    @Override
	 	                    public void run() {
	 	                    	sendAllApplianceStatesToPebble();
	 	                    }
	 	                });
	                }
	               
	            }
	        };
	        PebbleKit.registerReceivedDataHandler(this, pushHandler);
	        
	        pebbleConnectionReceiver = new BroadcastReceiver(){
				@Override
				public void onReceive(Context arg0, Intent arg1) {
					handler.post(new Runnable(){
						@Override
						public void run() {
							Log.d("Pebble", "Connected");
		                    sendAllApplianceStatesToPebble();
						}
					});
				}
	        };
	      this.registerReceiver(pebbleConnectionReceiver, new IntentFilter(Constants.PEBBLE_CONNECTED));
	}
	
	/**
	 * Initialises default classifier (ANN)
	 * and loads large pieces of data
	 */
	private void initialiseOtherComponents(){
		classifier = new ArtificialNeuralNetwork();
		featureExtractor = new SJFeatureExtractor();
		new LoadingTask(this).execute();
	}
	
	private void initialiseUI(){
		// setup action bar
		ActionBar actionBar = this.getActionBar();
		actionBar.setHomeButtonEnabled(true);
				
		classifiers = this.getResources().getStringArray(R.array.action_list);
		drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
		drawerList = (ListView) this.findViewById(R.id.left_drawer);
		drawerList.setOnItemClickListener(this);
				
		// classifier
		drawerList.setAdapter(new ClassifierAdapter(this, R.layout.classifier_list, classifiers));
		actionBar.setListNavigationCallbacks(classifierAdapter, this);
				
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
	           R.drawable.homevis_logo, R.string.app_name, R.string.app_name);
		
		drawerLayout.setDrawerListener(drawerToggle);
				
		btnVoiceInput = (Button) this.findViewById(R.id.btnVoiceInput);
		btnVoiceInput.setOnClickListener(this);
		FontManager.setButtonTypeface(btnVoiceInput, getAssets());
				
		/*
		 * EXPLANATION:
		 * This code was required to obtain MFCC data from the training
		 * set in order to build classifiers.
		 * It is commented out because it has no place in the final app.
		 */
		//btnTrain = (Button) this.findViewById(R.id.btnTrain);
		//btnTrain.setOnClickListener(this);
				
		initialiseImageViews();
		initialiseSlidingPanelLayout();
	}
	
	private void initialiseImageViews(){
		ivBackground = (ImageView) findViewById(R.id.ivBackground);
		ivBackground.setImageDrawable(getResources().getDrawable(R.drawable.bg));
		ivCoffee = (ImageView) findViewById(R.id.ivCoffee);
		ivHeating = (ImageView) findViewById(R.id.ivHeating);
		ivLights = (ImageView) findViewById(R.id.ivLights);
		ivRadio = (ImageView) findViewById(R.id.ivRadio);
		ivTelevision = (ImageView) findViewById(R.id.ivTelevision);
		
		refreshAllStates();
	}
	
	private void loadMATLABComponents(){
		/* 
		 * Support vector machine needs some
		 * time to load. Therefore it is best if it 
		 * is loaded when the app is started
		 * as opposed to when the classifier
		 * is switched.
		 */
		svm = new SupportVectorMachine();
		
		// This matrix is very big (600x600)
		this.loadCovarianceMatrix();	
		
		if (covarianceMatrix == null){
			Toast.makeText(getApplicationContext(), "covariance_matrix.mat not found! Please load correctly", Toast.LENGTH_LONG).show();
		}
		// Unit test on classifiers
		//this.runTestOnTestData();
	}
	
	private void refreshAllStates(){
		for (int i = 0; i < Constants.NUMBER_OF_CLASSES; i++)
			refreshUI(i);
	}
	/**
	 * Initialising the instructions panel
	 */
	private void initialiseSlidingPanelLayout(){
		 SlidingUpPanelLayout layout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
	        layout.setShadowDrawable(getResources().getDrawable(R.drawable.above_shadow));
	        layout.setAnchorPoint(0.0f);
	        layout.setPanelSlideListener(new PanelSlideListener() {
	            @Override
	            public void onPanelSlide(View panel, float slideOffset) {
	                //Log.i("Activity", "onPanelSlide, offset " + slideOffset);
	            }
	            @Override
	            public void onPanelExpanded(View panel) {
	                Log.i(TAG, "onPanelExpanded");
	            }
	            @Override
	            public void onPanelCollapsed(View panel) {
	                Log.i(TAG, "onPanelCollapsed");
	            }
	            @Override
	            public void onPanelAnchored(View panel) {
	                Log.i(TAG, "onPanelAnchored");
	            }                                                                                                                                                                                                        
	        });
	        tvSlidePanelHeader = (TextView) findViewById(R.id.tvSlidePanelHeader);
	        FontManager.setTextViewTypeface(tvSlidePanelHeader, getAssets());
	        tvSlidePanelBody = (TextView) findViewById(R.id.tvSlidePanelBody);
	        FontManager.setTextViewTypeface(tvSlidePanelBody, getAssets());
	}

	@Override
	public boolean onNavigationItemSelected(int position, long itemId) {
		Log.d("TAG", "Navigation bar item selected");
		return false;
	}

	/**
	 * Called whenever an Android button is 
	 * clicked.
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btnVoiceInput:
				if (covarianceMatrix != null)
					this.startVoiceRecording();
				else
					Toast.makeText(getApplicationContext(), "Cannot classify. covariance_matrix.mat missing", Toast.LENGTH_LONG).show();
				break;
			//case R.id.btnTrain:
			//	this.createTrainingData();
			//	break;
		}
	}
	
	private File soundFile;
	
	/**
	 * Runs when 'Record Command' button
	 * is pressed
	 */
	private  void startVoiceRecording(){
    	btnVoiceInput.setEnabled(false);
    	audioRecorder = ExtAudioRecorder.getInstanse(false);	
    	
    	// Will create new folder for temporarily storing voice sample if it does not 
    	// already exist
    	soundFile = new File(Constants.FOLDER_PATH_MAIN);
    	soundFile.mkdirs();
  
    	audioBuffer = null;
    	// Set naming 
    	audioRecorder.setOutputFile(soundFile.getAbsolutePath() + "/" + FILE_NAME);
    	
    	audioRecorder.prepare();
    	audioRecorder.start();
    	
    	waitForSetDuration();
    }
	
	/**
	 * This method is needed in order to allow
	 * the user to speak for the length of time
	 * specified in VOICE_RECORDING_DURATION
	 */
	private void waitForSetDuration(){
    	Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Thread.sleep(VOICE_RECORDING_DURATION);  
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				LandingActivity.this.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						stopVoiceRecording();
						Toast.makeText(getApplicationContext(), 
								"Voice recording stopped", Toast.LENGTH_SHORT).show();
						float[] originalSignal = featureExtractor.getFloatDataFromAudioByteBuffer(audioBuffer);
						attemptClassification(originalSignal);

						if (TEST_NOISE){
							Log.d("Testing", "White noise");
							attemptClassification(WhiteNoiseGenerator.addGaussianWhiteNoise(originalSignal));
						}
					}
				});
			}
    	});
    	t.start();
    }
    
	byte[] audioBuffer;
	
	/**
	 * Runs after VOICE_RECORDING_DURATION is
	 * reached
	 */
	private  void stopVoiceRecording(){
		Log.d("TAG", "Stopping...");
		audioBuffer = audioRecorder.stop();
		Log.d("AudioBuffer_Length", "" + audioBuffer.length);
		Log.d("Tag", "Successfully stopped");
		audioRecorder.release();
		Log.d("Released", "true");
        btnVoiceInput.setEnabled(true); 
     }

	
	/**
	 * Switch between classifiers depending
	 * on user input
	 */
	@Override
	public void onItemClick(AdapterView parent, View arg1, int position, long arg3) {
		
		switch (position){
			case 0:
				classifier = new ArtificialNeuralNetwork();
				Log.d("CLASSIFIER", "Artificial Neural Network selected");
				break;
			
			case 1:
				classifier = svm;
				Log.d("CLASSIFIER", "Support Vector Machine selected");
				break;
			
			case 2:
				classifier = new KNN();
				Log.d("CLASSIFIER", "KNN selected");	
				break;
				
			case 3:
				classifier = new NaiveBayesianClassifier();
				Log.d("CLASSIFIER", "Naive Bayesian Classifier selected");
				break;	
		}
		
	}
	
	/**
	 * Method that takes extracts features
	 * from TRAINING data (as opposed to user
	 * commands).
	 * For each voice sample
	 * (1) Extract features
	 * (2) Collapse into single-column
	 * (3) Write to .csv file
	 * 
	 * Then perform PCA Analysis in Matlab
	 */
	private void createTrainingData(){
		SJFeatureExtractor featureExtractor= new SJFeatureExtractor(
				Environment.getExternalStorageDirectory() + TRAINING_FOLDER_PATH);
		
		featureExtractor.writeFeaturesFromFolder();
	}
	
	/**
	 * The main method for the speech recognition
	 * process.
	 * 
	 * audioBuffer, filled during speech recording,
	 * serves as the input.
	 */
	private int attemptClassification(float[] originalSignal){
		int result = 0;
		
		if (audioBuffer != null){
			Log.d("audioBuffer length", "" + audioBuffer.length);
			// Extract features from voice sample
			
			double[][] testmfccs = featureExtractor.extractMFCCs(originalSignal);
			
			if (testmfccs != null){
				testmfccs = MatrixOperations.create1DVectorFrom2DMatrix(testmfccs);
				testmfccs = MatrixOperations.normaliseData(testmfccs);
				// Multiply with covariance matrix to reduce the number of features
				double[][] testVector = multiplyWithCovarianceMatrix(testmfccs);
				// Take first 26 coefficients as input into classifier
				double[][] classificationInput = new double[1][Constants.NUMBER_OF_FEATURES_PER_SAMPLE];
				
				for (int i = 0; i < Constants.NUMBER_OF_FEATURES_PER_SAMPLE; i++)
					classificationInput[0][i] = testVector[0][i];
				
				MatrixOperations.printMatrix(classificationInput, "~INPUT~");
				
				// Main classification happens here
				result = classifier.classify(classificationInput);
				toggleAppliance(result);
			} else {
				Log.e("ERROR", "MFCC Extraction failed");
			}
			
		} else {
			Log.e("ERROR", "Voice recording failed");
		}
		return result;
	} // End of attemptClassification()
	
	 int mRows = 0;
	 int mCol = 0;
	 int nRows = 0;
	 int nCol = 0;
	 
	 /**
	  * Necessary method for feature reduction
	  * @param m the original set of MFCCS used for prediction
	  * @return the result of multiplying with the covariance matrix
	  */
	 public double[][] multiplyWithCovarianceMatrix(double[][] m){
	    mRows = m.length;
	    mCol = m[0].length;
	    nRows = covarianceMatrix.length;
	    nCol = covarianceMatrix[0].length;
	    if(canBeMultipliedWithCovarianceMatrix(m) == false){
	        throw new IllegalArgumentException("Cannot multiply arrays");
	    }
	    double[][] answer = new double[mRows][nCol];
	    for(int i = 0; i < mRows; i++){
	        for(int j = 0; j < nCol; j++){
	            for(int k = 0; k < mCol; k++){
	                answer[i][j] += m[i][k] * covarianceMatrix[k][j];
	            }
	        }
	    }
	    return answer;
	}

	/**
	 * Check for conditions of multiplication
	 * @param m matrix to be multiplied with covariance matrix
	 * @return true if can be multiplied, false otherwise
	 */
	public boolean canBeMultipliedWithCovarianceMatrix(double[][] m){
	    mRows = m.length;
	    mCol = m[0].length;
	    nRows = covarianceMatrix.length;
	    
	    nCol = covarianceMatrix[0].length;
	    Log.v("MATRICES", "TEST=" + mRows + "x " + mCol +" | COV: " + nRows + " x " + nCol + "");
	    if(nRows == mCol){
	        return true;
	    }
	    return false;
	}
	
	/**
	 * Turn appliance ON/OFF
	 * @param i index of appliance (from 0 to 4)
	 */
	private void toggleAppliance(int i){
		Log.d("Appliance", "" + i);
		boolean applianceState = ApplianceStateStorage.getApplianceState(i, getApplicationContext());
		
		if (applianceState==false)
			applianceState = true;
		else
			applianceState = false;
		
		ApplianceStateStorage.saveApplianceState(i, applianceState, 
				getApplicationContext());
		refreshUI(i);
	}
	
	/**
	 * Displays updated UI based on appliance switched
	 * ON/OFF
	 * @param i index of appliance (ranging from 0-4)
	 */
	private void refreshUI(int i){
		//String newSetting;
		boolean applianceState = ApplianceStateStorage.getApplianceState(i, getApplicationContext());
		
		switch (i){
			case Constants.CLASS_COFFEE:
				toggleCoffeeUI(applianceState);
				break;
			case Constants.CLASS_HEATING:
				toggleHeatingUI(applianceState);
				break;
			case Constants.CLASS_LIGHTS:
				toggleLightsUI(applianceState);
				break;
			case Constants.CLASS_RADIO:
				toggleRadioUI(applianceState);
				break;
			case Constants.CLASS_TELEVISION:
				toggleTelevisionUI(applianceState);
				break;
		}
		
		// Send updated data to Pebble smartwatch
		PebbleDataSender.sendPebbleData(i, applianceState, getApplicationContext());
		
	} // End of refreshUI()
	
	private void toggleCoffeeUI(boolean newState){
		if (newState){
			ivCoffee.setImageDrawable(getResources().getDrawable(R.drawable.coffee_on));
		} else {
			ivCoffee.setImageDrawable(getResources().getDrawable(R.drawable.coffee_off));
		}
	}
	
	private void toggleHeatingUI(boolean newState){
		if (newState){
			ivHeating.setImageDrawable(getResources().getDrawable(R.drawable.heating_on));
		} else {
			ivHeating.setImageDrawable(getResources().getDrawable(R.drawable.heating_off));
		}
	}
	
	private void toggleLightsUI(boolean newState){
		if (newState){
			ivLights.setImageDrawable(getResources().getDrawable(R.drawable.lights_on));
		} else {
			ivLights.setImageDrawable(getResources().getDrawable(R.drawable.lights_off));
		}
	}
	
	private void toggleRadioUI(boolean newState){
		if (newState){
			ivRadio.setImageDrawable(getResources().getDrawable(R.drawable.radio_on));
		} else {
			ivRadio.setImageDrawable(getResources().getDrawable(R.drawable.radio_off));
		}
	}
	
	private void toggleTelevisionUI(boolean newState){
		if (newState){
			ivTelevision.setImageDrawable(getResources().getDrawable(R.drawable.television_on));
		} else {
			ivTelevision.setImageDrawable(getResources().getDrawable(R.drawable.television_off));
		}
	}
	
	private void sendAllApplianceStatesToPebble(){
		boolean[] states = ApplianceStateStorage.getAllApplianceStates(getApplicationContext());
		PebbleDataSender.sendAllApplianceStatesToPebble(states, getApplicationContext());
	}

	/**
	 * Loads .m file from UI 
	 */
	private void loadCovarianceMatrix(){
		String covString = Constants.FOLDER_PATH_MAIN 
				+ COVARIANCE_FILE;
		
		Log.d("Warning", "Loading covariance matrix...");
		try {
			MatFileReader mFileReader = new MatFileReader(covString);
			MLDouble j = (MLDouble) mFileReader.getMLArray(COVARIANCE_MATRIX);
			
			covarianceMatrix = j.getArray();
			
		} catch (FileNotFoundException e) {
			Log.e("File", "FileNotFound!!!");
			e.printStackTrace();
		} catch (IOException e){
			Log.e("File", "I/O Error");
			e.printStackTrace();
		}
	}

	/**
	 * Task that loads MATLAB components in the background 
	 * so as not to block the UI thread
	 * @author Sadruddin Junejo
	 *
	 */
	public class LoadingTask extends AsyncTask<Void, Void, Boolean> {

	    /** Shows user that the data is loading. */
	    private ProgressDialog dialog;
		
	    public LoadingTask(LandingActivity activity) {
	        dialog = new ProgressDialog(activity);
	    }

	    protected void onPreExecute() {
	        this.dialog.setMessage("Loading...");
	        this.dialog.show();
	    }
	    
	    protected Boolean doInBackground(final Void... args) {
	    	loadMATLABComponents();
	    	return true;
	    }
	    
	    @Override
	    protected void onPostExecute(final Boolean success) {
	        if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
	    }
	    
	} // End of LoadingTask
	
	/**
	 * This method was used to run test data
	 */
	private void runTestOnTestData(){
	 SJFeatureExtractor fExtractor = new SJFeatureExtractor(Environment.getExternalStorageDirectory() + "/training/NEU_NEURAL_NETWORK_TEST");
	 byte[][] bytes = fExtractor.extractBytesFromFolder();
	 double[][] testSet = new double[bytes.length][Constants.NUMBER_OF_FEATURES_PER_SAMPLE];
	 double[][] noisemfccs = new double[bytes.length][Constants.NUMBER_OF_FEATURES_PER_SAMPLE];
	 
	 byte[][] noisyBytes = new SJFeatureExtractor(Environment.getExternalStorageDirectory() + "/TEST_NOISE").extractBytesFromFolder();
	 // Bytes extracted. Now can extract MFCCS!
	 
	 Log.d("Bytes length", "" + bytes[0].length);
	 
	 for (int i = 0; i < bytes.length; i++){
		 
		 /**
		 // Turn it into a float
		 float[] floatSignal = fExtractor.getFloatDataFromAudioByteBuffer(bytes[i]);
		 
		 // Extract MFCCs
		 double[][] testmfccs = fExtractor.extractMFCCs(floatSignal);
		 if (testmfccs != null){				
				testmfccs = MatrixOperations.create1DVectorFrom2DMatrix(testmfccs);
				testmfccs = MatrixOperations.normaliseData(testmfccs);
				double[][] testVector = multiplyWithCovarianceMatrix(testmfccs);
				double[][] classificationInput = new double[1][Constants.NUMBER_OF_FEATURES_PER_SAMPLE];
				for (int x = 0; x < Constants.NUMBER_OF_FEATURES_PER_SAMPLE; x++)
					classificationInput[0][x] = testVector[0][x];
				testSet[i] = classificationInput[0];
		 	} 
		 **/
		 // Extract MFCCs
		 float[] noisySignal = fExtractor.getFloatDataFromAudioByteBuffer(noisyBytes[i]);
		 double[][] nmfccs = fExtractor.extractMFCCs(noisySignal);
		 if (nmfccs != null){				
				nmfccs = MatrixOperations.create1DVectorFrom2DMatrix(nmfccs);
				nmfccs = MatrixOperations.normaliseData(nmfccs);
				double[][] testVector = multiplyWithCovarianceMatrix(nmfccs);
				double[][] classificationInput = new double[1][Constants.NUMBER_OF_FEATURES_PER_SAMPLE];
				for (int x = 0; x < Constants.NUMBER_OF_FEATURES_PER_SAMPLE; x++)
					classificationInput[0][x] = testVector[0][x];
				noisemfccs[i] = classificationInput[0];
		 	} 
	 	}
	 
	 //this.bulkClassify(testSet, "test_results_plain.csv");
	 this.bulkClassify(noisemfccs, "test_results_noise.csv");
		
	}
	
	/**
	 * Called by the above method. Performs actual
	 * classification.
	 * @param testSet either the noise or the plain test set
	 * @param filename the file name to save the .csv file as
	 */
	private void bulkClassify(double[][] testSet, String filename){
		if (testSet != null){
			// Initialise classifiers
			ArtificialNeuralNetwork ann = new ArtificialNeuralNetwork();
			// svm already exists
			KNN kNN = new KNN();
			NaiveBayesianClassifier nbc = new NaiveBayesianClassifier();
			
			ArrayList<IClassifier> classifiers = new ArrayList<IClassifier>();
			classifiers.add(ann);
			classifiers.add(svm);
			classifiers.add(kNN);
			classifiers.add(nbc);
			double[][] testResults = new double[testSet.length][classifiers.size()];
			
			for (int i = 0; i < testSet.length; i++){
				for (int j = 0; j < classifiers.size(); j++){
					double[][] testVector = new double[1][testSet[i].length];
					// Copy testSet[i] into testVector
					for (int k = 0; k < testSet[i].length; k++){
						testVector[0][k] = testSet[i][k];
					}
					testResults[i][j] = classifiers.get(j).classify(testVector);
				}
			}
			
			// How do we print test results?
			Log.e("Testing", "Complete");
			
			MatrixOperations.writeToCSVFile(testResults,  Environment.getExternalStorageDirectory() + "/" + filename);
			Log.e("Writing", "Complete!");
		}
	}
	
} // End of LandingActivity

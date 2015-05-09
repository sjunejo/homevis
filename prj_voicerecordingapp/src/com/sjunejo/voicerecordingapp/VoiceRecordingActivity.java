package com.sjunejo.voicerecordingapp;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Used for recording speech samples serving as
 * training samples for the supervised learning
 * models
 * @author Sadruddin Junejo
 *
 */
public class VoiceRecordingActivity extends Activity implements OnClickListener {
    
    private Button btnStartStopVoiceRecording;
   
    // DEFAULT: Record for 1 second
    private static final int VOICE_RECORDING_DURATION = 1000;
    
    private static final String FOLDER_PATH = "/voices/";
    
    private static final String INITIAL_SUFFIX = "0.wav";
    
    /* The current appliances */
    private static final String[] FILE_NAMES = {"lights_","radio_", "television_", "heating_", "coffee_"};
    private static String current_filename_prefix = FILE_NAMES[0];

    /* Speech */
    private ExtAudioRecorder audioRecorder;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recording);
        initialiseViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    
    private void initialiseViews(){
        btnStartStopVoiceRecording = (Button) findViewById(R.id.btnStartStopRecording);
        btnStartStopVoiceRecording.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.btnStartStopRecording:
            startVoiceRecording();
            break;
        }
    }
    
    /**
     * Runs when Record button is pressed
     */
    private  void startVoiceRecording(){
    	btnStartStopVoiceRecording.setEnabled(false);
    	audioRecorder = ExtAudioRecorder.getInstanse(false);	
    	
    	// Will create new folder for storing voice samples if it does not 
    	// already exist
    	File soundFile = new File(Environment.getExternalStorageDirectory() + FOLDER_PATH);
    	soundFile.mkdirs();
  
    	// Set naming 
    	audioRecorder.setOutputFile(soundFile.getAbsolutePath() + "/" + newFileName());
    	
    	audioRecorder.prepare();
    	audioRecorder.start();
    	
    	waitForSetDuration();
    }
    
    private void waitForSetDuration(){
    	Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Thread.sleep(VOICE_RECORDING_DURATION);  
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				VoiceRecordingActivity.this.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						stopVoiceRecording();
						Toast.makeText(getApplicationContext(), "Voice recording stopped", Toast.LENGTH_SHORT).show();
					}
				});
			}
    	});
    	t.start();
    }
    
	private  void stopVoiceRecording(){
		audioRecorder.stop();
		audioRecorder.release();
        btnStartStopVoiceRecording.setEnabled(true); 
     }

	/**
	 * Incremental file names.
	 * @return file name
	 */
	private String newFileName(){
		int num = 0;
		String fileName = current_filename_prefix + INITIAL_SUFFIX;
		
        File photo = new File(Environment.getExternalStorageDirectory() + FOLDER_PATH, fileName);
        while (photo.exists()){
        	num++;
        	fileName = current_filename_prefix + num + ".wav";
            photo = new File(Environment.getExternalStorageDirectory() + FOLDER_PATH, fileName);
        }
		return fileName;
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.rdLights:
	            if (checked) current_filename_prefix = FILE_NAMES[0];
	            break;
	        case R.id.rdRadio:
	            if (checked) current_filename_prefix = FILE_NAMES[1];
	            break;
	        case R.id.rdTelevision:
	        	if (checked) current_filename_prefix = FILE_NAMES[2];
	        	break;
	        case R.id.rdHeating:
	        	if (checked) current_filename_prefix = FILE_NAMES[3];
	        	break;
	        case R.id.rdCoffee:
	        	if (checked) current_filename_prefix = FILE_NAMES[4];
	        	break;
	    }
	}

}


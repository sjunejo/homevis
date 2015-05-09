package com.sadruddinjunejo.homeautomationapp.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.util.Log;


/**
 * Takes in the path to a wav file,
 * creates a byte[] output stream,
 * can output float[] 
 * @author Sadruddin Junejo
 *
 */
public class AudioFileReader {
	
	public AudioFileReader(){
	}
	
	/**
	 * Obtains bytes from a .wav file
	 * @param filePath
	 * @return bytes corresponding to wav file
	 */
	public byte[] read(String filePath){
		File soundFile = new File(filePath);
		byte[] audioBytes = new byte[(int) soundFile.length()];
		try {
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(soundFile));
			int bla = buf.read(audioBytes, 0, audioBytes.length);
			Log.e("Bytes_read", "" + bla);
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return audioBytes;
	}
	

}

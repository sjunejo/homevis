package com.sadruddinjunejo.homeautomationapp.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Utility class for converting byte data
 * to float data
 * @author Sadruddin Junejo
 *
 */
public class ByteToFloat {
	
	public static float[] convertByteArrayToFloat(byte[] bytes){
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		float[] result = new float[floatBuffer.remaining()];
		return result;
	}

}

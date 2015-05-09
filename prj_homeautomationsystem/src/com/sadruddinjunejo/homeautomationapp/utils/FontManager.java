package com.sadruddinjunejo.homeautomationapp.utils;


import android.R.color;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TextView;

/**
 * Utility methods for changing font
 * of components
 * @author Sadruddin Junejo
 *
 */
public class FontManager {

	private static final String FONT_LANDING = "arial.ttf";
	
	public static void setTextViewTypeface(TextView tv, AssetManager asm){
		Typeface fs = Typeface.createFromAsset(asm, FONT_LANDING);
		tv.setTextColor(tv.getResources().getColor(color.white));
		tv.setTypeface(fs);
	}
	
	public static void setTextViewTypefaceBlack(TextView tv, AssetManager asm){
		Typeface fs = Typeface.createFromAsset(asm, FONT_LANDING);
		tv.setTypeface(fs);
	}
	
	
	public static void setButtonTypeface(Button btn, AssetManager asm){
		Typeface fs = Typeface.createFromAsset(asm, FONT_LANDING);
		btn.setTypeface(fs);
	}
	

	
}

package com.sadruddinjunejo.homeautomationapp.views;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sadruddinjunejo.homeautomationapp.R;
import com.sadruddinjunejo.homeautomationapp.utils.FontManager;

/**
 * Required to display the list of classifiers
 * @author Sadruddin Junejo
 *
 */
public class ClassifierAdapter extends ArrayAdapter {

    private Context mContext;
    private int id;
    private String[] items ;

	public ClassifierAdapter(Context context, int textViewResourceId , String[] list ) 
    {
        super(context, textViewResourceId, list);           
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent){
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView tv = (TextView) mView.findViewById(R.id.tvClassifier);
        FontManager.setTextViewTypefaceBlack(tv, mContext.getApplicationContext().getAssets());
        tv.setText(items[position]);

        return mView;
    }

}
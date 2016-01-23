package com.example.zsurfer;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class PageInformation extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pageinformation);
		
		int width;
		int height;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		getWindow().setLayout((int)(width*0.8), (int)(height*0.5));
	}

	
}

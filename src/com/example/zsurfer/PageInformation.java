package com.example.zsurfer;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

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
		Bundle bundle;
		TextView pageAddress;
		
		pageAddress = (TextView)findViewById(R.id.pageinformation_textView3);
		bundle = getIntent().getExtras();
		pageAddress.setText(bundle.getString("pgaddr"));
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		getWindow().setLayout((int)(width*0.8), (int)(height*0.5));
	}

	
}

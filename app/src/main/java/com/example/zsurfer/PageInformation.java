package com.example.zsurfer;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class PageInformation extends Activity implements View.OnClickListener
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
		Button ok;
		
		ok = (Button)findViewById(R.id.pageinformation_buttonOK);
		pageAddress = (TextView)findViewById(R.id.pageinformation_textView3);
		bundle = getIntent().getExtras();
		pageAddress.setText(bundle.getString("pgaddr"));
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		getWindow().setLayout((int)(width*0.8), (int)(height*0.4));
		
		ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) 
	{
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.pageinformation_buttonOK:
			finish();
			break;
		}
	}

	
}

package com.example.zsurfer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class Homepage extends Activity
{

	GridView grid;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		grid=(GridView)findViewById(R.id.gridView_homepage);
		
	}

	
}

package com.example.zsurfer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ViewHistory extends Activity
{
	ListView l;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		l=(ListView)findViewById(R.id.listViewHistory);
		
		
	}
	

}

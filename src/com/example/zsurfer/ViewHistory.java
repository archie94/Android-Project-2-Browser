package com.example.zsurfer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewHistory extends Activity
{
	ListView l;
	HistoryHandler hHandler;
	String histories[];

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		
		l=(ListView)findViewById(R.id.listViewHistory);
		hHandler=new HistoryHandler(this,null,null,1);
		
		
		int counter=0;
		String dbString = hHandler.databaseToString();
		int p=0,i;
		for(i=0;i<dbString.length();i++)
		{
			if(dbString.charAt(i)=='\n')//for each newline we create a new string 
			{
				counter++;
			}
		}
		histories=new String[counter];//we have total no of present string 
		//counter=0;
		p=0;
		for(i=0;i<dbString.length();i++)
		{
			if(dbString.charAt(i)=='\n')
			{
				histories[--counter]=dbString.substring(p, i);//store each individual memo into a string
				p=i+1;
			}
		}
		//create the list activity on the basis of the strings we have collected
		l.setAdapter(new ArrayAdapter<String>(ViewHistory.this,android.R.layout.simple_list_item_1,histories));
		
		l.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) 
			{
				// TODO Auto-generated method stub
				String msg=histories[position];
				Intent intent=new Intent(ViewHistory.this,SimpleBrowser.class);
				intent.putExtra("msg", msg);
				startActivity(intent);
			}
			
		});
	}
	

}

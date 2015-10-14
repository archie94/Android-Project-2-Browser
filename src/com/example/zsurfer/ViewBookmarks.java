package com.example.zsurfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewBookmarks extends Activity
{
	
	BookmarkHandler bHandler;
	ListView l;
	String bookmarks[];

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmarks);
		
		l=(ListView)findViewById(R.id.listViewBookmarks);
		bHandler=new BookmarkHandler(this,null,null,1);
		
		
		int counter=0;
		String dbString = bHandler.databaseToString();
		int p=0,i;
		for(i=0;i<dbString.length();i++)
		{
			if(dbString.charAt(i)=='\n')//for each newline we create a new string 
			{
				counter++;
			}
		}
		bookmarks=new String[counter];//we have total no of present string 
		//counter=0;
		p=0;
		for(i=0;i<dbString.length();i++)
		{
			if(dbString.charAt(i)=='\n')
			{
				bookmarks[--counter]=dbString.substring(p, i);//store each individual memo into a string
				p=i+1;
			}
		}
		//create the list activity on the basis of the strings we have collected
		l.setAdapter(new ArrayAdapter<String>(ViewBookmarks.this,android.R.layout.simple_list_item_1,bookmarks));
		
		
		l.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) 
			{
				// TODO Auto-generated method stub
				String msg=bookmarks[position];
				Intent intent=new Intent(ViewBookmarks.this,SimpleBrowser.class);
				intent.putExtra("link", msg);
				startActivity(intent);
			}
			
		});
	}

	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	

	

	
	
	

}

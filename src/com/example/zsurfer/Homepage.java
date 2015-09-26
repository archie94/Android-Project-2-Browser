package com.example.zsurfer;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

class WebPages
{
	private String wbpg;
	WebPages(String wbpg)
	{
		this.wbpg=wbpg;
	}
}


class HomeAdapter extends BaseAdapter
{

	ArrayList<WebPages> list;
	HomeAdapter()
	{
		list=new ArrayList<WebPages>();
		String pages[]={"Gmail","Google","Facebook","Twitter","Youtube","Quora","Flipkart","AmazonIndia","Wikipedia","Yahoo"};
		for(int i=0;i<pages.length;i++)
		{
			WebPages temp=new WebPages(pages[i]);
			list.add(temp);
		}
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
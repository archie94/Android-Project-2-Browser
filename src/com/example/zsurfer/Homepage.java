package com.example.zsurfer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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
	Context context;
	HomeAdapter(Context context)
	{
		this.context=context;
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
		return list.size();
	}

	@Override
	public Object getItem(int i) 
	{
		// TODO Auto-generated method stub
		return list.get(i);// returns the object from the array list at position i 
	}

	@Override
	public long getItemId(int i) 
	{
		// TODO Auto-generated method stub
		return i; // returns the id of the clicked item 
	}

	// use a view holder class here for images 
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) 
	{
		// TODO Auto-generated method stub
		View row=view;
		if(row==null)//creating stuff for the first time 
		{
			// using layout inflater is a costly operation so we use it only for the first time 
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.homepage_grid, viewGroup,false);
			
		}
		else	//recycling stuff
		{
			
		}
		
		return null;
	}
	
}
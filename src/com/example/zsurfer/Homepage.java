package com.example.zsurfer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
		grid.setAdapter(new HomeAdapter(this));
		
	}

	
}

class WebPages
{
	String wbpg;
	Bitmap wbpgnm;
	WebPages(String wbpg,Bitmap bitimg)
	{
		this.wbpg=wbpg;
		this.wbpgnm=bitimg;
	}
}


class HomeAdapter extends BaseAdapter
{

	ArrayList<WebPages> list;
	Context context;
	Bitmap bitimg[];
	HomeAdapter(Context context)
	{
		this.context=context;
		// now we create a list that will store web pages 
		list=new ArrayList<WebPages>();
		String pages[]={"Gmail","Google","Facebook","Twitter","Youtube","Quora","Flipkart","AmazonIndia","Wikipedia","Yahoo"};
		int pageNames[]={R.drawable.gmail,R.drawable.google,R.drawable.facebook,R.drawable.twitter,R.drawable.youtube,R.drawable.quora,R.drawable.flipkart,R.drawable.amazon,R.drawable.wikipedia,R.drawable.yahoo};
		bitimg=new Bitmap[pages.length];
		for(int i=0;i<bitimg.length;i++)
		{
			bitimg[i]=BitmapFactory.decodeResource(context.getResources(),pageNames[i]);
			bitimg[i]=Bitmap.createScaledBitmap(bitimg[i], 80, 80, true);
		}
		for(int i=0;i<pages.length;i++)
		{
			WebPages temp=new WebPages(pages[i],bitimg[i]); // create a new web page instance with image and name 
			list.add(temp);// add a web page to the list 
		}
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size(); // this returns the number of web pages available 
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
	
	class ViewHolder 
	{
		
		ImageView webPageImage;
		TextView webPageName;
		ViewHolder(View v)
		{
			webPageImage=(ImageView)v.findViewById(R.id.grid_imageView);
			webPageName= (TextView)v.findViewById(R.id.grid_textView);
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) 
	{
		// TODO Auto-generated method stub
		// this method will be called every time an item is needed to be created 
		
		View row=view;
		ViewHolder holder;
		if(row==null)//creating stuff for the first time 
		{
			// using layout inflater is a costly operation so we use it only for the first time 
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.homepage_grid, viewGroup,false);
			holder = new ViewHolder(row);
			row.setTag(holder);
		}
		else	//recycling stuff
		{
			holder=(ViewHolder)row.getTag();// when we are recycling we are not calling the constructor to save resources 
		}
		WebPages temp = list.get(i);
		holder.webPageImage.setBackgroundDrawable(new BitmapDrawable(temp.wbpgnm));
		holder.webPageName.setText(temp.wbpg);
		return row;
	}
	
}
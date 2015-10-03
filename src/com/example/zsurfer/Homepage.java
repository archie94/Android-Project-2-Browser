package com.example.zsurfer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.Toast;

public class Homepage extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener
{
	EditText url;
	Button go;
	String str="";
	GridView grid;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		grid=(GridView)findViewById(R.id.gridView_homepage);
		grid.setAdapter(new HomeAdapter(this));
		
		url=(EditText)findViewById(R.id.home_editText1);
		go=(Button)findViewById(R.id.home_bGO);
		
		if(isNetworkAvailable()==false)//check if network is available 
        {
        	Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
        }
		
		url.setOnClickListener(this);
		go.setOnClickListener(this);
		grid.setOnItemClickListener(this);
		
	}
	@Override
	public void onClick(View arg) 
	{
		// TODO Auto-generated method stub
		switch(arg.getId())
		{
		case R.id.home_editText1:
			break;
		case R.id.home_bGO:
			str=url.getText().toString();
			if(str.startsWith("http"))
			{
				
			}
			else if(str.startsWith("www."))
			{
				str="https://"+str;
			}
			else
			{
				str="https://www.google.com/search?q="+str;
			}
			break;
		}
		if(str!="")
		{
			Intent i = new Intent (Homepage.this,SimpleBrowser.class);
			i.putExtra("link", str);
			startActivity(i);
		}
	}
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) 
	{
		// TODO Auto-generated method stub
		String links[]={"https://accounts.google.com",
				"https://www.google.com","https://www.facebook.com",
				"https://www.twitter.com","https://www.youtube.com",
				"https://www.quora.com","https://www.flipkart.com",
				"https://www.amazon.in","https://www.wikipedia.com",
				"https://www.yahoo.com"
		};
		Intent intent =new Intent(Homepage.this,SimpleBrowser.class);
		intent.putExtra("link", links[i]);
		startActivity(intent);
	}
	
	
	private boolean isNetworkAvailable() 
    {
		// TODO Auto-generated method stub
    	//method to check if there is Internet connection 
    	ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo isActive=cm.getActiveNetworkInfo();
		return (isActive!=null && isActive.isConnected());
	}

	
}

class WebPages
{
	Bitmap wbpgnm;
	WebPages(Bitmap bitimg)
	{
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
		//String pages[]={"Gmail","Google","Facebook","Twitter","Youtube","Quora","Flipkart","AmazonIndia","Wikipedia","Yahoo"};
		int pageNames[]={R.drawable.gmail,R.drawable.google,R.drawable.facebook,R.drawable.twitter,R.drawable.youtube,R.drawable.quora,R.drawable.flipkart,R.drawable.amazon,R.drawable.wikipedia,R.drawable.yahoo};
		bitimg=new Bitmap[pageNames.length];
		for(int i=0;i<bitimg.length;i++)
		{
			bitimg[i]=BitmapFactory.decodeResource(context.getResources(),pageNames[i]);
			bitimg[i]=Bitmap.createScaledBitmap(bitimg[i], 240, 240, true);
		}
		for(int i=0;i<pageNames.length;i++)
		{
			WebPages temp=new WebPages(bitimg[i]); // create a new web page instance with image and name 
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
		ViewHolder(View v)
		{
			webPageImage=(ImageView)v.findViewById(R.id.grid_imageView);
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
		return row;
	}
	
}
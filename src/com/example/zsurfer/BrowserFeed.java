package com.example.zsurfer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BrowserFeed extends Activity
{

	ListView list ; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browserfeed);
		
		list=(ListView)findViewById(R.id.bf_listview);
		
		
	}
	
	

}

class Feed
{
	TextView title;
	public Feed(String title)
	{
		this.title.setText(title);
	}
}

class BrowserFeedAdapter extends ArrayAdapter<Feed>
{
	Context context;
	ArrayList<Feed> feeds;

	public BrowserFeedAdapter(Context context, int resource,int textViewResourceId, ArrayList<Feed> objects) 
	{
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=context;
		
	}
	
	
	class ViewHolder
	{
		TextView feedTitle;
		public ViewHolder(View v)
		{
			feedTitle=(TextView)v.findViewById(R.id.bf_title);
		}
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		View row=convertView;
		ViewHolder holder;
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.browserfeed_row,parent,false );
			holder = new ViewHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)row.getTag();
		}
		return row;
	}
	
	
	
}
package com.example.zsurfer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BrowserFeed extends BaseAdapter
{
	private final Context context;
	private final List headlines;
	private final List links;

	public BrowserFeed(Context context,List headlines, List links)
	{
		this.context = context;
		this.headlines = headlines;
		this.links = links;
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return headlines.size();
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolder
	{
		ImageView imageView;
		TextView title,link;
		public ViewHolder(View v)
		{
			imageView = (ImageView)v.findViewById(R.id.bf_imageView1);
			title = (TextView)v.findViewById(R.id.bf_title);
			link = (TextView)v.findViewById(R.id.bf_link);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		View rowView;
		ViewHolder viewHolder;
		rowView = convertView;
		if(rowView == null)
		{
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.browserfeed_row, parent,false);
			viewHolder = new ViewHolder(rowView);
			rowView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) rowView.getTag();
		}
		
		
		viewHolder.title.setText((String)headlines.get(position));
		viewHolder.link.setText((String) links.get(position));
		
		
		return rowView;
	}
	
}
package com.example.zsurfer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Class to provide a custom list for browser feed
 * @author Arka
 * @version 26 February 2016
 */
public class BrowserFeed extends BaseAdapter
{
	private final Context context;
	private final List headlines;
	private final List links;
	private final List pubDate;
	private final List imageUrl;
	private CustomInterface interFace;

	/**
	 * Constructor method
	 * @param context
	 * @param headlines
	 * @param links
	 * @param pubDate
	 * @param imageUrl
	 */
	public BrowserFeed(Context context,List headlines, List links, List pubDate,List imageUrl)
	{
		this.context = context;
		this.headlines = headlines;
		this.links = links;
		this.pubDate = pubDate;
		this.imageUrl = imageUrl;
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
	
	public void setInterface(CustomInterface interFace)
	{
		this.interFace = interFace;
	}

	class ViewHolder
	{
		ImageView imageView;
		TextView title,link,date;
		public ViewHolder(View v)
		{
			imageView = (ImageView)v.findViewById(R.id.bf_imageView1);
			title = (TextView)v.findViewById(R.id.bf_title);
			link = (TextView)v.findViewById(R.id.bf_link);
			date = (TextView)v.findViewById(R.id.bf_pubDate);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
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
		viewHolder.date.setText((String)pubDate.get(position));
		String s = (String)imageUrl.get(position);
		if(s.length()>0)
		{
			Bitmap bitmap = downloadImage((String)imageUrl.get(position));
			viewHolder.imageView.setImageBitmap(bitmap);
		}
		
		
		
		rowView.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				if(interFace != null)
				{
					interFace.onCustomListClick(position, arg0);
				}
			}
		});
		
		return rowView;
	}
	
	
	private Bitmap downloadImage(String url)
	{
		//  Download the feed image as bitmap 
		Bitmap bitmap = null;
		InputStream inputStream = null;
		BitmapFactory.Options  bmOption = new BitmapFactory.Options();
		bmOption.inSampleSize = 1;
		
		try
		{
			inputStream = getHttpConnection(url);
			bitmap = BitmapFactory.decodeStream(inputStream, null, bmOption);
			inputStream.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	private InputStream getHttpConnection(String url) throws IOException 
	{
		// establish http connection and and return the input stream 
		InputStream inputStream = null;
		try 
		{
			URL Url = new URL(url);
			HttpURLConnection httpConnection = (HttpURLConnection)Url.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();
			if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				inputStream = httpConnection.getInputStream();
            }
			
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return inputStream;
	}

	/**
	 * Interface for implementing actions on clicking the browser feeds
	 * @author Arka
	 * @version 27 February 2016
	 */
	public interface CustomInterface
	{
		public void onCustomListClick(int position, View view);
	}
	
}
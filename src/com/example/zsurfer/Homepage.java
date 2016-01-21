package com.example.zsurfer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.zsurfer.BrowserFeed.CustomInterface;

public class Homepage extends ListActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CustomInterface
{
	EditText url;
	Button go,vHistory,vBookmarks,Refresh;
	String str="";
	GridView grid;
	ListView lv;
	
	
	//ListView feedList;
	List headlines;
	List links;
	List pubDate;
	List imageUrl;
	
	String[] feedUrl = {"https://news.google.com/news?cf=all&hl=en&pz=1&ned=in&output=rss",
			"http://feeds.pcworld.com/pcworld/latestnews",
			"http://rss.cnn.com/rss/edition.rss",
	};
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage);
		
		TabHost tabHost = (TabHost)findViewById(R.id.homepage_tabhost);
		tabHost.setup();
		TabSpec tabSpec = tabHost.newTabSpec("tag1");
		tabSpec.setContent(R.id.tab1);
		tabSpec.setIndicator("Home");
		tabHost.addTab(tabSpec);
		
		tabSpec = tabHost.newTabSpec("tag2");
		tabSpec.setContent(R.id.tab2);
		tabSpec.setIndicator("Daily Feed");
		tabHost.addTab(tabSpec);
		
		
		
		
		
		grid=(GridView)findViewById(R.id.gridView_homepage);
		grid.setAdapter(new HomeAdapter(this));
		
		url=(EditText)findViewById(R.id.home_editText1);
		go=(Button)findViewById(R.id.home_bGO);
		vHistory=(Button)findViewById(R.id.home_bHistory);
		vBookmarks=(Button)findViewById(R.id.home_bShowBkMrk);
		Refresh = (Button)findViewById(R.id.home_refresh);
		lv = (ListView)findViewById(android.R.id.list);
		
		//feedList = (ListView)findViewById(android.R.id.list);
		
		
		//new DisplayFeed().execute("http://feeds.pcworld.com/pcworld/latestnews");
		new DisplayFeed().execute(feedUrl);
		
		
		
		if(isNetworkAvailable()==false)//check if network is available 
        {
        	Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
        }
		
		url.setOnClickListener(this);
		go.setOnClickListener(this);
		vHistory.setOnClickListener(this);
		vBookmarks.setOnClickListener(this);
		grid.setOnItemClickListener(this);
		Refresh.setOnClickListener(this);
		url.setOnEditorActionListener(new OnEditorActionListener() 
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
            {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) 
                {
                    searchGoogle();
                    handled = true;
                }
                return handled;
            }

			private void searchGoogle() 
			{
				// TODO Auto-generated method stub
				try
				{
					str=url.getText().toString();// get text to be searched from edit text
					if(str.length()>0)
					{
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
					
						Intent i = new Intent (Homepage.this,SimpleBrowser.class);
						i.putExtra("link", str);
						startActivity(i);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
        });
	}




	private class DisplayFeed extends AsyncTask<String,Integer,String>
	{

		@Override
		protected String doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			headlines = new ArrayList();
	        links = new ArrayList();
	        pubDate = new ArrayList();
	        imageUrl = new ArrayList();
	        
	        int n = params.length;
	         
	        for(int i=0;i<n;i++)
	        {
	        	
	        
	        	try {
	        		URL url = new URL(params[i]);
	         
	        		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        		factory.setNamespaceAware(false);
	        		XmlPullParser xpp = factory.newPullParser();
	         
	                // We will get the XML from an input stream
	        		xpp.setInput(url.openConnection().getInputStream(), "UTF_8");
	         
	                /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
	                 * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
	                 * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
	                 * so we should skip the "<title>" tag which is a child of "<channel>" tag,
	                 * and take in consideration only "<title>" tag which is a child of "<item>"
	                 *
	                 * In order to achieve this, we will make use of a boolean variable.
	                 */
	        		boolean insideItem = false;
	        		boolean insideImage = false;
	            
	         
	                // Returns the type of current event: START_TAG, END_TAG, etc..
	        		int eventType = xpp.getEventType();
	        		while (eventType != XmlPullParser.END_DOCUMENT) 
	        		{
	        			boolean isImageAvailable = false;
	        			
	        			if (eventType == XmlPullParser.START_TAG) 
	        			{
	         
	        				if (xpp.getName().equalsIgnoreCase("item")) 
	        				{
	        					insideItem = true;
	        				} 
	        				else if (xpp.getName().equalsIgnoreCase("title")) 
	        				{
	        					if (insideItem)
	        						headlines.add(xpp.nextText()); //extract the headline
	        				} 
	        				else if (xpp.getName().equalsIgnoreCase("link")) 
	        				{
	        					if (insideItem)
	        						links.add(xpp.nextText()); //extract the link of article
	        				}
	        				else if(xpp.getName().equalsIgnoreCase("pubdate"))
	        				{
	        					if(insideItem)
	        						pubDate.add(xpp.nextText());
	        				}
	        				else if(xpp.getName().equalsIgnoreCase("image"))
	        				{
	        					if(insideItem)
	        						insideImage = true;
	        				}
	        				else if(xpp.getName().equalsIgnoreCase("url"))
	        				{
	        					if(insideImage)
	        					{
	        						imageUrl.add(xpp.nextText());
	        						isImageAvailable = true;
	        					}
	        				}
	        			}
	        			else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item"))
	        			{
	        				insideItem=false;
	        				if(isImageAvailable == false && insideItem == false)
		        				imageUrl.add("");
	        			}
	        			else if(eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("image"))
	        			{
	        				insideImage = false;
	        			}
	        			
	        			
	         
	        			eventType = xpp.next(); //move to next element
	        		}
	         
	        	}
	        	catch (MalformedURLException e) 
	        	{
	        		e.printStackTrace();
	        	}
	        	catch (XmlPullParserException e) 
	        	{
	        		e.printStackTrace();
	        	}
	        	catch (IOException e) 
	        	{
	        		e.printStackTrace();
	        	}
	        }
			return "Feed Parsed";
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) 
		{
		  super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			BrowserFeed customList = new BrowserFeed(Homepage.this,headlines,links,pubDate, imageUrl);
			customList.setInterface(Homepage.this);
			lv.setAdapter(customList);
			
	        Toast.makeText(getApplicationContext(), ""+headlines.size()+" "+imageUrl.size(),Toast.LENGTH_LONG).show();
		}
		
	}
	
	
	public InputStream getInputStream(URL url) 
	{
		   try 
		   {
		       return url.openConnection().getInputStream();
		   } 
		   catch (IOException e) 
		   {
		       return null;
		   }
	}
	
	@Override
	public void onCustomListClick(int position, View view) 
	{
		// TODO Auto-generated method stub
		Intent i = new Intent (Homepage.this,SimpleBrowser.class);
		i.putExtra("link", (String)links.get(position));
		startActivity(i);
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
			if(str.length()>0)
			{
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
			
				Intent i = new Intent (Homepage.this,SimpleBrowser.class);
				i.putExtra("link", str);
				startActivity(i);
			}
			break;
		case R.id.home_bHistory:
			Intent i=new Intent("com.example.zsurfer.VIEWHISTORY");
			startActivity(i);
			break;
		case R.id.home_bShowBkMrk:
			Intent i2=new Intent("com.example.zsurfer.VIEWBOOKMARKS");
			startActivity(i2);
			break;
			
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





//---------------------------------------------------------------------------------------------------------------------------

/*
 * The following classes will create a custom grid view 
 */
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
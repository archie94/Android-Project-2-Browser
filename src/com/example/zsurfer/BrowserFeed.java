package com.example.zsurfer;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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
	public class FeedsAsyncTask extends AsyncTask<String,Void,Boolean>
	{

		@Override
		protected Boolean doInBackground(String... params) 
		{
			// TODO Auto-generated method stub
			// does thing in a background thread 
			try
			{
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(params[0]);
				HttpResponse response = client.execute(post);
				
				int status = response.getStatusLine().getStatusCode();
				
				if(status==200)
				{
					HttpEntity entity = response.getEntity();
					String data = EntityUtils.toString(entity);
					
					JSONObject jObj = new JSONObject(data);
					JSONArray jArray = jObj.getJSONArray(""); // pass name of object in here --- ???
					for(int i=0;i<jArray.length();i++)
					{
						JSONObject jRealObj = jArray.getJSONObject(i);
						String title = jRealObj.getString(""); // pass key here 
					}
				}
			}
			catch(ClientProtocolException e)
			{
				e.printStackTrace();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
		
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
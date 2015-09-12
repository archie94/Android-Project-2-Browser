package com.example.zsurfer;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.webkit.WebChromeClient;

@SuppressLint("SetJavaScriptEnabled") public class SimpleBrowser extends Activity implements View.OnClickListener
{
	WebView contentView;
	EditText url;
	String currentUrl;
	Button Go,Back,Forward,Refresh,Home,addBookmark,vHistory,vBookMark;
	HistoryHandler hHandler;
	BookmarkHandler bHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        // Add progress bar support 
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        // Set xml for the content view
        setContentView(R.layout.activity_simple_browser);
        
        // make progress bar visible
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        contentView=(WebView)findViewById(R.id.WebView);
        
        
        contentView.getSettings().setJavaScriptEnabled(true);//enables javascript in our browser
        contentView.getSettings().setLoadWithOverviewMode(true);//web page completely zoomed down
        contentView.getSettings().setUseWideViewPort(true);//
        contentView.setWebViewClient(new ourViewClient());//overrides a method so that a link in any web page does not load up in default browser
        contentView.setWebViewClient(new WebViewClient()
        {

			@Override
			public void onPageFinished(WebView view, String url) 
			{
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				// add to history when page has finished loading 
				History h=new History(contentView.getUrl());
        		hHandler.addHistory(h);
        	    Toast.makeText(getApplicationContext(), "added ",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) 
			{
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(getApplicationContext(), "Failed to load page",Toast.LENGTH_LONG).show();
			}
        	
        });
        
        contentView.setDownloadListener(new DownloadListener()
        {//enable downloading files through web view in my browser
        	public void onDownloadStart(String url, String userAgent,String contentDisposition, String mimetype,long contentLength)
        	{
        		Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
        	}
        });
        
        
        if(isNetworkAvailable()==false)//check if network is available 
        {
        	Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
        }
        /*else
        {
        	Toast.makeText(getApplicationContext(), "Internet Connection",Toast.LENGTH_LONG).show();
        }*/
        
        Bundle bundle=getIntent().getExtras();
       
        if(bundle==null)
        {
        	//will load google search page as the default page 
        	try
        	{
        		contentView.loadUrl("https://www.google.com");
        		currentUrl=contentView.getUrl();
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        }
        else
        {
        	// will get the address from bundle 
        	try
        	{
        		contentView.loadUrl(bundle.getString("msg"));
        		currentUrl=contentView.getUrl();
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        }
        
        initialise();//initialize all other variable after completing the settings for WebView
        
        //url.setOnClickListener(this);
        
        Go.setOnClickListener(this);
        Back.setOnClickListener(this);
        Forward.setOnClickListener(this);
        Refresh.setOnClickListener(this);
        Home.setOnClickListener(this);
        addBookmark.setOnClickListener(this);
        vHistory.setOnClickListener(this);
        vBookMark.setOnClickListener(this);
        
        
        
        currentUrl=contentView.getUrl();
        url.setText(currentUrl);
        // set ChromeClient, and defines on ProgressChanged
        // this updates the progress bar 
        final Activity MyActivity = this;
        contentView.setWebChromeClient(new WebChromeClient() 
        {
         public void onProgressChanged(WebView view, int progress)   
         {
          //Make the bar disappear after URL is loaded, and changes string to Loading...
          MyActivity.setTitle("Surfing...");
          MyActivity.setProgress(progress * 100); //Make the bar disappear after URL is loaded
  
          // Return the app name after finish loading
             if(progress == 100)
             {
            	 MyActivity.setTitle(R.string.app_name);
             }
             
             // get current url as the web page loads  and set the url in the edit text 
             currentUrl=contentView.getUrl();
     		 url.setText(currentUrl);
     		
         }
        });
    }
    
    
    
    
    private boolean isNetworkAvailable() 
    {
		// TODO Auto-generated method stub
    	//method to check if there is Internet connection 
    	ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo isActive=cm.getActiveNetworkInfo();
		return (isActive!=null && isActive.isConnected());
	}
    
    


	private void initialise() 
	{
		// TODO Auto-generated method stub
		// Initialize all buttons edit texts etc  ... 
		//note web view is defined earlier 
		
		
		url=(EditText)findViewById(R.id.editText1);
		Go=(Button)findViewById(R.id.bGO);
		Back=(Button)findViewById(R.id.bBack);
		Forward=(Button)findViewById(R.id.bForward);
		Refresh=(Button)findViewById(R.id.bRefresh);
		Home=(Button)findViewById(R.id.bHome);
		addBookmark=(Button)findViewById(R.id.bBKMRK);
		hHandler=new HistoryHandler(this,null,null,1);
		vHistory=(Button)findViewById(R.id.bHistory);
		vBookMark=(Button)findViewById(R.id.bShowBkMrk);
		bHandler=new BookmarkHandler(this,null,null,1);
	}


	
	
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK && contentView.canGoBack())
		{
			//On pressing the back button reload the previous page 
			contentView.goBack();
			return true;
		}
		return super.onKeyUp(keyCode, event); // if no previous page close application 
	}


	
	
	
	@Override
	public void onClick(View arg0) 
	{
		// TODO Auto-generated method stub
		
		switch(arg0.getId())
		{
		case R.id.bGO:
			String address=url.getText().toString();
			//load the web page as given
			try
			{
				contentView.loadUrl(address);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			break;
		case R.id.bBack:
			// go back a web page 
			if(contentView.canGoBack())// check if we can go back 
			{
				contentView.goBack();
			}
			break;
		case R.id.bForward:
			// go forward a web page 
			if(contentView.canGoForward())//check if we can go forward 
				contentView.goForward();
			break;
		case R.id.bHome:
			//our home page is google 
			try
			{
				contentView.loadUrl("http://www.google.com");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			break;
		case R.id.bRefresh:
			//refresh the page 
			contentView.reload();
			break;
		case R.id.bBKMRK:
			
			BookMarks b=new BookMarks(contentView.getUrl());
			bHandler.addDB(b);
			Toast.makeText(getApplicationContext(), "Bookmark  added ",Toast.LENGTH_LONG).show();
			
			
			break;
		case R.id.bHistory:
			Intent i=new Intent("com.example.zsurfer.VIEWHISTORY");
			startActivity(i);
			
			break;
			
		case R.id.bShowBkMrk:
			Intent i2=new Intent("com.example.zsurfer.VIEWBOOKMARKS");
			startActivity(i2);
			
			break;
			
		
		}
	}// end of onClick()


    
    
}

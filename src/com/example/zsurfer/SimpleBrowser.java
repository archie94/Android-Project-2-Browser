package com.example.zsurfer;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.webkit.WebChromeClient;

@SuppressLint("SetJavaScriptEnabled") public class SimpleBrowser extends Activity implements View.OnClickListener
{
	WebView contentView;
	EditText url;
	String currentUrl;
	Button Go,Back,Forward,Refresh,Home,addBookmark;

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
        contentView.setDownloadListener(new DownloadListener()
        {//enable downloading files through web view in my browser
        	public void onDownloadStart(String url, String userAgent,String contentDisposition, String mimetype,long contentLength)
        	{
        		Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
        	}
        });
        
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
        
        initialise();//initialise all other variable after completing the settings for WebView
        
        //url.setOnClickListener(this);
        
        Go.setOnClickListener(this);
        Back.setOnClickListener(this);
        Forward.setOnClickListener(this);
        Refresh.setOnClickListener(this);
        Home.setOnClickListener(this);
        addBookmark.setOnClickListener(this);
        
        currentUrl=contentView.getUrl();
        url.setText(currentUrl);
        // set Chrome Client, and defines on ProgressChanged
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
                MyActivity.setTitle(R.string.app_name);
             
             // get current url as the web page loads  and set the url in the edit text 
             currentUrl=contentView.getUrl();
     		 url.setText(currentUrl);
         }
        });
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
				contentView.goBack();
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
			break;
		
		}
	}// end of onClick()


    
    
}

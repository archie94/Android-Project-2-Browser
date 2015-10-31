package com.example.zsurfer;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
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
	Bitmap back,forward,refresh,home;

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
        
        //overrides a method so that a link in any web page does not load up in default browser
        contentView.setWebViewClient(new ourViewClient());
        contentView.setWebViewClient(new WebViewClient()
        {
        	boolean willSave=true; 

			@Override
			public void onPageFinished(WebView view, String url) 
			{
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				// add to history when page has finished loading and page has loaded successfully 
				addToHistory();
				
			}

			private void addToHistory() 
			{
				// TODO Auto-generated method stub
				if(willSave==true)
				{
					History h=new History(contentView.getUrl());
					hHandler.addHistory(h);
					Toast.makeText(getApplicationContext(), "added ",Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) 
			{
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(getApplicationContext(), "Failed to load page",Toast.LENGTH_LONG).show();
				willSave=false;
				addToHistory();
			}
        	
        });
        
      //enable downloading files through web view in my browser
        contentView.setDownloadListener(new DownloadListener()
        {
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
        
        browserWork(bundle);
       
        /*if(bundle==null)
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
        		contentView.loadUrl(bundle.getString("link"));
        		currentUrl=contentView.getUrl();
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        }
        
        
        
        initialise();//initialize all other variable after completing the settings for WebView
        
        
        Go.setOnClickListener(this);
        Back.setOnClickListener(this);
        Forward.setOnClickListener(this);
        Refresh.setOnClickListener(this);
        Home.setOnClickListener(this);
        addBookmark.setOnClickListener(this);
        vHistory.setOnClickListener(this);
        vBookMark.setOnClickListener(this);
        url.setOnEditorActionListener(new OnEditorActionListener() 
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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
				String address=url.getText().toString();// get text to be searched from edit text
				try
				{
					contentView.loadUrl("https://www.google.com/search?q="+address);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
        });
        
        
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
        });*/
    }
    
    
    
    
    private void browserWork(Bundle bundle) 
    {
		// TODO Auto-generated method stub
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
        		contentView.loadUrl(bundle.getString("link"));
        		currentUrl=contentView.getUrl();
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
        }
        
        
        
        initialise();//initialize all other variable after completing the settings for WebView
        
        
        Go.setOnClickListener(this);
        Back.setOnClickListener(this);
        Forward.setOnClickListener(this);
        Refresh.setOnClickListener(this);
        Home.setOnClickListener(this);
        addBookmark.setOnClickListener(this);
        vHistory.setOnClickListener(this);
        vBookMark.setOnClickListener(this);
        url.setOnEditorActionListener(new OnEditorActionListener() 
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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
				String address=url.getText().toString();// get text to be searched from edit text
				try
				{
					contentView.loadUrl("https://www.google.com/search?q="+address);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
        });
        
        
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




	@Override
	public void onWindowFocusChanged(boolean hasFocus) 
    {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		// will add the background to our buttons here  
		
		int height=Back.getHeight(); // store height of each button 
		int width=Back.getWidth(); // store width of each button 
		
		/* create a bitmap and then 
		 * scale the bitmap according to 
		 * our height and width 
		 */
		
		back=BitmapFactory.decodeResource(getResources(), R.drawable.back);
		back=Bitmap.createScaledBitmap(back, width, height, true);
		Resources r1=getResources();
		Back.setBackground(new BitmapDrawable(r1,back));
		
		forward=BitmapFactory.decodeResource(getResources(), R.drawable.forward);
		forward=Bitmap.createScaledBitmap(forward, width, height, true);
		Resources r2=getResources();
		Forward.setBackground(new BitmapDrawable(r2,forward));
		
		refresh=BitmapFactory.decodeResource(getResources(), R.drawable.refresh);
		refresh=Bitmap.createScaledBitmap(refresh, width, height, true);
		Resources r3=getResources();
		Refresh.setBackground(new BitmapDrawable(r3,refresh));
		
		home=BitmapFactory.decodeResource(getResources(), R.drawable.home);
		home=Bitmap.createScaledBitmap(home, width, height, true);
		Resources r4=getResources();
		Home.setBackground(new BitmapDrawable(r4,home));
		
		
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
			if(address.startsWith("http"))
			{
				//load the web page as given
				try
				{
					contentView.loadUrl(address);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else if(address.startsWith("www."))
			{
				try
				{
					contentView.loadUrl("https://"+address);
				}
				catch(Exception e)
				{
					//e.printStackTrace();
				}
				finally
				{
					// if we cannot load page then try searching 
					try
					{
						contentView.loadUrl("https://www.google.com/search?q="+address);
					}
					catch(Exception e2)
					{
						e2.printStackTrace();
					}
				}
			}
			else
			{
				// treat the string as a text to be searched 
				try
				{
					contentView.loadUrl("https://www.google.com/search?q="+address);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
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
			// we load our home page 
			try
			{
				Intent i = new Intent(SimpleBrowser.this,Homepage.class);
				startActivity(i);
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




	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
		/*  pause the web view when the activity goes 
		 * in the background 
		 */
		contentView.onPause();
	}




	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		/* resume the web view again 
		 * when it comes in foreground 
		 */
		contentView.onResume();
	}




	@Override
	protected void onNewIntent(Intent intent) 
	{
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		Bundle bundle = getIntent().getExtras();
		browserWork(bundle);
	}
	
	
	
	
}

package com.example.zsurfer;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

@SuppressLint("SetJavaScriptEnabled") public class SimpleBrowser extends Activity implements View.OnClickListener
{
	WebView contentView;
	EditText url;
	Button Go,Back,Forward,Refresh,Home;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_browser);
        
        contentView=(WebView)findViewById(R.id.WebView);
        
        
        contentView.getSettings().setJavaScriptEnabled(true);//enables javascript in our browser
        contentView.getSettings().setLoadWithOverviewMode(true);//web page completely zoomed down
        contentView.getSettings().setUseWideViewPort(true);//
        contentView.setWebViewClient(new ourViewClient());//overrides a method so that a link in any web page does not load up in default browser
        
        try
        {
        	contentView.loadUrl("https://www.google.com");
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
	}


	@Override
	public void onClick(View arg0) 
	{
		// TODO Auto-generated method stub
		switch(arg0.getId())
		{
		case R.id.bGO:
			String address=url.getText().toString();
			contentView.loadUrl(address);
			break;
		case R.id.bBack:
			if(contentView.canGoBack())
				contentView.goBack();
			break;
		case R.id.bForward:
			if(contentView.canGoForward())
				contentView.goForward();
			break;
		case R.id.bHome://our home page is google 
			contentView.loadUrl("http://www.google.com");
			break;
		case R.id.bRefresh:
			contentView.reload();
			break;
		
		}
	}


    
    
}

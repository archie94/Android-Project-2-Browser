package com.example.zsurfer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Class defines the main browsing activity 
 * @author Arka
 * @version 26 February 2016
 */
@SuppressLint("SetJavaScriptEnabled") public class SimpleBrowser extends Activity implements View.OnClickListener {
	CustomisedWebView webView;
	AutoCompleteTextView url;
	String currentUrl;
	Button Go,Back,Forward,Refresh,Home,addBookmark, Options;
	HistoryHandler hHandler;
	BookmarkHandler bHandler;
	java.util.List<String> autocompleteHints;

	final Bitmap[] webViewBitmap = new Bitmap[1];
	private boolean isNavigationBarOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add progress bar support 
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_simple_browser);
        
        // make progress bar visible
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
		webView=(CustomisedWebView)findViewById(R.id.WebView);


		webView.getSettings().setJavaScriptEnabled(true);//enables javascript in our browser
		webView.getSettings().setLoadWithOverviewMode(true);//web page completely zoomed down
		webView.getSettings().setUseWideViewPort(true);//
		webView.getSettings().setAppCacheMaxSize(8 * 1024 * 1024); // 8 MB for cache
		webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        
        //overrides a method so that a link in any web page does not load up in default browser
		webView.setWebViewClient(new ourViewClient());
		webView.setWebViewClient(new WebViewClient() {
			boolean willSave = true;

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				// add to history when page has finished loading and page has loaded successfully 
				addToHistory();
				webViewBitmap[0] = createWebViewToBitmap();
			}

			private void addToHistory() {
				// TODO Auto-generated method stub
				if (willSave == true) {
					History h = new History(webView.getUrl());
					hHandler.addHistory(h);
					Log.d("SimpleBrowser : ", "Added to history" + h.toString());
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				Log.d("SimpleBrowser : ", "Failed to load page");
				willSave = false;
				addToHistory();
			}


		});
        
        //enable downloading files through web view in my browser
		webView.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});

		// disable the navigation bar while scrolling
		webView.setOnScrollChangedCallback(new CustomisedWebView.OnScrollChangedCallback() {
			@Override
			public void onScroll(int l, int t) {
				disableNavigationBar();
			}
		});

		// enable the navigation bar when a tap is detected on webview
		webView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					enableNavigationBar();
				}
				return false;
			}
		});

        /*
         * Check if network is available
         * If not available display a proper Toast
         */
        if(isNetworkAvailable()==false) {
        	Toast.makeText(getApplicationContext(), "No Internet Connection",Toast.LENGTH_LONG).show();
			webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }
        
        Bundle bundle=getIntent().getExtras();
        
        browserWork(bundle);
    }


	/**
	 * Enable the navigation bar
	 */
	private void enableNavigationBar() {
		isNavigationBarOpen = true;
		// enable navigation bar
		url.setVisibility(View.VISIBLE);
		Go.setVisibility(View.VISIBLE);
		Back.setVisibility(View.VISIBLE);
		Forward.setVisibility(View.VISIBLE);
		Refresh.setVisibility(View.VISIBLE);
		Home.setVisibility(View.VISIBLE);
		addBookmark.setVisibility(View.VISIBLE);
		Options.setVisibility(View.VISIBLE);
	}

	/**
	 * Disable the navigation bar
	 */
	private void disableNavigationBar() {
		isNavigationBarOpen = false;
		// disable navigation bar
		url.setVisibility(View.GONE);
		Go.setVisibility(View.GONE);
		Back.setVisibility(View.GONE);
		Forward.setVisibility(View.GONE);
		Refresh.setVisibility(View.GONE);
		Home.setVisibility(View.GONE);
		addBookmark.setVisibility(View.GONE);
		Options.setVisibility(View.GONE);
	}

	/**
	 * Calculates the Letter Size Paper's Height depending on the LetterSize Dimensions and Given width.
	 * @param width
	 * @return
	 */
	private int getLetterSizeHeight(int width) {
		return (int)((float) (11*width)/8.5);
	}

	/**
	 * Create a bitmap of the snapshot of webView
	 * @return : bitmap of snapshot
	 */
	private Bitmap createWebViewToBitmap() {
		webView.measure(View.MeasureSpec.makeMeasureSpec(
						View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
		webView.setDrawingCacheEnabled(true);
		webView.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(webView.getMeasuredWidth(),
				webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		int iHeight = bitmap.getHeight();
		canvas.drawBitmap(bitmap, 0, iHeight, paint);
		webView.draw(canvas);
		return bitmap;
	}

	/**
	 * Create PDF of the webview
	 * @param pdfName : The name of PDF file
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void createWebViewPDF(String pdfName) throws IOException, DocumentException {
		File pdfDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
									"/ZsurferPDF");
		if( !pdfDirectory.exists()) { // create directory if it does not exist
			if ( pdfDirectory.mkdir()) {
				Log.d("SimpleBrowser : ", "PDF storage directory created");
			}
		}

		String pdfPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ZsurferPDF";
		com.itextpdf.text.Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(pdfPath + "/" + pdfName + ".pdf"));
		document.open();

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		webViewBitmap[0].compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		byte[] bytes = byteArrayOutputStream.toByteArray();
		Image image = Image.getInstance(bytes);
		image.scaleToFit(PageSize.A4.getHeight(), PageSize.A4.getWidth());
		document.add(image);

		document.close();
		Toast.makeText(this, "Saved as PDF", Toast.LENGTH_SHORT).show();
	}

	/**
     * Load web page from bundle 
     * If bundle is null load google search page
     * @param bundle
     */
    private void browserWork(Bundle bundle) {
    	if(bundle==null) {
        	//will load google search page as the default page 
        	try {
				webView.loadUrl("https://www.google.com");
        		currentUrl=webView.getUrl();
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        } else {
        	// will get the address from bundle 
        	try {
				webView.loadUrl(bundle.getString("link"));
        		currentUrl=webView.getUrl();
        	} catch(Exception e) {
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
        Options.setOnClickListener(this);
        url.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchGoogle();
                    handled = true;
                }
                return handled;
            }

			private void searchGoogle() {
				// TODO Auto-generated method stub
				String address=url.getText().toString();// get text to be searched from edit text
				try {
					webView.loadUrl("https://www.google.com/search?q="+address);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
        });
        
        
        currentUrl=webView.getUrl();
        url.setText(currentUrl);
        /*
         * set ChromeClient, and defines on ProgressChanged 
         * this updates the progress bar 
         */
        final Activity MyActivity = this;
		webView.setWebChromeClient(new WebChromeClient()
        {
         public void onProgressChanged(WebView view, int progress)   
         {
          //Make the bar disappear after URL is loaded, and changes string to Loading...
          MyActivity.setTitle("Surfing...");
          MyActivity.setProgress(progress * 100); //Make the bar disappear after URL is loaded
  
          // Return the app name after finish loading
             if(progress == 100) {
            	 MyActivity.setTitle(R.string.app_name);
             }
             
             // get current url as the web page loads  and set the url in the edit text 
             currentUrl=webView.getUrl();
     		 url.setText(currentUrl);
     		
         }
        });
	}
    
    /**
     * Add background to buttons here  
     */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		
		int height=Back.getHeight(); // store height of each button 
		int width=Back.getWidth(); // store width of each button 
		
		/* create a bitmap and then 
		 * scale the bitmap according to 
		 * our height and width 
		 */
		Bitmap back,forward,refresh,home,go,option,bookmark;
		
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
		
		go=BitmapFactory.decodeResource(getResources(), R.drawable.go);
		go=Bitmap.createScaledBitmap(go, width, height, true);
		Resources r5=getResources();
		Go.setBackground(new BitmapDrawable(r5,go));
		
		option=BitmapFactory.decodeResource(getResources(), R.drawable.option);
		option=Bitmap.createScaledBitmap(option, width, height, true);
		Resources r6=getResources();
		Options.setBackground(new BitmapDrawable(r6,option));

		bookmark=BitmapFactory.decodeResource(getResources(), R.drawable.bookmark);
		bookmark=Bitmap.createScaledBitmap(bookmark, width, height, true);
		Resources r7=getResources();
		addBookmark.setBackground(new BitmapDrawable(r7, bookmark));

		enableNavigationBar();
	}

	/**
	 * check if there is Internet connection
	 */
	private boolean isNetworkAvailable() {
    	ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo isActive=cm.getActiveNetworkInfo();
		return (isActive!=null && isActive.isConnected());
	}
    
    /**
     * Initialize all buttons edit texts etc  ...
     * note web view is defined earlier 
     */
	private void initialise() {
		url=(AutoCompleteTextView)findViewById(R.id.editText1);
		Go=(Button)findViewById(R.id.bGO);
		Back=(Button)findViewById(R.id.bBack);
		Forward=(Button)findViewById(R.id.bForward);
		Refresh=(Button)findViewById(R.id.bRefresh);
		Home=(Button)findViewById(R.id.bHome);
		addBookmark=(Button)findViewById(R.id.bBKMRK);
		hHandler=new HistoryHandler(this,null,null,1);
		Options = (Button)findViewById(R.id.bOptions);
		bHandler=new BookmarkHandler(this,null,null,1);
	}

	/**
	 * On pressing the back button reload the previous page
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyUp(keyCode, event); // if no previous page close application 
	}

	/**
	 * Define actions of buttons here 
	 */
	@Override
	public void onClick(View arg0) {

		switch(arg0.getId()) {

		case R.id.bGO:
			String address=url.getText().toString();

			if(address.startsWith("http")) {
				//load the web page as given
				try {
					webView.loadUrl(address);
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else if(address.startsWith("www.")) {
				try {
					webView.loadUrl("https://"+address);
				} catch(Exception e) {
					//e.printStackTrace();
				} finally {
					// if we cannot load page then try searching 
					try {
						webView.loadUrl("https://www.google.com/search?q="+address);
					} catch(Exception e2) {
						e2.printStackTrace();
					}
				}
			} else {
				// treat the string as a text to be searched 
				try {
					webView.loadUrl("https://www.google.com/search?q="+address);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.bBack:
		   /* 
		    * go back a web page
			* after checking if we can go back
			*/
			if(webView.canGoBack()) {
				webView.goBack();
			}
			break;
		case R.id.bForward:
			/*
			 * go forward a web page
			 * after checking if we can go forward
			 */
			if(webView.canGoForward()) {
				webView.goForward();
			}
			break;
		case R.id.bHome:
			// we load our home page 
			try {
				Intent i = new Intent(SimpleBrowser.this,Homepage.class);
				startActivity(i);
			} catch(Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.bRefresh:
			//refresh the page 
			webView.reload();
			break;
		case R.id.bBKMRK:
			/*
			 * add the current web page as a bookmark 
			 */
			BookMarks b=new BookMarks(webView.getUrl());
			bHandler.addDB(b);
			Toast.makeText(getApplicationContext(), "Bookmark  added ",Toast.LENGTH_LONG).show();
			break;
		case R.id.bOptions:
			PopupMenu popup = new PopupMenu(SimpleBrowser.this,Options);
			popup.getMenuInflater().inflate(R.layout.browser_options, popup.getMenu());
			
			popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() 
			{
				
				@Override
				public boolean onMenuItemClick(MenuItem item) 
				{
					// TODO Auto-generated method stub
					if(item.getTitle().equals("Bookmarks")) {
						startActivity(new Intent("com.example.zsurfer.VIEWBOOKMARKS"));
					} else if(item.getTitle().equals("History")) {
						startActivity(new Intent("com.example.zsurfer.VIEWHISTORY"));
					} else if(item.getTitle().equals("Share Page")) {
						Intent i = new Intent();
						i.setAction(Intent.ACTION_SEND);
						i.putExtra(Intent.EXTRA_TEXT, url.getText().toString());
						i.setType("text/plain");
						startActivity(Intent.createChooser(i, "Share Page using"));
					} else if(item.getTitle().equals("Save Page")) {
						try {
							createWebViewPDF(webView.getTitle());
						} catch (IOException e) {
							e.printStackTrace();
						} catch (DocumentException e) {
							e.printStackTrace();
						}
					} else if(item.getTitle().equals("Find In Page")) {

					} else if(item.getTitle().equals("Page Info")) {
						startActivity(new Intent(SimpleBrowser.this,PageInformation.class).putExtra("pgaddr", url.getText().toString()));
					}
					return false;
				}
			});
			
			popup.show();
			break;

		}
	}// end of onClick()




	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/*  pause the web view when the activity goes 
		 * in the background 
		 */
		webView.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/* resume the web view again 
		 * when it comes in foreground 
		 */
		webView.onResume();
	}

	/**
	 * Replace the current intent of this activity with the pre-existing intent  
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		Bundle bundle = getIntent().getExtras();
		browserWork(bundle);
	}
	
}

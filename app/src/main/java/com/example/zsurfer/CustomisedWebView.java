package com.example.zsurfer;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by arka on 4/4/16.
 */
public class CustomisedWebView extends WebView {
    private OnScrollChangedCallback onScrollChangedCallback;
    public CustomisedWebView(Context context) {
        super(context);
    }

    public CustomisedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomisedWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if( onScrollChangedCallback != null) {
            onScrollChangedCallback.onScroll( l, t);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return onScrollChangedCallback;
    }

    public void setOnScrollChangedCallback( OnScrollChangedCallback onScrollChangedCallback) {
        this.onScrollChangedCallback = onScrollChangedCallback;
    }

    public static interface OnScrollChangedCallback {
        public void onScroll(int l, int t);
    }
}

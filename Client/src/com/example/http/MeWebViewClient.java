package com.example.http;

import android.net.http.SslError;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MeWebViewClient extends WebViewClient {

	@Override
	@JavascriptInterface
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	@JavascriptInterface
	public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
		handler.proceed();
	}
	
	@Override
	@JavascriptInterface
	public void onPageFinished(WebView view, String url) {
		//view.loadUrl("javascript:(function(){document.getElementById('mA').click();})()");
		view.loadUrl("www.google.ru");
	}
}

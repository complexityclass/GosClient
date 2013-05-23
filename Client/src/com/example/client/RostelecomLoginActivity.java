package com.example.client;

import java.lang.annotation.Annotation;

import com.example.http.MeWebViewClient;
import com.example.http.WebAppInterface;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class RostelecomLoginActivity extends Activity {
	
	WebView webView;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_rostelecom_login);
		Intent webIntent = getIntent();
		final String url = webIntent.getStringExtra("url");

		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSaveFormData(true);
		webView.getSettings().setSavePassword(true);
		webView.setWebViewClient(new MeWebViewClient());
		
		Button buttoner = (Button) findViewById(R.id.button1);
	    buttoner.setOnClickListener(new OnClickListener() {

	        public void onClick(View v) {
	            webView.addJavascriptInterface(new Object(){
	            	@JavascriptInterface
	            	public void test(){
	            		Log.d("JS", "test");
	            	}
	            },"Android");
	            webView.loadUrl("javascript:(function(){document.getElementById('mA').click();})()");
	        }
	    });
		
		webView.loadUrl(url);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rostelecom_login, menu);
		return true;
	}
	
	
	class JsObject{
		@JavascriptInterface
		public String toString(){
			return "injectedObject";
		}
	}

}

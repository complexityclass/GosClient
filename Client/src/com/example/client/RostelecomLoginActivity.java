package com.example.client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class RostelecomLoginActivity extends Activity {
	
	WebView webView;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_rostelecom_login);
		
		Intent webIntent = getIntent();
		String url = webIntent.getStringExtra("url");

		webView = (WebView) findViewById(R.id.webView1);

		WebSettings webSettings = webView.getSettings();
		webSettings.setSavePassword(true);
		webSettings.setSaveFormData(true);
		webSettings.setJavaScriptEnabled(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rostelecom_login, menu);
		return true;
	}

}

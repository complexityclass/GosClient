package com.example.client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author complexityclass
 * 
 */

public class WebViewActivity extends Activity {

	WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		Intent webIntent = getIntent();
		String url = webIntent.getStringExtra("url");

		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				webView.loadUrl("javascript:(function() { document.getElementsByTagName('button')[1].click(); })()");
			}

		});

		webView.loadUrl(url);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_web_view, menu);
		return true;
	}
}

package com.example.client;

import android.net.http.SslError;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * @author complexityclass
 * 
 */

public class WebViewActivity extends Activity {

	WebView webView;

	String url;

	private int page;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_web_view);

		Intent webIntent = getIntent();
		// url = webIntent.getStringExtra("url");

		webView = (WebView) findViewById(R.id.webView1);

		WebSettings webSettings = webView.getSettings();
		webSettings.setSavePassword(true);
		webSettings.setSaveFormData(true);
		webSettings.setJavaScriptEnabled(true);

		final Activity activity = this;

		page = 0;

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				activity.setProgress(newProgress * 100);

				if (newProgress == 100) {
					activity.setTitle("run");
				}
			}

		});

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.indexOf("esia") <= 0) {
					page = 1;
				} else {
					page = 0;
				}
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				view.loadDataWithBaseURL("/Client/assets/tmp1/index.htm", "/Client/assets/tmp1/test.js",
						"text/javascript", "utf-8", "");
				// view.loadUrl("javascript:(function() { document.getElementsByTagName('button')[1].click(); })()");

			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Toast.makeText(getApplicationContext(), "Error: " + description + " " + failingUrl, Toast.LENGTH_LONG)
						.show();
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

		});

		// webView.loadUrl(url);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_web_view, menu);
		return true;
	}
}

package com.example.client;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.example.http.MeWebViewClient;
import com.example.http.PostData;
import com.example.http.WebAppInterface;

import android.net.http.SslError;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.graphics.Bitmap;

public class RostelecomLoginActivity extends Activity {

	WebView webView;
	String url;

	public static int auth = 1;

	@SupressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rostelecom_login);

		Intent webIntent = getIntent();
		final String url = webIntent.getStringExtra("url");

		webView = (WebView) findViewById(R.id.webView1);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSavePassword(true);
		webSettings.setSaveFormData(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar1);

		webView.setWebViewClient(new WebViewClient() {

			public void onPageLoad(WebView view, String url) {
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				webView.setVisibility(View.INVISIBLE);
			}

			public void onPageFinished(final WebView view, String url) {

				view.loadUrl("javascript:(function f(){if (portal.UID == 0) {pLogin(2);} else {alert('authorized');if(document.URL.indexOf('Details',0) < 0){"
						+ "var links = document.getElementsByTagName('a');for (i = 0; i < links.length; i++) {if (links[i].className == 'private-cab-link') {links[i].click();}}"
						+ "}else{alert('ok');}}})()");

				view.loadUrl("javascript:(function erase(){if(document.URL.indexOf('Details',0) > 0){ var obj = document.getElementsByTagName('div');"
						+ "for(i = 0; i < obj.length; i++){if(obj[i].className == 'header' || obj[i].className == 'footer' || "
						+ "obj[i].className == 'category-menu-wrapper' || obj[i].className == 'r-layout pull-right'){obj[i].parentNode.removeChild(obj[i]);}}"
						+ "var arr = document.getElementsByTagName('ul');for(i = 0; i < arr.length; i++){if(arr[i].className == 'nav nav-tabs'){arr[i].parentNode.removeChild(arr[i]);}}}})()");

				System.out.println("INJECTING JAVASCRIPT REDIRECTING TO ///");

				webView.setVisibility(View.VISIBLE);

			}

			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});

		webView.loadUrl(url);

		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				new PostData(getApplicationContext()).execute();

			}
		});

		Button buttonEdit = (Button) findViewById(R.id.editProfile);
		buttonEdit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent editProfileIntent = new Intent(v.getContext(), EditProfileActivity.class);
				startActivity(editProfileIntent);

			}
		});

		Button buttonCab = (Button) findViewById(R.id.personCab);

		buttonCab.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent personCabIntent = new Intent(v.getContext(), PersonCabActivity.class);
				startActivity(personCabIntent);

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.rostelecom_login, menu);
		return true;
	}

	public class myJavaScriptInterface {
		@JavascriptInterface
		public void setVisible() {
			runOnUiThread(new Runnable() {
				public void run() {
					webView.setVisibility(View.VISIBLE);
					webView.loadUrl("javascript:alert('Hello')");

				}
			});
		}
	}

}

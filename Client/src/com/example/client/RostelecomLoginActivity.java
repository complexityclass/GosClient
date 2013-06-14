package com.example.client;

import java.lang.annotation.Annotation;

import com.example.http.MeWebViewClient;
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
import android.widget.Toast;
import android.graphics.Bitmap;

public class RostelecomLoginActivity extends Activity {

	WebView webView;
	String url;

	public static int auth;

	@SupressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rostelecom_login);

		auth = 1;

		Intent webIntent = getIntent();
		final String url = webIntent.getStringExtra("url");

		webView = (WebView) findViewById(R.id.webView1);

		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSavePassword(true);
		webSettings.setSaveFormData(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		final Button button = (Button) findViewById(R.id.button1);
		final EditText loginField = (EditText) findViewById(R.id.editText1);
		final EditText passField = (EditText) findViewById(R.id.editText2);
		/*
		 * loginField.setVisibility(View.INVISIBLE);
		 * passField.setVisibility(View.INVISIBLE);
		 * button.setVisibility(View.INVISIBLE);
		 */
		webView.setWebViewClient(new WebViewClient() {

			public void onPageLoad(WebView view, String url) {
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				webView.setVisibility(View.INVISIBLE);
			}

			public void onPageFinished(final WebView view, String url) {

				if (auth == 1) {
					view.loadUrl("javascript:pLogin(2)");

					System.out.println("INJECTING JAVASCRIPT REDIRECTING TO ///");

					webView.setVisibility(View.VISIBLE);
					loginField.setVisibility(View.VISIBLE);
					passField.setVisibility(View.VISIBLE);
					button.setVisibility(View.VISIBLE);

					loginField.setHint("SNILS");
					passField.setHint("Pass");

					button.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {

							String login = loginField.getText().toString();
							String password = passField.getText().toString();

							webView.loadUrl("javascript:document.getElementById('username').value = " + login);
							webView.loadUrl("javascript:document.getElementById('password').value = " + password);
							// webView.loadUrl("javascript:document.getElementsByClassName('button-blue-big in_button display showCaptchaSignInBtn')[0].click();");
							// //webView.loadUrl("javascript:document.getElementsByClassName('col-01')[0]");
							System.out.println("login : " + login + "password : " + password);

							webView.loadUrl("javascript:"
									+ "var arr = document.getElementsByTagName('button'); "
									+ "for(i = 0, l = arr.length; i < l; ++i)"
									+ "{ if(arr[i].className == 'button-blue-big in_button display showCaptchaSignInBtn')"
									+ "{arr[i].click()} " + "}");
							
							auth = 2;

						}
					});
				} else if(auth == 2) {

					System.out.println("PUTKEY");

					// loginField.setVisibility(View.GONE);
					// passField.setVisibility(View.GONE);
					
					webView.loadUrl(url);
					auth = 3;

				} else if(auth == 3){
					System.out.println("END ");
				}

			}

			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});

		webView.loadUrl(url);

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

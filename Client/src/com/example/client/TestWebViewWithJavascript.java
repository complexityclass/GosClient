package com.example.client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.Button;

public class TestWebViewWithJavascript extends Activity {
	
	WebView webview;
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_web_view_with_javascript);
		
		Intent webIntent = getIntent();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_web_view_with_javascript, menu);
		return true;
	}

}

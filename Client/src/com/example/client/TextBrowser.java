package com.example.client;

import java.util.concurrent.ExecutionException;

import com.example.http.Connect;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TextBrowser extends Activity {

	public static final String url = "http://pgu.khv.gov.ru/";
	public static Integer page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_browser);

		final EditText text = (EditText) findViewById(R.id.textContent);
		Button button = (Button) findViewById(R.id.buttonLoad);

		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				NetworkThread netThread = new NetworkThread();
				netThread.execute(url);

				try {
					text.setText(netThread.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_browser, menu);
		return true;
	}

	private class NetworkThread extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			
			String result = "OKI?";
			
			Connect connect = new Connect(getApplicationContext());
			StringBuffer stringBuffer = connect.topgu(params[0]);
			result = stringBuffer.toString();
			result = connect.doGet("http://pgu.khv.gov.ru/?a=Authorization&category=Snils");
			
			return result;

		}

	}

}
package com.example.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.htmlcleaner.TagNode;

import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SearchActivity extends Activity {

	private EditText queryField;
	private TextView responseField;
	private Button searchButton;
	private static final String PAGE_URL = "http://pgu.khv.gov.ru/?a=Search&query=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);

		queryField = (EditText) findViewById(R.id.editText1);
		responseField = (TextView) findViewById(R.id.textView1);
		searchButton = (Button) findViewById(R.id.button1);

		queryField.setText("������");
		responseField.setText("�� ������");

		searchButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				String html = "obtaining";

				try {
					String encodeQuery = URLEncoder.encode(queryField.getText()
							.toString(), "UTF-8");
					queryField.setText(encodeQuery);

					if (NetworkStats.isNetworkAvailable(SearchActivity.this)) {
						DownloadHtml downloadHtml = new DownloadHtml();
						downloadHtml.execute(PAGE_URL + encodeQuery);
						try {
							html = downloadHtml.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				responseField.setText(html);

			}
		});

	}

	private class DownloadHtml extends AsyncTask<String, Integer, String> {

		String result;

		@Override
		protected String doInBackground(String... urls) {
			try {
				result = NetworkStats.getOutputFromURL(urls[0]);

			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

		}

	}

}

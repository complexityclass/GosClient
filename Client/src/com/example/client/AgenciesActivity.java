package com.example.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpException;
import org.htmlcleaner.TagNode;

import com.example.http.GetDataListener;
import com.example.http.MyHttpClientUsage;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AgenciesActivity extends Activity {

	public List<String> linksText;
	public List<Integer> pics;
	public String myHTML = "";
	/** URL for GET request */
	private static final String PAGE_URL = "http://pgu.khv.gov.ru/?a=Departments&category=Regional";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		linksText = new ArrayList<String>();
		pics = new ArrayList<Integer>();

		if (NetworkStats.isNetworkAvailable(this)) {
			DownloadHtml downloadHtml = new DownloadHtml();
			downloadHtml.execute(PAGE_URL);
			try {
				myHTML = downloadHtml.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(myHTML);

	}

	/** Send request in separate thread using AsyncTask */
	private class DownloadHtml extends AsyncTask<String, Integer, String> {

		String result = "";

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

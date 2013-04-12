package com.example.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.htmlcleaner.TagNode;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.client.R.string;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author complexityclass
 * 
 *         Activity class for {@link String AreasOfActivity}
 * 
 */
public class AreasOfActivity extends Activity {

	/*
	 * List<String> linksText : link handler List<Integer> pics : image handler
	 */
	public List<String> linksText;
	public List<Integer> pics;
	public static final String PAGE_URL = "http://pgu.khv.gov.ru/?a=Work&category=Regional";

	private ListView currentlistView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_layout);

		linksText = new ArrayList<String>();
		pics = new ArrayList<Integer>();

		/*
		 * Check the device network connection
		 */
		if (NetworkStats.isNetworkAvailable(this)) {
			/* get html from POST request */
			DownloadHtml downloadHtml = new DownloadHtml();
			downloadHtml.execute(PAGE_URL);
			try {
				linksText = downloadHtml.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		/* print result in LogCat */
		for (Iterator<String> iterator = linksText.iterator(); iterator.hasNext();) {
			System.out.println(iterator.next().toString());
		}

		News[] values = new News[linksText.size()];
		int i = 0;

		for (Iterator<String> iterator = linksText.iterator(); iterator.hasNext();) {
			String temp = iterator.next().toString();
			values[i] = new News(R.drawable.arrow, temp);
			i++;
		}

		/* Set NewsAdapter {@link NewsAdapter} */
		NewsAdapter adapter = new NewsAdapter(this, R.layout.list_row, values);
		currentlistView = (ListView) findViewById(R.id.listView1);
		View header = (View) getLayoutInflater().inflate(R.layout.list_header_row_areas, null);
		currentlistView.addHeaderView(header);
		currentlistView.setAdapter(adapter);

	}

	/*
	 * Class for loading html in background thread String url Integer progress
	 * value List<String> links handler
	 */
	private class DownloadHtml extends AsyncTask<String, Integer, List<String>> {

		List<String> resultList = new ArrayList<String>();

		/* run in BG thread */
		@Override
		protected List<String> doInBackground(String... urls) {
			try {
				String result = NetworkStats.getOutputFromURL(urls[0]);

				/* html parsing */
				HtmlParser parser;
				try {
					parser = new HtmlParser(result);
					List<TagNode> links = parser.getSpanText("category-menu__text");
					for (Iterator<TagNode> iterator = links.iterator(); iterator.hasNext();) {
						TagNode linkElement = (TagNode) iterator.next();
						resultList.add(linkElement.getText().toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return resultList;
		}

		/* in UI thread */
		@Override
		protected void onPostExecute(List<String> resultList) {

		}

	}

}

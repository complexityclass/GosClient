package com.example.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpException;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import com.example.http.GetDataListener;
import com.example.http.MyHttpClientUsage;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author complexityclass Activity "Ведомства"
 */
public class AgenciesActivity extends ListActivity {

	public List<String> linksText;
	public List<Integer> pics;
	public String myHTML = "";
	/** URL for GET request */
	private static final String PAGE_URL = "http://pgu.khv.gov.ru/?a=Departments&category=Regional";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		linksText = new ArrayList<String>();
		pics = new ArrayList<Integer>();

		if (NetworkStats.isNetworkAvailable(this)) {
			DownloadHtml downloadHtml = new DownloadHtml();
			downloadHtml.execute(PAGE_URL);
			try {
				linksText = downloadHtml.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("ELEMENTS IN LIST :" + linksText.size());

		for (Iterator<String> iterator = linksText.iterator(); iterator
				.hasNext();) {
			System.out.println(iterator.next().toString());
		}

		String values[] = new String[linksText.size()];

		int i = 0;
		for (Iterator<String> iterator = linksText.iterator(); iterator
				.hasNext();) {
			values[i] = iterator.next().toString();
			i++;
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.rowlayout, R.id.label, values);
		setListAdapter(adapter);

	}

	/** Send request in separate thread using AsyncTask */

	private class DownloadHtml extends AsyncTask<String, Integer, List<String>> {

		List<String> resultList = new ArrayList<String>();

		@Override
		protected List<String> doInBackground(String... urls) {
			try {
				String result = NetworkStats.getOutputFromURL(urls[0]);

				HtmlParser parser;
				try {
					parser = new HtmlParser(result);
					List<TagNode> links = parser
							.getLinks("category-menu__link");
					for (Iterator<TagNode> iterator = links.iterator(); iterator
							.hasNext();) {
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

		@Override
		protected void onPostExecute(List<String> resultList) {

		}

	}

}

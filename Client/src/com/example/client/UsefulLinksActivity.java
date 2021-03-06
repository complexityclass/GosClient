package com.example.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.htmlcleaner.TagNode;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * @author complexity Activity shows useful links and redirected to WebView
 * 
 */
public class UsefulLinksActivity extends Activity {

	private static final String PAGE_URL = "http://pgu.khv.gov.ru/?a=Static&content=51";
	private ListView currentListView;

	private List<String> linksText;
	private List<Integer> pics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_layout);

		if (NetworkStats.isNetworkAvailable(this)) {
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

		for (Iterator<String> iterator = linksText.iterator(); iterator.hasNext();) {
			System.out.println(iterator.next().toString());
		}

		News values[] = new News[linksText.size()];

		int i = 0;
		for (Iterator<String> iterator = linksText.iterator(); iterator.hasNext();) {
			String temp = iterator.next().toString();
			values[i] = new News(R.drawable.arrow, temp);
			i++;
		}

		NewsAdapter adapter = new NewsAdapter(this, R.layout.list_row, values);

		currentListView = (ListView) findViewById(R.id.listView1);
		View header = (View) getLayoutInflater().inflate(R.layout.list_header_row_areas, null);
		currentListView.addHeaderView(header);
		currentListView.setAdapter(adapter);

	}

	private class DownloadHtml extends AsyncTask<String, Integer, List<String>> {

		List<String> resultList = new ArrayList<String>();
		List<String> links = new ArrayList<String>();

		@Override
		protected List<String> doInBackground(String... urls) {

			String result = NetworkStats.getOutputFromURL(urls[0]);
			System.out.println(result);
			HtmlParser parser;
			
			try {
				parser = new HtmlParser(result);
				List<TagNode> links = parser.getObjectByTagAndClass("p", "MsoNormalCxSpFirst");

				for (Iterator<TagNode> iterator = links.iterator(); iterator.hasNext();) {
					
					TagNode linkElement = (TagNode) iterator.next();
					resultList.add(linkElement.getText().toString());
					
					
					//System.out.println("LINK ! " + linkElement.getAttributeByName("href").toString());
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}

			return resultList;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			super.onPostExecute(result);
		}

	}
	/*
	private class DownloadLinks extends AsyncTask<String, Integer, List<String>> {
		List<String> linkList = new ArrayList<String>();

		@Override
		protected List<String> doInBackground(String... urls) {
			String result = NetworkStats.getOutputFromURL(urls[0]);
			HtmlParser parser;

			try {
				parser = new HtmlParser(result);
				List<TagNode> links = parser.getObjectByTagAndClass("p", "MsoNormalCxSpFirst");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	*/

}

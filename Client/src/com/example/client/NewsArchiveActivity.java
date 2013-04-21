package com.example.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.htmlcleaner.TagNode;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.adapters.TupleAB;
import com.example.client.R.string;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author complexityclass
 * 
 *         Activity class for {@link String AreasOfActivity}
 * 
 */
public class NewsArchiveActivity extends Activity {

	/*
	 * List<String> linksText : link handler List<Integer> pics : image handler
	 */
	public List<String> linksText;
	public List<Integer> pics;

	public List<TupleAB<String, String>> newsList;

	public static final String DOMAIN = "http://pgu.khv.gov.ru/";

	public static final String PAGE_URL = "http://pgu.khv.gov.ru/?a=NewsList&range=All";

	private ListView currentlistView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_layout);

		// linksText = new ArrayList<String>();
		pics = new ArrayList<Integer>();

		newsList = new ArrayList<TupleAB<String, String>>();

		/*
		 * Check the device network connection
		 */
		if (NetworkStats.isNetworkAvailable(this)) {
			/* get html from POST request */
			DownloadHtml downloadHtml = new DownloadHtml();
			downloadHtml.execute(PAGE_URL);
			try {
				newsList = downloadHtml.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		/*
		 * for (Iterator<String> iterator = linksText.iterator();
		 * iterator.hasNext();) {
		 * System.out.println(iterator.next().toString()); }
		 */

		// News[] values = new News[linksText.size()];
		News[] values = new News[newsList.size()];

		for (int i = 0; i < newsList.size(); i++) {

			values[i] = new News(R.drawable.arrow, newsList.get(i).getA());
		}

		/*
		 * for (Iterator<String> iterator = linksText.iterator();
		 * iterator.hasNext();) { String temp = iterator.next().toString();
		 * values[i] = new News(R.drawable.arrow, temp); i++;
		 * 
		 * /* Set NewsAdapter {@link NewsAdapter}
		 */
		NewsAdapter adapter = new NewsAdapter(this, R.layout.list_row, values);
		currentlistView = (ListView) findViewById(R.id.listView1);
		View header = (View) getLayoutInflater().inflate(R.layout.list_header_row_areas, null);
		currentlistView.addHeaderView(header);
		currentlistView.setAdapter(adapter);

		currentlistView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				DownloadFullNews downloadNews = new DownloadFullNews();

				downloadNews.execute(DOMAIN + newsList.get(position).getB());

				String news = "";

				try {
					news = downloadNews.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Toast toast = Toast.makeText(getApplicationContext(), news, Toast.LENGTH_LONG);
				System.out.println(news);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();

			}
		});

	}

	/*
	 * Class for loading html in background thread String url Integer progress
	 * value List<String> links handler
	 */
	private class DownloadHtml extends AsyncTask<String, Integer, List<TupleAB<String, String>>> {

		// List<String> resultList = new ArrayList<String>();

		List<TupleAB<String, String>> resultTuple = new ArrayList<TupleAB<String, String>>();

		/* run in BG thread */
		@Override
		protected List<TupleAB<String, String>> doInBackground(String... urls) {
			try {
				String result = NetworkStats.getOutputFromURL(urls[0]);

				/* html parsing */
				HtmlParser parser;
				try {
					parser = new HtmlParser(result);
					List<TagNode> links = parser.getObjectByTagAndClass("ul", "news-list");
					for (Iterator<TagNode> iterator = links.iterator(); iterator.hasNext();) {
						HtmlParser parser2x = new HtmlParser(iterator.next());

						List<TagNode> links2x = parser2x.getObjectByTagAndClass("a", "link");
						List<TagNode> dates2x = parser2x.getObjectByTagAndClass("p", "news-list__date");
						List<TagNode> preview2x = parser2x.getObjectByTagAndClass("p", "news-list__text");

						for (int i = 0, j = 0; i < Math.min(links2x.size(), dates2x.size()); i++) {

							boolean isPreviewText = i % 2 == 0 ? false : true;

							TupleAB<String, String> tmpTuple = new TupleAB<String, String>(dates2x.get(i).getText()
									.toString()
									+ "\n" + preview2x.get(j).getText().toString() + "...", links2x.get(i).getText()
									.toString());

							resultTuple.add(tmpTuple);

							j++;
							j++;

						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return resultTuple;
		}

		/* in UI thread */
		@Override
		protected void onPostExecute(List<TupleAB<String, String>> resultList) {

		}

	}

	private class DownloadFullNews extends AsyncTask<String, Integer, String> {

		String fullNews = "";

		@Override
		protected String doInBackground(String... urls) {

			String result = NetworkStats.getOutputFromURL(urls[0]);

			HtmlParser parser;

			try {
				parser = new HtmlParser(result);

				List<TagNode> texts = parser.getObjectByTagAndClass("div",
						"tab-pane active marginRight10 tab-pane-pretty-content");
				
				for(Iterator<TagNode> iterator = texts.iterator(); iterator.hasNext();){
					
					fullNews += iterator.next().getText().toString();
					
				}


			} catch (Exception e) {
				e.printStackTrace();
			}

			return fullNews;

		}

	}

}

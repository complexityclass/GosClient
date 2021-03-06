package com.example.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpException;
import org.htmlcleaner.TagNode;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.http.MyHttpClientUsage;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NewsActivity extends Activity {

	private ListView currentlistView;
	public static final String PAGE_URL = "http://pgu.khv.gov.ru/?a=NewsList";
	private Map<Integer, String> news;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_layout);

		news = new HashMap<Integer, String>();

		if (NetworkStats.isNetworkAvailable(this)) {
			DownloadHtml downloadHtml = new DownloadHtml();
			downloadHtml.execute(PAGE_URL);
			try {
				news = downloadHtml.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		for (Map.Entry<Integer, String> entry : news.entrySet()) {
			System.out.println("KEY : " + entry.getKey() + " VALUE :" + entry.getValue());
		}

		News[] values = new News[news.size() / 2];
		int i = 0;

		Iterator<Map.Entry<Integer, String>> itr = news.entrySet().iterator();
		while (itr.hasNext()) {
			String tmp1 = itr.next().toString();
			String tmp2 = itr.next().toString();
			String tmp = tmp1 + "\n" + tmp2;
			System.out.println(tmp);
			values[i] = new News(R.drawable.arrow, tmp);
		}

		NewsAdapter adapter = new NewsAdapter(this, R.layout.list_row, values);

		currentlistView = (ListView) findViewById(R.id.listView1);

		View header = (View) getLayoutInflater().inflate(R.layout.list_header_row_areas, null);
		currentlistView.addHeaderView(header);
		currentlistView.setAdapter(adapter);
	}

	private class DownloadHtml extends AsyncTask<String, Integer, Map<Integer, String>> {

		Map<Integer, String> resultMap = new HashMap<Integer, String>();

		@Override
		protected Map<Integer, String> doInBackground(String... urls) {
			try {
				String result = NetworkStats.getOutputFromURL(urls[0]);

				HtmlParser parser;
				try {
					parser = new HtmlParser(result);

					List<TagNode> data = parser.getNews("news-group__date");
					List<TagNode> title = parser.getNews("news-group__title");

					Iterator<TagNode> iterator2 = title.iterator();
					Integer count = 0;
					for (Iterator<TagNode> iterator1 = data.iterator(); iterator1.hasNext();) {
						TagNode dataElement = (TagNode) iterator1.next();
						TagNode titleElement = (TagNode) iterator2.next();
						TagNode restElement = (TagNode) iterator2.next();

						if (dataElement != null && titleElement != null) {
							resultMap.put(count, dataElement.getText().toString());
							resultMap.put(++count, titleElement.getText().toString());
							count++;
						} else if (titleElement == null) {
							resultMap.put(count, dataElement.getText().toString());
							resultMap.put(count++, "");
							count++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}

			return resultMap;
		}

		@Override
		protected void onPostExecute(Map<Integer, String> resultMap) {
		
		}
	}
}

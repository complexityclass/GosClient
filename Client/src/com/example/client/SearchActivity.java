package com.example.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.htmlcleaner.TagNode;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends Activity {

	private ListView currentlistView;
	//private TextView queryField;

	public List<String> linksText;
	public List<Integer> pics;

	private static final String SEARCH_URL = "http://pgu.khv.gov.ru/?a=Search&query=";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_layout);

		Intent fromMain = getIntent();
	//	queryField = (TextView) findViewById(R.id.textQuery);
		String query = fromMain.getStringExtra("query");
		String html = fromMain.getStringExtra("html");
		//queryField.setText(query);

		ParseHtml parse = new ParseHtml();
		parse.execute(html);
		try {
			linksText = parse.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		for (Iterator<String> iterator = linksText.iterator(); iterator
				.hasNext();) {
			System.out.println(iterator.next().toString());
		}

		News[] values = new News[linksText.size()];
		int i = 0;

		for (Iterator<String> iterator = linksText.iterator(); iterator
				.hasNext();) {
			String temp = iterator.next().toString();
			values[i] = new News(R.drawable.arrow, temp);
			i++;
		}

		NewsAdapter adapter = new NewsAdapter(this, R.layout.list_row, values);

		currentlistView = (ListView) findViewById(R.id.listView1);
		View header = (View) getLayoutInflater().inflate(
				R.layout.list_header_row_areas, null);
		currentlistView.addHeaderView(header);
		currentlistView.setAdapter(adapter);

	}

	private class ParseHtml extends AsyncTask<String, Integer, List<String>> {

		String html;
		List<String> resultList = new ArrayList<String>();
		Map<String,List<String>> resultMap = new HashMap<String, List<String>>();

		@Override
		protected List<String> doInBackground(String... urls) {
			try {
				html = urls[0];
				HtmlParser parser;

				parser = new HtmlParser(html);
				List<TagNode> links = parser.getObjectByTagAndClass("h2",
						"slide-tabs__title");
				for (Iterator<TagNode> iterator = links.iterator(); iterator
						.hasNext();) {
					TagNode linkElement = (TagNode) iterator.next();
					
					/*
					List<TagNode> linkChildrens = linkElement.getChildren();
					
					for(Iterator<TagNode> iterator2 = linkChildrens.iterator(); iterator2.hasNext();){
						String type = 
					}
					*/
					
					resultList.add(linkElement.getText().toString());
				}

			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return resultList;
		}

		@Override
		protected void onPostExecute(List<String> result) {

		}

	}

}

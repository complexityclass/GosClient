package com.example.client;

import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpException;
import org.htmlcleaner.TagNode;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.http.MyHttpClientUsage;
import com.example.parser.HtmlParser;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NewsActivity extends Activity {

	private ListView currentlistView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newslayout);

		News[] news_data = new News[] { new News(R.drawable.banana, "Banana"),
				new News(R.drawable.tree, "Tree") };

		NewsAdapter adapter = new NewsAdapter(this, R.layout.list_row,
				news_data);
		
		currentlistView  = (ListView) findViewById(R.id.listView1);
		
		View header = (View)getLayoutInflater().inflate(R.layout.list_header_row, null);
		currentlistView.addHeaderView(header);
		currentlistView.setAdapter(adapter);
	}

}

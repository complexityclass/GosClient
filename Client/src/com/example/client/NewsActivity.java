package com.example.client;

import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpException;
import org.htmlcleaner.TagNode;

import com.example.http.MyHttpClientUsage;
import com.example.parser.HtmlParser;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

public class NewsActivity extends ListActivity {
/*
	Handler h;
	public static final int MAX_NEWS = 100;
	String[] values;
	int index;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rowlayout);

		values = new String[MAX_NEWS];
		index = 0;

		h = new Handler() {

			public void handleMessage(android.os.Message msg) {
				String result = String.valueOf(msg.getData());
				values[index] = result;
				index++;
			}
			
			
		};
		
		RequestParams params = new RequestParams();
		params.put("a", "Citizens");
		params.put("category", "Regional");

		MyHttpClientUsage connect = new MyHttpClientUsage(h);
		try {
			connect.gethtml(params);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.rowlayout, R.id.label, values);
		setListAdapter(adapter);
	}
	*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsrowlayout);
	}
	
	
	
	
}

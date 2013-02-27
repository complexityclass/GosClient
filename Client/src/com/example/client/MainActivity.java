package com.example.client;

import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpException;
import org.htmlcleaner.TagNode;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.http.MyHttpClientUsage;
import com.example.parser.HtmlParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author complexityclass First activity manage app directions
 * 
 */
public class MainActivity extends Activity {

	private ListView currentlistView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newslayout);

		News[] newsData = new News[] {
				new News(R.drawable.banana, "���������"),
				new News(R.drawable.banana, "� �������"),
				new News(R.drawable.banana, "���������"),
				new News(R.drawable.banana, "������������"),
				new News(R.drawable.banana, "����������� ������"),
				new News(R.drawable.banana, "��������� ��������"),
				new News(R.drawable.banana, "����������� ������������"),
				new News(R.drawable.banana, "����������"),
				new News(R.drawable.banana, "���������� ���������") };

		NewsAdapter adapter = new NewsAdapter(this, R.layout.list_row, newsData);

		currentlistView = (ListView) findViewById(R.id.listView1);

		View header = (View) getLayoutInflater().inflate(
				R.layout.list_header_row_main, null);
		currentlistView.addHeaderView(header);
		currentlistView.setAdapter(adapter);

		currentlistView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				String pen = parent.getItemAtPosition(position).toString();

				if ("� �������".equals(pen)) {
					Intent myIntent = new Intent(v.getContext(),
							AboutUsActivity.class);
					startActivity(myIntent);

				}
				if ("�������".equals(pen)) {
					Intent newsIntent = new Intent(v.getContext(),
							NewsActivity.class);
					startActivity(newsIntent);
				}
				if ("���������".equals(pen)) {
					Intent citizensRegIntent = new Intent(v.getContext(),
							CitizenRegActivity.class);
					startActivity(citizensRegIntent);
				}
				if ("���������".equals(pen)) {
					Intent agenciesIntent = new Intent(v.getContext(),
							AgenciesActivity.class);
					startActivity(agenciesIntent);
				}

			}
		});

	}
}

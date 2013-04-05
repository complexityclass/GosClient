package com.example.client;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.htmlcleaner.TagNode;

import com.example.adapters.CustomExpandableListAdapter;
import com.example.adapters.ExpandableListAdapter;
import com.example.adapters.ExpandableListChildsParentsAdapter;
import com.example.adapters.TupleAB;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ExpandableListView;

public class ServiceDataActivity extends Activity {

	String url = "http://pgu.khv.gov.ru/?a=Organizations&category=Regional&catalog=790&cat1=792&sid=2700000010000233273";

	ArrayList<String> listGroups;
	ArrayList<ArrayList<String>> listChilds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exp_list_view_layout);

		Intent urlIntent = getIntent();
		url = urlIntent.getStringExtra("url");

		DownloadListChilds downloadListChilds = new DownloadListChilds();
		downloadListChilds.execute(url);

		try {
			listChilds = downloadListChilds.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DownLoadListParents downloadlistParents = new DownLoadListParents();
		downloadlistParents.execute(url);

		try {
			listGroups = downloadlistParents.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Iterator<String> iterator = listGroups.iterator(); iterator.hasNext();) {
			System.out.println(iterator.next().toString() + "!!!!");
		}

		ExpandableListView listView = (ExpandableListView) findViewById(R.id.exListView);
		ExpandableListChildsParentsAdapter adapter = new ExpandableListChildsParentsAdapter(getApplicationContext(),
				listChilds, listGroups);
		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expandable_list, menu);
		return true;
	}

	/** Send request in separate thread using AsyncTask */

	private class DownloadListChilds extends AsyncTask<String, Integer, ArrayList<ArrayList<String>>> {

		ArrayList<ArrayList<String>> childList;

		@Override
		protected ArrayList<ArrayList<String>> doInBackground(String... urls) {

			childList = new ArrayList<ArrayList<String>>();

			try {
				String result = NetworkStats.getOutputFromURL(urls[0]);

				HtmlParser parser;

				try {
					parser = new HtmlParser(result);

					List<TagNode> lister = parser.getObjectByTagAndClass("div", "slide-tabs__item");
					for (Iterator<TagNode> iterator = lister.iterator(); iterator.hasNext();) {

						TagNode[] usClass = iterator.next().getElementsByName("div", false);
						ArrayList<String> group = new ArrayList<String>();
						for (int i = 0; i < usClass.length && usClass != null; i++) {

							group.add(usClass[i].getText().toString());
						}
						childList.add(group);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}

			return childList;
		}

		@Override
		protected void onPostExecute(ArrayList<ArrayList<String>> lister) {

		}

	}

	private class DownLoadListParents extends AsyncTask<String, Integer, ArrayList<String>> {
		ArrayList<String> parentList;

		@Override
		protected ArrayList<String> doInBackground(String... urls) {

			parentList = new ArrayList<String>();

			String result = NetworkStats.getOutputFromURL(urls[0]);
			HtmlParser parser2;

			try {
				parser2 = new HtmlParser(result);

				List<TagNode> lister = parser2.getObjectByTagAndClass("h2", "slide-tabs__title");
				for (Iterator<TagNode> iterator = lister.iterator(); iterator.hasNext();) {
					TagNode noder = (TagNode) iterator.next();
					parentList.add(noder.getText().toString());
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return parentList;
		}

	}

}
package com.example.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.htmlcleaner.TagNode;

import com.example.adapters.CustomExpandableListAdapter;
import com.example.adapters.ExpandableListAdapter;
import com.example.adapters.TupleAB;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ExpandableListView;

public class ExpandableListActivity extends Activity {

	String url = "http://pgu.khv.gov.ru/?a=Citizens&category=Regional&catalog=770";
	ArrayList<ArrayList<String>> listChilds;
	ArrayList<String> listGroups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exp_list_view_layout);

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

		for (Iterator<ArrayList<String>> iter = listChilds.iterator(); iter.hasNext();) {
			for (Iterator<String> iterIn = iter.next().iterator(); iterIn.hasNext();) {
				System.out.println(iterIn.next().toString());
			}
		}

		ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();
		ArrayList<String> children1 = new ArrayList<String>();
		ArrayList<String> children2 = new ArrayList<String>();

		children1.add("Child_1");
		children1.add("Child_2");
		groups.add(children1);
		children2.add("Child_1");
		children2.add("Child_2");
		children2.add("Child_3");
		groups.add(children2);

		ExpandableListView listView = (ExpandableListView) findViewById(R.id.exListView);
		ExpandableListAdapter adapter = new ExpandableListAdapter(getApplicationContext(), listChilds);
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

					List<TagNode> lister = parser.getObjectByTagAndClass("ul", "list");
					for (Iterator<TagNode> iterator = lister.iterator(); iterator.hasNext();) {

						TagNode[] usClass = iterator.next().getElementsByName("a", true);
						ArrayList<String> group = new ArrayList<String>();
						for (int i = 0; i < usClass.length && usClass != null; i++) {

							TagNode element = usClass[i];
							group.add(element.getText().toString());

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

}
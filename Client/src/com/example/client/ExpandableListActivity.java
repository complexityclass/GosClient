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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

public class ExpandableListActivity extends Activity {

	public static final String DOMAIN = "http://pgu.khv.gov.ru/";

	public static String parentName = "com.example.client.MainActivity";

	String url = "http://pgu.khv.gov.ru/?a=Citizens&category=Regional&catalog=770";
	ArrayList<ArrayList<String>> listChilds;
	ArrayList<String> listGroups;

	ArrayList<ArrayList<String>> listChild2;

	ArrayList<ArrayList<TupleAB<String, String>>> tupleListList = new ArrayList<ArrayList<TupleAB<String, String>>>();

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
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		DownloadListTupleChilds downloadListTupleChilds = new DownloadListTupleChilds();
		downloadListTupleChilds.execute(url);

		try {
			tupleListList = downloadListTupleChilds.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		listChild2 = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < tupleListList.size(); i++) {

			ArrayList<String> tempList = new ArrayList<String>();

			for (int j = 0; j < tupleListList.get(i).size(); j++) {
				// System.out.println("text :" +
				// tupleListList.get(i).get(j).getA() + "href :" +
				// tupleListList.get(i).get(j).getB());
				tempList.add(tupleListList.get(i).get(j).getA().toString());
				// System.out.println("This is RIGHT SYSO" + tempList.get(j));
			}
			listChild2.add(tempList);
		}

		ExpandableListView listView = (ExpandableListView) findViewById(R.id.exListView);
		// ExpandableListAdapter adapter = new
		// ExpandableListAdapter(getApplicationContext(), listChilds);
		ExpandableListChildsParentsAdapter adapter = new ExpandableListChildsParentsAdapter(getApplicationContext(),
				listChilds, listGroups);
		listView.setAdapter(adapter);

		listView.setOnChildClickListener(new OnChildClickListener() {

			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

				Intent intent = new Intent(v.getContext(), ServiceDataActivity.class);
				intent.putExtra("url", DOMAIN + tupleListList.get(groupPosition).get(childPosition).getB());
				startActivity(intent);

				return true;

			}
		});

		String invokeActivity = getCallingActivity() != null ? getCallingActivity().getClassName() : "ImagineActivity";

		if (parentName.equals(invokeActivity)) {

			Toast toast = Toast.makeText(getApplicationContext(), "Найдено " + listGroups.size() + " совпадений",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		}

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

					List<TagNode> lister = parser.getObjectByTagAndClass("div",
							"slide-tabs__text slide-tabs__text_state_opened");
					for (Iterator<TagNode> iterator = lister.iterator(); iterator.hasNext();) {

						TagNode[] usClass = iterator.next().getElementsByName("li", true);
						ArrayList<String> group = new ArrayList<String>();
						for (int i = 0; i < usClass.length && usClass != null; i++) {

							// System.out.println(usClass[i].getText().toString());
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

	private class DownloadListTupleChilds extends
			AsyncTask<String, Integer, ArrayList<ArrayList<TupleAB<String, String>>>> {
		ArrayList<ArrayList<TupleAB<String, String>>> tupleListList;

		@Override
		protected ArrayList<ArrayList<TupleAB<String, String>>> doInBackground(String... urls) {

			tupleListList = new ArrayList<ArrayList<TupleAB<String, String>>>();

			try {
				String result = NetworkStats.getOutputFromURL(urls[0]);

				HtmlParser parser;

				try {
					parser = new HtmlParser(result);

					List<TagNode> lister = parser.getObjectByTagAndClass("ul", "list");
					for (Iterator<TagNode> iterator = lister.iterator(); iterator.hasNext();) {

						TagNode[] usClass = iterator.next().getElementsByName("a", true);

						ArrayList<TupleAB<String, String>> group = new ArrayList<TupleAB<String, String>>();

						for (int i = 0; i < usClass.length && usClass != null; i++) {

							// System.out.println(usClass[i].getAttributeByName("href").toString());
							TagNode element = usClass[i];

							String s1 = element.getText().toString();
							String s2 = usClass[i].getAttributeByName("href").toString();

							TupleAB<String, String> tuple = new TupleAB<String, String>(s1, s2);

							group.add(tuple);

						}
						tupleListList.add(group);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}

			return tupleListList;

		}

	}

}

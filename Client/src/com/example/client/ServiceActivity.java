package com.example.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.htmlcleaner.TagNode;

import com.example.adapters.ExpandableListChildsParentsAdapter;
import com.example.adapters.TupleAB;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SlidingDrawer;

/**
 * @author complexityclass
 * 
 */
public class ServiceActivity extends Activity {

	public static final String DOMAIN = "http://pgu.khv.gov.ru/";
	String url = "http://pgu.khv.gov.ru/?a=Citizens&category=Regional&catalog=770";
	ArrayList<ArrayList<String>> listChilds;
	ArrayList<String> listGroups;
	ArrayList<ArrayList<TupleAB<String, String>>> tupleListList = new ArrayList<ArrayList<TupleAB<String, String>>>();

	Button getServicesButton;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service);

		Intent urlIntent = getIntent();
		url = urlIntent.getStringExtra("url");

		context = ServiceActivity.this;

		DownloadListChilds downloadListChilds = new DownloadListChilds();
		downloadListChilds.execute(url);

		try {
			listChilds = downloadListChilds.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
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

		ExpandableListView listView = (ExpandableListView) findViewById(R.id.exListView);
		ExpandableListChildsParentsAdapter adapter = new ExpandableListChildsParentsAdapter(getApplicationContext(),
				listChilds, listGroups);

		listView.setAdapter(adapter);

		getServicesButton = (Button) findViewById(R.id.buttonGetSevice);

		getServicesButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent newIntent = new Intent(context,WebViewActivity.class);
				newIntent.putExtra("url",url);
				startActivity(newIntent);

			}
		});

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
							"slide-tabs__item");
					for (Iterator<TagNode> iterator = lister.iterator(); iterator.hasNext();) {

						TagNode[] usClass = iterator.next().getElementsByName("div", true);
						ArrayList<String> group = new ArrayList<String>();
						for (int i = 0; i < usClass.length && usClass != null; i++) {

							System.out.println(usClass[i].getText().toString());
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

	}

}

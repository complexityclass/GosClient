package com.example.client;

import java.util.ArrayList;
import java.util.Map;

import com.example.adapters.ExpandableListAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ExpandableListView;

public class ExpandableListViewTest extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expandable_list_view_test);

		ExpandableListView listView = (ExpandableListView) findViewById(R.id.exListView);

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

		ExpandableListAdapter adapter = new ExpandableListAdapter(getApplicationContext(), groups);
		listView.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expandable_list_view_test, menu);
		return true;
	}

}

package com.example.client;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CitizensRegionalActivity extends ListActivity {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] values = new String[] { "О", "Нов"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.rowlayout, R.id.label,values);
		setListAdapter(adapter);
	}
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
      String item = (String) getListAdapter().getItem(position);
      ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
      
      String pen = adapter.getItem(position);
      if("О проекте".equals(pen)){
    	  Intent myIntent = new Intent(v.getContext(),AboutUsActivity.class);
    	  startActivity(myIntent);
      }
      if("Новости".equals(pen)){
    	  Intent newsIntent = new Intent(v.getContext(),NewsActivity.class);
    	  startActivity(newsIntent);
      }
      
    }

	
	
	

}

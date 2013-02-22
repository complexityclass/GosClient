package com.example.client;

import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpException;
import org.htmlcleaner.TagNode;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] values = new String[] { "О проекте", "Новости","Гражданам","Ведомства"};
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
      if("Гражданам".equals(pen)){
    	  Intent citizensRegIntent = new Intent(v.getContext(),CitizenRegActivity.class);
    	  startActivity(citizensRegIntent);
      }
      if("Ведомства".equals(pen)){
    	  Intent agenciesIntent = new Intent(v.getContext(),AgenciesActivity.class);
    	  startActivity(agenciesIntent);
      }
      
    }

}

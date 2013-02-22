package com.example.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CitizenRegActivity extends Activity {
	
	// Array of strings storing country names
    String[] countries = new String[] {
        "Banana",
        "Tea",
        "Banana2",
        "Tea2",
        "Banana",
        "Tea",
        "Banana2",
        "Tea2"
        , "Banana",
        "Tea",
        "Banana2",
        "Tea2",
        "Banana",
        "Tea",
        "Banana2",
        "Tea2"
    };
    
    int flags[] = new int[]{
    		R.drawable.banana,
    		R.drawable.tree,
    		R.drawable.banana,
    		R.drawable.tree,
    		R.drawable.banana,
    		R.drawable.tree,
    		R.drawable.banana,
    		R.drawable.tree,
    		R.drawable.banana,
    		R.drawable.tree,
    		R.drawable.banana,
    		R.drawable.tree,
    		R.drawable.banana,
    		R.drawable.tree,
    		R.drawable.banana,
    		R.drawable.tree
    };
    
    String[] currency = new String[]{
    		"Banana",
    		"Tree",
    		"Banana2",
    		"Tree2",
    		"Banana",
    		"Tree",
    		"Banana2",
    		"Tree2",
    		"Banana",
    		"Tree",
    		"Banana2",
    		"Tree2",
    		"Banana",
    		"Tree",
    		"Banana2",
    		"Tree2",
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 
        // Each row in the list stores country name, currency and flag
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
 
        for(int i=0;i<16;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("txt", "Country : " + countries[i]);
            hm.put("cur","Currency : " + currency[i]);
            hm.put("flag", Integer.toString(flags[i]) );
            aList.add(hm);
        }
 
        // Keys used in Hashmap
        String[] from = { "flag","txt","cur" };
 
        // Ids of views in listview_layout
        int[] to = { R.id.flag,R.id.txt,R.id.cur};
 
        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);
 
        // Getting a reference to listview of main.xml layout file
        ListView listView = ( ListView ) findViewById(R.id.listview);
 
        // Setting the adapter to the listView
        listView.setAdapter(adapter);
    }
    
    
	

}

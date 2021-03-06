package com.example.client;

import java.util.Iterator;
import java.util.List;

import org.htmlcleaner.TagNode;

import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.TextView;

public class SpotsTreatmentActivity extends Activity {
	
	private TextView largeText;
	private static final String PAGE_URL = "http://pgu.khv.gov.ru/?a=Static&content=49";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        largeText = (TextView) findViewById(R.id.textView1);
        
        /*Check network connection*/
		if (NetworkStats.isNetworkAvailable(this)) {

			DownloadHtml downloadHtml = new DownloadHtml();
			downloadHtml.execute(PAGE_URL);
			
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.activity_test, menu);
    	return true;
    }
    
    /**Send request in separate thread using AsyncTask*/
    private class DownloadHtml extends AsyncTask<String, Integer, String> {
    	String result = "";
    	
    	@Override
    	protected String doInBackground(String... urls) {
    		try {
    			result = NetworkStats.getOutputFromURL(urls[0]);
    		} catch (Exception e) {
    			Log.d("Background Task", e.toString());
    		}
    		return result;
    	}
    	
    	@Override
    	protected void onPostExecute(String result) {
    		HtmlParser parser;
    		StringBuilder resultBuilder = new StringBuilder();
    		try {
    			//parsing html to get all <div> tag source
    			parser = new HtmlParser(result);
    			List<TagNode> links = parser.getContentByClassName("ab");
    			for (Iterator<TagNode> iterator = links.iterator(); iterator
    					.hasNext();) {
    				TagNode divElement = (TagNode) iterator.next();
    				resultBuilder.append(divElement.getText().toString());
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    		String plainText = resultBuilder.toString();
    		
    		//Set TextView params
    		largeText.setText(plainText);
    		largeText.setGravity(Gravity.LEFT);
    		largeText.setPadding(3, 3, 3, 3);
    		largeText.setTextSize(10);
    		
    	}
    }
}
        
        
        
        

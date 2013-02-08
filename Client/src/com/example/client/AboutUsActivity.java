package com.example.client;

import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpException;
import org.htmlcleaner.TagNode;

import com.example.http.MyHttpClientUsage;
import com.example.parser.HtmlParser;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class AboutUsActivity extends Activity {
	
	Handler h;
	TextView largeText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        largeText = (TextView) findViewById(R.id.textView1);
        
        h = new Handler(){
        	
        	public void handleMessage(android.os.Message msg){
        		HtmlParser parser;
        		StringBuilder result = new StringBuilder();
				try {
					parser = new HtmlParser(String.valueOf(msg.getData()));
					List<TagNode> links = parser.getContentByClassName("ab");
					for(Iterator<TagNode> iterator = links.iterator(); iterator.hasNext();){
						
						TagNode divElement = (TagNode) iterator.next();
						result.append(divElement.getText().toString());
					}
						
					
				} catch (Exception e) {
					e.printStackTrace();
				}
        		
        		
        		largeText.setText(result.toString());
        	}
        	
        };
        
        MyHttpClientUsage connect = new MyHttpClientUsage(h);
        try {
			connect.getInfoAbout();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  
	
	

}

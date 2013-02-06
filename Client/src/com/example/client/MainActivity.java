package com.example.client;

import org.apache.http.HttpException;

import com.example.http.MyHttpClientUsage;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Handler h;
	TextView largeText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        largeText = (TextView) findViewById(R.id.textView1);
        
        h = new Handler(){
        	
        	public void handleMessage(android.os.Message msg){
        		largeText.setText(String.valueOf(msg.getData()));
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

package com.example.http;

import org.apache.http.HttpException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyHttpClientUsage {
	
	//public String response;
	public int tester;
	Handler h;
	
	public MyHttpClientUsage(Handler h){
		this.h = h;
	}
	
	public void getInfoAbout() throws HttpException{
		
		RequestParams params = new RequestParams();
		params.put("a", "Static");
		params.put("content", "47");
	
		MyHttpClient.get("", params, new AsyncHttpResponseHandler(){
			 @Override
			    public void onSuccess(String response) {
			          System.out.println(response);
			          Bundle b = new Bundle();
			          b.putString("html", response);
			          Message msg = new Message();
			          msg.setData(b);
			          MyHttpClientUsage.this.h.sendMessage(msg);   
			    }
		});
	}
	
	public void agencies() throws HttpException{
		
		RequestParams params = new RequestParams();
		params.put("a","Departments");
		params.put("category", "Regional");
		
		MyHttpClient.get("", params, new AsyncHttpResponseHandler(){
			 @Override
			    public void onSuccess(String response) {
			          System.out.println(response);
			          Bundle b = new Bundle();
			          b.putString("html", response);
			          Message msg = new Message();
			          msg.setData(b);
			          MyHttpClientUsage.this.h.sendMessage(msg);   
			    }
		});
	}
	
	
	
	public void gethtml(RequestParams parametrs) throws HttpException{
		
		MyHttpClient.get("", parametrs, new AsyncHttpResponseHandler(){
			 @Override
			    public void onSuccess(String response) {
			          System.out.println(response);
			          Bundle b = new Bundle();
			          b.putString("html", response);
			          Message msg = new Message();
			          msg.setData(b);
			          MyHttpClientUsage.this.h.sendMessage(msg);   
			    }
		});
	
	}
		
		
		
	
}
				 	
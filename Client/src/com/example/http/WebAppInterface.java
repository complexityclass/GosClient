package com.example.http;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
	
	Context mContext;
	
	public WebAppInterface(Context context) {
		mContext = context;
	}
	
	@JavascriptInterface
	public void showToast(String toast){
		Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
	}

}
	
package com.example.client;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpException;
import org.htmlcleaner.TagNode;

import com.example.http.MyHttpClientUsage;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity ("� �������")
 * @author complexityclass
 *
 */
public class AboutUsActivity extends Activity {
	
	/**View for big info text*/
	private TextView largeText;
	/**URL for GET request */
	private static final String PAGE_URL = "http://pgu.khv.gov.ru/?a=Static&content=47";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		largeText = (TextView) findViewById(R.id.textView1);

		if (NetworkStats.isNetworkAvailable(this)) {

			DownloadHtml downloadHtml = new DownloadHtml();
			downloadHtml.execute(PAGE_URL);
			
		}
		
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

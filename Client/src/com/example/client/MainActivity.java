package com.example.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpException;
import org.htmlcleaner.TagNode;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.http.MyHttpClientUsage;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author complexityclass First activity manage app directions First screen of
 *         the App. Show Main menu and service search bar.
 * 
 */
public class MainActivity extends Activity {

	private ListView currentlistView;
	private ImageButton searchButton;
	private EditText queryText;

	private ProgressDialog progressDialog;

	private static final String SEARCH_URL = "http://pgu.khv.gov.ru/?a=Search&query=";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_searchbar_layout);

		// MainActivity has static content
		News[] newsData = new News[] {
				new News(R.drawable.arrow, getString(R.string.agencies_ru)),
				// new News(R.drawable.arrow,
				// getString(R.string.about_project_ru)),
				new News(R.drawable.arrow, getString(R.string.areas_of_ru)),
				new News(R.drawable.arrow, getString(R.string.citizens_ru)),
				new News(R.drawable.arrow, getString(R.string.news_ru)),
				new News(R.drawable.arrow, getString(R.string.organizations_ru)),
				new News(R.drawable.arrow, getString(R.string.electronic_services_ru)),
				new News(R.drawable.arrow, getString(R.string.life_situations_ru)),
				new News(R.drawable.arrow, getString(R.string.information_ru)),
				new News(R.drawable.arrow, getString(R.string.visitor_statistics_ru)),
				new News(R.drawable.arrow, getString(R.string.search_news_ru)),
				new News(R.drawable.arrow, "ExpandableListView") };

		// set custom adapter
		NewsAdapter adapter = new NewsAdapter(this, R.layout.list_row, newsData);

		currentlistView = (ListView) findViewById(R.id.listView1);
		View header = (View) getLayoutInflater().inflate(R.layout.list_header_row_main, null);
		currentlistView.addHeaderView(header);
		currentlistView.setAdapter(adapter);

		/**
		 * Click method on Item. Each item is instance of News.class
		 */
		currentlistView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				String pen;

				final String about_project = getString(R.string.about_project_ru);
				final String news = getString(R.string.news_ru);
				final String citizens = getString(R.string.citizens_ru);
				final String agencies = getString(R.string.agencies_ru);
				final String areas_of = getString(R.string.areas_of_ru);
				final String searchByNews = getString(R.string.search_news_ru);
				final String information = getString(R.string.information_ru);
				final String lifeSituations = getString(R.string.life_situations_ru);
				final String expListView = "ExpandableListView";

				// Open new activities by click
				// Change to CASE !!!
				try {
					pen = parent.getItemAtPosition(position).toString();
				} catch (NullPointerException e) {
					pen = "mainMenu";
				}
				if (about_project.equals(pen)) {
					Intent myIntent = new Intent(v.getContext(), AboutUsActivity.class);

					// progressDialog = new ProgressDialog(this);
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

					startActivity(myIntent);

				}
				if (news.equals(pen)) {
					Intent newsIntent = new Intent(v.getContext(), NewsActivity.class);
					startActivity(newsIntent);
				}
				if (citizens.equals(pen)) {
					Intent citizensRegIntent = new Intent(v.getContext(), CitizenRegActivity.class);
					startActivity(citizensRegIntent);
				}
				if (agencies.equals(pen)) {
					Intent agenciesIntent = new Intent(v.getContext(), AgenciesActivity.class);
					startActivity(agenciesIntent);
				}

				if (areas_of.equals(pen)) {
					Intent areasIntent = new Intent(v.getContext(), AreasOfActivity.class);
					startActivity(areasIntent);
				}

				if (searchByNews.equals(pen)) {
					Intent searchIntent = new Intent(v.getContext(), SearchActivity.class);
					startActivity(searchIntent);
				}

				if (information.equals(pen)) {
					Intent informationIntent = new Intent(v.getContext(), InformationActivity.class);
					startActivity(informationIntent);
				}

				if (lifeSituations.equals(pen)) {
					Intent lifeSitIntent = new Intent(v.getContext(), LifeSituationsActivity.class);
					startActivity(lifeSitIntent);
				}

				if (expListView.equals(pen)) {
					Intent expListViewIntent = new Intent(v.getContext(), ExpandableListActivity.class);
					startActivity(expListViewIntent);
				}

			}
		});

		searchButton = (ImageButton) findViewById(R.id.ImageButton01);
		queryText = (EditText) findViewById(R.id.editText1);

		searchButton.setOnClickListener(new OnClickListener() {
			/** Check network connection ang GET html */
			public void onClick(View v) {

				String html = "obtaining";
				queryText.setText("������");

				try {
					String encodeQuery = URLEncoder.encode(queryText.getText().toString(), "UTF-8");

					if (NetworkStats.isNetworkAvailable(MainActivity.this)) {
						DownloadHtml downloadHtml = new DownloadHtml(MainActivity.this);
						downloadHtml.execute(SEARCH_URL + encodeQuery);
						try {
							html = downloadHtml.get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}

						/** Open Search activity {SearchActivity.java} */
						Intent searchIntent = new Intent(v.getContext(), SearchActivity.class);
						searchIntent.putExtra("html", html);
						searchIntent.putExtra("query", queryText.getText().toString());
						startActivity(searchIntent);

					}

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				System.out.println(html);

			}
		});
	}

	public void turnOnProgressDialog(ProgressDialog progressDialog) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
	}

	private class DownloadHtml extends AsyncTask<String, Integer, String> {

		String result;
		Activity parent;

		DownloadHtml(Activity parent) {
			this.parent = parent;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("Loading..."); // //!!!!! add to
														// strings.xml
			progressDialog.setProgress(0);
		}

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
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			progressDialog.setProgress(50);
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.setProgress(100);
			progressDialog.dismiss();
		}

	}
}

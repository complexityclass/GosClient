package com.example.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.spec.EncodedKeySpec;
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
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author complexityclass First activity manage app directions First screen of
 *         the App. Show Main menu and service search bar.
 * 
 */
public class MainActivity extends Activity {

	private ListView currentlistView;
	private ImageButton searchButton;
	private ImageButton loginButton;
	private EditText queryText;

	ProgressDialog dialog;

	// private static final String SEARCH_URL =
	// "http://pgu.khv.gov.ru/?a=Search&query=";

	private static final String SEARCH_URL = "http://pgu.khv.gov.ru/?a=Search&type=Services&query=";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_searchbar_layout);
	

		// MainActivity has static content
		News[] newsData = new News[] {
				new News(R.drawable.arrow, getString(R.string.citizens_ru)),
				new News(R.drawable.arrow, getString(R.string.organizations_ru)),
				new News(R.drawable.arrow, getString(R.string.electronic_services_ru)),
				new News(R.drawable.arrow, getString(R.string.life_situations_ru)),
				new News(R.drawable.arrow, getString(R.string.agencies_ru)),
				new News(R.drawable.arrow, getString(R.string.areas_of_ru)),
				// new News(R.drawable.arrow, getString(R.string.news_ru)),
				// new News(R.drawable.arrow,
				// getString(R.string.life_situations_ru)),
				new News(R.drawable.arrow, getString(R.string.information_ru)),
				new News(R.drawable.arrow, getString(R.string.news_archive_ru)),
		// new News(R.drawable.arrow, getString(R.string.search_news_ru)),
		// new News(R.drawable.arrow, "ExpandableListView")
		};

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
				final String organizations = getString(R.string.organizations_ru);
				final String electronicServices = getString(R.string.electronic_services_ru);
				final String visitorStat = getString(R.string.news_archive_ru);

				// Open new activities by click
				// Change to CASE !!!
				try {
					pen = parent.getItemAtPosition(position).toString();
				} catch (NullPointerException e) {
					pen = "mainMenu";
				}
				if (about_project.equals(pen)) {
					Intent myIntent = new Intent(v.getContext(), AboutUsActivity.class);

					startActivity(myIntent);

				} else if (news.equals(pen)) {
					Intent newsIntent = new Intent(v.getContext(), NewsActivity.class);
					startActivity(newsIntent);

				} else if (citizens.equals(pen)) {
					Intent citizensRegIntent = new Intent(v.getContext(), CitizenRegActivity.class);
					startActivity(citizensRegIntent);

				} else if (agencies.equals(pen)) {
					Intent agenciesIntent = new Intent(v.getContext(), AgenciesActivity.class);
					startActivity(agenciesIntent);

				} else if (areas_of.equals(pen)) {
					Intent areasIntent = new Intent(v.getContext(), AreasOfActivity.class);
					startActivity(areasIntent);

				} else if (searchByNews.equals(pen)) {
					Intent searchIntent = new Intent(v.getContext(), SearchActivity.class);
					startActivity(searchIntent);

				} else if (information.equals(pen)) {
					Intent informationIntent = new Intent(v.getContext(), InformationActivity.class);
					startActivity(informationIntent);

				} else if (lifeSituations.equals(pen)) {
					Intent lifeSitIntent = new Intent(v.getContext(), LifeSituationsActivity.class);
					startActivity(lifeSitIntent);

				} else if (expListView.equals(pen)) {
					Intent expListViewIntent = new Intent(v.getContext(), ServiceDataActivity.class);
					startActivity(expListViewIntent);

				} else if (organizations.equals(pen)) {
					Intent organizationsIntent = new Intent(v.getContext(), OrganizationsActivity.class);
					startActivity(organizationsIntent);

				} else if (electronicServices.equals(pen)) {

					Intent electronicIntent = new Intent(v.getContext(), ElectronicServicesActivity.class);
					startActivity(electronicIntent);
				}else if(visitorStat.equals(pen)){
					
					Intent archiveNews = new Intent(v.getContext(),NewsArchiveActivity.class);
					startActivity(archiveNews);
					
				}

			}
			
			
		});

		searchButton = (ImageButton) findViewById(R.id.ImageButton01);
		queryText = (EditText) findViewById(R.id.editText1);
		
	
		queryText.setText("����� �����");

		queryText.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				queryText.setText("");
				queryText.setCursorVisible(true);

			}
		});

		searchButton.setOnClickListener(new OnClickListener() {
			/** Check network connection ang GET html */
			public void onClick(View v) {

				String html = "obtaining";
				//queryText.setText("������");

				String encodeQuery1 = new String(queryText.getText().toString());
				String encodeQuery = "";
				try {
					encodeQuery = URLEncoder.encode(queryText.getText().toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	System.out.println(SEARCH_URL + encodeQuery);

				Intent newIntent = new Intent(v.getContext(), ExpandableListActivity.class);
				newIntent.putExtra("url", SEARCH_URL + encodeQuery);
				startActivityForResult(newIntent, 43);

			}
		});
		
		loginButton = (ImageButton) findViewById(R.id.imageButton1);
		loginButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//Intent esiaIntent = new Intent(v.getContext(), EsiaLoginActivity.class);
				//startActivity(esiaIntent);
				
				/*Intent rostelekomLogin = new Intent(v.getContext(), RostelecomLoginActivity.class);
				rostelekomLogin.putExtra("url", "http://pgu.khv.gov.ru/");
				startActivityForResult(rostelekomLogin, 43);*/
				
				Intent cryptoIntent = new Intent(v.getContext(),PrivateOfficeActivity.class);
				startActivity(cryptoIntent);
				
				
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
		}

		@Override
		protected void onPostExecute(String result) {

		}

	}
}

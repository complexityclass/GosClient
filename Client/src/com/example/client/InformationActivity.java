package com.example.client;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.client.R.string;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author complexityc Class for information about Portal Handle links for:
 *         AboutUsActivity
 * 
 * 
 */
public class InformationActivity extends Activity {

	private ListView currentlistView;
	private static final String UN_CARD_URL = "http://services.khv.ru/uek.aspx";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_layout);

		// InformationActivity has static context
		News[] newsData = new News[] {
				new News(R.drawable.arrow, getString(R.string.about_project_ru)),
				new News(R.drawable.arrow,
						getString(R.string.spots_treatment_ru)),
				new News(R.drawable.arrow, getString(R.string.useful_links_ru)),
				new News(R.drawable.arrow,
						getString(R.string.universal_card_ru)),
				new News(R.drawable.arrow, getString(R.string.faq_ru)),
				new News(R.drawable.arrow,
						getString(R.string.visitor_statistics_ru)) };

		// set custom adapter
		NewsAdapter adapter = new NewsAdapter(this, R.layout.list_row, newsData);
		currentlistView = (ListView) findViewById(R.id.listView1);
		View header = (View) getLayoutInflater().inflate(
				R.layout.list_header_row_information, null);
		currentlistView.addHeaderView(header);
		currentlistView.setAdapter(adapter);

		currentlistView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				String pen;

				final String about_project = getString(R.string.about_project_ru);
				final String spots_treatment = getString(R.string.spots_treatment_ru);
				final String useful_links = getString(R.string.useful_links_ru);
				final String universal_card = getString(string.universal_card_ru);
				final String faq = getString(R.string.faq_ru);
				final String visitor_statistic = getString(R.string.visitor_statistics_ru);

				try {
					pen = parent.getItemAtPosition(position).toString();
				} catch (NullPointerException e) {
					pen = "mainMenu";
				}

				if (about_project.equals(pen)) {
					Intent aboutIntent = new Intent(v.getContext(),
							AboutUsActivity.class);
					startActivity(aboutIntent);

				} else if (spots_treatment.equals(pen)) {
					Intent spotsIntent = new Intent(v.getContext(),
							SpotsTreatmentActivity.class);
					startActivity(spotsIntent);
				} else if (useful_links.equals(pen)) {
					Intent usefulLinksIntent = new Intent(v.getContext(),
							UsefulLinksActivity.class);
					startActivity(usefulLinksIntent);
				} else if (universal_card.equals(pen)) {
					Intent unCardIntent = new Intent(v.getContext(),
							WebViewActivity.class);
					unCardIntent.putExtra("url", UN_CARD_URL);
					startActivity(unCardIntent);
				} else if (faq.equals(pen)) {
					Intent swiper = new Intent(v.getContext(),
							SwipeActivity.class);
					startActivity(swiper);				}

			}
		});
	}

}

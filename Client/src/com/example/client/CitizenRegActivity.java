package com.example.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.htmlcleaner.TagNode;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.http.NetworkStats;
import com.example.parser.HtmlParser;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabWidget;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CitizenRegActivity extends FragmentActivity implements
		ActionBar.TabListener {

	CollectionPagerAdapter mCollectionPagerAdapter;
	ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tain);

		mCollectionPagerAdapter = new CollectionPagerAdapter(
				getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mCollectionPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mCollectionPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mCollectionPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

	}

	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public class CollectionPagerAdapter extends FragmentPagerAdapter {

		final int NUM_ITEMS = 3; // number of tabs

		public CollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new TabFragment();
			Bundle args = new Bundle();
			args.putInt(TabFragment.ARG_OBJECT, i);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String tabLabel = null;
			switch (position) {
			case 0:
				tabLabel = getString(R.string.federational);
				break;
			case 1:
				tabLabel = getString(R.string.regional);
				break;
			case 2:
				tabLabel = getString(R.string.munitipal);
				break;
			}

			return tabLabel;
		}

	}

	public static class TabFragment extends Fragment {

		public static final String ARG_OBJECT = "object";
		public static final String REGIONAL_URL = "http://pgu.khv.gov.ru/?a=Citizens&category=Regional";
		public static final String MUNICIPAL_URL = "http://pgu.khv.gov.ru/?a=Citizens&category=Municipal";
		public static final String FEDERAL_URL = "http://pgu.khv.gov.ru/?a=Citizens&category=Federal";

		public String currentUrl;

		public List<String> linksText;
		public List<Integer> pics;
		public String myHTML = "";

		private ListView currentListView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			Bundle args = getArguments();
			int position = args.getInt(ARG_OBJECT);

			String url = "http://pgu.khv.gov.ru/?a=Organizations&category=Regional";

			int tabLayout = 0;
			switch (position) {
			case 0:
				tabLayout = R.layout.listview_layout;
				currentUrl = REGIONAL_URL;
				break;
			case 1:
				tabLayout = R.layout.listview_layout;
				currentUrl = MUNICIPAL_URL;
				break;
			case 2:
				tabLayout = R.layout.listview_layout;
				currentUrl = FEDERAL_URL;
				break;
			}

			View rootView = inflater.inflate(tabLayout, container, false);
			perform(rootView);
			return rootView;
		}

		public void perform(View v) {

			currentListView = (ListView) v.findViewById(R.id.listView1);

			linksText = new ArrayList<String>();
			pics = new ArrayList<Integer>();

			DownloadHtml downloadHtml = new DownloadHtml();
			downloadHtml.execute(currentUrl);

			try {
				linksText = downloadHtml.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (Iterator<String> iterator = linksText.iterator(); iterator
					.hasNext();) {
				System.out.println(iterator.next().toString());
			}

			News values[] = new News[linksText.size()];

			int i = 0;
			for (Iterator<String> iterator = linksText.iterator(); iterator
					.hasNext();) {
				String temp = iterator.next().toString();
				values[i] = new News(R.drawable.arrow, temp);
				i++;
			}

			NewsAdapter adapter = new NewsAdapter(getActivity(),
					R.layout.list_row, values);
			currentListView.setAdapter(adapter);

		}

		private class DownloadHtml extends
				AsyncTask<String, Integer, List<String>> {

			List<String> resultList = new ArrayList<String>();

			@Override
			protected List<String> doInBackground(String... urls) {
				try {
					String result = NetworkStats.getOutputFromURL(urls[0]);

					HtmlParser parser;
					try {
						parser = new HtmlParser(result);
						List<TagNode> links = parser.getObjectByTagAndClass(
								"span", "category-menu__text");
						for (Iterator<TagNode> iterator = links.iterator(); iterator
								.hasNext();) {
							TagNode linkElement = (TagNode) iterator.next();
							resultList.add(linkElement.getText().toString());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					Log.d("Background Task", e.toString());
				}
				return resultList;
			}

			@Override
			protected void onPostExecute(List<String> resultList) {

			}

		}
	}

}

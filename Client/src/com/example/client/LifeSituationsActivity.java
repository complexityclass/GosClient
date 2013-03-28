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

public class LifeSituationsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	CollectionPagerAdapter mCollectionPagerAdapter;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the
	 * object collection.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tain);

		// Create an adapter that when requested, will return a fragment
		// representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must
		// use
		// getSupportFragmentManager.
		mCollectionPagerAdapter = new CollectionPagerAdapter(
				getSupportFragmentManager());

		// Set up action bar.

		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		//
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mCollectionPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mCollectionPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
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
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
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

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public static class TabFragment extends Fragment {

		public static final String ARG_OBJECT = "object";
		public static final String REGIONAL_URL = "http://pgu.khv.gov.ru/?a=Organizations&category=Regional";
		public static final String MUNICIPAL_URL = "http://pgu.khv.gov.ru/?a=Organizations&category=Municipal";
		public static final String FEDERAL_URL = "http://pgu.khv.gov.ru/?a=Organizations&category=Federal";
		
		private static final String PAGE_URL = "http://pgu.khv.gov.ru/?a=Departments&category=Regional";
		private static final String PAGE_URL2 = "http://pgu.khv.gov.ru/?a=Departments&category=Federal";
		private static final String PAGE_URL3 = "http://pgu.khv.gov.ru/?a=Departments&category=Municipal";
		
		public String currentUrl;
		
		public List<String> linksText;
		public List<Integer> pics;
		public String myHTML = "";
		
		String[] values=new String[]{"India", "java", "c++","Ad.Java", "Linux", "Unix"};
		
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
				currentUrl = PAGE_URL;
				break;
			case 1:
				tabLayout = R.layout.listview_layout;
				currentUrl = PAGE_URL2;
				break;
			case 2:
				tabLayout = R.layout.listview_layout;
				currentUrl = PAGE_URL3;
				break;
			}
			
			View rootView = inflater.inflate(tabLayout, container, false);
			perform(rootView);
			return rootView;
		}
		
		public void perform(View v){
			
			currentListView = (ListView) v.findViewById(R.id.listView1);
			//ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,values);
		    //currentListView.setAdapter(adapter);
			
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
			
			NewsAdapter adapter = new NewsAdapter(getActivity(), R.layout.list_row, values);
			currentListView.setAdapter(adapter);
			
			
		} 
		
		private class DownloadHtml extends AsyncTask<String, Integer, List<String>> {

			List<String> resultList = new ArrayList<String>();

			@Override
			protected List<String> doInBackground(String... urls) {
				try {
					String result = NetworkStats.getOutputFromURL(urls[0]);

					HtmlParser parser;
					try {
						parser = new HtmlParser(result);
						List<TagNode> links = parser
								.getLinks("category-menu__link");
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
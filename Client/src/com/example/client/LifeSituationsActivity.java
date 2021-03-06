package com.example.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.impl.client.TunnelRefusedException;
import org.htmlcleaner.TagNode;

import com.example.adapters.News;
import com.example.adapters.NewsAdapter;
import com.example.adapters.TupleAB;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabWidget;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LifeSituationsActivity extends FragmentActivity implements
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

		actionBar.getTabAt(0).setCustomView(R.layout.tab_layout);
		TextView txt1 = (TextView) actionBar.getTabAt(0).getCustomView()
				.findViewById(R.id.textView1);
		txt1.setText(R.string.federational);

		actionBar.getTabAt(1).setCustomView(R.layout.tab_layout);
		TextView txt2 = (TextView) actionBar.getTabAt(1).getCustomView()
				.findViewById(R.id.textView1);
		txt2.setText(R.string.regional);

		actionBar.getTabAt(2).setCustomView(R.layout.tab_layout);
		TextView txt3 = (TextView) actionBar.getTabAt(2).getCustomView()
				.findViewById(R.id.textView1);
		txt3.setText(R.string.munitipal);

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
		public static final String REGIONAL_URL = "http://pgu.khv.gov.ru/?a=Living&category=Regional";
		public static final String MUNICIPAL_URL = "http://pgu.khv.gov.ru/?a=Living&category=Municipal";
		public static final String FEDERAL_URL = "http://pgu.khv.gov.ru/?a=Living&category=Federal";

		public static final String DOMAIN = "http://pgu.khv.gov.ru/";

		public String currentUrl;

		public List<TupleAB<String, String>> linksText;
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

			linksText = new ArrayList<TupleAB<String, String>>();
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

			News values[] = new News[linksText.size()];

			for (int i = 0; i < linksText.size(); i++) {
				String temp = linksText.get(i).getA();
				System.out.println(linksText.get(i).getB());
				values[i] = new News(R.drawable.arrow, temp);
			}

			NewsAdapter adapter = new NewsAdapter(getActivity(),
					R.layout.list_row, values);
			currentListView.setAdapter(adapter);

			currentListView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					System.out.println(DOMAIN + linksText.get(position).getB());
					/*Intent newIntent = new Intent(view.getContext(),
							WebViewActivity.class);
					newIntent.putExtra("url", DOMAIN
							+ linksText.get(position).getB());
					startActivity(newIntent);*/
					
					Intent newIntent = new Intent(view.getContext(),ExpandableListActivity.class);
					newIntent.putExtra("url", DOMAIN + linksText.get(position).getB());
					startActivity(newIntent);

				}

			});

		}

		private class DownloadHtml extends
				AsyncTask<String, Integer, List<TupleAB<String, String>>> {

			List<String> resultList = new ArrayList<String>();
			List<String> hrefers = new ArrayList<String>();

			List<TupleAB<String, String>> resultTuple = new ArrayList<TupleAB<String, String>>();

			@Override
			protected List<TupleAB<String, String>> doInBackground(
					String... urls) {
				try {
					String result = NetworkStats.getOutputFromURL(urls[0]);

					HtmlParser parser;
					try {
						parser = new HtmlParser(result);

						List<TagNode> links = parser.getObjectByTagAndClass(
								"span", "category-menu__text");
						List<TagNode> hrefLinks = parser
								.getObjectByTagAndClass("a",
										"category-menu__link");

						for (Iterator<TagNode> iterator = links.iterator(); iterator
								.hasNext();) {
							TagNode linkElement = (TagNode) iterator.next();
							resultList.add(linkElement.getText().toString());
						}

						for (Iterator<TagNode> iterator = hrefLinks.iterator(); iterator
								.hasNext();) {
							TagNode hrefer = (TagNode) iterator.next();
							// System.out.println(hrefer.getAttributeByName("href").toString());
							hrefers.add(hrefer.getAttributeByName("href")
									.toString());
						}

						for (int i = 0; i < Math.min(resultList.size() - 1,
								hrefers.size() - 1); i++) {
							String s1 = resultList.get(i);
							String s2 = hrefers.get(i);
							TupleAB<String, String> temp = new TupleAB<String, String>(
									s1, s2);
							resultTuple.add(temp);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					Log.d("Background Task", e.toString());
				}
				return resultTuple;
			}

			@Override
			protected void onPostExecute(
					List<TupleAB<String, String>> resultList) {

			}

		}
	}

}
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
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author complexityclass
 * 
 *         Class for electronical services catalog. Handle services list with
 *         links for each of them.
 * 
 */
public class ElectronicServicesActivity extends FragmentActivity implements ActionBar.TabListener {

	CollectionPagerAdapter mCollectionPagerAdapter;
	ViewPager mViewPager;
	ProgressBar mProgress;

	public static final int MUNICIPAL_PAGES = 12;
	public static final int REGIONAL_PAGES = 6;

	public static int positionNum = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tain);

		// mProgress =(ProgressBar) findViewById(R.id.progressbar);

		mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());

		final ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);

		// showProgress(true);

		mViewPager.setAdapter(mCollectionPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		for (int i = 0; i < mCollectionPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(mCollectionPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

		// showProgress(false);

		actionBar.getTabAt(0).setCustomView(R.layout.tab_layout);
		TextView txt1 = (TextView) actionBar.getTabAt(0).getCustomView().findViewById(R.id.textView1);
		txt1.setText(R.string.federational);

		actionBar.getTabAt(1).setCustomView(R.layout.tab_layout);
		TextView txt2 = (TextView) actionBar.getTabAt(1).getCustomView().findViewById(R.id.textView1);
		txt2.setText(R.string.munitipal);

	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	public class CollectionPagerAdapter extends FragmentPagerAdapter {

		/* number of Tabs */
		final int NUM_ITEMS = 2;

		public CollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/* Get item on position i */
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
				positionNum = 0;
				break;
			case 1:
				tabLabel = getString(R.string.regional);
				positionNum = 1;
				break;
			}

			return tabLabel;
		}

	}

	public static class TabFragment extends Fragment {

		public static final String ARG_OBJECT = "object";
		public static final String MUNICIPAL_URL = "http://pgu.khv.gov.ru/?a=Services&category=Municipal";
		public static final String REGIONAL_URL = "http://pgu.khv.gov.ru/?a=Services&category=Regional";
		public static final String DOMAIN = "http://pgu.khv.gov.ru/";

		public String currentUrl;
		public List<TupleAB<String, String>> linksText;
		public List<Integer> pics;
		public String myHTML = "";

		private ListView currentListView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

			Bundle args = getArguments();
			int position = args.getInt(ARG_OBJECT);

			String url = "http://pgu.khv.gov.ru/?a=Services&category=Regional";

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
			}

			View rootView = inflater.inflate(tabLayout, container, false);

			perform(rootView);
			return rootView;
		}

		// Obtain html
		public void perform(View v) {

			currentListView = (ListView) v.findViewById(R.id.listView1);
			linksText = new ArrayList<TupleAB<String, String>>();
			pics = new ArrayList<Integer>();

			DownloadHtml downloadHtml = new DownloadHtml();
			downloadHtml.execute(currentUrl);

			try {

				linksText = downloadHtml.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			News[] values = new News[linksText.size()];

			for (int i = 0; i < linksText.size(); i++) {
				String temp = linksText.get(i).getA();
				values[i] = new News(R.drawable.arrow, temp);
			}

			NewsAdapter adapter = new NewsAdapter(getActivity(), R.layout.list_row, values);
			currentListView.setAdapter(adapter);

			currentListView.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					/*
					 * Intent newIntent = new Intent(view.getContext(),
					 * ExpandableListActivity.class); newIntent.putExtra("url",
					 * DOMAIN + linksText.get(position).getB());
					 * startActivity(newIntent);
					 */
					System.out.println("that's work");

					/*
					 * Intent newIntent = new Intent(view.getContext(),
					 * WebViewActivity.class); newIntent.putExtra("url", DOMAIN
					 * + linksText.get(position).getB());
					 * startActivity(newIntent);
					 */

					Intent newIntent = new Intent(view.getContext(), ServiceActivity.class);
					newIntent.putExtra("url", DOMAIN + linksText.get(position).getB());
					startActivity(newIntent);

				}

			});

		}

		private class DownloadHtml extends AsyncTask<String, Integer, List<TupleAB<String, String>>> {

			List<String> titleList = new ArrayList<String>();
			List<String> hrefList = new ArrayList<String>();
			List<TupleAB<String, String>> resultTuple = new ArrayList<TupleAB<String, String>>();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected List<TupleAB<String, String>> doInBackground(String... urls) {

				int maxPage = 0;

				maxPage = positionNum == 0 ? MUNICIPAL_PAGES : REGIONAL_PAGES;

				for (Integer j = 1; j < maxPage; j++) {

					System.out.println("parse page!");

					String curUrl = j == 1 ? urls[0] : urls[0] + "&page=" + j.toString();

					try {
						String result = NetworkStats.getOutputFromURL(curUrl);
						HtmlParser parser;

						try {

							parser = new HtmlParser(result);

							List<TagNode> titles = parser.getObjectByTagAndClass("h3", "folder-list__title");
							List<TagNode> hrefs = parser.getObjectByTagAndClass("a", "On folder-list__link");

							for (Iterator<TagNode> iterator = titles.iterator(); iterator.hasNext();) {
								TagNode titleElement = (TagNode) iterator.next();
								titleList.add(titleElement.getText().toString());
							}

							for (Iterator<TagNode> iterator = hrefs.iterator(); iterator.hasNext();) {
								TagNode hrefElement = (TagNode) iterator.next();
								hrefList.add(hrefElement.getAttributeByName("href").toString());
							}

							for (int i = 0; i < Math.min(titles.size() - 1, hrefs.size() - 1); i++) {

								String s1 = titleList.get(i);
								String s2 = hrefList.get(i);
								System.out.println("TITLES WITH LINKS" + s1 + " " + s2);
								TupleAB<String, String> temp = new TupleAB<String, String>(s1, s2);
								resultTuple.add(temp);

							}

							/*
							 * for (int i = 0; i < Math.min(titles.size() - 1,
							 * hrefs.size() - 1); i++) {
							 * 
							 * String s1 = titles.get(i).getText().toString();
							 * String s2 = hrefs.get(i).getText().toString();
							 * TupleAB<String, String> temp = new
							 * TupleAB<String, String>(s1, s2);
							 * resultTuple.add(temp); }
							 */

						} catch (Exception e) {
							e.printStackTrace();
						}

					} catch (Exception e) {
						Log.d("BackgroundTask", e.toString());
					}
				}

				return resultTuple;

			}

			@Override
			protected void onPostExecute(List<TupleAB<String, String>> resultList) {

			}

		}

	}

	public void showProgress(boolean b) {

		mProgress.setVisibility(b ? View.VISIBLE : View.GONE);
		mViewPager.setVisibility(b ? View.VISIBLE : View.GONE);

	}

}

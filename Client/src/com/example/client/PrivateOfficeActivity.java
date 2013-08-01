package com.example.client;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.example.adapters.PersonalDataAdapter;
import com.example.adapters.PersonalRow;
import com.example.http.Connect;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PrivateOfficeActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_office);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.private_office, menu);
		return true;
	}

	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		Fragment fragment;

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			switch (position) {
			case 0:
				fragment = new PersonalDataFragment();
				break;
			case 1:
				fragment = new PersonalServicesFragment();
				break;
			}

			fragment = new PersonalDataFragment();

			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.personal_data).toUpperCase(l);
			case 1:
				return getString(R.string.required_services).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_private_office_dummy, container, false);
			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	public static class PersonalDataFragment extends Fragment {

		ListView listview;

		Context globalcontext;

		PersonalRow[] cont;

		public static final String ARG_SECTION_NUMBER = "section_number";

		public PersonalDataFragment() {

		}

		public void setArray(Map<String, String> map) {
			cont = new PersonalRow[map.size()];
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.p_data_view, container, false);

			globalcontext = this.getActivity();

			Map<String, String> personalMap = new HashMap<String, String>();

			List<String> key = Arrays.asList("���", "�������", "��������", "���� ��������", "���", "�����", "�������",
					"����������� �����", "���", "������� ��", "������");

			List<String> values = Arrays.asList("�������", "�����", "�������������", "23.09.1992", "�������",
					"178-12-321-123 56", "+7 916 053 24 73", "complexityclass@gmail.com", "77777-77777-77777", "",
					"141700");

			cont = new PersonalRow[Math.min(key.size(), values.size())];

			for (int i = 0; i < Math.min(key.size(), values.size()); i++) {

				cont[i] = new PersonalRow(key.get(i), values.get(i));

			}

			listview = (ListView) rootView.findViewById(R.id.listViewpData);

			final PersonalDataAdapter pdataAdapter = new PersonalDataAdapter(getActivity(), R.layout.per_data_row, cont);
			listview.setAdapter(pdataAdapter);

			listview.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

					Log.d("CHECKED", pdataAdapter.getData(position));

					AlertDialog.Builder editAlert = new AlertDialog.Builder(globalcontext);
					editAlert.setTitle("");
					editAlert.setMessage("��������" + pdataAdapter.getData(position).split("::")[0]);

					final EditText input = new EditText(getActivity());

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT);

					input.setLayoutParams(lp);
					editAlert.setView(input);

					editAlert.setPositiveButton("������� ����� ��������", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							String newValue = input.getText().toString();
							Log.d("BUTTON CLICK NEW VALUE: ", newValue);
							pdataAdapter.setData(position, newValue);

						}
					});

					editAlert.show();

				}

			});

			return rootView;
		}

	}

	public static class PersonalServicesFragment extends Fragment {

		ListView listview;
		PersonalRow[] cont;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.p_data_view, container, false);

			cont = new PersonalRow[3];

			cont[0] = new PersonalRow("Question", "Answer");

			listview = (ListView) rootView.findViewById(R.id.listViewpData);

			final PersonalDataAdapter pdataAdapter = new PersonalDataAdapter(getActivity(), R.layout.per_data_row, cont);
			listview.setAdapter(pdataAdapter);

			NetworkThread netThread = new NetworkThread();

			netThread.execute("start");

			try {
				String s = netThread.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			return rootView;
		}

		private class NetworkThread extends AsyncTask<String, Integer, String> {

			@Override
			protected String doInBackground(String... params) {

				String result = "";

				Connect connect = new Connect();

				result = connect.doGetwithSession("https://pgu.khv.gov.ru/?a=PersonCab&category=Details",
						"dimlcjjlb19vd09ou2hfu78cj7");

				return result;

			}

		}

	}

}

package com.example.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.adapters.CustomEditProfileAdapter;
import com.example.adapters.MultiChoiceImpl;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EditProfileActivity extends ListActivity {

	public static final String TAG = "Profile";
	private Random randomGenerator = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile_main);

		int size = getRandomNumber(200);

		ListView listView = getListView();

		Integer[] colors = generateListOfColors(size).toArray(new Integer[0]);
		ArrayAdapter<Integer> customAdapter = new CustomEditProfileAdapter(this, R.layout.activity_edit_profile_row,
				colors, listView);
		listView.setAdapter(customAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceImpl(listView));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(TAG, "onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private List<Integer> generateListOfColors(int size) {
		List<Integer> colorList = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			colorList.add(generateRandomColor());
		}
		return colorList;
	}

	private int generateRandomColor() {
		return Color.rgb(getRandomNumber(256), getRandomNumber(256), getRandomNumber(256));
	}

	public int getRandomNumber(int maxValue) {
		return randomGenerator.nextInt(maxValue);
	}

}

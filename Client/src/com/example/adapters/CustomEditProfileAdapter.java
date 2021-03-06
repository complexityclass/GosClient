package com.example.adapters;

import com.example.client.R;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CustomEditProfileAdapter extends ArrayAdapter<Integer> {

	private ListView listView;

	public CustomEditProfileAdapter(Context context, int textViewResourceId, Integer[] objects, ListView listView) {
		super(context, textViewResourceId, objects);
		this.listView = listView;
	}

	static class ViewHolder {
		TextView text;
		View indicator;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		Integer color = getItem(position);
		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
			rowView = inflater.inflate(R.layout.activity_edit_profile_row, parent, false);
			ViewHolder h = new ViewHolder();
			h.text = (TextView) rowView.findViewById(R.id.item_text);
			h.indicator = rowView.findViewById(R.id.item_image);
			rowView.setTag(h);
		}

		ViewHolder h = (ViewHolder) rowView.getTag();
		h.text.setText("#" + Integer.toHexString(color).replaceFirst("ff", ""));
		h.indicator.setBackgroundColor(color);

		h.indicator.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

			private void selectRow(View v) {
				listView.setItemChecked(position, !isItemChecked(position));
			}

			private boolean isItemChecked(int pos) {
				SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
				return sparseBooleanArray.get(pos);
			}
		});

		return rowView;

	}
}

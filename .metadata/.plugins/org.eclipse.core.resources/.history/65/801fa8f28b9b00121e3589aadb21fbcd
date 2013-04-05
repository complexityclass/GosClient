package com.example.adapters;

import com.example.client.R;
import com.example.client.LifeSituationsActivity.TabFragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author complexityclass Class for mange ListView rows
 * 
 */
public class NewsAdapter extends ArrayAdapter<News> {

	Context context;
	int layoutResourceId;
	News data[] = null;

	public NewsAdapter(Context context, int layoutResourceId, News[] data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	
	public NewsAdapter(TabFragment fragment, int layoutResourceId, News[] data) {
		super(fragment.getActivity(), layoutResourceId, data);
		this.context = fragment.getActivity();
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	

	/**
	 * @param int position in ListView
	 * @param View
	 *            convertView current view
	 * @param ViewGroup
	 *            parent
	 * */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		NewsHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new NewsHolder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);

			row.setTag(holder);

		} else {
			holder = (NewsHolder) row.getTag();
		}

		News news = data[position];
		holder.txtTitle.setText(news.title);
		holder.imgIcon.setImageResource(news.icon);

		return row;

	}

	/** Class holder for News */
	static class NewsHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}

}

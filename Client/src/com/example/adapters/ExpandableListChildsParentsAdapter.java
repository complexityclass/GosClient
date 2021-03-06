package com.example.adapters;

import java.util.ArrayList;

import com.example.client.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author complexityclass Custom adapter for Expandable ListView
 */
public class ExpandableListChildsParentsAdapter extends BaseExpandableListAdapter {

	private ArrayList<ArrayList<String>> mGroups;
	private ArrayList<String> mParents;
	private Context mContext;
	
	public ExpandableListChildsParentsAdapter(Context context, ArrayList<ArrayList<String>> groups, ArrayList<String> parents) {
		mContext = context;
		mGroups = groups;
		mParents = parents;
	}

	public int getGroupCount() {
		return mGroups.size();
	}

	public int getChildrenCount(int groupPosition) {
		return mGroups.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return mGroups.get(groupPosition).get(childPosition);
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public boolean hasStableIds() {
		return true;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.exp_list_group_view, null);
		}

		// Change smth if group expanded/ spanded
		if (isExpanded) {

		} else {

		}

		TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
		
		//textGroup.setText("Group " + Integer.toString(groupPosition));
		
		textGroup.setText(mParents.get(groupPosition));

		return convertView;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.exp_list_child_view, null);
		}

		TextView textChild = (TextView) convertView.findViewById(R.id.textChild);
		textChild.setText(mGroups.get(groupPosition).get(childPosition));

		return convertView;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
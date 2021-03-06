package com.example.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.client.EditProfileActivity;
import com.example.client.R;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Toast;

public class MultiChoiceImpl implements AbsListView.MultiChoiceModeListener {
	private AbsListView listView;

	public MultiChoiceImpl(AbsListView listView) {
		this.listView = listView;
	}

	// ����� ���������� ��� ����� ��������� ��������� ��������� �����
	public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
		Log.d(EditProfileActivity.TAG, "onItemCheckedStateChanged");
		int selectedCount = listView.getCheckedItemCount();
		// ������� ���������� ���������� ����� � Context Action Bar
		setSubtitle(actionMode, selectedCount);
	}

	// ����� �������� CAB �� xml
	public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
		Log.d(EditProfileActivity.TAG, "onCreateActionMode");
		MenuInflater inflater = actionMode.getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
		return true;
	}

	public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
		Log.d(EditProfileActivity.TAG, "onPrepareActionMode");
		return false;
	}

	// ���������� ��� ����� �� ����� Item �� �AB
	public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
		String text = "Action - " + menuItem.getTitle() + " ; Selected items: " + getSelectedFiles();
		Toast.makeText(listView.getContext(), text, Toast.LENGTH_LONG).show();
		return false;
	}

	public void onDestroyActionMode(ActionMode actionMode) {
		Log.d(EditProfileActivity.TAG, "onDestroyActionMode");
	}

	private void setSubtitle(ActionMode mode, int selectedCount) {
		switch (selectedCount) {
		case 0:
			mode.setSubtitle(null);
			break;
		default:
			mode.setTitle(String.valueOf(selectedCount));
			break;
		}
	}

	private List<String> getSelectedFiles() {
		List<String> selectedFiles = new ArrayList<String>();

		SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
		for (int i = 0; i < sparseBooleanArray.size(); i++) {
			if (sparseBooleanArray.valueAt(i)) {
				Integer selectedItem = (Integer) listView.getItemAtPosition(sparseBooleanArray.keyAt(i));
				selectedFiles.add("#" + Integer.toHexString(selectedItem).replaceFirst("ff", ""));
			}
		}
		return selectedFiles;
	}
}
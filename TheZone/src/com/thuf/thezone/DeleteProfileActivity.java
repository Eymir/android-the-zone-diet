package com.thuf.thezone;

import android.app.Dialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.thuf.thezone.database.TableProfile;
import com.thuf.thezone.database.ZoneDatabase;

public class DeleteProfileActivity extends ListActivity implements TableProfile,
		OnClickListener {
	private static final int[] TO = { R.id.item_name, R.id.item_gender, R.id.item_weight };
	private static final String[] FROM = { COLUMN_NAME_PROFILE, COLUMN_GENDER,
			COLUMN_WEIGHT_PROFILE };

	private SimpleCursorAdapter adapter;
	private ZoneDatabase database;
	private Cursor cursor;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load_profile);
		setListeners();

		database = new ZoneDatabase(this);
		cursor = database.getAllProfiles();
		startManagingCursor(cursor);
		adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, FROM, TO);
		setListAdapter(adapter);
	}

	private void setListeners() {
		findViewById(R.id.tv_column_name).setOnClickListener(this);
		findViewById(R.id.tv_column_gender).setOnClickListener(this);
		findViewById(R.id.tv_column_weight).setOnClickListener(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		openDeleteDialog(id);

	}

	private void openDeleteDialog(final long id) {
		final Dialog deleteDialog = new Dialog(this);
		deleteDialog.setContentView(R.layout.delete_dialog);
		deleteDialog.setTitle(getResources().getString(R.string.title_delete_dialog));

		deleteDialog.findViewById(R.id.dialog_button_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						deleteDialog.dismiss();
					}
				});
		deleteDialog.findViewById(R.id.dialog_button_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						deleteDialog.dismiss();
						database.deleteProfile(id);
						database.deleteStatisticsForProfile(id);
						Cursor cursor = database.getAllProfiles();
						adapter.changeCursor(cursor);
						adapter.notifyDataSetChanged();

					}
				});
		deleteDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_column_name:
			cursor = database.getAllProfiles(COLUMN_NAME_PROFILE, COLUMN_WEIGHT_PROFILE);
			break;
		case R.id.tv_column_gender:
			cursor = database.getAllProfiles(COLUMN_GENDER, COLUMN_NAME_PROFILE);
			break;
		case R.id.tv_column_weight:
			cursor = database.getAllProfiles(COLUMN_WEIGHT_PROFILE, COLUMN_NAME_PROFILE);
			break;
		}
		adapter.changeCursor(cursor);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onStop() {
		database.close();
		super.onStop();
	}
}

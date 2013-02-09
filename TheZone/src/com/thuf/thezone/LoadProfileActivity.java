package com.thuf.thezone;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.thuf.thezone.database.TableProfile;
import com.thuf.thezone.database.ZoneDatabase;
import com.thuf.thezone.objects.Profile;

public class LoadProfileActivity extends ListActivity implements TableProfile, OnClickListener {
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
		Cursor selected = adapter.getCursor();
		selected.moveToPosition(position);
		Profile profile = database.cursorToProfile(selected);

		Intent profileActivity = new Intent(this, ProfileActivity.class);
		profileActivity.putExtra("Profile", profile);
		startActivity(profileActivity);
	}

	@Override
	protected void onPause() {
		super.onPause();
		database.close();
		finish();
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
}

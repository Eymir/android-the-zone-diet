package com.thuf.thezone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.thuf.thezone.database.TableProfileStatistics;
import com.thuf.thezone.objects.Profile;

public class SelectStatisticsActivity extends Activity implements OnClickListener,
		TableProfileStatistics {
	private static final String EXTRA_STATS = "STATS";
	Profile profile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_statistic);
		this.profile = (Profile) getIntent().getSerializableExtra("Profile");
		setListeners();
	}

	private void setListeners() {
		findViewById(R.id.button_weight_statistics).setOnClickListener(this);
		findViewById(R.id.button_waist_statistics).setOnClickListener(this);
		findViewById(R.id.button_wrist_statistics).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, StatisticsActivity.class);
		intent.putExtra("Profile", this.profile);
		switch (v.getId()) {
		case R.id.button_weight_statistics:
			intent.putExtra(EXTRA_STATS, COLUMN_WEIGHT_STATISTICS);
			break;
		case R.id.button_waist_statistics:
			intent.putExtra(EXTRA_STATS, COLUMN_WAIST_STATISTICS);
			break;
		case R.id.button_wrist_statistics:
			intent.putExtra(EXTRA_STATS, COLUMN_WRIST_STATISTICS);
			break;
		}
		startActivity(intent);
	}
}

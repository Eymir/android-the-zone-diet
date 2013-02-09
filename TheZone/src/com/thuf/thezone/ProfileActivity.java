package com.thuf.thezone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.thuf.thezone.objects.Man;
import com.thuf.thezone.objects.Profile;
import com.thuf.thezone.objects.Woman;

@SuppressLint("DefaultLocale")
public class ProfileActivity extends Activity implements OnClickListener {
	private TextView tvName;
	private TextView tvWeight;
	private TextView tvHeight;
	private TextView tvWaist;
	private TextView tvWrist;
	private TextView tvWristOrHip;
	private TextView tvGender;
	private TextView tvBodyMassIndex;
	private TextView tvFatsPercent;
	private TextView tvDailyBlocks;
	private ImageView ivActivityLevel;
	private float physicalActivity = 0.5f;
	private Profile profile;

	public static final int REQUEST_UPDATE_PROFILE = 3;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_help:
			Dialog helpDialog = new Dialog(this);
			helpDialog.setContentView(R.layout.help_dialog);
			helpDialog.setTitle("What contains one block?");
			helpDialog.show();
			return true;
		case R.id.menu_list:
			Dialog listDialog = new Dialog(this);
			listDialog.setContentView(R.layout.list_dialog);
			listDialog.setTitle(R.string.title_list_dialog);
			ListView lv = (ListView) listDialog.findViewById(R.id.listView1);
			String[] listExamples = getResources().getStringArray(R.array.example_list_data);
			ListAdapter adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, listExamples);
			lv.setAdapter(adapter);
			listDialog.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		this.profile = (Profile) getIntent().getSerializableExtra("Profile");
		findViews();
		setListeners();
		setText(this.profile);

		SeekBar bar = (SeekBar) findViewById(R.id.seekBar1);
		bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				physicalActivity = progress / 10f + 0.5f;
				switch (progress) {
				case 0:
				case 1:
					ivActivityLevel.setBackgroundResource(R.drawable.sport_type_one);
					break;
				case 2:
				case 3:
					ivActivityLevel.setBackgroundResource(R.drawable.sport_type_two);
					break;
				case 4:
				case 5:
					ivActivityLevel.setBackgroundResource(R.drawable.sport_type_three);
					break;

				}
				setText(profile);
			}
		});
	}

	private void findViews() {
		tvName = (TextView) findViewById(R.id.tv_name);
		tvWeight = (TextView) findViewById(R.id.tv_weight);
		tvHeight = (TextView) findViewById(R.id.tv_height);
		tvWaist = (TextView) findViewById(R.id.tv_waist);
		tvWrist = (TextView) findViewById(R.id.tv_wrist);
		tvWristOrHip = (TextView) findViewById(R.id.tv_wrist_or_hip);
		tvGender = (TextView) findViewById(R.id.tv_gender);
		tvBodyMassIndex = (TextView) findViewById(R.id.tv_body_mass_index);
		tvFatsPercent = (TextView) findViewById(R.id.tv_fats_percent);
		tvDailyBlocks = (TextView) findViewById(R.id.tv_daily_blocks);
		ivActivityLevel = (ImageView) findViewById(R.id.iv_activity_level);
	}

	private void setText(Profile p) {
		String weightUnits;
		String lengthUnits;
		if (p.isImperialSystem()) {
			weightUnits = " lbs";
			lengthUnits = " inch";
		} else {
			weightUnits = " kg";
			lengthUnits = " cm";
		}

		tvName.setText(p.getName());
		tvWeight.setText((p.getWeight() + weightUnits));
		tvHeight.setText((p.getHeight() + lengthUnits));
		tvWaist.setText((p.getWaist() + lengthUnits));

		String gender = null;
		if (p.isMan()) {
			gender = "male";
			String label = getResources().getString(R.string.label_wrist);
			tvWristOrHip.setText(label);
			tvWrist.setText((p.getWrist() + lengthUnits));
		} else {
			gender = "female";
			String label = getResources().getString(R.string.label_hip);
			tvWristOrHip.setText(label);
			tvWrist.setText((p.getWrist() + lengthUnits));
		}
		tvGender.setText(gender);
		int height = p.getHeight();
		int weight = p.getWeight();
		boolean imperial = p.isImperialSystem();
		tvBodyMassIndex.setText(calculateBMI(height, weight, imperial));

		if (this.profile.isMan()) {
			int fatsPercent = createMan();
			if (fatsPercent != -1) {
				tvFatsPercent.setText((fatsPercent + "%"));
				setDailyBlocksText(fatsPercent);
			} else {
				tvFatsPercent.setText("n/a");
				tvDailyBlocks.setText("Daily blocks: n/a");
			}
		} else {
			float fatsPercent = createWoman();
			if (fatsPercent > 0) {
				String fats = String.format("%.1f", fatsPercent) + "%";
				tvFatsPercent.setText(fats);
				setDailyBlocksText((int) fatsPercent);
			} else {
				tvFatsPercent.setText("n/a");
				tvDailyBlocks.setText("n/a");
			}
		}
	}

	private String calculateBMI(int height, int weight, boolean imperial) {
		float bodyMassIndex = 0f;
		if (imperial) {
			bodyMassIndex = (float) ((weight * 703) / Math.pow(height, 2));
		} else {
			float heightInMetric = (float) height / 100.0f;
			bodyMassIndex = weight / (heightInMetric * heightInMetric);
		}
		return String.format("%.2f", bodyMassIndex);
	}

	private int createMan() {
		Man man = new Man(profile.getWeight(), profile.getWaist(), profile.getWrist(),
				!profile.isImperialSystem());
		int fatsPercent = man.getFatsPercent();
		return fatsPercent;
	}

	private float createWoman() {
		Woman woman = new Woman(profile.getHeight(), profile.getWaist(), profile.getWrist(),
				!profile.isImperialSystem());

		Log.d("ThuF", "In createWoman method");
		float fatsPercent = woman.getFatsPercent();
		return fatsPercent;
	}

	private void setDailyBlocksText(int fatsPercent) {
		int dailyBlocks;
		if (profile.isImperialSystem()) {
			dailyBlocks = (int) ((profile.getWeight() - profile.getWeight() * fatsPercent
					/ 100)
					* physicalActivity / 7);
		} else {
			int weightInKgs = profile.getWeight();
			int weightInPounds = (int) (weightInKgs * 2.2075055187637969094922737306843);
			dailyBlocks = (int) ((weightInPounds - weightInPounds * fatsPercent / 100)
					* physicalActivity / 7);
		}
		tvDailyBlocks.setText("Daily blocks: " + dailyBlocks);
	}

	private void setListeners() {
		findViewById(R.id.button_update_profile).setOnClickListener(this);
		findViewById(R.id.button_statistics).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.button_update_profile:
			intent = new Intent(this, UpdateProfile.class);
			intent.putExtra("Profile", this.profile);
			startActivityForResult(intent, REQUEST_UPDATE_PROFILE);
			break;
		case R.id.button_statistics:
			intent = new Intent(this, SelectStatisticsActivity.class);
			intent.putExtra("Profile", this.profile);
			startActivity(intent);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_UPDATE_PROFILE:
			this.profile = (Profile) data.getSerializableExtra("Profile");
			setText(this.profile);
			break;
		}
	}
}

package com.thuf.thezone;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.thuf.thezone.database.ZoneDatabase;
import com.thuf.thezone.objects.Man;
import com.thuf.thezone.objects.Profile;
import com.thuf.thezone.objects.Woman;

public class NewProfileActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener, android.widget.RadioGroup.OnCheckedChangeListener {
	private EditText etName;
	private EditText etWeight;
	private EditText etHeight;
	private EditText etWaist;
	private EditText etWrist;
	private RadioGroup rgGender;
	private CompoundButton switchButtonImperial;
	private Button bCreate;

	private boolean imperialSystem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_profile);
		findViews();
		setListeners();
	}

	private void findViews() {
		etName = (EditText) findViewById(R.id.et_name);
		etWeight = (EditText) findViewById(R.id.et_weight);
		etHeight = (EditText) findViewById(R.id.et_height);
		etWaist = (EditText) findViewById(R.id.et_waist);
		etWrist = (EditText) findViewById(R.id.et_wrist);
		rgGender = (RadioGroup) findViewById(R.id.radio_group_gender);
		bCreate = (Button) findViewById(R.id.button_create_profile);
		switchButtonImperial = (CompoundButton) findViewById(R.id.check_box_imperial);
	}

	private void setListeners() {
		rgGender.setOnCheckedChangeListener(this);
		bCreate.setOnClickListener(this);
		switchButtonImperial.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_create_profile:
			createProfile();
			break;
		default:
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Resources res = getResources();
		switch (buttonView.getId()) {
		case R.id.check_box_imperial:
			imperialSystem = !isChecked;
			if (isChecked) {
				// Metric system
				etWeight.setHint(res.getString(R.string.hint_weight_metric));
				etHeight.setHint(res.getString(R.string.hint_height_metric));
				etWaist.setHint(res.getString(R.string.hint_waist_metric));
				etWrist.setHint(res.getString(R.string.hint_wrist_metric));
			} else {
				// Imperial system
				etWeight.setHint(res.getString(R.string.hint_weight_imperial));
				etHeight.setHint(res.getString(R.string.hint_height_imperial));
				etWaist.setHint(res.getString(R.string.hint_waist_imperial));
				etWrist.setHint(res.getString(R.string.hint_wrist_imperial));
			}
			break;
		default:
		}
	}

	private void createProfile() {
		String nameInput = etName.getText().toString();
		String weightInput = etWeight.getText().toString();
		String heightInput = etHeight.getText().toString();
		String waistInput = etWaist.getText().toString();
		String wristInput = etWrist.getText().toString();

		if (!TextUtils.isEmpty(nameInput) && !TextUtils.isEmpty(weightInput)
				&& !TextUtils.isEmpty(heightInput) && !TextUtils.isEmpty(waistInput)
				&& !TextUtils.isEmpty(wristInput)) {
			String name = nameInput;
			int weight = Integer.parseInt(weightInput);
			int height = Integer.parseInt(heightInput);
			float waist = Float.parseFloat(waistInput);
			float wrist = Float.parseFloat(wristInput);
			boolean man = isMale();

			Profile profile = new Profile(name, weight, height, waist, wrist, man,
					imperialSystem);
			ZoneDatabase database = new ZoneDatabase(this);
			if (man) {
				try {
					Man pMan = new Man(profile.getWeight(), profile.getWaist(),
							profile.getWrist(), !profile.isImperialSystem());
					pMan.getFatsPercent();
					long id = database.insertProfile(profile);
					profile.setId(id);
					database.insertStatisticsForProfile(profile);
				} catch (IndexOutOfBoundsException e) {
					Toast.makeText(this, "Incorrect values", Toast.LENGTH_LONG).show();
					database.close();
					return;
				}
			} else {
				try {
					Woman pWoman = new Woman(profile.getHeight(), profile.getWaist(),
							profile.getWrist(), !profile.isImperialSystem());
					pWoman.getFatsPercent();
					long id = database.insertProfile(profile);
					profile.setId(id);
					database.insertStatisticsForProfile(profile);
				} catch (IndexOutOfBoundsException e) {
					Toast.makeText(this, "Incorrect values", Toast.LENGTH_LONG).show();
					database.close();
					return;
				}
			}
			database.close();

			Intent profileActivity = new Intent(this, ProfileActivity.class);
			profileActivity.putExtra("Profile", profile);
			startActivity(profileActivity);
			finish();
		} else {
			Toast.makeText(this, "Please insert all values", Toast.LENGTH_LONG).show();
		}
	}

	private boolean isMale() {
		RadioGroup rg = (RadioGroup) findViewById(R.id.radio_group_gender);
		int checkedButton = rg.getCheckedRadioButtonId();
		switch (checkedButton) {
		case R.id.radio_man:
			return true;
		case R.id.radio_woman:
			return false;
		default:
			return true;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		boolean metricSystem = switchButtonImperial.isChecked();
		Resources resources = getResources();

		switch (checkedId) {
		case R.id.radio_man:
			String wristHint = null;
			if (metricSystem) {
				wristHint = resources.getString(R.string.hint_wrist_metric);
			} else {
				wristHint = resources.getString(R.string.hint_wrist_imperial);
			}
			etWrist.setHint(wristHint);
			break;
		case R.id.radio_woman:
			String hipHint = null;
			if (metricSystem) {
				hipHint = resources.getString(R.string.hint_hip_metric);
			} else {
				hipHint = resources.getString(R.string.hint_hip_imperial);
			}
			etWrist.setHint(hipHint);
			break;
		}
	}
}

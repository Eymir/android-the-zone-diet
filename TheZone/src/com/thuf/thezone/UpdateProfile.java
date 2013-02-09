package com.thuf.thezone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.thuf.thezone.database.ZoneDatabase;
import com.thuf.thezone.objects.Man;
import com.thuf.thezone.objects.Profile;

public class UpdateProfile extends Activity implements OnClickListener {
	private EditText etName;
	private EditText etWeight;
	private EditText etHeight;
	private EditText etWaist;
	private EditText etWrist;
	private CompoundButton switchButtonImperial;
	private Button bConfirm;

	private Profile profile;
	private int UPDATE_RESULT = RESULT_CANCELED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_profile);
		profile = (Profile) getIntent().getSerializableExtra("Profile");
		findViews();
		setText();
		setListeners();
	}

	private void findViews() {
		etName = (EditText) findViewById(R.id.et_name);
		etWeight = (EditText) findViewById(R.id.et_weight);
		etHeight = (EditText) findViewById(R.id.et_height);
		etWaist = (EditText) findViewById(R.id.et_waist);
		etWrist = (EditText) findViewById(R.id.et_wrist);
		switchButtonImperial = (CompoundButton) findViewById(R.id.check_box_imperial);
		bConfirm = (Button) findViewById(R.id.button_create_profile);
	}

	private void setText() {
		etName.setText("" + profile.getName());
		etWeight.setText("" + profile.getWeight());
		etHeight.setText("" + profile.getHeight());
		etWaist.setText("" + profile.getWaist());
		etWrist.setText("" + profile.getWrist());
		RadioButton rbWoman = (RadioButton) findViewById(R.id.radio_woman);
		if (!profile.isMan()) {
			rbWoman.setChecked(true);
		}

		// Gender can't be changed while updating profile
		findViewById(R.id.radio_man).setClickable(false);
		findViewById(R.id.radio_woman).setClickable(false);
		// Measuring system can't be changed while updating profile
		switchButtonImperial.setClickable(false);

		if (profile.isImperialSystem()) {
			switchButtonImperial.setChecked(false);
		} else {
			switchButtonImperial.setChecked(true);
		}

		bConfirm.setText(getResources().getString(R.string.label_button_confirm_profile));
	}

	private void setListeners() {
		bConfirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_create_profile:
			updateProfile();
			break;
		default:
		}
	}

	private void updateProfile() {
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
					this.profile.isImperialSystem());
			ZoneDatabase database = new ZoneDatabase(this);
			long id = this.profile.getId();
			profile.setId(id);
			this.profile = profile;

			if (man) {
				try {
					Man pMan = new Man(profile.getWeight(), profile.getWaist(),
							profile.getWrist(), !profile.isImperialSystem());
					pMan.getFatsPercent();
					database.updateProfileById(id, profile);
					database.insertStatisticsForProfile(profile);
				} catch (IndexOutOfBoundsException e) {
					Toast.makeText(this, "Incorrect values", Toast.LENGTH_LONG).show();
					this.UPDATE_RESULT = RESULT_CANCELED;
					database.close();
					return;
				}
			} else {
				// TODO ->
				// Woman !!! Still to do !!!
				database.updateProfileById(id, profile);
				database.insertStatisticsForProfile(profile);
			}

			this.UPDATE_RESULT = RESULT_OK;
			database.close();
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
	public void finish() {
		if (this.UPDATE_RESULT == RESULT_OK) {
			Intent data = new Intent();
			data.putExtra("Profile", this.profile);
			setResult(RESULT_OK, data);
		}
		super.finish();
	}
}

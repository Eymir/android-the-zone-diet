package com.thuf.thezone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setListeners();
	}

	private void setListeners() {
		findViewById(R.id.button_new_profile).setOnClickListener(this);
		findViewById(R.id.button_load_profile).setOnClickListener(this);
		findViewById(R.id.button_delete_profile).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.button_new_profile:
			intent = new Intent(this, NewProfileActivity.class);
			startActivity(intent);
			break;
		case R.id.button_load_profile:
			intent = new Intent(this, LoadProfileActivity.class);
			startActivity(intent);
			break;
		case R.id.button_delete_profile:
			intent = new Intent(this, DeleteProfileActivity.class);
			startActivity(intent);
			break;
		}
	}
}

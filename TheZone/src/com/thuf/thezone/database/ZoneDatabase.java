package com.thuf.thezone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.thuf.thezone.objects.Profile;

public class ZoneDatabase implements TableProfile, TableProfileStatistics {
	private DatabaseOpenHelper dbHelper;
	private SQLiteDatabase database;

	public ZoneDatabase(Context context) {
		this.dbHelper = new DatabaseOpenHelper(context);
		this.database = this.dbHelper.getReadableDatabase();
	}

	public void insertStatisticsForProfile(Profile profile) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_STATISTICS_FOR_PROFILE_ID, profile.getId());
		values.put(COLUMN_WEIGHT_STATISTICS, profile.getWeight());
		values.put(COLUMN_WAIST_STATISTICS, profile.getWaist());
		values.put(COLUMN_WRIST_STATISTICS, profile.getWrist());
		long id = this.database.insert(TABLE_NAME_STATISTICS, null, values);

		Log.d("ThuF", "STATISTICS ENTRY WITH ID=" + id + ", FOR PROFILE ID=" + profile.getId());
	}

	public void deleteStatisticsForProfile(long id) {
		this.database.delete(TABLE_NAME_STATISTICS, COLUMN_STATISTICS_FOR_PROFILE_ID + " = "
				+ id, null);
	}

	public float[] getStatisticsForProfile(long profileID, String whichStatistic) {
		String[] columns = { whichStatistic };
		Cursor cursor = database.query(TABLE_NAME_STATISTICS, columns,
				COLUMN_STATISTICS_FOR_PROFILE_ID + " = " + profileID, null, null, null,
				COLUMN_ID_STATISTICS);
		float[] result = new float[cursor.getCount()];
		int index = 0;
		while (cursor.moveToNext()) {
			result[index] = cursor.getFloat(cursor.getColumnIndex(whichStatistic));
			index++;
		}
		return result;
	}

	/**
	 * @param profile
	 * @return id for current profile
	 */
	public long insertProfile(Profile profile) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_PROFILE, profile.getName());
		values.put(COLUMN_WEIGHT_PROFILE, profile.getWeight());
		values.put(COLUMN_HEIGHT_PROFILE, profile.getHeight());
		values.put(COLUMN_WAIST_PROFILE, profile.getWaist());
		values.put(COLUMN_WRIST_PROFILE, profile.getWrist());
		String gender = "Man";
		if (!profile.isMan()) {
			gender = "Woman";
		}
		values.put(COLUMN_GENDER, gender);
		int imperialSystem = 0;
		if (profile.isImperialSystem()) {
			imperialSystem = 1;
		}
		values.put(COLUMN_IMPERIAL_SYSTEM, imperialSystem);
		long id = this.database.insert(TABLE_NAME_PROFILE, null, values);
		profile.setId(id);

		Log.d("ThuF", "DATABASE NEW PROFILE ENTRY WITH ID=" + id);
		return id;
	}

	public void deleteProfile(long id) {
		this.database.delete(TABLE_NAME_PROFILE, COLUMN_ID_PROFILE + " = " + id, null);
	}

	public void updateProfileById(long id, Profile profile) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_PROFILE, profile.getName());
		values.put(COLUMN_WEIGHT_PROFILE, profile.getWeight());
		values.put(COLUMN_HEIGHT_PROFILE, profile.getHeight());
		values.put(COLUMN_WAIST_PROFILE, profile.getWaist());
		values.put(COLUMN_WRIST_PROFILE, profile.getWrist());
		boolean man = profile.isMan();
		if (man) {
			values.put(COLUMN_GENDER, "Man");
		} else {
			values.put(COLUMN_GENDER, "Woman");
		}
		this.database.update(TABLE_NAME_PROFILE, values, COLUMN_ID_PROFILE + " = " + id, null);
	}

	public Profile cursorToProfile(Cursor cursor) {
		int idIndex = cursor.getColumnIndex(COLUMN_ID_PROFILE);
		int nameIndex = cursor.getColumnIndex(COLUMN_NAME_PROFILE);
		int weightIndex = cursor.getColumnIndex(COLUMN_WEIGHT_PROFILE);
		int heightIndex = cursor.getColumnIndex(COLUMN_HEIGHT_PROFILE);
		int waistIndex = cursor.getColumnIndex(COLUMN_WAIST_PROFILE);
		int wristIndex = cursor.getColumnIndex(COLUMN_WRIST_PROFILE);
		int genderIndex = cursor.getColumnIndex(COLUMN_GENDER);
		int imperialIndex = cursor.getColumnIndex(COLUMN_IMPERIAL_SYSTEM);

		long id = cursor.getLong(idIndex);
		String name = cursor.getString(nameIndex);
		int weight = cursor.getInt(weightIndex);
		int height = cursor.getInt(heightIndex);
		float waist = cursor.getFloat(waistIndex);
		float wrist = cursor.getFloat(wristIndex);
		String gender = cursor.getString(genderIndex);
		int imperial = cursor.getInt(imperialIndex);

		boolean man = true;
		if (gender.equalsIgnoreCase("woman")) {
			man = false;
		}
		boolean imperialSystem = false;
		if (imperial == 1) {
			imperialSystem = true;
		}

		Profile profile = new Profile(name, weight, height, waist, wrist, man, imperialSystem);
		profile.setId(id);
		return profile;
	}

	public Cursor getAllProfiles() {
		Cursor cursor = this.database.query(TABLE_NAME_PROFILE, null, null, null, null, null,
				COLUMN_ID_PROFILE);
		return cursor;
	}

	public Cursor getAllProfiles(String sortedByArg1) {
		Cursor cursor = this.database.query(TABLE_NAME_PROFILE, null, null, null, null, null,
				sortedByArg1);
		return cursor;
	}

	public Cursor getAllProfiles(String sortedByArg1, String sortedByArg2) {
		Cursor cursor = this.database.query(TABLE_NAME_PROFILE, null, null, null, null, null,
				sortedByArg1 + ", " + sortedByArg2);
		return cursor;
	}

	public void close() {
		this.database.close();
	}
}

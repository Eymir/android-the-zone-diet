package com.thuf.thezone.database;

public interface TableProfile {
	public static final String TABLE_NAME_PROFILE = "Profiles";
	public static final String COLUMN_ID_PROFILE = "_id";
	public static final String COLUMN_NAME_PROFILE = "name";
	public static final String COLUMN_WEIGHT_PROFILE = "weight";
	public static final String COLUMN_HEIGHT_PROFILE = "height";
	public static final String COLUMN_WAIST_PROFILE = "waist";
	public static final String COLUMN_WRIST_PROFILE = "wrist";
	public static final String COLUMN_GENDER = "isMan";
	public static final String COLUMN_IMPERIAL_SYSTEM = "isImperialSystem";

	public static final String CREATE_PROFILE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME_PROFILE + " (" + "'" + COLUMN_ID_PROFILE + "'"
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + "'" + COLUMN_NAME_PROFILE + "'"
			+ " VARCHAR, " + "'" + COLUMN_WEIGHT_PROFILE + "'" + " INTEGER, " + "'"
			+ COLUMN_HEIGHT_PROFILE + "'" + " INTEGER, " + "'" + COLUMN_WAIST_PROFILE + "'"
			+ " REAL, " + "'" + COLUMN_WRIST_PROFILE + "'" + " REAL, " + "'" + COLUMN_GENDER
			+ "'" + " VARCHAR, " + "'" + COLUMN_IMPERIAL_SYSTEM + "' INTEGER);";
}

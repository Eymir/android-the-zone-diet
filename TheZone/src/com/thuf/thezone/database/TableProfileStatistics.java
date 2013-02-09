package com.thuf.thezone.database;

public interface TableProfileStatistics {
	public static final String TABLE_NAME_STATISTICS = "Statistics";
	public static final String COLUMN_ID_STATISTICS = "_id";
	public static final String COLUMN_STATISTICS_FOR_PROFILE_ID = "profileID";
	public static final String COLUMN_WEIGHT_STATISTICS = "Weight";
	public static final String COLUMN_WAIST_STATISTICS = "Waist";
	public static final String COLUMN_WRIST_STATISTICS = "Wrist";

	public static final String CREATE_STATISTICS_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME_STATISTICS + " (" + "'" + COLUMN_ID_STATISTICS + "'"
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + "'" + COLUMN_STATISTICS_FOR_PROFILE_ID
			+ "'" + " INTEGER, " + "'" + COLUMN_WEIGHT_STATISTICS + "'" + " INTEGER, " + "'"
			+ COLUMN_WAIST_STATISTICS + "'" + " REAL, " + "'" + COLUMN_WRIST_STATISTICS + "'"
			+ " REAL);";
}

package com.thuf.thezone.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper implements TableProfile,
		TableProfileStatistics {
	private static final String DATABASE_NAME = "TheZone.db";
	private static final int DATABASE_VERSION = 1;

	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROFILE_TABLE);
		db.execSQL(CREATE_STATISTICS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TALBE IF EXISTS " + TABLE_NAME_PROFILE);
		db.execSQL("DROP TALBE IF EXISTS " + TABLE_NAME_STATISTICS);
		onCreate(db);
	}

}

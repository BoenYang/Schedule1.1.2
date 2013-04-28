package com.wmkj.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "course.db";
	private static final int VERSION = 1;

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE IF NOT EXISTS course (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "courseid INTEGER,"
				+ "coursename VARCHAR,"
				+ "coursetime VARCHAR,"
				+ "courseplace VARCHAR,"
				+ "courseplan VARCHAR," + "courseteacher VARCHAR)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS timedata (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
														"firstday VARCHAR," +
														"classnum INTEGER," +
														"weeknum INTEGER)");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS timetable (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				                                          "timeid INTEGER," +
				                                          "starttime VARCHAR," +
				                                          "endtime VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE course ADD COLUMN other STRING");
	}

}

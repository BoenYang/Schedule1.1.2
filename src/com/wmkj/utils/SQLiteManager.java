package com.wmkj.utils;

import java.util.ArrayList;
import java.util.List;

import com.wmkj.data.Course;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteManager {
	SQLiteHelper sqLiteHelper;
	SQLiteDatabase db;
	Context context;

	public SQLiteManager(Context context) {
		this.context = context;
		sqLiteHelper = new SQLiteHelper(context);
	}

	public boolean add(Course course) {
		db = sqLiteHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("INSERT INTO course VALUES(NULL,?,?,?,?,?,?)",
					new Object[] { course.id, course.courseName,
							course.courseTime, course.coursePlace,
							course.coursePlan, course.courseTeacher });
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			return false;
		} finally {
			db.endTransaction();
			db.close();
			db = null;
		}
		return true;
	}

	public boolean addtimedata(String firstday, int classNum, int week) {
		db = sqLiteHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("INSERT INTO timedata VALUES(NULL,?,?,?)", new Object[] {
					firstday, classNum, week });
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			return false;
		} finally {
			db.endTransaction();
			db.close();
			db = null;
		}
		return true;
	}

	public int rawClassNum() {
		db = sqLiteHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM timedata WHERE classnum != ?",
				new String[] { "0" });
		int classnum = -1;
		while (cursor.moveToNext()) {
			classnum = cursor.getInt(cursor.getColumnIndex("classnum"));
		}
		cursor.close();
		db.close();
		cursor = null;
		db = null;
		return classnum;
	}

	public int rawWeekNum() {
		db = sqLiteHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM timedata WHERE weeknum != ?",
				new String[] { "0" });
		int weeknum = -1;
		while (cursor.moveToNext()) {
			weeknum = cursor.getInt(cursor.getColumnIndex("weeknum"));
		}
		cursor.close();
		db.close();
		cursor = null;
		db = null;
		return weeknum;
	}

	public String rawFirstday() {
		db = sqLiteHelper.getWritableDatabase();
		String firstday = "error";
		Cursor cursor = db.rawQuery(
				"SELECT * FROM timedata WHERE firstday != ?",
				new String[] { "" });
		while (cursor.moveToNext()) {
			firstday = cursor.getString(cursor.getColumnIndex("firstday"));
			break;
		}
		cursor.close();
		db.close();
		cursor = null;
		db = null;
		return firstday;
	}

	public List<Course> rawQueryByCourseName(String name) {
		db = sqLiteHelper.getWritableDatabase();
		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = db.rawQuery("SELECT * FROM course WHERE coursename =?",
				new String[] { name });
		while (cursor.moveToNext()) {
			Course course = new Course(1);

			course.id = cursor.getInt(cursor.getColumnIndex("courseid"));

			course.courseTeacher = cursor.getString(cursor
					.getColumnIndex("courseteacher"));

			course.courseName = cursor.getString(cursor
					.getColumnIndex("coursename"));

			course.courseTime = cursor.getString(cursor
					.getColumnIndex("coursetime"));

			course.coursePlace = cursor.getString(cursor
					.getColumnIndex("courseplace"));

			course.coursePlan = cursor.getString(cursor
					.getColumnIndex("courseplan"));

			courses.add(course);
		}
		cursor.close();
		db.close();
		cursor = null;
		db = null;
		return courses;
	}

	public List<Course> rawQueryCourses(int week) {
		db = sqLiteHelper.getWritableDatabase();
		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM course WHERE coursename !=?",
				new String[] { "" });
		String coursePlan;
		while (cursor.moveToNext()) {
			Course course = new Course(1);
			coursePlan = cursor.getString(cursor.getColumnIndex("courseplan"));
			if (coursePlan.charAt(week - 1) == '1') {

				course.id = cursor.getInt(cursor.getColumnIndex("courseid"));

				course.courseTeacher = cursor.getString(cursor
						.getColumnIndex("courseteacher"));

				course.courseName = cursor.getString(cursor
						.getColumnIndex("coursename"));

				course.courseTime = cursor.getString(cursor
						.getColumnIndex("coursetime"));

				course.coursePlace = cursor.getString(cursor
						.getColumnIndex("courseplace"));

				course.coursePlan = cursor.getString(cursor
						.getColumnIndex("courseplan"));

				courses.add(course);
			} else {
				continue;
			}
		}
		cursor.close();
		db.close();
		cursor = null;
		db = null;
		return courses;
	}

	public List<Course> rawQueryAllCourses(String coursename) {
		db = sqLiteHelper.getWritableDatabase();
		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM course WHERE coursename = ?",
				new String[] { coursename });
		while (cursor.moveToNext()) {
			Course course = new Course(1);
			course.id = cursor.getInt(cursor.getColumnIndex("courseid"));

			course.courseTeacher = cursor.getString(cursor
					.getColumnIndex("courseteacher"));

			course.courseName = cursor.getString(cursor
					.getColumnIndex("coursename"));

			course.courseTime = cursor.getString(cursor
					.getColumnIndex("coursetime"));

			course.coursePlace = cursor.getString(cursor
					.getColumnIndex("courseplace"));

			course.coursePlan = cursor.getString(cursor
					.getColumnIndex("courseplan"));

			courses.add(course);
		}
		cursor.close();
		db.close();
		cursor = null;
		db = null;
		return courses;
	}

	public boolean updateCourseInfo(Course course) {
		db = sqLiteHelper.getWritableDatabase();
		db.beginTransaction();
		ContentValues cv = new ContentValues();
		try {
			cv.put("coursename", course.courseName);
			cv.put("courseplace", course.coursePlace);
			cv.put("coursetime", course.courseTime);
			cv.put("courseplan", course.coursePlan);
			cv.put("courseteacher", course.courseTeacher);
			db.update("course", cv, "courseid=?",
					new String[] { course.id + "" });
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			return false;
		} finally {
			db.endTransaction();
			db.close();
			db = null;
		}
		return true;
	}

	public List<Course> rawQueryAll() {
		db = sqLiteHelper.getWritableDatabase();
		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM course WHERE coursename != ?",
				new String[] { "" });
		while (cursor.moveToNext()) {
			Course course = new Course(1);
			course.id = cursor.getInt(cursor.getColumnIndex("courseid"));

			course.courseTeacher = cursor.getString(cursor
					.getColumnIndex("courseteacher"));

			course.courseName = cursor.getString(cursor
					.getColumnIndex("coursename"));

			course.courseTime = cursor.getString(cursor
					.getColumnIndex("coursetime"));

			course.coursePlace = cursor.getString(cursor
					.getColumnIndex("courseplace"));

			course.coursePlan = cursor.getString(cursor
					.getColumnIndex("courseplan"));

			courses.add(course);
		}
		cursor.close();
		db.close();
		cursor = null;
		db = null;
		return courses;
	}

	public boolean deleteCourse(Course course) {
		db = sqLiteHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM course WHERE courseid=? AND coursename=?",
					new Object[] { course.id, course.courseName });
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			return false;
		} finally {
			db.endTransaction();
			db.close();
			db = null;
		}
		return true;
	}

	public boolean deleteCourseByName(String coursename) {
		db = sqLiteHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM course WHERE coursename=?",
					new Object[] { coursename });
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			return false;
		} finally {
			db.endTransaction();
			db.close();
			db = null;
		}
		return true;
	}

	public List<String> getTimeTable() {
		db = sqLiteHelper.getWritableDatabase();
		List<String> timeList = new ArrayList<String>();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM timetable WHERE timeid != ?",
				new String[] { "0" });
		while (cursor.moveToNext()) {
			String str = cursor.getString(cursor.getColumnIndex("starttime"))
					+ "~" + cursor.getString(cursor.getColumnIndex("endtime"));
			timeList.add(str);
		}
		cursor.close();
		db.close();
		db = null;
		cursor = null;
		return timeList;
	}

	public boolean addTimeTable(String startT, String endT, int id) {
		db = sqLiteHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL("INSERT INTO timetable VALUES(NULL,?,?,?)",
					new Object[] { id, startT, endT });
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			return false;
		} finally {
			db.endTransaction();
			db.close();
			db = null;
		}
		return true;
	}

	public boolean updateTimeTable(String startT, String endT, int id) {
		db = sqLiteHelper.getWritableDatabase();
		db.beginTransaction();
		ContentValues cv = new ContentValues();
		try {
			cv.put("starttime", startT);
			cv.put("endtime", endT);
			db.update("timetable", cv, "timeid=?", new String[] { id + "" });
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			return false;
		} finally {
			db.endTransaction();
			db.close();
			db = null;
		}
		return true;
	}

}

package com.wmkj.view;

import java.util.ArrayList;
import java.util.List;

import com.wmkj.activity.MainInterface;
import com.wmkj.data.Course;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class Table extends TableView {

	private static Form forms[][];
	private static List<com.wmkj.view.TextView> thTextViews = new ArrayList<TextView>();
	public boolean isFirst = true;
	private static String weekStrs[];
	public int week ;

	public Table(Context context) {
		super(context);
		weekStrs = new String[7];
		weekStrs[0] = "星期一";
		weekStrs[1] = "星期二";
		weekStrs[2] = "星期三";
		weekStrs[3] = "星期四";
		weekStrs[4] = "星期五";
		weekStrs[5] = "星期六";
		weekStrs[6] = "星期天";
	}
	
	public Table(Context context, AttributeSet attrs) {
		super(context, attrs);
		weekStrs = new String[7];
		weekStrs[0] = "星期一";
		weekStrs[1] = "星期二";
		weekStrs[2] = "星期三";
		weekStrs[3] = "星期四";
		weekStrs[4] = "星期五";
		weekStrs[5] = "星期六";
		weekStrs[6] = "星期天";
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isFirst) {
			addForm();
			setTextView();
			isFirst = false;
		}
		loadData(week);
		drawData(canvas);
	}

	/**
	 * draw every textview
	 * 
	 * @param canvas
	 */
	private void drawData(Canvas canvas) {
		// draw forms textview
		for (int i = 0; i < forms.length; i++) {
			for (int j = 0; j < forms[i].length; j++) {
				forms[i][j].tv.drawFormText(canvas);
			}
		}

		// draw table head textview
		int thTextViewSize = thTextViews.size();
		for (int i = 0; i < thTextViewSize; i++) {
			if (i < row) {
				thTextViews.get(i).drawVerticalText(canvas);
			} else {
				thTextViews.get(i).drawHorizonText(canvas);
			}
		}
	}

	/**
	 * set textviews
	 */
	public void setTextView() {

		// add table head textview
		TextView tv;
		for (int i = 0; i < row; i++) {
			tv = new TextView(0, thHeight + tdHeight * i,
					thWidth, tdHeight);
			tv.text = ""+(i+1);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(10);
			thTextViews.add(tv);
		}

		// add table head textview
		for (int i = 0; i < column; i++) {
			tv = new TextView( thWidth + tdWidth * i, 0, tdWidth,
					thHeight);
			tv.text = weekStrs[i];
			tv.setTextSize(10);
			tv.setTextColor(Color.WHITE);
			thTextViews.add(tv);
		}
	}

	public void resetFromView() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				forms[i][j].tv.text = "";
				forms[i][j].tv.classname = "";
				forms[i][j].tv.classplace = "";
			}
		}
	}

	/**
	 * add from objects
	 */
	public void addForm() {
		forms = new Form[row][column];
		Form form;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				form = new Form(i, j);
				forms[i][j] = form;
				forms[i][j].tv = new TextView(forms[i][j].x, forms[i][j].y,
						tdWidth, tdHeight);
				forms[i][j].tv.setTextColor(Color.WHITE);
				forms[i][j].tv.setTextSize(10);
			}
		}
	}

	/**
	 * load data form databases and transform to text
	 * 
	 * @param week
	 *            the week you want to load
	 * @return
	 */
	public boolean loadData(int week) {
		Course course;
		String str;
		String strs[];
		List<Course> courses = MainInterface.sqLiteManager
				.rawQueryCourses(week);

		int courseSize = courses.size();
		for (int i = 0; i < courseSize; i++) {
			course = courses.get(i);
			str = course.courseTime;
			if (str == "") {
				return false;
			}
			strs = str.split("-");
			int r, c;
			c = Integer.parseInt(strs[0]) - 1;
			r = Integer.parseInt(strs[1]) - 1;
			forms[r][c].tv.classname = course.courseName;
			forms[r][c].tv.classplace = course.coursePlace;
		}
		courses.clear();
		courses = null;
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void setWeek(int week) {
		this.week = week;
		updateTable();
	}

	private void updateTable() {
		resetFromView();
		loadData(week);
		invalidate();
	}

}
